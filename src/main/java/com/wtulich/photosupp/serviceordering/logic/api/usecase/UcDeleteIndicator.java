package com.wtulich.photosupp.serviceordering.logic.api.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.logic.api.exception.EntityHasAssignedEntitiesException;

public interface UcDeleteIndicator {

    void deleteIndicator(Long id) throws EntityDoesNotExistException, EntityHasAssignedEntitiesException;
}
