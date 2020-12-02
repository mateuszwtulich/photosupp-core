package com.wtulich.photosupp.serviceordering.dataaccess.api.dao;

import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.PriceIndicatorEntity;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.PriceIndicatorKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceIndicatorDao extends JpaRepository<PriceIndicatorEntity, Long> {

    List<PriceIndicatorEntity> findAllByIndicator_Id(Long id);
}
