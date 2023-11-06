package com.storereservation.storereservation.repository;

import com.storereservation.storereservation.entity.MemberEntity;
import com.storereservation.storereservation.entity.ReserveEntity;
import com.storereservation.storereservation.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReserveRepository extends JpaRepository<ReserveEntity,Long> {
    List<ReserveEntity> findByMember(MemberEntity member);

    List<ReserveEntity> findByRestaurant(RestaurantEntity restaurant);
}
