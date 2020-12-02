package com.wtulich.photosupp.serviceordering.dataaccess.api.dao;

import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingDao extends JpaRepository<BookingEntity, Long> {

    List<BookingEntity> findAllByService_Id(Long id);
    List<BookingEntity> findAllByUser_Id(Long id);
    boolean existsByName(String name);
}
