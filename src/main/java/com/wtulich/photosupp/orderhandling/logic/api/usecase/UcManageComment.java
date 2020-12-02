package com.wtulich.photosupp.orderhandling.logic.api.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.orderhandling.logic.api.to.CommentEto;
import com.wtulich.photosupp.orderhandling.logic.api.to.CommentTo;

import java.util.Optional;

public interface UcManageComment {

    Optional<CommentEto> createComment(CommentTo commentTo) throws EntityDoesNotExistException;

    Optional<CommentEto> updateComment(CommentTo commentTo, Long id) throws EntityDoesNotExistException;
}
