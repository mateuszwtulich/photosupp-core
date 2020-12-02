package com.wtulich.photosupp.serviceordering.logic.api.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.serviceordering.logic.api.to.ServiceEto;
import com.wtulich.photosupp.serviceordering.logic.api.to.ServiceTo;

import java.util.Optional;

public interface UcManageService {

    Optional<ServiceEto> createService(ServiceTo serviceTo) throws EntityAlreadyExistsException, EntityDoesNotExistException;

    Optional<ServiceEto> updateService(ServiceTo serviceTo, Long id) throws EntityDoesNotExistException, EntityAlreadyExistsException;
}
