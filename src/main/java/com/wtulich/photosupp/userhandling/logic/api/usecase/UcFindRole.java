package com.wtulich.photosupp.userhandling.logic.api.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.userhandling.logic.api.to.PermissionEto;
import com.wtulich.photosupp.userhandling.logic.api.to.RoleEto;

import java.util.List;
import java.util.Optional;

public interface UcFindRole {

    Optional<RoleEto> findRole(Long id) throws EntityDoesNotExistException;

    Optional<List<RoleEto>> findAllRoles();

    Optional<List<PermissionEto>> findAllPermissions();
}
