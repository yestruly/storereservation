package com.storereservation.storereservation.service;

import com.storereservation.storereservation.configuration.exception.CustomException;
import com.storereservation.storereservation.configuration.exception.ErrorCode;
import com.storereservation.storereservation.dto.Review;
import com.storereservation.storereservation.entity.MemberEntity;
import com.storereservation.storereservation.entity.ReserveEntity;
import com.storereservation.storereservation.entity.RestaurantEntity;
import com.storereservation.storereservation.entity.ReviewEntity;
import com.storereservation.storereservation.repository.MemberRepository;
import com.storereservation.storereservation.repository.ReserveRepository;
import com.storereservation.storereservation.repository.RestaurantRepository;
import com.storereservation.storereservation.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

import static com.storereservation.storereservation.entity.reserveConstants.ArriveStatus.ARRIVE;

@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final MemberRepository memberRepository;
    private final ReserveRepository reserveRepository;


    //리뷰 작성
    @Transactional
    public void registerReview(Review.AccessReview review, Principal principal) {
        String userName = principal.getName();
        MemberEntity member = memberRepository.findByUsername(userName)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USERNAME));
        RestaurantEntity restaurant = restaurantRepository.findById(review.getRestaurantId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESTAURANT));
        ReserveEntity reserve = reserveRepository.findById(review.getReservation())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_APPROVE_RESERVE));

        if (!userName.equals(reserve.getMember().getUsername())) {
            throw new CustomException(ErrorCode.UNMATCHED_REVIEW);
        }

        if (!reserve.getArriveStatus().equals(ARRIVE)) {
            throw new CustomException(ErrorCode.UNMATCHED_REVIEW);
        }

        if (review.getStar() < 0 || review.getStar() > 5) {
            throw new CustomException(ErrorCode.STAR_FIVE_TO_ZERO);
        }

        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setRestaurant(restaurant);
        reviewEntity.setMember(member);
        reviewEntity.setStar(review.getStar());
        reviewEntity.setContents(review.getContents());
        reviewRepository.save(reviewEntity);

        restaurant.getReview().add(reviewEntity);
        restaurant.calculateStar();
        restaurantRepository.save(restaurant);

    }

    //작성한 리뷰 수정
    @Transactional
    public void updateReview(Long id, Review.AccessReview review, Principal principal) {
        String userName = principal.getName();
        ReviewEntity reviewEntity = reviewRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));
        RestaurantEntity restaurant = restaurantRepository.findById(review.getRestaurantId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESTAURANT));

        if (!userName.equals(reviewEntity.getMember().getUsername())) {
            throw new CustomException(ErrorCode.UNMATCHED_REVIEW);
        }
        if (review.getStar() < 0 || review.getStar() > 5) {
            throw new CustomException(ErrorCode.STAR_FIVE_TO_ZERO);
        }

        reviewEntity.setContents(review.getContents());
        reviewEntity.setStar(review.getStar());
        reviewRepository.save(reviewEntity);

        restaurant.calculateStar();
        restaurantRepository.save(restaurant);
    }

    //리뷰 작성한 고객이 리뷰 삭제
    @Transactional
    public void deleteReviewByCustomer(Long id, Principal principal) {
        String userName = principal.getName();
        ReviewEntity review = reviewRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));

        if (!userName.equals(review.getMember().getUsername())) {
            throw new CustomException(ErrorCode.NOT_MY_RESERVE);
        }
        reviewRepository.delete(review);

        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.calculateStar();
        restaurantRepository.save(restaurant);

    }

    //작성한 리뷰에 해당하는 매장이 리뷰 삭제
    @Transactional
    public void deleteReviewByRestaurant(Long id, Principal principal) {
        String storeName = principal.getName();
        ReviewEntity review = reviewRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));

        if (!storeName.equals(review.getRestaurant().getStorename())) {
            new CustomException(ErrorCode.UNMATCHED_REVIEW);
        }
        reviewRepository.delete(review);

        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.calculateStar();
        restaurantRepository.save(restaurant);

    }
}
