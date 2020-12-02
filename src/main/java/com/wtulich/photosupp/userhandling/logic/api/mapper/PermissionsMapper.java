package com.wtulich.photosupp.userhandling.logic.api.mapper;

import com.wtulich.photosupp.userhandling.dataaccess.api.entity.PermissionEntity;
import com.wtulich.photosupp.userhandling.logic.api.to.PermissionEto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionsMapper {
    PermissionEto toPermissionEto(PermissionEntity permissionEntity);
}
