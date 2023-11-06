package com.storereservation.storereservation.dto;

import com.storereservation.storereservation.entity.reserveConstants.ArriveStatus;
import com.storereservation.storereservation.entity.reserveConstants.ReserveStatus;
import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Reserve {
    private Long id;
    private Long memberId;
    private Long restaurantId;
    private String restaurantName;
    private LocalDateTime reserveDate;
    private Long phone;
    private ReserveStatus reserveStatus = ReserveStatus.DEFAULT;
    private ArriveStatus arriveStatus =ArriveStatus.DEFAULT;
}
