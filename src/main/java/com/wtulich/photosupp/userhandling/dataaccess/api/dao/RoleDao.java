package com.wtulich.photosupp.userhandling.dataaccess.api.dao;

import com.wtulich.photosupp.userhandling.dataaccess.api.entity.PermissionEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleDao extends JpaRepository<RoleEntity, Long> {

    boolean existsByName(String name);
    boolean existsByPermissionsIn(List<PermissionEntity> permissionEntities);
}
