package com.wtulich.photosupp.userhandling.logic.impl.validator;

import com.wtulich.photosupp.userhandling.dataaccess.api.dao.AccountDao;
import com.wtulich.photosupp.userhandling.logic.api.exception.AccountAlreadyExistsException;
import com.wtulich.photosupp.userhandling.logic.api.to.AccountTo;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

@Named
public class AccountValidator {

    @Inject
    private AccountDao accountDao;

    public void verifyIfAccountAlreadyExists(AccountTo accountTo) throws AccountAlreadyExistsException {
        if(accountDao.existsByEmail(accountTo.getEmail())) {
            throw new AccountAlreadyExistsException("Account with email " + accountTo.getEmail() + " already exists");
        }
    }

    public void verifyIfValidEmailAddress(String email) throws AddressException {
        InternetAddress emailAddr = new InternetAddress(email);
        emailAddr.validate();
    }
}
