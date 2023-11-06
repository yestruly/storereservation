package com.storereservation.storereservation.controller;

import com.storereservation.storereservation.dto.Restaurant;
import com.storereservation.storereservation.service.RestaurantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/partner/restaurant")
@Api(tags = "파트너 매장 API")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @ApiOperation(value = "매장 등록", notes = "이름, 설명, 주소 작성")
    @PostMapping("/register")
    public ResponseEntity<String> registerRestaurant(@RequestBody Restaurant.RegisterRestaurant restaurant, Principal principal) {
        restaurantService.registerRestaurant(restaurant,principal);
        return ResponseEntity.ok("등록 성공");

    }

    @ApiOperation(value = "매장 정보 수정", notes = "이름, 설명, 주소 수정")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateRestaurant(@PathVariable("id") Long id,
            @RequestBody Restaurant.UpdateRestaurant restaurant, Principal principal) {
        restaurantService.updateRestaurant(id,restaurant,principal);
        return ResponseEntity.ok("수정 성공");

    }

    @ApiOperation(value = "매장 삭제", notes = "매장 정보 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable("id") Long id,Principal principal) {
        restaurantService.deleteRestaurant(id,principal);
        return ResponseEntity.ok( "삭제 성공");
    }

}
