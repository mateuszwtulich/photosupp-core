package com.wtulich.photosupp.orderhandling.logic.api.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;

public interface UcDeleteComment {

    void deleteComment(Long id) throws EntityDoesNotExistException;
}
