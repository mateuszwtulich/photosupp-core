package com.wtulich.photosupp.serviceordering.logic.api.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.logic.api.exception.UnprocessableEntityException;
import com.wtulich.photosupp.serviceordering.logic.api.to.CalculateCto;
import com.wtulich.photosupp.serviceordering.logic.api.to.CalculateTo;

import java.util.Optional;

public interface UcCalculateService {

    Optional<CalculateCto> calculateService(CalculateTo calculateTo) throws EntityDoesNotExistException, UnprocessableEntityException;
}
