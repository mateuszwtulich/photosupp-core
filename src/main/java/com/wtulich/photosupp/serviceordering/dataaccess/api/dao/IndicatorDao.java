package com.wtulich.photosupp.serviceordering.dataaccess.api.dao;

import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.IndicatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndicatorDao extends JpaRepository<IndicatorEntity, Long> {

    boolean existsByName(String name);
}
