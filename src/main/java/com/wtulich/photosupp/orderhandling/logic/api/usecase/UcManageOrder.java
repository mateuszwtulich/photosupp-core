package com.wtulich.photosupp.orderhandling.logic.api.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.orderhandling.logic.api.exception.OrderStatusInappropriateException;
import com.wtulich.photosupp.orderhandling.logic.api.to.OrderEto;
import com.wtulich.photosupp.orderhandling.logic.api.to.OrderTo;

import java.util.Optional;

public interface UcManageOrder {

    Optional<OrderEto> createOrder(OrderTo orderTo) throws EntityDoesNotExistException, EntityAlreadyExistsException;

    Optional<OrderEto> updateOrder(OrderTo orderTo, String orderNumber) throws EntityDoesNotExistException, EntityAlreadyExistsException;

    Optional<OrderEto> finishOrder(String orderNumber)  throws EntityDoesNotExistException, OrderStatusInappropriateException;

    Optional<OrderEto> acceptOrder(String orderNumber)  throws EntityDoesNotExistException, OrderStatusInappropriateException;

    Optional<OrderEto> sendOrderToVerification(String orderNumber)  throws EntityDoesNotExistException, OrderStatusInappropriateException;
}
