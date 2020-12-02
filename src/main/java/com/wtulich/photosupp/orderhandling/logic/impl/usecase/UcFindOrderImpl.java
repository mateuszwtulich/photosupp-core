package com.wtulich.photosupp.orderhandling.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.orderhandling.dataaccess.api.dao.OrderDao;
import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.OrderEntity;
import com.wtulich.photosupp.orderhandling.logic.api.mapper.OrderMapper;
import com.wtulich.photosupp.orderhandling.logic.api.to.OrderEto;
import com.wtulich.photosupp.orderhandling.logic.api.usecase.UcFindOrder;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.BookingEntity;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.AddressMapper;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.BookingMapper;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.IndicatorMapper;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.ServiceMapper;
import com.wtulich.photosupp.serviceordering.logic.api.to.BookingEto;
import com.wtulich.photosupp.serviceordering.logic.api.to.PriceIndicatorEto;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Validated
@Named
public class UcFindOrderImpl implements UcFindOrder {

    private static final Logger LOG = LoggerFactory.getLogger(UcFindOrderImpl.class);
    private static final String ID_CANNOT_BE_NULL = "id cannot be a null value";
    private static final String GET_ORDER_LOG = "Get Order with order number {} from database.";
    private static final String GET_ALL_ORDERS_LOG = "Get all Orders from database.";
    private static final String GET_ALL_ORDERS_BY_USER_ID_LOG = "Get all Orders from database with user id {} from database.";

    @Inject
    private OrderDao orderDao;

    @Inject
    private UserDao userDao;

    @Inject
    private OrderMapper orderMapper;

    @Inject
    private BookingMapper bookingMapper;

    @Inject
    private ServiceMapper serviceMapper;

    @Inject
    private AddressMapper addressMapper;

    @Inject
    private UserMapper userMapper;

    @Inject
    private IndicatorMapper indicatorMapper;

    @Inject
    private AccountMapper accountMapper;

    @Override
    public Optional<List<OrderEto>> findAllOrders() {
        LOG.debug(GET_ALL_ORDERS_LOG);

        return Optional.of(orderDao.findAll().stream()
                .map(orderEntity -> toOrderEto(orderEntity))
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<OrderEto> findOrder(String orderNumber) throws EntityDoesNotExistException {
        OrderEntity orderEntity = orderDao.findByOrderNumber(orderNumber).orElseThrow(() ->
                new EntityDoesNotExistException("Order with order number " + orderNumber + " does not exist."));

        LOG.debug(GET_ORDER_LOG);

        return Optional.of(toOrderEto(orderEntity));
    }

    @Override
    public Optional<List<OrderEto>> findAllOrdersByUserId(Long userId) throws EntityDoesNotExistException {
        LOG.debug(GET_ALL_ORDERS_BY_USER_ID_LOG, userId);

        UserEntity userEntity = userDao.findById(userId).orElseThrow(() ->
                new EntityDoesNotExistException("Order with user id " + userId + " does not exist."));

        return Optional.of(orderDao.findAllByUser_Id(userEntity.getId()).stream()
                .map(orderEntity -> toOrderEto(orderEntity))
                .collect(Collectors.toList()));
    }

    private OrderEto toOrderEto(OrderEntity orderEntity) {
        OrderEto orderEto = orderMapper.toOrderEto(orderEntity);

        if( orderEntity.getBooking() != null){
            orderEto.setBooking(toBookingEto(orderEntity.getBooking()));
        }

        orderEto.setCoordinator(toUserEto(orderEntity.getCoordinator()));
        orderEto.setUser(toUserEto(orderEntity.getUser()));

        return orderEto;
    }

    private BookingEto toBookingEto(BookingEntity bookingEntity) {
        BookingEto bookingEto = bookingMapper.toBookingEto(bookingEntity);
        bookingEto.setServiceEto(serviceMapper.toServiceEto(bookingEntity.getService()));
        bookingEto.setUserEto(toUserEto(bookingEntity.getUser()));

        if( bookingEntity.getAddress() != null ){
            bookingEto.setAddressEto(addressMapper.toAddressEto(bookingEntity.getAddress()));
        }

        if( bookingEntity.getPriceIndicatorList() != null ) {
            bookingEto.setPriceIndicatorEtoList(
                    bookingEntity.getPriceIndicatorList().stream()
                            .map(priceIndicatorEntity ->
                                    new PriceIndicatorEto(
                                            indicatorMapper.toIndicatorEto(priceIndicatorEntity.getIndicator()),
                                            bookingEntity.getId(),
                                            priceIndicatorEntity.getPrice(),
                                            priceIndicatorEntity.getAmount()
                                    )).collect(Collectors.toList()));
        }

        return bookingEto;
    }

    private UserEto toUserEto(UserEntity userEntity) {
        UserEto userEto = userMapper.toUserEto(userEntity);
        userEto.setAccountEto(accountMapper.toAccountEto(userEntity.getAccount()));

        return userEto;
    }
}
