package com.wtulich.photosupp.serviceordering.logic.impl.validator;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.IndicatorDao;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class IndicatorValidator {

    @Inject
    private IndicatorDao indicatorDao;

    public void verifyIfIndicatorNameAlreadyExists(String name) throws EntityAlreadyExistsException {
        if (indicatorDao.existsByName(name)) {
            throw new EntityAlreadyExistsException("Indicator with name " + name + " already exists");
        }
    }
}
