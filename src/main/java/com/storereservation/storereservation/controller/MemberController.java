package com.storereservation.storereservation.controller;

import com.storereservation.storereservation.configuration.security.TokenProvider;
import com.storereservation.storereservation.dto.Member;
import com.storereservation.storereservation.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    //일반 고객 회원가입
    @PostMapping("/signup/customer")
    public ResponseEntity<?> customerSignup(@RequestBody Member.SignUp request){
        var result = this.memberService.customerRegister(request);
        return ResponseEntity.ok(result);
    }

    //파트너 회원(매장 측) 회원가입
    @PostMapping("/signup/manager")
    public ResponseEntity<?> managerSignup(@RequestBody Member.SignUp request){
        var result = this.memberService.managerRegister(request);
        return ResponseEntity.ok(result);
    }

    //로그인
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody Member.SignIn request){
        var member = this.memberService.authenticate(request);
        var token = this.tokenProvider.generateToken(member.getUsername(), member.getRoles());
        return ResponseEntity.ok(token);
    }


}
