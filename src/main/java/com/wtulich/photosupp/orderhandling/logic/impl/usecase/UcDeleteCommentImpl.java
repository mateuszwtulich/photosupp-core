package com.wtulich.photosupp.orderhandling.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.orderhandling.dataaccess.api.dao.CommentDao;
import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.CommentEntity;
import com.wtulich.photosupp.orderhandling.logic.api.usecase.UcDeleteComment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import javax.inject.Inject;
import javax.inject.Named;

@Validated
@Named
public class UcDeleteCommentImpl implements UcDeleteComment {

    private static final Logger LOG = LoggerFactory.getLogger(UcDeleteCommentImpl.class);
    private static final String DELETE_COMMENT_LOG = "Delete Comment with id {} in database.";

    @Inject
    private CommentDao commentDao;

    @Override
    public void deleteComment(Long id) throws EntityDoesNotExistException {
        CommentEntity commentEntity = commentDao.findById(id).orElseThrow(() ->
                new EntityDoesNotExistException("Comment with id " + id + " does not exist."));

        LOG.debug(DELETE_COMMENT_LOG, commentEntity.getId());

        commentDao.deleteById(commentEntity.getId());
    }
}
