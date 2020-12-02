package com.wtulich.photosupp.userhandling.dataaccess.api.dao;

import com.wtulich.photosupp.userhandling.dataaccess.api.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionDao extends JpaRepository<PermissionEntity, Long> {
}
