package com.wtulich.photosupp.orderhandling.dataaccess.api.dao;

import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDao extends JpaRepository<OrderEntity, Long> {

    boolean existsByBooking_Id(Long bookingId);

    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    List<OrderEntity> findAllByUser_Id(Long id);

    void deleteByOrderNumber(String orderNumber);
}
