package com.storereservation.storereservation.entity;

import com.storereservation.storereservation.entity.reserveConstants.ArriveStatus;
import com.storereservation.storereservation.entity.reserveConstants.ReserveStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reserve")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReserveEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member")
    private MemberEntity member;

    @ManyToOne
    @JoinColumn(name ="restaurant")
    private RestaurantEntity restaurant;

    @Column
    private LocalDateTime reservedate;
    @Column
    private Long phone;
    @Column
    @Enumerated(EnumType.STRING)
    private ReserveStatus reserveStatus = ReserveStatus.DEFAULT;
    @Column
    @Enumerated(EnumType.STRING)
    private ArriveStatus arriveStatus =ArriveStatus.DEFAULT;


}
