package com.storereservation.storereservation.dto;

import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {
    private Long id;
    private String storename;
    private String location;
    private String explanation;
    private Double star;

    @Data
    public static class RegisterRestaurant {
        private String stroename;
        private String location;
        private String explanation;
        private Double star;

    }

    @Data
    @Builder
    public static class GetRestaurant {
        private String stroename;
        private String location;
        private String explanation;
        private Double star;
        private List<Review.GetReview> reviewList;
    }

    @Data
    public class UpdateRestaurant {
        private String stroename;
        private String location;
        private String explanation;
        private Double star;
        private List<Review.GetReview> reviewList;
    }

    @Data
    public class DeleteRestaurant {
        private String stroename;
        private String location;
        private String explanation;
        private Double star;
        private List<Review.GetReview> reviewList;
    }

}
