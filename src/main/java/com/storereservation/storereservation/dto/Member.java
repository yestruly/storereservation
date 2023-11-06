package com.storereservation.storereservation.dto;

import com.storereservation.storereservation.entity.MemberEntity;
import lombok.Data;

import java.util.Collections;
import java.util.List;

import static com.storereservation.storereservation.dto.constants.Role.ROLE_CUSTOMER;
import static com.storereservation.storereservation.dto.constants.Role.ROLE_MANAGER;

public class Member extends MemberEntity {
    @Data
    public static class SignIn{
        private String username;
        private String password;
    }

    @Data
    public static class SignUp{
        private String username;
        private String password;
        private String email;
        private List<String> roles;

        public MemberEntity toCustomerEntity(){
            return MemberEntity.builder()
                    .username(this.username)
                    .password(this.password)
                    .email(this.email)
                    .roles(Collections.singletonList(String.valueOf(ROLE_CUSTOMER)))
                    .build();

        }

        public MemberEntity toManagerEntity(){
            return MemberEntity.builder()
                    .username(this.username)
                    .password(this.password)
                    .email(this.email)
                    .roles(Collections.singletonList(String.valueOf(ROLE_MANAGER)))
                    .build();

        }
    }
}
