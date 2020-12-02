package com.wtulich.photosupp.orderhandling.logic.api.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.orderhandling.logic.api.to.MediaContentEto;
import com.wtulich.photosupp.orderhandling.logic.api.to.MediaContentTo;

import java.util.Optional;

public interface UcManageMediaContent {

    Optional<MediaContentEto> addMediaContent(MediaContentTo mediaContentTo) throws EntityDoesNotExistException;
}
