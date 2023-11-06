package com.storereservation.storereservation.service;

import com.storereservation.storereservation.configuration.exception.CustomException;
import com.storereservation.storereservation.configuration.exception.ErrorCode;
import com.storereservation.storereservation.dto.Restaurant;
import com.storereservation.storereservation.dto.Review;
import com.storereservation.storereservation.entity.MemberEntity;
import com.storereservation.storereservation.entity.RestaurantEntity;
import com.storereservation.storereservation.repository.MemberRepository;
import com.storereservation.storereservation.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.storereservation.storereservation.dto.constants.Role.ROLE_MANAGER;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final MemberRepository memberRepository;

    //매장 정보 등록
    @Transactional
    public void registerRestaurant(@RequestBody Restaurant.RegisterRestaurant register,
                           Principal principal){

        String userName = principal.getName();
        MemberEntity member = memberRepository.findByUsername(userName)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USERNAME));

        if(!member.getRoles().contains(ROLE_MANAGER)){
            throw new CustomException(ErrorCode.ONLY_ROLE_MANAGER);
        }

        // 상점명이 없거나 이미 등록된 상점명인경우
        if (register.getStroename() != null && restaurantRepository.existsByStorename(register.getStroename())) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_RESTAURANT);
        }

        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setStorename(register.getStroename());
        restaurant.setLocation(register.getLocation());
        restaurant.setExplanation(register.getExplanation());
        restaurant.setStar(0.0);
        restaurantRepository.save(restaurant);
    }


    //매장 정보 수정
    @Transactional
    public void updateRestaurant(Long id, Restaurant.UpdateRestaurant updateRestaurant, Principal principal){
        String userName = principal.getName();
        MemberEntity member = memberRepository.findByUsername(userName)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USERNAME));

        RestaurantEntity restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESTAURANT));

        if(!member.getRoles().equals(ROLE_MANAGER)){
            throw new CustomException(ErrorCode.ONLY_ROLE_MANAGER);
        }

        if(!member.getId().equals(restaurant.getMember().getId())){
            throw new CustomException(ErrorCode.NOT_MY_RESTAURANT);
        }

        restaurant.setStorename(updateRestaurant.getStroename());
        restaurant.setExplanation(updateRestaurant.getExplanation());
        restaurant.setLocation(updateRestaurant.getLocation());
        restaurantRepository.save(restaurant);
    }

    //매장 정보 삭제
    @Transactional
    public void deleteRestaurant(Long id, Principal principal){
        String userName = principal.getName();
        MemberEntity member = memberRepository.findByUsername(userName)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USERNAME));

        RestaurantEntity restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESTAURANT));

        if(!member.getRoles().equals(ROLE_MANAGER)){
            throw new CustomException(ErrorCode.ONLY_ROLE_MANAGER);
        }

        if(!member.getId().equals(restaurant.getMember().getId())){
            throw new CustomException(ErrorCode.NOT_MY_RESTAURANT);
        }

        restaurantRepository.delete(restaurant);
    }


    //전체 매장 정보 가져오기
    public ResponseEntity<List<Restaurant>> getRestaurantList(Pageable pageable){
        Page<RestaurantEntity> page = restaurantRepository.findAll(pageable);
        List<RestaurantEntity> restaurantEntityList = page.getContent();

        List<Restaurant> restaurantList = new ArrayList<>();
        for(RestaurantEntity restaurantEntity : restaurantEntityList){
            restaurantEntity.calculateStar();
            Restaurant restaurant = Restaurant.builder()
                    .id(restaurantEntity.getId())
                    .storename(restaurantEntity.getStorename())
                    .explanation(restaurantEntity.getExplanation())
                    .location(restaurantEntity.getLocation())
                    .star(restaurantEntity.getStar())
                    .build();
            restaurantList.add(restaurant);
        }

        return ResponseEntity.ok(restaurantList);
    }

    //매장 상세 정보 확인
    public Restaurant.GetRestaurant getRestaurant(Long id){
        RestaurantEntity restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USERNAME));
        List<Review.GetReview> reviewList = restaurant.getReview().stream().map(
                reviewEntity -> {
                    Review.GetReview review = new Review.GetReview();
                    review.setId(reviewEntity.getId());
                    review.setRestaurantId(id);
                    review.setMemberId(reviewEntity.getMember().getId());
                    review.setContents(reviewEntity.getContents());
                    review.setStar(reviewEntity.getStar());
                    return review;
                }
        ).collect(Collectors.toList());
        restaurant.calculateStar();

        return Restaurant.GetRestaurant.builder()
                .stroename(restaurant.getStorename())
                .location(restaurant.getLocation())
                .explanation(restaurant.getExplanation())
                .star(restaurant.getStar())
                .reviewList(reviewList)
                .build();

    }

    //ㄱㄴㄷ 순으로 매장 정렬
    public ResponseEntity<List<Restaurant>> getRestaurantByStorename(Pageable pageable){
        Page<RestaurantEntity> restaurantPage = restaurantRepository.findAllByOrderByStorenameAsc(pageable);
        List<RestaurantEntity> restaurantEntityList = restaurantPage.getContent();

        List<Restaurant> restaurantList = restaurantEntityList.stream().map(this::convertTo).collect(Collectors.toList());

        return ResponseEntity.ok(restaurantList);
    }

    //별점 순으로 매장 정렬
    public ResponseEntity<List<Restaurant>> getRestaurantByStar(Pageable pageable){
        Page<RestaurantEntity> restaurantPage = restaurantRepository.findAllByOrderByStarDesc(pageable);
        List<RestaurantEntity> restaurantEntityList = restaurantPage.getContent();

        List<Restaurant> restaurantList = restaurantEntityList.stream().map(this::convertTo).collect(Collectors.toList());

        return ResponseEntity.ok(restaurantList);
    }

    //매장 명으로 매장 검색
    public ResponseEntity<List<Restaurant>> searchRestaurantName(String name, Pageable pageable){
        Page<RestaurantEntity> restaurantEntityPage = restaurantRepository.findByStorenameContainingIgnoreCase(name,pageable);
        List<RestaurantEntity> restaurantEntityList = restaurantEntityPage.getContent();
        List<Restaurant> restaurantList = restaurantEntityList.stream().map(this::convertTo).collect(Collectors.toList());

        return ResponseEntity.ok(restaurantList);
    }

    public Restaurant convertTo(RestaurantEntity restaurantEntity){
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurant.getId());
        restaurant.setStorename(restaurant.getStorename());
        restaurant.setExplanation(restaurant.getExplanation());
        restaurant.setLocation(restaurant.getLocation());
        restaurant.setStar(restaurant.getStar());

        return restaurant;
    }

}
