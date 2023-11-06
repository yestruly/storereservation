package com.storereservation.storereservation.repository;

import com.storereservation.storereservation.entity.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    boolean existsByStorename(String storename);

    Page<RestaurantEntity> findAll(Pageable pageable);

    Page<RestaurantEntity> findByStorenameContainingIgnoreCase(String name, Pageable pageable);

    Page<RestaurantEntity> findAllByOrderByStarDesc(Pageable pageable);

    Page<RestaurantEntity> findAllByOrderByStorenameAsc(Pageable pageable);

    RestaurantEntity findByStorename(String storename);
}
