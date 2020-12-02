package com.wtulich.photosupp.serviceordering.logic.api.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.serviceordering.logic.api.to.IndicatorEto;
import com.wtulich.photosupp.serviceordering.logic.api.to.IndicatorTo;

import java.util.Optional;

public interface UcManageIndicator {

    Optional<IndicatorEto> createIndicator(IndicatorTo indicatorTo) throws EntityAlreadyExistsException;

    Optional<IndicatorEto> updateIndicator(IndicatorTo indicatorTo, Long id) throws EntityDoesNotExistException, EntityAlreadyExistsException;
}
