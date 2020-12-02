package com.wtulich.photosupp.serviceordering.logic.impl.validator;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.ServiceDao;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class ServiceValidator {

    @Inject
    private ServiceDao serviceDao;

    public void verifyIfServiceNameAlreadyExists(String name) throws EntityAlreadyExistsException {
        if (serviceDao.existsByName(name)) {
            throw new EntityAlreadyExistsException("Service with name " + name + " already exists");
        }
    }
}
