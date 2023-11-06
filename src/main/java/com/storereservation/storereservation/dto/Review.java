package com.storereservation.storereservation.dto;

import lombok.*;

public class Review {
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class AccessReview {
        private Long id;
        private Long restaurantId;
        private Long memberId;
        private Long reservation;
        private String contents;
        private int star;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class GetReview {
        private Long id;
        private Long restaurantId;
        private Long memberId;
        private String contents;
        private int star;
    }

}
