package com.storereservation.storereservation.controller;

import com.storereservation.storereservation.dto.Reserve;
import com.storereservation.storereservation.entity.reserveConstants.ReserveStatus;
import com.storereservation.storereservation.service.ReserveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reserve")
@Api(tags = "매장 예약 관리 API")
public class RestaurantReserveController {
    ReserveService reserveService;

    @ApiOperation(value = "매장측 예약 변경", tags = "매장에서 예약 상태 변경(취소, 확정)")
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateReserveByRestaurant(@PathVariable("id") Long id,
                                                            @RequestParam("status") ReserveStatus status, Principal principal){
        reserveService.updateReserveByRestaurant(id, status, principal);
        return ResponseEntity.ok("예약 변경 성공");
    }

    @ApiOperation(value = "예약 전체 목록", notes = "매장 전체의 목록을 가져옴")
    @GetMapping("/{id}/list")
    public ResponseEntity<List<Reserve>> getReserveListByRestaurant(@PathVariable("id") Long id,Principal principal) {
        List<Reserve> reserveList =  reserveService.getReserveListByRestaurant(id,principal);
        return ResponseEntity.ok(reserveList);

    }

}
