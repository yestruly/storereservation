package com.storereservation.storereservation.controller;

import com.storereservation.storereservation.dto.Reserve;
import com.storereservation.storereservation.service.ReserveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reserve")
@RequiredArgsConstructor
@Api(tags = "예약 API")
public class CustomerReserveController {
    private final ReserveService reserveService;

    @ApiOperation(value = "예약 진행", notes = "예약할 상점과 일시 선택해 예약 진행")
    @PostMapping("/register")
    public ResponseEntity<String> registerReserve(@RequestBody Reserve reserve, Principal principal) {
        reserveService.registerReserve(reserve,principal);
        return ResponseEntity.ok("예약 성공");

    }

    @ApiOperation(value = "예약 취소", notes = "예약을 취소함")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReserve(@PathVariable("id") Long id,Principal principal) {
        reserveService.deleteReserve(id,principal);
        return ResponseEntity.ok( "삭제 성공");
    }

    @ApiOperation(value = "예약 변경", notes = "예약 일시 변경")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateReserve(@PathVariable("id") Long id, Principal principal, LocalDateTime dateTime) {
        reserveService.updateReserve(id,principal,dateTime);
        return ResponseEntity.ok( "삭제 성공");
    }

    @ApiOperation(value = "예약 전체 목록", notes = "고객이 예약한 전체의 목록을 가져옴")
    @GetMapping("/list")
    public ResponseEntity<List<Reserve>> getReserveListByCustomer(Principal principal) {
        List<Reserve> reserveList =  reserveService.getReserveListByCustomer(principal);
        return ResponseEntity.ok(reserveList);

    }

    @ApiOperation(value = "예약 확정", notes = "예약 시간 10분 전 도착 완료 시 예약 확정됨")
    @PutMapping("/{id}/checkIn")
    public ResponseEntity<String> checkArrive(@PathVariable("id") Long id, Principal principal){
        reserveService.checkArrive(id,principal);
        return ResponseEntity.ok("예약 확정");
    }

}
