package com.codehows.wqproject.domain.account.controller;

import com.codehows.wqproject.domain.account.requestDto.AccountUpdateReq;
import com.codehows.wqproject.domain.account.service.AccountService;
import com.codehows.wqproject.domain.auth.requestDto.UserFormDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/regist")
    public ResponseEntity<?> signup(@Valid @RequestBody UserFormDto userFormDto) {
        try{
            accountService.regist(userFormDto);
            return ResponseEntity.ok().build();
        }catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @GetMapping("/authorities")
    public ResponseEntity<?> authority() {
        HashMap<String, Object> res = new HashMap<>();
        res.put("authorities", accountService.getAuthorities());;
        res.put("members", accountService.getUsers());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("/update")
    public ResponseEntity<?> update(AccountUpdateReq req) {
        accountService.updateAuthorities(req.getMemberId(), req.getMemberRole());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{memberId}")
    public ResponseEntity<?> delete(@PathVariable String memberId) {
        accountService.deleteMember(memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
