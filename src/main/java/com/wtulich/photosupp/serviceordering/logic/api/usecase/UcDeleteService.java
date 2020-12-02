package com.wtulich.photosupp.serviceordering.logic.api.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.logic.api.exception.EntityHasAssignedEntitiesException;

public interface UcDeleteService {

    void deleteService(Long id) throws EntityDoesNotExistException, EntityHasAssignedEntitiesException;
}
