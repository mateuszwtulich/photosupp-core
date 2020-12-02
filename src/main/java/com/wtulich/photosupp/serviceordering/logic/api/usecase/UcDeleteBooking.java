package com.wtulich.photosupp.serviceordering.logic.api.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;

public interface UcDeleteBooking {

    void deleteBooking(Long id) throws EntityDoesNotExistException, EntityAlreadyExistsException;
}
