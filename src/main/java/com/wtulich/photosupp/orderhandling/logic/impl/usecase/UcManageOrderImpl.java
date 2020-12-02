package com.wtulich.photosupp.orderhandling.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.utils.enums.OrderStatus;
import com.wtulich.photosupp.orderhandling.dataaccess.api.dao.OrderDao;
import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.CommentEntity;
import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.OrderEntity;
import com.wtulich.photosupp.orderhandling.logic.api.exception.OrderStatusInappropriateException;
import com.wtulich.photosupp.orderhandling.logic.api.mapper.OrderMapper;
import com.wtulich.photosupp.orderhandling.logic.api.to.CommentEto;
import com.wtulich.photosupp.orderhandling.logic.api.to.CommentTo;
import com.wtulich.photosupp.orderhandling.logic.api.to.OrderEto;
import com.wtulich.photosupp.orderhandling.logic.api.to.OrderTo;
import com.wtulich.photosupp.orderhandling.logic.api.usecase.UcManageOrder;
import com.wtulich.photosupp.orderhandling.logic.impl.validator.OrderValidator;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.BookingDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.BookingEntity;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.AddressMapper;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.BookingMapper;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.IndicatorMapper;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.ServiceMapper;
import com.wtulich.photosupp.serviceordering.logic.api.to.BookingEto;
import com.wtulich.photosupp.serviceordering.logic.api.to.BookingTo;
import com.wtulich.photosupp.serviceordering.logic.api.to.PriceIndicatorEto;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.UserDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.UserEntity;
import com.wtulich.photosupp.userhandling.logic.api.mapper.AccountMapper;
import com.wtulich.photosupp.userhandling.logic.api.mapper.PermissionsMapper;
import com.wtulich.photosupp.userhandling.logic.api.mapper.RoleMapper;
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
import java.util.stream.Collectors;

@Validated
@Named
public class UcManageOrderImpl implements UcManageOrder {

    private static final Logger LOG = LoggerFactory.getLogger(UcManageOrderImpl.class);
    private static final String ID_CANNOT_BE_NULL = "id cannot be a null value";
    private static final String CREATE_ORDER_LOG = "Create Order with user id {} in database.";
    private static final String UPDATE_ORDER_LOG = "Update Order with number {} from database.";
    private static final String FINISH_ORDER_LOG = "Finish Order with number {} in database.";
    private static final String ACCEPT_ORDER_LOG = "Accept Order with number {} in database.";
    private static final String SEND_TO_VERIFICATION_ORDER_LOG = "Send to verification Order with number {} in database.";

    @Inject
    private OrderDao orderDao;

    @Inject
    private BookingDao bookingDao;

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

    @Inject
    private OrderValidator orderValidator;

    @Override
    public Optional<OrderEto> createOrder(OrderTo orderTo) throws EntityDoesNotExistException, EntityAlreadyExistsException {
        LOG.debug(CREATE_ORDER_LOG, orderTo.getUserId());

        OrderEntity orderEntity = toOrderEntity(orderTo);
        return Optional.of(toOrderEto(orderDao.save(orderEntity)));
    }

    @Override
    public Optional<OrderEto> updateOrder(OrderTo orderTo, String orderNumber) throws EntityDoesNotExistException, EntityAlreadyExistsException {
        Objects.requireNonNull(orderNumber, ID_CANNOT_BE_NULL);

        OrderEntity orderEntity = getOrderByOrderNumber(orderNumber);

        LOG.debug(UPDATE_ORDER_LOG, orderNumber);

        return Optional.of(toOrderEto(mapOrderEntity(orderEntity, orderTo)));
    }

    @Override
    public Optional<OrderEto> finishOrder(String orderNumber) throws EntityDoesNotExistException, OrderStatusInappropriateException {
        OrderEntity orderEntity = getOrderByOrderNumber(orderNumber);

        orderValidator.verifyIfOrderCanBeFinished(orderEntity);

        LOG.debug(FINISH_ORDER_LOG, orderNumber);
        orderEntity.setStatus(OrderStatus.FINISHED);

        return Optional.of(toOrderEto(orderEntity));
    }

    @Override
    public Optional<OrderEto> acceptOrder(String orderNumber) throws EntityDoesNotExistException, OrderStatusInappropriateException {
        OrderEntity orderEntity = getOrderByOrderNumber(orderNumber);

        orderValidator.verifyIfOrderCanBeAccepted(orderEntity);

        LOG.debug(ACCEPT_ORDER_LOG, orderNumber);
        orderEntity.setStatus(OrderStatus.IN_PROGRESS);

        return Optional.of(toOrderEto(orderEntity));
    }

    @Override
    public Optional<OrderEto> sendOrderToVerification(String orderNumber) throws EntityDoesNotExistException, OrderStatusInappropriateException {
        OrderEntity orderEntity = getOrderByOrderNumber(orderNumber);

        orderValidator.verifyIfOrderCanBeSentToVerification(orderEntity);

        LOG.debug(SEND_TO_VERIFICATION_ORDER_LOG, orderNumber);
        orderEntity.setStatus(OrderStatus.TO_VERIFY);

        return Optional.of(toOrderEto(orderEntity));
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

    private OrderEntity toOrderEntity(OrderTo orderTo) throws EntityDoesNotExistException, EntityAlreadyExistsException {
        OrderEntity orderEntity = orderMapper.toOrderEntity(orderTo);

        if( orderTo.getBookingId() != null){
            orderValidator.verifyIfBookingHasAssignedOrders(orderTo.getBookingId());

            BookingEntity bookingEntity = getBookingById(orderTo.getBookingId());
            bookingEntity.setConfirmed(true);
            orderEntity.setBooking(bookingEntity);
        }

        orderEntity.setCoordinator(getUserById(orderTo.getCoordinatorId()));
        orderEntity.setUser(getUserById(orderTo.getUserId()));
        orderEntity.setStatus(OrderStatus.NEW);
        orderEntity.setCreatedAt(LocalDate.now());

        return orderEntity;
    }

    private OrderEntity getOrderByOrderNumber(String orderNumber) throws EntityDoesNotExistException {
        return orderDao.findByOrderNumber(orderNumber).orElseThrow(() ->
                new EntityDoesNotExistException("Order with order number " + orderNumber + " does not exist."));
    }

    private UserEntity getUserById(Long userId) throws EntityDoesNotExistException {
        return userDao.findById(userId).orElseThrow(() ->
                new EntityDoesNotExistException("User with id " + userId + " does not exist."));
    }

    private BookingEntity getBookingById(Long bookingId) throws EntityDoesNotExistException {
        return bookingDao.findById(bookingId).orElseThrow(() ->
                new EntityDoesNotExistException("Booking with id " + bookingId + " does not exist."));
    }

    private UserEto toUserEto(UserEntity userEntity) {
        UserEto userEto = userMapper.toUserEto(userEntity);
        userEto.setAccountEto(accountMapper.toAccountEto(userEntity.getAccount()));

        return userEto;
    }

    private OrderEntity mapOrderEntity(OrderEntity orderEntity, OrderTo orderTo) throws EntityDoesNotExistException, EntityAlreadyExistsException {
        orderEntity.setPrice(orderTo.getPrice());
        orderEntity.setCoordinator(getUserById(orderTo.getCoordinatorId()));

        if(orderTo.getBookingId() != null && ((orderEntity.getBooking() != null &&
                !(orderEntity.getBooking().getId().equals(orderTo.getBookingId()))) ||
                (orderEntity.getBooking() == null))){

            orderValidator.verifyIfBookingHasAssignedOrders(orderTo.getBookingId());
            BookingEntity bookingEntity = getBookingById(orderTo.getBookingId());
            bookingEntity.setConfirmed(true);
            orderEntity.setBooking(bookingEntity);
        }

        return orderEntity;
    }
}
