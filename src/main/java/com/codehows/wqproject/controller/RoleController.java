package com.codehows.wqproject.controller;

import com.codehows.wqproject.dto.MemberDto;
import com.codehows.wqproject.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@Slf4j
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/authorities")
    public ResponseEntity<Map<String, Object>> authority() {
        return ResponseEntity.ok(roleService.getAuthorities());
    }

    @PatchMapping("/update/{memberId}")
    public ResponseEntity<?> update(@PathVariable String memberId, @RequestPart(value = "memberRole") String memberRole) {
        roleService.updateAuthorities(memberId, memberRole);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{memberId}")
    public ResponseEntity<?> delete(@PathVariable String memberId) {
        roleService.deleteMember(memberId);
        return ResponseEntity.ok().build();
    }

}
