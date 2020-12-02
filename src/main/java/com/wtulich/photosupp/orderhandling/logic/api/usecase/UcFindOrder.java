package com.wtulich.photosupp.orderhandling.logic.api.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.orderhandling.logic.api.to.OrderEto;

import java.util.List;
import java.util.Optional;

public interface UcFindOrder {

    Optional<List<OrderEto>> findAllOrders();

    Optional<OrderEto> findOrder(String orderNumber) throws EntityDoesNotExistException;

    Optional<List<OrderEto>> findAllOrdersByUserId(Long userId) throws EntityDoesNotExistException;
}
