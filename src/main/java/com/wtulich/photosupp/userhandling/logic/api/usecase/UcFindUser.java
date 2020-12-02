package com.wtulich.photosupp.userhandling.logic.api.usecase;

import java.util.Optional;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.userhandling.logic.api.to.UserEto;

import java.util.List;

public interface UcFindUser {

    Optional<UserEto> findUser(Long id) throws EntityDoesNotExistException;

    Optional<List<UserEto>> findAllUsers();

    Optional<List<UserEto>> findAllUsersByRoleId(Long roleId);
}
