package com.wtulich.photosupp.orderhandling.dataaccess.api.dao;

import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.MediaContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaContentDao extends JpaRepository<MediaContentEntity, Long> {

    void deleteAllByOrder_OrderNumber(String orderNumber);

    List<MediaContentEntity> findAllByOrder_OrderNumber(String orderNumber);
}
