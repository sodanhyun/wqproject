package com.codehows.wqproject.domain.account.service.impl;

import com.codehows.wqproject.auth.jwt.RefreshTokenService;
import com.codehows.wqproject.auth.user.Role;
import com.codehows.wqproject.domain.account.responseDto.UserDto;
import com.codehows.wqproject.domain.account.service.AccountService;
import com.codehows.wqproject.entity.Answer;
import com.codehows.wqproject.entity.User;
import com.codehows.wqproject.entity.Question;
import com.codehows.wqproject.repository.user.UserRepository;
import com.codehows.wqproject.repository.RefreshTokenRepository;
import com.codehows.wqproject.repository.answer.AnswerRepository;
import com.codehows.wqproject.repository.question.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
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
    private final RefreshTokenService refreshTokenService;

    public HashMap<String, Object> getAuthorities() {
        HashMap<String, Object> result = new HashMap<>();
        List<String> authList = Stream.of(Role.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        result.put("authorities", authList);
        List<UserDto> userDtoList = userRepository.authorityEdit()
                .stream()
                .map(UserDto::of)
                .toList();
        result.put("members", userDtoList);
        return result;
    }

    public void updateAuthorities(String userId, String memberRole) {
        HashMap<String, Role> roles = new HashMap<>();
        roles.put("ADMIN", Role.ADMIN);
        roles.put("USER", Role.USER);
        roles.put("TEMP", Role.TEMP);

        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);
        user.updateRole(roles.get(memberRole));
    }

    public void deleteMember(String userId) {
        for(Question q : questionRepository.findAllByMemberId(userId)) {
            for(Answer a : answerRepository.findAllByQuestion(q)) {
                answerRepository.delete(a);
            }
            questionRepository.delete(q);
        }
        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        refreshTokenService.deleteByUser(user);
        userRepository.delete(user);
    }

}
