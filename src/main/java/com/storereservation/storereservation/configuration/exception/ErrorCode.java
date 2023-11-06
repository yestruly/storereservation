package com.storereservation.storereservation.configuration.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    NOT_FOUND_USERNAME(HttpStatus.NOT_FOUND,"존재하지 않는 회원입니다."),
   INCORRECT_PASSWORD(HttpStatus.NOT_FOUND,"비밀번호가 일치하지 않습니다."),
    ALREADY_EXIST_MEMBER(HttpStatus.NOT_FOUND,"이미 존재하는 아이디입니다."),
    ONLY_ROLE_MANAGER(HttpStatus.BAD_REQUEST,"파트너 회원만 매장 정보 등록 가능합니다."),
    ALREADY_EXIST_RESTAURANT(HttpStatus.NOT_FOUND,"이미 존재하는 매장입니다"),
    NOT_FOUND_RESTAURANT(HttpStatus.NOT_FOUND,"존재하지 않는 매장입니다."),
    NOT_MY_RESTAURANT(HttpStatus.BAD_REQUEST,"본인이 등록한 매장 정보가 아닙니다"),
    INVALID_RESERVE_DATE(HttpStatus.BAD_REQUEST,"예약할 수 없는 일시입니다."),
    NOT_MY_RESERVE(HttpStatus.BAD_REQUEST,"본인이 예약한 건이 아닙니다"),
    NOT_APPROVE_RESERVE(HttpStatus.BAD_REQUEST,"미승인 예약건입니다."),
    CANCEL_RESERVE(HttpStatus.BAD_REQUEST,"체크인 시간이 지나 자동 취소된 예약건입니다."),
    NOT_FOUND_REVIEW(HttpStatus.NOT_FOUND,"존재하지 않는 리뷰입니다."),
    UNMATCHED_REVIEW(HttpStatus.BAD_REQUEST,"리뷰를 다시 확인해주세요"),
    STAR_FIVE_TO_ZERO(HttpStatus.BAD_REQUEST,"별점은 0~5점만 가능합니다.");


    private final HttpStatus httpStatus;
    private final String comment;

}
