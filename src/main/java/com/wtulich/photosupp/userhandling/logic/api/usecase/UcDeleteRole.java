package com.wtulich.photosupp.userhandling.logic.api.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.userhandling.logic.api.exception.RoleHasAssignedUsersException;

public interface UcDeleteRole {

    void deleteRole(Long id) throws EntityDoesNotExistException, RoleHasAssignedUsersException;
}
