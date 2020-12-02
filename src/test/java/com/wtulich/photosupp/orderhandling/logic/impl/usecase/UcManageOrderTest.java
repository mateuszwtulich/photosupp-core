package com.wtulich.photosupp.orderhandling.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import com.wtulich.photosupp.general.utils.enums.OrderStatus;
import com.wtulich.photosupp.orderhandling.dataaccess.api.dao.OrderDao;
import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.OrderEntity;
import com.wtulich.photosupp.orderhandling.logic.api.exception.OrderStatusInappropriateException;
import com.wtulich.photosupp.orderhandling.logic.api.to.OrderEto;
import com.wtulich.photosupp.orderhandling.logic.api.to.OrderTo;
import com.wtulich.photosupp.orderhandling.logic.impl.validator.OrderValidator;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.BookingDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.*;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.AddressMapper;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.IndicatorMapper;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.ServiceMapper;
import com.wtulich.photosupp.serviceordering.logic.api.to.BookingEto;
import com.wtulich.photosupp.serviceordering.logic.api.to.PriceIndicatorEto;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.UserDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.AccountEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.PermissionEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.RoleEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.UserEntity;
import com.wtulich.photosupp.userhandling.logic.api.to.AccountEto;
import com.wtulich.photosupp.userhandling.logic.api.to.PermissionEto;
import com.wtulich.photosupp.userhandling.logic.api.to.RoleEto;
import com.wtulich.photosupp.userhandling.logic.api.to.UserEto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class UcManageOrderTest {

    @Autowired
    private UcManageOrderImpl ucManageOrder;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private IndicatorMapper indicatorMapper;

    @Autowired
    private ServiceMapper serviceMapper;

    @MockBean
    private OrderDao orderDao;

    @MockBean
    private BookingDao bookingDao;

    @MockBean
    private UserDao userDao;

    @MockBean
    private OrderValidator orderValidator;

    private OrderEntity orderEntity;
    private OrderEto orderEto;
    private OrderTo orderTo;
    private UserEntity userEntity;
    private UserEntity userEntity2;
    private BookingEntity bookingEntity;

    @BeforeEach
    void setUp() {
        List<PermissionEntity> permissionEntities = new ArrayList<>();
        PermissionEntity permissionEntity = new PermissionEntity(ApplicationPermissions.AUTH_USER, "User has possibility to use CRUD operations one every functionality.");
        permissionEntity.setId(1L);
        permissionEntities.add(permissionEntity);

        RoleEntity roleEntity = new RoleEntity( "MANAGER", "Manager with all permissions in order management", permissionEntities);
        roleEntity.setId(1L);
        AccountEntity accountEntity = new AccountEntity( "user1", "passw0rd", "user1@test.com", true);
        accountEntity.setId(1L);

        userEntity = new UserEntity("NAME", "SURNAME", roleEntity, accountEntity);
        userEntity.setId(1L);

        List<PermissionEntity> permissionEntities2 = new ArrayList<>();
        PermissionEntity permissionEntity2 = new PermissionEntity(ApplicationPermissions.AUTH_USER, "Standard user with no special permissions.");
        permissionEntity2.setId(6L);
        permissionEntities2.add(permissionEntity2);

        RoleEntity roleEntity2 = new RoleEntity( "USER", "Standard user with no special permissions", permissionEntities2);
        roleEntity2.setId(2L);
        AccountEntity accountEntity2 = new AccountEntity( "user2", "passw0rd", "user2@test.com", true);
        accountEntity2.setId(2L);

        userEntity2 = new UserEntity("NAME2", "SURNAME2", roleEntity2, accountEntity2);
        userEntity2.setId(2L);

        AddressEntity addressEntity =  new AddressEntity("Wroclaw", "Wroblewskiego", "27", null, "51-627");
        ServiceEntity serviceEntity = new ServiceEntity("Film produktowy", "Film produktow na bialym tle i odpowiednim oswietleniu", 500D, "pl");
        IndicatorEntity indicatorEntity = new IndicatorEntity("Podroz sluzbowa", "Paliwo, amortyzacja", "pl", 20, 40);
        serviceEntity.setIndicatorList(List.of(indicatorEntity));
        serviceEntity.setId(1L);
        indicatorEntity.setId(1L);

        bookingEntity = new BookingEntity("Film dla TestCompany", "Film produktowy z dojazdem", 900D,
                addressEntity, userEntity2, serviceEntity, false, LocalDate.now(), LocalDate.now(), LocalDate.now());
        bookingEntity.setId(1L);

        PriceIndicatorEntity priceIndicatorEntity = new PriceIndicatorEntity(indicatorEntity, bookingEntity, 400, 10);
        List<PriceIndicatorEntity> priceIndicatorEntities = new ArrayList<>();
        priceIndicatorEntities.add(priceIndicatorEntity);
        bookingEntity.setPriceIndicatorList(priceIndicatorEntities);


        orderEntity = new OrderEntity("INVIU_00001", OrderStatus.IN_PROGRESS, 1000D, LocalDate.now(), userEntity2, userEntity,  bookingEntity);

//        List<PermissionEto> permissionEtoList = new ArrayList<>();
//        permissionEtoList.add(new PermissionEto(1L, ApplicationPermissions.A_CRUD_SUPER, "DESC1"));
//        RoleEto roleEto = new RoleEto(1L, "MANAGER", "Manager with all permissions in order management", permissionEtoList);
        AccountEto accountEto = new AccountEto(1L, "user1", "passw0rd", "user1@test.com", true);
        UserEto userEto = new UserEto(1L, "NAME", "SURNAME", accountEto, null);

//        List<PermissionEto> permissionEtoList2 = new ArrayList<>();
//        permissionEtoList2.add(new PermissionEto(6L, ApplicationPermissions.AUTH_USER, "Standard user with no special permissions."));
//        RoleEto roleEto2 = new RoleEto(2L, "USER", "Standard user with no special permissions", permissionEtoList2);
        AccountEto accountEto2 = new AccountEto(2L, "user2", "passw0rd", "user2@test.com", true);
        UserEto userEto2 = new UserEto(2L, "NAME2", "SURNAME2", accountEto2, null);

        BookingEto bookingEto = new BookingEto(1L, "Film dla TestCompany", "Film produktowy z dojazdem", serviceMapper.toServiceEto(serviceEntity),
                addressMapper.toAddressEto(addressEntity), userEto, false, 900D,
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( LocalDate.now()),
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( LocalDate.now()),
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( LocalDate.now()),
                null);

        PriceIndicatorEto priceIndicatorEto = new PriceIndicatorEto(indicatorMapper.toIndicatorEto(indicatorEntity), bookingEto.getId(), 400, 10);
        List<PriceIndicatorEto> priceIndicatorEtoList = new ArrayList<>();
        priceIndicatorEtoList.add(priceIndicatorEto);

        bookingEto.setPriceIndicatorEtoList(priceIndicatorEtoList);

        orderEto = new OrderEto("INVIU_00001", userEto, userEto2, OrderStatus.IN_PROGRESS, bookingEto, 1000D,
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format(LocalDate.now()));

        orderTo = new OrderTo(1L, 2L, 1L, 1000D);
    }

    @Test
    @DisplayName("Test createOrder Success")
    void testCreateOrderSuccess() throws EntityDoesNotExistException, EntityAlreadyExistsException {
        //Arrange
        when(orderDao.save(any())).thenReturn(orderEntity);
        when(userDao.findById(userEntity.getId())).thenReturn(Optional.ofNullable(userEntity));
        when(userDao.findById(userEntity2.getId())).thenReturn(Optional.ofNullable(userEntity2));
        when(bookingDao.findById(bookingEntity.getId())).thenReturn(Optional.ofNullable(bookingEntity));

        //Act
        Optional<OrderEto> result = ucManageOrder.createOrder(orderTo);

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(orderEto).isEqualToComparingFieldByField(result.get());
    }

    @Test
    @DisplayName("Test createOrder EntityDoesNotExistException")
    void testCreateCommentEntityDoesNotExistException() {
        //Arrange
        when(userDao.findById(userEntity.getId())).thenReturn(Optional.ofNullable(null));

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucManageOrder.createOrder(orderTo));
    }

    @Test
    @DisplayName("Test createOrder EntityDoesNotExistException")
    void testCreateCommentEntityDoesNotExistException2() {
        //Arrange
        when(userDao.findById(userEntity.getId())).thenReturn(Optional.ofNullable(userEntity));
        when(userDao.findById(userEntity2.getId())).thenReturn(Optional.ofNullable(userEntity2));
        when(bookingDao.findById(bookingEntity.getId())).thenReturn(Optional.ofNullable(null));

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucManageOrder.createOrder(orderTo));
    }

    @Test
    @DisplayName("Test createOrder EntityAlreadyExistsException")
    void testCreateCommentEntityAlreadyExistsException() throws EntityAlreadyExistsException {
        //Arrange
        when(userDao.findById(userEntity.getId())).thenReturn(Optional.ofNullable(userEntity));
        when(userDao.findById(userEntity2.getId())).thenReturn(Optional.ofNullable(userEntity2));

        doThrow(EntityAlreadyExistsException.class)
                .when(orderValidator).verifyIfBookingHasAssignedOrders(orderTo.getBookingId());

        //Act Assert
        Assertions.assertThrows(EntityAlreadyExistsException.class, () ->
                ucManageOrder.createOrder(orderTo));
    }

    @Test
    @DisplayName("Test updateOrder Success")
    void testUpdateOrderSuccess() throws EntityDoesNotExistException, EntityAlreadyExistsException {
        //Arrange
        orderTo.setPrice(900D);
        orderEto.setPrice(900D);
        when(orderDao.findByOrderNumber(orderEntity.getOrderNumber())).thenReturn(Optional.ofNullable(orderEntity));
        when(userDao.findById(userEntity.getId())).thenReturn(Optional.ofNullable(userEntity));
        when(bookingDao.findById(bookingEntity.getId())).thenReturn(Optional.ofNullable(bookingEntity));

        //Act
        Optional<OrderEto> result = ucManageOrder.updateOrder(orderTo, orderEntity.getOrderNumber());

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(orderEto).isEqualToComparingFieldByField(result.get());
    }

    @Test
    @DisplayName("Test updateOrder EntityDoesNotExistException")
    void testUpdateOrderEntityDoesNotExistException() {
        //Arrange
        when(orderDao.findByOrderNumber("INVIU_00002")).thenReturn(Optional.empty());

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucManageOrder.updateOrder(orderTo, "INVIU_00002"));
    }

    @Test
    @DisplayName("Test updateOrder EntityAlreadyExistsException")
    void testUpdateOrderEntityAlreadyExistsException() throws EntityAlreadyExistsException {
        //Arrange
        orderTo.setBookingId(2L);
        when(orderDao.findByOrderNumber(orderEntity.getOrderNumber())).thenReturn(Optional.ofNullable(orderEntity));
        when(userDao.findById(userEntity.getId())).thenReturn(Optional.ofNullable(userEntity));
        doThrow(EntityAlreadyExistsException.class)
                .when(orderValidator).verifyIfBookingHasAssignedOrders(orderTo.getBookingId());

        //Act Assert
        Assertions.assertThrows(EntityAlreadyExistsException.class, () ->
                ucManageOrder.updateOrder(orderTo, orderEntity.getOrderNumber()));
    }

    @Test
    @DisplayName("Test acceptOrder Success")
    void testAcceptOrderSuccess() throws EntityDoesNotExistException, OrderStatusInappropriateException {
        //Arrange
        orderEntity.setStatus(OrderStatus.NEW);
        orderEto.setStatus(OrderStatus.IN_PROGRESS);
        when(orderDao.findByOrderNumber(orderEntity.getOrderNumber())).thenReturn(Optional.ofNullable(orderEntity));

        //Act
        Optional<OrderEto> result = ucManageOrder.acceptOrder(orderEntity.getOrderNumber());

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(orderEto).isEqualToComparingFieldByField(result.get());
    }

    @Test
    @DisplayName("Test acceptOrder EntityDoesNotExistException")
    void testAcceptOrderEntityDoesNotExistException() {
        //Arrange
        when(orderDao.findByOrderNumber("INVIU_00002")).thenReturn(Optional.empty());

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucManageOrder.acceptOrder("INVIU_00002"));
    }

    @Test
    @DisplayName("Test acceptOrder OrderStatusInappropriateException")
    void testAcceptOrderEntityAlreadyExistsException() throws OrderStatusInappropriateException {
        //Arrange
        orderEntity.setStatus(OrderStatus.TO_VERIFY);
        when(orderDao.findByOrderNumber(orderEntity.getOrderNumber())).thenReturn(Optional.ofNullable(orderEntity));
        doThrow(OrderStatusInappropriateException.class)
                .when(orderValidator).verifyIfOrderCanBeAccepted(orderEntity);

        //Act Assert
        Assertions.assertThrows(OrderStatusInappropriateException.class, () ->
                ucManageOrder.acceptOrder(orderEntity.getOrderNumber()));
    }

    @Test
    @DisplayName("Test sendToVerificationOrder Success")
    void testSendToVerificationOrderSuccess() throws EntityDoesNotExistException, OrderStatusInappropriateException {
        //Arrange
        orderEntity.setStatus(OrderStatus.IN_PROGRESS);
        orderEto.setStatus(OrderStatus.TO_VERIFY);
        when(orderDao.findByOrderNumber(orderEntity.getOrderNumber())).thenReturn(Optional.ofNullable(orderEntity));

        //Act
        Optional<OrderEto> result = ucManageOrder.sendOrderToVerification(orderEntity.getOrderNumber());

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(orderEto).isEqualToComparingFieldByField(result.get());
    }

    @Test
    @DisplayName("Test sendToVerificationOrder EntityDoesNotExistException")
    void testSendToVerificationOrderEntityDoesNotExistException() {
        //Arrange
        when(orderDao.findByOrderNumber("INVIU_00002")).thenReturn(Optional.empty());

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucManageOrder.sendOrderToVerification("INVIU_00002"));
    }

    @Test
    @DisplayName("Test sendToVerificationOrder OrderStatusInappropriateException")
    void testSendToVerificationOrderEntityAlreadyExistsException() throws OrderStatusInappropriateException {
        //Arrange
        orderEntity.setStatus(OrderStatus.FINISHED);
        when(orderDao.findByOrderNumber(orderEntity.getOrderNumber())).thenReturn(Optional.ofNullable(orderEntity));
        doThrow(OrderStatusInappropriateException.class)
                .when(orderValidator).verifyIfOrderCanBeSentToVerification(orderEntity);

        //Act Assert
        Assertions.assertThrows(OrderStatusInappropriateException.class, () ->
                ucManageOrder.sendOrderToVerification(orderEntity.getOrderNumber()));
    }

    @Test
    @DisplayName("Test finishOrder Success")
    void testFinishOrderSuccess() throws EntityDoesNotExistException, OrderStatusInappropriateException {
        //Arrange
        orderEntity.setStatus(OrderStatus.TO_VERIFY);
        orderEto.setStatus(OrderStatus.FINISHED);
        when(orderDao.findByOrderNumber(orderEntity.getOrderNumber())).thenReturn(Optional.ofNullable(orderEntity));

        //Act
        Optional<OrderEto> result = ucManageOrder.finishOrder(orderEntity.getOrderNumber());

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(orderEto).isEqualToComparingFieldByField(result.get());
    }

    @Test
    @DisplayName("Test finishOrder EntityDoesNotExistException")
    void testFinishOrderEntityDoesNotExistException() {
        //Arrange
        when(orderDao.findByOrderNumber("INVIU_00002")).thenReturn(Optional.empty());

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucManageOrder.finishOrder("INVIU_00002"));
    }

    @Test
    @DisplayName("Test finishOrder OrderStatusInappropriateException")
    void testFinishOrderEntityAlreadyExistsException() throws OrderStatusInappropriateException {
        //Arrange
        orderEntity.setStatus(OrderStatus.NEW);
        when(orderDao.findByOrderNumber(orderEntity.getOrderNumber())).thenReturn(Optional.ofNullable(orderEntity));
        doThrow(OrderStatusInappropriateException.class)
                .when(orderValidator).verifyIfOrderCanBeFinished(orderEntity);

        //Act Assert
        Assertions.assertThrows(OrderStatusInappropriateException.class, () ->
                ucManageOrder.finishOrder(orderEntity.getOrderNumber()));
    }
}
