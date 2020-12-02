package com.wtulich.photosupp.orderhandling.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.logic.api.exception.EntityHasAssignedEntitiesException;
import com.wtulich.photosupp.orderhandling.dataaccess.api.dao.MediaContentDao;
import com.wtulich.photosupp.orderhandling.dataaccess.api.dao.OrderDao;
import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.OrderEntity;
import com.wtulich.photosupp.orderhandling.logic.api.usecase.UcDeleteOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import javax.inject.Inject;
import javax.inject.Named;

@Validated
@Named
public class UcDeleteOrderImpl implements UcDeleteOrder {

    private static final Logger LOG = LoggerFactory.getLogger(UcDeleteOrderImpl.class);
    private static final String DELETE_ORDER_LOG = "Delete Order with order status {} in database.";

    @Inject
    private OrderDao orderDao;

    @Inject
    private MediaContentDao mediaContentDao;

    @Override
    public void deleteOrder(String orderStatus) throws EntityDoesNotExistException, EntityHasAssignedEntitiesException {
        OrderEntity orderEntity = orderDao.findByOrderNumber(orderStatus).orElseThrow(() ->
                new EntityDoesNotExistException("Order with order status " + orderStatus + " does not exist."));

        if(mediaContentDao.findAllByOrder_OrderNumber(orderEntity.getOrderNumber()).isEmpty()){
            LOG.debug(DELETE_ORDER_LOG, orderEntity.getOrderNumber());

            orderDao.deleteByOrderNumber(orderEntity.getOrderNumber());
        } else {
            throw new EntityHasAssignedEntitiesException("Order with order status " + orderStatus + " has assigned media content.");
        }
    }
}
