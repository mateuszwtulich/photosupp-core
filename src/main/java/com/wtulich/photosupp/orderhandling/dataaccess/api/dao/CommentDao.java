package com.wtulich.photosupp.orderhandling.dataaccess.api.dao;

import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentDao extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findAllByOrder_OrderNumber(String orderNumber);
}
