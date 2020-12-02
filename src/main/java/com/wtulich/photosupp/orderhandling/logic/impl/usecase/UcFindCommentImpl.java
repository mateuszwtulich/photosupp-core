package com.wtulich.photosupp.orderhandling.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.orderhandling.dataaccess.api.dao.CommentDao;
import com.wtulich.photosupp.orderhandling.dataaccess.api.dao.OrderDao;
import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.CommentEntity;
import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.OrderEntity;
import com.wtulich.photosupp.orderhandling.logic.api.mapper.CommentMapper;
import com.wtulich.photosupp.orderhandling.logic.api.to.CommentEto;
import com.wtulich.photosupp.orderhandling.logic.api.usecase.UcFindComment;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.UserEntity;
import com.wtulich.photosupp.userhandling.logic.api.mapper.AccountMapper;
import com.wtulich.photosupp.userhandling.logic.api.mapper.UserMapper;
import com.wtulich.photosupp.userhandling.logic.api.to.UserEto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Validated
@Named
public class UcFindCommentImpl implements UcFindComment {

    private static final Logger LOG = LoggerFactory.getLogger(UcFindCommentImpl.class);
    private static final String ID_CANNOT_BE_NULL = "id cannot be a null value";
    private static final String GET_ALL_COMMENTS_LOG = "Get all Comments of Order with order number {} from database.";

    @Inject
    private CommentDao commentDao;

    @Inject
    private OrderDao orderDao;

    @Inject
    private CommentMapper commentMapper;

    @Inject
    private UserMapper userMapper;

    @Inject
    private AccountMapper accountMapper;

    @Override
    public Optional<List<CommentEto>> findAllCommentsByOrderNumber(String orderNumber) throws EntityDoesNotExistException {

        Objects.requireNonNull(orderNumber, ID_CANNOT_BE_NULL);
        OrderEntity orderEntity = orderDao.findByOrderNumber(orderNumber).orElseThrow(() ->
                new EntityDoesNotExistException("Order with order number " + orderNumber + " does not exist."));

        LOG.debug(GET_ALL_COMMENTS_LOG);

        return Optional.of(commentDao.findAllByOrder_OrderNumber(orderEntity.getOrderNumber()).stream()
                .map(commentEntity -> toCommentEto(commentEntity))
                .collect(Collectors.toList()));
    }

    private CommentEto toCommentEto(CommentEntity commentEntity){
        CommentEto commentEto = commentMapper.toCommentEto(commentEntity);
        commentEto.setOrderNumber(commentEntity.getOrder().getOrderNumber());
        commentEto.setUserEto(toUserEto(commentEntity.getUser()));

        return commentEto;
    }

    private UserEto toUserEto(UserEntity userEntity) {
        UserEto userEto = userMapper.toUserEto(userEntity);
        userEto.setAccountEto(accountMapper.toAccountEto(userEntity.getAccount()));

        return userEto;
    }
}
