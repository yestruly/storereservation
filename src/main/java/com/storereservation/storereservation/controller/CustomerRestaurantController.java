package com.storereservation.storereservation.controller;

import com.storereservation.storereservation.dto.Restaurant;
import com.storereservation.storereservation.service.RestaurantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurant")
@Api(tags = "매장 정보 검색 API")
public class CustomerRestaurantController {
    private final RestaurantService restaurantService;

    @ApiOperation(value = "매장 전체 목록", notes = "등록된 매장의 전체 목록을 가져움")
    @GetMapping
    public ResponseEntity<List<Restaurant>> getRestaurantList(Pageable pageable) {
        return restaurantService.getRestaurantList(pageable);

    }

    @ApiOperation(value = "매장 상세 내용", notes = "매장의 상세 내용을 가져옴")
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant.GetRestaurant> getRestaurant(@PathVariable("id") Long id) {
        Restaurant.GetRestaurant restaurant = restaurantService.getRestaurant(id);
        return ResponseEntity.ok(restaurant);

    }

    @ApiOperation(value = "가나다순", notes = "매장명을 가나다순으로 정렬")
    @GetMapping("/abc")
    public ResponseEntity<List<Restaurant>> getRestaurantByStorename(Pageable pageable) {
        return restaurantService.getRestaurantByStorename(pageable);

    }

    @ApiOperation(value = "별점순", notes = "매장명을 별점순으로 정렬")
    @GetMapping("/star")
    public ResponseEntity<List<Restaurant>> getRestaurantByStar(Pageable pageable) {
        return restaurantService.getRestaurantByStar(pageable);
    }

    @ApiOperation(value = "매장명 검색", notes = "매장명으로 매장 정보 검색")
    @GetMapping("/{storename}")
    public ResponseEntity<List<Restaurant>> getRestaurant(@PathVariable("storename") String storename, Pageable pageable) {
         return restaurantService.searchRestaurantName(storename,pageable);

    }
}
