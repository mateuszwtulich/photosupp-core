package com.wtulich.photosupp.orderhandling.logic.impl;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.logic.api.exception.EntityHasAssignedEntitiesException;
import com.wtulich.photosupp.orderhandling.logic.api.OrderHandling;
import com.wtulich.photosupp.orderhandling.logic.api.exception.OrderStatusInappropriateException;
import com.wtulich.photosupp.orderhandling.logic.api.to.*;
import com.wtulich.photosupp.orderhandling.logic.api.usecase.*;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderHandlingImpl implements OrderHandling {

    @Inject
    private UcDeleteComment ucDeleteComment;

    @Inject
    private UcDeleteMediaContent ucDeleteMediaContent;

    @Inject
    private UcDeleteOrder ucDeleteOrder;

    @Inject
    private UcFindComment ucFindComment;

    @Inject
    private UcFindMediaContent ucFindMediaContent;

    @Inject
    private UcFindOrder ucFindOrder;

    @Inject
    private UcManageComment ucManageComment;

    @Inject
    private UcManageMediaContent ucManageMediaContent;

    @Inject
    private UcManageOrder ucManageOrder;

    @Override
    public void deleteComment(Long id) throws EntityDoesNotExistException {
        ucDeleteComment.deleteComment(id);
    }

    @Override
    public void deleteMediaContent(Long id) throws EntityDoesNotExistException {
        ucDeleteMediaContent.deleteMediaContent(id);
    }

    @Override
    public void deleteAllMediaContent(String orderNumber) throws EntityDoesNotExistException {
        ucDeleteMediaContent.deleteAllMediaContent(orderNumber);
    }

    @Override
    public void deleteOrder(String orderNumber) throws EntityDoesNotExistException, EntityHasAssignedEntitiesException {
        ucDeleteOrder.deleteOrder(orderNumber);
    }

    @Override
    public Optional<List<CommentEto>> findAllCommentsByOrderNumber(String orderNumber) throws EntityDoesNotExistException {
        return ucFindComment.findAllCommentsByOrderNumber(orderNumber);
    }

    @Override
    public Optional<List<MediaContentEto>> findAllMediaContentByOrderNumber(String orderNumber) throws EntityDoesNotExistException {
        return ucFindMediaContent.findAllMediaContentByOrderNumber(orderNumber);
    }

    @Override
    public Optional<List<OrderEto>> findAllOrders() {
        return ucFindOrder.findAllOrders();
    }

    @Override
    public Optional<OrderEto> findOrder(String orderNumber) throws EntityDoesNotExistException {
        return ucFindOrder.findOrder(orderNumber);
    }

    @Override
    public Optional<List<OrderEto>> findAllOrdersByUserId(Long userId) throws EntityDoesNotExistException {
        return ucFindOrder.findAllOrdersByUserId(userId);
    }

    @Override
    public Optional<MediaContentEto> addMediaContent(MediaContentTo mediaContentTo) throws EntityDoesNotExistException {
        return ucManageMediaContent.addMediaContent(mediaContentTo);
    }

    @Override
    public Optional<OrderEto> createOrder(OrderTo orderTo) throws EntityDoesNotExistException, EntityAlreadyExistsException {
        return ucManageOrder.createOrder(orderTo);
    }

    @Override
    public Optional<OrderEto> updateOrder(OrderTo orderTo, String orderNumber) throws EntityDoesNotExistException, EntityAlreadyExistsException {
        return ucManageOrder.updateOrder(orderTo, orderNumber);
    }

    @Override
    public Optional<OrderEto> finishOrder(String orderNumber) throws EntityDoesNotExistException, OrderStatusInappropriateException {
        return ucManageOrder.finishOrder(orderNumber);
    }

    @Override
    public Optional<OrderEto> acceptOrder(String orderNumber) throws EntityDoesNotExistException, OrderStatusInappropriateException {
        return ucManageOrder.acceptOrder(orderNumber);
    }

    @Override
    public Optional<OrderEto> sendOrderToVerification(String orderNumber) throws EntityDoesNotExistException, OrderStatusInappropriateException {
        return ucManageOrder.sendOrderToVerification(orderNumber);
    }

    @Override
    public Optional<CommentEto> createComment(CommentTo commentTo) throws EntityDoesNotExistException {
        return ucManageComment.createComment(commentTo);
    }

    @Override
    public Optional<CommentEto> updateComment(CommentTo commentTo, Long id) throws EntityDoesNotExistException {
        return ucManageComment.updateComment(commentTo, id);
    }
}
