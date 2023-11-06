package com.storereservation.storereservation.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String storename;

    @Column
    private String location;

    @Column
    private String explanation;

    @Column
    private Double star;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReviewEntity> review;

    @ManyToOne
    @JoinColumn(name = "member")
    private MemberEntity member;

    public void calculateStar(){
        if(review.isEmpty()){
            this.star = 0.0;
        }else{
            double sum = 0.0;
            for(ReviewEntity reviewEntity : review){
                sum+= reviewEntity.getStar();
            }

            this.star = Math.round((sum / review.size()) * 10.0) / 10.0;
        }
    }
}
