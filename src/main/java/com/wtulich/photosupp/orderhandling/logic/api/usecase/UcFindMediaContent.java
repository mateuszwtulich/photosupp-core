package com.wtulich.photosupp.orderhandling.logic.api.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.orderhandling.logic.api.to.MediaContentEto;

import java.util.List;
import java.util.Optional;

public interface UcFindMediaContent {

    Optional<List<MediaContentEto>> findAllMediaContentByOrderNumber(String orderNumber) throws EntityDoesNotExistException;
}
