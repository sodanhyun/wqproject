package com.codehows.wqproject.repository.member;

import com.codehows.wqproject.entity.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends CrudRepository<Member, String>, MemberRepositoryCustom {

    List<Member> findAll();

    List<Member> authorityEdit();

}
