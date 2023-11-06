package com.storereservation.storereservation.repository;


import com.storereservation.storereservation.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    boolean existsByUsername(String user_name);

    Optional<MemberEntity> findByUsername(String user_name);
    //    Optional<MemberEntity> findByUsername(String user_name);
//    boolean existsByUsername(String username);
}
