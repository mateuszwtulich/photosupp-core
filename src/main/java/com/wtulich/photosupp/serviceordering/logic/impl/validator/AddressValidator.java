package com.wtulich.photosupp.serviceordering.logic.impl.validator;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.AddressDao;
import com.wtulich.photosupp.serviceordering.logic.api.to.AddressTo;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class AddressValidator {

    @Inject
    private AddressDao addressDao;

    public boolean isAddressAlreadyExists(AddressTo addressTo) {
        boolean isAddressAlreadyExists = false;
        if (addressDao.existsByCityAndStreetAndBuildingNumberAndApartmentNumberAndPostalCode(
                addressTo.getCity(),
                addressTo.getStreet(),
                addressTo.getBuildingNumber(),
                addressTo.getApartmentNumber(),
                addressTo.getPostalCode())) {
            isAddressAlreadyExists = true;
        }

        return isAddressAlreadyExists;
    }
}
