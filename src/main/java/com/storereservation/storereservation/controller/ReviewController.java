package com.storereservation.storereservation.controller;

import com.storereservation.storereservation.dto.Review;
import com.storereservation.storereservation.service.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
@Api(tags = "리뷰 API")
public class ReviewController {
    private final ReviewService reviewService;

    @ApiOperation(value = "리뷰 작성", notes = "방문 완료한 상점에 대한 리뷰 작성")
    @PostMapping("/register")
    public ResponseEntity<String> registerReview(@RequestBody Review.AccessReview review, Principal principal) {
        reviewService.registerReview(review,principal);
        return ResponseEntity.ok("리뷰 등록 성공");

    }

    @ApiOperation(value = "리뷰 수정", notes = "작성한 리뷰를 수정")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateReview(@PathVariable("id") Long id,Review.AccessReview review, Principal principal) {
        reviewService.updateReview(id,review, principal);
        return ResponseEntity.ok("리뷰 수정 성공");
    }

    @ApiOperation(value = "리뷰 삭제", notes = "회원 본인이 작성한 리뷰 삭제")
    @DeleteMapping("/{id}/customer")
    public ResponseEntity<String> deleteReviewByCustomer(@PathVariable("id") Long id,Principal principal) {
        reviewService.deleteReviewByCustomer(id, principal);
        return ResponseEntity.ok( "리뷰 삭제 성공 -> 회원");
    }

    @ApiOperation(value = "예약 취소", notes = "예약을 취소함")
    @DeleteMapping("/{id}/restaurant")
    public ResponseEntity<String> deleteReviewByRestaurant(@PathVariable("id") Long id,Principal principal) {
       reviewService.deleteReviewByRestaurant(id, principal);
        return ResponseEntity.ok( "리뷰 삭제 성공 -> 상점");
    }
}
