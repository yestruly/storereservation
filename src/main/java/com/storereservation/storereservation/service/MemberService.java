package com.storereservation.storereservation.service;

import com.storereservation.storereservation.configuration.exception.CustomException;
import com.storereservation.storereservation.configuration.exception.ErrorCode;
import com.storereservation.storereservation.entity.MemberEntity;
import com.storereservation.storereservation.dto.Member;
import com.storereservation.storereservation.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
@AllArgsConstructor
@Slf4j
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USERNAME));
    }

    //일반 회원등록
    public MemberEntity customerRegister(Member.SignUp member) {
        boolean exist = this.memberRepository.existsByUsername(member.getUsername());

        if(exist){
            throw new CustomException(ErrorCode.ALREADY_EXIST_MEMBER);
        }
        member.setPassword(passwordEncoder.encode(member.getPassword()));

        return this.memberRepository.save(member.toCustomerEntity());
    }

    //관리자 회원등록
    public MemberEntity managerRegister(Member.SignUp member) {
        boolean exist = this.memberRepository.existsByUsername(member.getUsername());

        if(exist){
            throw new CustomException(ErrorCode.ALREADY_EXIST_MEMBER);
        }
        member.setPassword(passwordEncoder.encode(member.getPassword()));

        return this.memberRepository.save(member.toManagerEntity());
    }

    //로그인
    public MemberEntity authenticate(Member.SignIn member) {
        MemberEntity user = memberRepository.findByUsername(member.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USERNAME));

        if (!passwordEncoder.matches(member.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }

        return user;
    }
}

