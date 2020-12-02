package com.wtulich.photosupp.orderhandling.logic.api.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;

public interface UcDeleteMediaContent {

    void deleteMediaContent(Long id) throws EntityDoesNotExistException;
    void deleteAllMediaContent(String orderNumber) throws EntityDoesNotExistException;
}
