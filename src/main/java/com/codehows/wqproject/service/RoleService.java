package com.codehows.wqproject.service;

import com.codehows.wqproject.constant.Role;
import com.codehows.wqproject.dto.MemberDto;
import com.codehows.wqproject.entity.Answer;
import com.codehows.wqproject.entity.Member;
import com.codehows.wqproject.entity.Question;
import com.codehows.wqproject.repository.member.MemberRepository;
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
public class RoleService {

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public HashMap<String, Object> getAuthorities() {
        HashMap<String, Object> result = new HashMap<>();
        List<String> authList = Stream.of(Role.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        result.put("authorities", authList);
        List<MemberDto> memberDtoList = memberRepository.authorityEdit()
                .stream()
                .map(MemberDto::of)
                .toList();
        result.put("members", memberDtoList);
        return result;
    }

    public void updateAuthorities(String memberId, String memberRole) {
        HashMap<String, Role> roles = new HashMap<>();
        roles.put("ADMIN", Role.ADMIN);
        roles.put("USER", Role.USER);
        roles.put("TEMP", Role.TEMP);

        Member member = memberRepository.findById(memberId)
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
        memberRepository.deleteById(memberId);
    }

}
