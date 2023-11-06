package com.storereservation.storereservation.service;

import com.storereservation.storereservation.configuration.exception.CustomException;
import com.storereservation.storereservation.configuration.exception.ErrorCode;
import com.storereservation.storereservation.dto.Reserve;
import com.storereservation.storereservation.entity.MemberEntity;
import com.storereservation.storereservation.entity.ReserveEntity;
import com.storereservation.storereservation.entity.RestaurantEntity;
import com.storereservation.storereservation.entity.reserveConstants.ReserveStatus;
import com.storereservation.storereservation.repository.MemberRepository;
import com.storereservation.storereservation.repository.ReserveRepository;
import com.storereservation.storereservation.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.storereservation.storereservation.dto.constants.Role.ROLE_MANAGER;
import static com.storereservation.storereservation.entity.reserveConstants.ArriveStatus.ARRIVE;
import static com.storereservation.storereservation.entity.reserveConstants.ArriveStatus.CANCEL;
import static com.storereservation.storereservation.entity.reserveConstants.ReserveStatus.APPROVE;

@Service
@AllArgsConstructor
public class ReserveService {
    private final ReserveRepository reserveRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;

    //예약 등록
    @Transactional
    public void registerReserve(Reserve reserve, Principal principal){
        RestaurantEntity restaurant = restaurantRepository.findById(reserve.getRestaurantId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESTAURANT));

        MemberEntity member = memberRepository.findById(reserve.getMemberId())
                .orElseThrow(() ->  new CustomException(ErrorCode.NOT_FOUND_USERNAME));

        if(reserve.getReserveDate().isBefore(LocalDateTime.now())){
            throw new CustomException(ErrorCode.INVALID_RESERVE_DATE);
        }

        ReserveEntity reservation = new ReserveEntity();
        reservation.setRestaurant(restaurant);
        reservation.setMember(member);
        reservation.setReservedate(reserve.getReserveDate());
        reservation.setReserveStatus(reserve.getReserveStatus());
        reserveRepository.save(reservation);

    }

    //예약 취소
    @Transactional
    public void deleteReserve(Long id, Principal principal){
        String userName = principal.getName();
        MemberEntity member = memberRepository.findByUsername(userName)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USERNAME));
        ReserveEntity reserve = reserveRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_APPROVE_RESERVE));

        if(!member.getUsername().equals(reserve.getMember().getUsername())){
            throw new CustomException(ErrorCode.NOT_MY_RESERVE);
        }

         reserveRepository.delete(reserve);

    }

    //예약 일시 변경
    @Transactional
    public void updateReserve(Long id, Principal principal, LocalDateTime dateTime){
        String userName = principal.getName();
        MemberEntity member = memberRepository.findByUsername(userName)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USERNAME));
        ReserveEntity reserve = reserveRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_APPROVE_RESERVE));

        if(!member.getUsername().equals(reserve.getMember().getUsername())){
            throw new CustomException(ErrorCode.NOT_MY_RESERVE);
        }
        if(dateTime.isBefore(LocalDateTime.now())){
            throw new CustomException(ErrorCode.INVALID_RESERVE_DATE);
        }

        reserve.setReservedate(dateTime);
        reserveRepository.save(reserve);
    }

    //도착 여부 설정
    @Transactional
    public void checkArrive(Long id, Principal principal){
        String userName = principal.getName();
        MemberEntity member = memberRepository.findByUsername(userName)
                .orElseThrow(() ->new CustomException(ErrorCode.NOT_FOUND_USERNAME));
        ReserveEntity reserve = reserveRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_APPROVE_RESERVE));

        if(!member.getUsername().equals(reserve.getMember().getUsername())){
            throw new CustomException(ErrorCode.NOT_MY_RESERVE);
        }
        if(!reserve.getReserveStatus().equals(APPROVE)){
            throw new CustomException(ErrorCode.NOT_APPROVE_RESERVE);
        }

        LocalDateTime reserveDate = reserve.getReservedate();
        LocalDateTime checkIn = reserveDate.minusMinutes(10);
        LocalDateTime arrive = LocalDateTime.now();
        if(arrive.isAfter(checkIn)){
            reserve.setArriveStatus(CANCEL);
            reserveRepository.save(reserve);
            throw new CustomException(ErrorCode.CANCEL_RESERVE);
        }

        reserve.setArriveStatus(ARRIVE);
        reserveRepository.save(reserve);
    }

    //매장 측에서 예약 관리
    @Transactional
    public void updateReserveByRestaurant(Long id, ReserveStatus status, Principal principal){
        String userName = principal.getName();
        MemberEntity member = memberRepository.findByUsername(userName)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USERNAME));
        ReserveEntity reserve = reserveRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_APPROVE_RESERVE));

        RestaurantEntity restaurant = reserve.getRestaurant();
        if(!member.getRoles().equals(ROLE_MANAGER)){
            throw new CustomException(ErrorCode.ONLY_ROLE_MANAGER);
        }
        if(!userName.equals(member.getUsername())){
            throw new CustomException(ErrorCode.NOT_MY_RESTAURANT);
        }

        reserve.setReserveStatus(status);
        reserveRepository.save(reserve);
    }


    public List<Reserve> getReserveListByCustomer(Principal principal){
        MemberEntity member = memberRepository.findByUsername(principal.getName())
                .orElseThrow(() ->new CustomException(ErrorCode.NOT_FOUND_USERNAME));

        List<ReserveEntity> reserveEntityList = reserveRepository.findByMember(member);
        return reserveEntityList.stream().map(this::convertTo).collect(Collectors.toList());
    }

    public List<Reserve> getReserveListByRestaurant(Long id,Principal principal){
        String userName = principal.getName();

        MemberEntity member = memberRepository.findByUsername(userName)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USERNAME));
        RestaurantEntity restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESTAURANT));

        if(!member.getRoles().equals(ROLE_MANAGER)){
            throw new CustomException(ErrorCode.ONLY_ROLE_MANAGER);
        }
        if(!userName.equals(member.getUsername())){
            throw new CustomException(ErrorCode.NOT_MY_RESTAURANT);
        }

        List<ReserveEntity> reseveList = reserveRepository.findByRestaurant(restaurant);
        return reseveList.stream().map(this::convertTo).collect(Collectors.toList());

    }

    private Reserve convertTo(ReserveEntity reserveEntity) {
        Reserve reserve = new Reserve();
        reserve.setId(reserve.getId());
        reserve.setMemberId(reserve.getMemberId());
        reserve.setRestaurantId(reserve.getRestaurantId());
        reserve.setRestaurantName(reserve.getRestaurantName());
        reserve.setReserveDate(reserve.getReserveDate());
        reserve.setPhone(reserve.getPhone());
        reserve.setReserveStatus(reserve.getReserveStatus());
        reserve.setArriveStatus(reserve.getArriveStatus());

        return reserve;
    }

}
