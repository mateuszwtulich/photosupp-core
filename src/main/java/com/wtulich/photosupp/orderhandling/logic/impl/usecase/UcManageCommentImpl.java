package com.wtulich.photosupp.orderhandling.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.orderhandling.dataaccess.api.dao.CommentDao;
import com.wtulich.photosupp.orderhandling.dataaccess.api.dao.OrderDao;
import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.CommentEntity;
import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.OrderEntity;
import com.wtulich.photosupp.orderhandling.logic.api.mapper.CommentMapper;
import com.wtulich.photosupp.orderhandling.logic.api.to.CommentEto;
import com.wtulich.photosupp.orderhandling.logic.api.to.CommentTo;
import com.wtulich.photosupp.orderhandling.logic.api.usecase.UcManageComment;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.UserDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.UserEntity;
import com.wtulich.photosupp.userhandling.logic.api.mapper.AccountMapper;
import com.wtulich.photosupp.userhandling.logic.api.mapper.UserMapper;
import com.wtulich.photosupp.userhandling.logic.api.to.UserEto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Validated
@Named
public class UcManageCommentImpl implements UcManageComment {

    private static final Logger LOG = LoggerFactory.getLogger(UcManageCommentImpl.class);
    private static final String ID_CANNOT_BE_NULL = "id cannot be a null value";
    private static final String CREATE_COMMENT_LOG = "Create Comment with content {} in database.";
    private static final String UPDATE_COMMENT_LOG = "Update Comment with id {} from database.";

    @Inject
    private CommentDao commentDao;

    @Inject
    private OrderDao orderDao;

    @Inject
    private UserDao userDao;

    @Inject
    private CommentMapper commentMapper;

    @Inject
    private UserMapper userMapper;

    @Inject
    private AccountMapper accountMapper;

    @Override
    public Optional<CommentEto> createComment(CommentTo commentTo) throws EntityDoesNotExistException {
        LOG.debug(CREATE_COMMENT_LOG, commentTo.getContent());

        CommentEntity commentEntity = toCommentEntity(commentTo);
        return Optional.of(toCommentEto(commentDao.save(commentEntity)));
    }

    @Override
    public Optional<CommentEto> updateComment(CommentTo commentTo, Long id) throws EntityDoesNotExistException {
        Objects.requireNonNull(id, ID_CANNOT_BE_NULL);

        CommentEntity commentEntity = commentDao.findById(id).orElseThrow(() ->
                new EntityDoesNotExistException("Comment with id " + id + " does not exist."));

        LOG.debug(UPDATE_COMMENT_LOG, id);

        commentEntity.setContent(commentTo.getContent());

        return Optional.of(toCommentEto(commentEntity));
    }

    private CommentEto toCommentEto(CommentEntity commentEntity){
        CommentEto commentEto = commentMapper.toCommentEto(commentEntity);
        commentEto.setOrderNumber(commentEntity.getOrder().getOrderNumber());
        commentEto.setUserEto(toUserEto(commentEntity.getUser()));

        return commentEto;
    }

    private CommentEntity toCommentEntity(CommentTo commentTo) throws EntityDoesNotExistException {
        CommentEntity commentEntity = commentMapper.toCommentEntity(commentTo);

        commentEntity.setOrder(getOrderByOrderNumber(commentTo.getOrderNumber()));
        commentEntity.setUser(getUserById(commentTo.getUserId()));
        commentEntity.setCreatedAt(LocalDate.now());

        return commentEntity;
    }

    private OrderEntity getOrderByOrderNumber(String orderNumber) throws EntityDoesNotExistException {
        return orderDao.findByOrderNumber(orderNumber).orElseThrow(() ->
                new EntityDoesNotExistException("Order with order number " + orderNumber + " does not exist."));
    }

    private UserEntity getUserById(Long userId) throws EntityDoesNotExistException {
        return userDao.findById(userId).orElseThrow(() ->
                new EntityDoesNotExistException("User with id " + userId + " does not exist."));
    }

    private UserEto toUserEto(UserEntity userEntity) {
        UserEto userEto = userMapper.toUserEto(userEntity);
        userEto.setAccountEto(accountMapper.toAccountEto(userEntity.getAccount()));

        return userEto;
    }
}
