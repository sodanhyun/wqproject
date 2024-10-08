package com.codehows.wqproject.domain.account.service.impl;

import com.codehows.wqproject.constant.enumVal.UserRole;
import com.codehows.wqproject.domain.account.responseDto.AccountInfoRes;
import com.codehows.wqproject.domain.account.service.AccountService;
import com.codehows.wqproject.domain.auth.requestDto.UserFormDto;
import com.codehows.wqproject.entity.User;
import com.codehows.wqproject.entity.Question;
import com.codehows.wqproject.repository.RefreshTokenRepository;
import com.codehows.wqproject.repository.AnswerRepository;
import com.codehows.wqproject.repository.QuestionRepository;
import com.codehows.wqproject.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public void regist(UserFormDto userFormDto) {
        if (userRepository.findById(userFormDto.getId()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }
        User member = User.createByOwn(userFormDto, new BCryptPasswordEncoder());
        userRepository.save(member);
    }

    public List<UserRole> getAuthorities() {
        return Stream.of(UserRole.values())
                .collect(Collectors.toList());
    }

    public List<AccountInfoRes> getUsers() {
        return userRepository.getUsersNotUserRole()
                .stream()
                .map(AccountInfoRes::of)
                .toList();
    }

    public void updateAuthorities(String userId, String userRole) {
        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        user.updateRole(UserRole.valueOf(userRole.split("_")[1]));
    }

    public void deleteUser(String memberId) {
        for(Question q : questionRepository.findAllByMemberId(memberId)) {
            answerRepository.deleteAll(answerRepository.findAllByQuestion(q));
            questionRepository.delete(q);
        }
        User user = userRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
        refreshTokenRepository.deleteByUser(user);
        userRepository.delete(user);
    }

    public User findUserById(String userId) {
        return userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
    }

}
