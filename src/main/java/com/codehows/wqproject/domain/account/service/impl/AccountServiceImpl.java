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

    public List<String> getAuthorities() {
        return Stream.of(UserRole.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public List<AccountInfoRes> getUsers() {
        return userRepository.authorityEdit()
                .stream()
                .map(AccountInfoRes::of)
                .toList();
    }

    public void updateAuthorities(String userId, String userRole) {
        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        user.updateRole(UserRole.valueOf(userRole));
    }

    public void deleteMember(String memberId) {
        for(Question q : questionRepository.findAllByMemberId(memberId)) {
            answerRepository.deleteAll(answerRepository.findAllByQuestion(q));
            questionRepository.delete(q);
        }
        User user = userRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
        refreshTokenRepository.deleteByUser(user);
        userRepository.delete(user);
    }

}
