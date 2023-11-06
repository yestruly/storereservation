package com.storereservation.storereservation.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEntity {
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
    private String contents;
    @Column
    private int star;
}
