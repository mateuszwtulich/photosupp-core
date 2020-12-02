package com.wtulich.photosupp.serviceordering.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.orderhandling.logic.api.to.OrderEto;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.BookingDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.BookingEntity;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.AddressMapper;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.BookingMapper;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.IndicatorMapper;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.ServiceMapper;
import com.wtulich.photosupp.serviceordering.logic.api.to.BookingEto;
import com.wtulich.photosupp.serviceordering.logic.api.to.PriceIndicatorEto;
import com.wtulich.photosupp.serviceordering.logic.api.usecase.UcFindBooking;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.UserDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.UserEntity;
import com.wtulich.photosupp.userhandling.logic.api.mapper.AccountMapper;
import com.wtulich.photosupp.userhandling.logic.api.mapper.PermissionsMapper;
import com.wtulich.photosupp.userhandling.logic.api.mapper.RoleMapper;
import com.wtulich.photosupp.userhandling.logic.api.mapper.UserMapper;
import com.wtulich.photosupp.userhandling.logic.api.to.RoleEto;
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
public class UcFindBookingImpl implements UcFindBooking {

    private static final Logger LOG = LoggerFactory.getLogger(UcFindBookingImpl.class);
    private static final String ID_CANNOT_BE_NULL = "id cannot be a null value";
    private static final String GET_BOOKING_LOG = "Get Booking with id {} from database.";
    private static final String GET_ALL_BOOKINGS_LOG = "Get all Bookings from database.";
    private static final String GET_ALL_BOOKINGS_BY_USER_ID_LOG = "Get all Bookings by user id {} from database.";

    @Inject
    private BookingDao bookingDao;

    @Inject
    private UserDao userDao;

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
    private RoleMapper roleMapper;

    @Inject
    private AccountMapper accountMapper;

    @Inject
    private PermissionsMapper permissionsMapper;


    @Override
    public Optional<BookingEto> findBooking(Long id) throws EntityDoesNotExistException {

        Objects.requireNonNull(id, ID_CANNOT_BE_NULL);
        LOG.debug(GET_BOOKING_LOG, id);

        return Optional.of(toBookingEto(bookingDao.findById(id).orElseThrow(() ->
                new EntityDoesNotExistException("Booking with id " + id + " does not exist."))));
    }

    @Override
    public Optional<List<BookingEto>> findAllBookings() {
        LOG.debug(GET_ALL_BOOKINGS_LOG);

        return Optional.of(bookingDao.findAll().stream()
                .map(bookingEntity -> toBookingEto(bookingEntity))
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<List<BookingEto>> findAllBookingsByUserId(Long userId) throws EntityDoesNotExistException {
        LOG.debug(GET_ALL_BOOKINGS_BY_USER_ID_LOG, userId);

        UserEntity userEntity = userDao.findById(userId).orElseThrow(() ->
                new EntityDoesNotExistException("Order with user id " + userId + " does not exist."));

        return Optional.of(bookingDao.findAllByUser_Id(userEntity.getId()).stream()
                .map(bookingEntity -> toBookingEto(bookingEntity))
                .collect(Collectors.toList()));
    }

    private BookingEto toBookingEto(BookingEntity bookingEntity){
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

    private UserEto toUserEto(UserEntity userEntity){
        UserEto userEto = userMapper.toUserEto(userEntity);
        userEto.setAccountEto(accountMapper.toAccountEto(userEntity.getAccount()));

        RoleEto roleEto = roleMapper.toRoleEto(userEntity.getRole());
        roleEto.setPermissionEtoList(userEntity.getRole().getPermissions().stream()
                .map(p -> permissionsMapper.toPermissionEto(p))
                .collect(Collectors.toList()));
        userEto.setRoleEto(roleEto);
        return userEto;
    }
}
