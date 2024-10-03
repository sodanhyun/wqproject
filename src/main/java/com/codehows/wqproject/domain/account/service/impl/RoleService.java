package com.codehows.wqproject.domain.account.service.impl;

import com.codehows.wqproject.auth.user.Role;
import com.codehows.wqproject.domain.auth.requestDto.UserFormDto;
import com.codehows.wqproject.entity.Answer;
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

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public HashMap<String, Object> getAuthorities() {
        HashMap<String, Object> result = new HashMap<>();
        List<String> authList = Stream.of(Role.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        result.put("authorities", authList);
        List<UserFormDto> userFormDtoList = userRepository.authorityEdit()
                .stream()
                .map(UserFormDto::of)
                .toList();
        result.put("members", userFormDtoList);
        return result;
    }

    public void updateAuthorities(String memberId, String memberRole) {
        HashMap<String, Role> roles = new HashMap<>();
        roles.put("ADMIN", Role.ADMIN);
        roles.put("USER", Role.USER);
        roles.put("TEMP", Role.TEMP);

        User member = userRepository.findById(memberId)
                .orElseThrow(EntityNotFoundException::new);
        member.updateRole(roles.get(memberRole));
    }

    public void deleteMember(String memberId) {
        for(Question q : questionRepository.findAllByMemberId(memberId)) {
            for(Answer a : answerRepository.findAllByQuestion(q)) {
                answerRepository.delete(a);
            }
            questionRepository.delete(q);
        }
        refreshTokenRepository.deleteById(memberId);
        userRepository.deleteById(memberId);
    }

}
