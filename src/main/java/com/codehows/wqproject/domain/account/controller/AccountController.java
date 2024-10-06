package com.codehows.wqproject.domain.account.controller;

import com.codehows.wqproject.domain.account.service.AccountService;
import com.codehows.wqproject.domain.account.service.impl.AccountServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/authorities")
    public ResponseEntity<?> authority() {
        HashMap<String, Object> response = new HashMap<>();
        response.put("authorities", accountService.getAuthorities());;
        response.put("members", accountService.getUsers());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/update/{memberId}")
    public ResponseEntity<?> update(@PathVariable String memberId, @RequestPart(value = "memberRole") String memberRole) {
        accountService.updateAuthorities(memberId, memberRole);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{memberId}")
    public ResponseEntity<?> delete(@PathVariable String memberId) {
        accountService.deleteMember(memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
