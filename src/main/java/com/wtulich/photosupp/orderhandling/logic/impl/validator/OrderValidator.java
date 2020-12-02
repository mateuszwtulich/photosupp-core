package com.wtulich.photosupp.orderhandling.logic.impl.validator;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.general.utils.enums.OrderStatus;
import com.wtulich.photosupp.orderhandling.dataaccess.api.dao.OrderDao;
import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.OrderEntity;
import com.wtulich.photosupp.orderhandling.logic.api.exception.OrderStatusInappropriateException;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.BookingEntity;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class OrderValidator {
    private static String ORDER_STATUS_INAPPROPRIATE = "Status of Order is inappropriate for that change.";

    @Inject
    private OrderDao orderDao;

    public void verifyIfBookingHasAssignedOrders(Long bookingId) throws EntityAlreadyExistsException {
        if(orderDao.existsByBooking_Id(bookingId)){
            throw new EntityAlreadyExistsException("Booking with id " + bookingId + " has assigned Order.");
        }
    }

    public void verifyIfOrderCanBeFinished(OrderEntity orderEntity) throws OrderStatusInappropriateException {
        if(!orderEntity.getStatus().equals(OrderStatus.TO_VERIFY)){
            throw new OrderStatusInappropriateException(ORDER_STATUS_INAPPROPRIATE);
        }
    }

    public void verifyIfOrderCanBeAccepted(OrderEntity orderEntity) throws OrderStatusInappropriateException {
        if(!orderEntity.getStatus().equals(OrderStatus.NEW)){
            throw new OrderStatusInappropriateException(ORDER_STATUS_INAPPROPRIATE);
        }
    }

    public void verifyIfOrderCanBeSentToVerification(OrderEntity orderEntity) throws OrderStatusInappropriateException {
        if(!orderEntity.getStatus().equals(OrderStatus.IN_PROGRESS)){
            throw new OrderStatusInappropriateException(ORDER_STATUS_INAPPROPRIATE);
        }
    }
}
