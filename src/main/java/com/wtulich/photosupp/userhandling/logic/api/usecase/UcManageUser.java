package com.wtulich.photosupp.userhandling.logic.api.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.userhandling.logic.api.exception.AccountAlreadyExistsException;
import com.wtulich.photosupp.userhandling.logic.api.to.AccountEto;
import com.wtulich.photosupp.userhandling.logic.api.to.AccountTo;
import com.wtulich.photosupp.userhandling.logic.api.to.UserEto;
import com.wtulich.photosupp.userhandling.logic.api.to.UserTo;
import org.springframework.validation.Errors;

import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface
UcManageUser {

    Optional<UserEto> createUserAndAccountEntities(UserTo userTo, HttpServletRequest request, Errors errors)
            throws AccountAlreadyExistsException, AddressException, EntityDoesNotExistException;

    Optional<UserEto> updateUser(UserTo userTo, Long userId) throws EntityDoesNotExistException;

    Optional<AccountEto> updateUserAccount(AccountTo accountTo, Long userId) throws AccountAlreadyExistsException, AddressException, EntityDoesNotExistException;

    void updatePassword(AccountTo accountTo) throws EntityDoesNotExistException;
}
