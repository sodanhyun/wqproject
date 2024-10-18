package com.codehows.wqproject.domain.account.controller;

import com.codehows.wqproject.commonDto.PageDto;
import com.codehows.wqproject.domain.account.requestDto.AccountUpdateReq;
import com.codehows.wqproject.domain.account.responseDto.AccountInfoRes;
import com.codehows.wqproject.domain.account.responseDto.AccountPageRes;
import com.codehows.wqproject.domain.account.service.AccountService;
import com.codehows.wqproject.domain.auth.requestDto.UserFormDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.codehows.wqproject.constant.PageConstant.MAX_SIZE_PER_PAGE;

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

    @GetMapping(value={"/authorities", "/authorities/{page}"})
    public ResponseEntity<?> getUsersInfo(@PathVariable(required = false) Optional<Integer> page,
                                          @RequestParam(value = "itemsPerPage", required = false) Optional<Integer> itemsPerPage) {
        Pageable pageable = PageRequest.of(page.orElse(0), itemsPerPage.orElse(MAX_SIZE_PER_PAGE));
        Page<AccountInfoRes> pages = accountService.getUsersByPaging(pageable);
        AccountPageRes res = new AccountPageRes();
        res.setContent(pages.getContent());
        res.setAuthorities(accountService.getAuthorities());
        res.setPageNumber(pages.getNumber());
        res.setTotalPages(pages.getTotalPages());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("/update")
    public ResponseEntity<?> update(AccountUpdateReq req) {
        accountService.updateAuthorities(req.getId(), req.getUserRole());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{memberId}")
    public ResponseEntity<?> delete(@PathVariable String memberId) {
        accountService.deleteUser(memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
