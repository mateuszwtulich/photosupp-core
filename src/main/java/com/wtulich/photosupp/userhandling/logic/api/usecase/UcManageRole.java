package com.wtulich.photosupp.userhandling.logic.api.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.userhandling.logic.api.to.RoleEto;
import com.wtulich.photosupp.userhandling.logic.api.to.RoleTo;

import java.util.Optional;

public interface UcManageRole {

    Optional<RoleEto> createRole(RoleTo roleTo) throws EntityAlreadyExistsException, EntityDoesNotExistException;

    Optional<RoleEto> updateRole(RoleTo roleTo, Long id) throws EntityAlreadyExistsException, EntityDoesNotExistException;
}
