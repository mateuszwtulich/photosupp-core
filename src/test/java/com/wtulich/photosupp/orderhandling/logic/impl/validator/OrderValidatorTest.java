package com.wtulich.photosupp.orderhandling.logic.impl.validator;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import com.wtulich.photosupp.general.utils.enums.OrderStatus;
import com.wtulich.photosupp.orderhandling.dataaccess.api.dao.OrderDao;
import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.OrderEntity;
import com.wtulich.photosupp.orderhandling.logic.api.exception.OrderStatusInappropriateException;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.*;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.AccountEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.PermissionEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.RoleEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.UserEntity;
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
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class OrderValidatorTest {

    @Autowired
    private OrderValidator orderValidator;

    @MockBean
    private OrderDao orderDao;

    private OrderEntity orderEntity;
    private BookingEntity bookingEntity;

    @BeforeEach
    void setUp() {
        AddressEntity addressEntity =  new AddressEntity("Wroclaw", "Wroblewskiego", "27", null, "51-627");
        addressEntity.setId(1L);

        ServiceEntity serviceEntity = new ServiceEntity("Film produktowy", "Film produktow na bialym tle i odpowiednim oswietleniu", 500D, "pl");
        serviceEntity.setId(1L);

        IndicatorEntity indicatorEntity = new IndicatorEntity("Podroz sluzbowa", "Paliwo, amortyzacja", "pl", 20, 40);
        indicatorEntity.setId(1L);

        List<PermissionEntity> permissionEntities = new ArrayList<>();
        PermissionEntity permissionEntity = new PermissionEntity(ApplicationPermissions.AUTH_USER, "User has possibility to use CRUD operations one every functionality.");
        permissionEntity.setId(1L);
        permissionEntities.add(permissionEntity);

        RoleEntity roleEntity = new RoleEntity( "MANAGER", "Manager with all permissions in order management", permissionEntities);
        roleEntity.setId(1L);
        AccountEntity accountEntity = new AccountEntity( "user1", "passw0rd", "user1@test.com", true);
        accountEntity.setId(1L);

        UserEntity userEntity = new UserEntity("NAME", "SURNAME", roleEntity, accountEntity);
        userEntity.setId(1L);

        bookingEntity = new BookingEntity("Film dla TestCompany", "Film produktowy z dojazdem", 1400D,
                addressEntity, userEntity, serviceEntity, false, LocalDate.now(), LocalDate.now(), LocalDate.now());
        bookingEntity.setId(1L);

        PriceIndicatorEntity priceIndicatorEntity = new PriceIndicatorEntity(indicatorEntity, bookingEntity, 400, 10);
        List<PriceIndicatorEntity> priceIndicatorEntities = new ArrayList<>();
        priceIndicatorEntities.add(priceIndicatorEntity);
        bookingEntity.setPriceIndicatorList(priceIndicatorEntities);

        List<PermissionEntity> permissionEntities2 = new ArrayList<>();
        PermissionEntity permissionEntity2 = new PermissionEntity(ApplicationPermissions.AUTH_USER, "Standard user with no special permissions.");
        permissionEntity2.setId(6L);
        permissionEntities2.add(permissionEntity2);

        RoleEntity roleEntity2 = new RoleEntity( "USER", "Standard user with no special permissions", permissionEntities2);
        roleEntity2.setId(2L);
        AccountEntity accountEntity2 = new AccountEntity( "user2", "passw0rd", "user2@test.com", true);
        accountEntity2.setId(2L);

        UserEntity userEntity2 = new UserEntity("NAME2", "SURNAME2", roleEntity2, accountEntity2);
        userEntity2.setId(2L);

        orderEntity = new OrderEntity("INVIU_00001", OrderStatus.IN_PROGRESS, 1000D, LocalDate.now(), userEntity2, userEntity,  bookingEntity );
    }

    @Test
    @DisplayName("Test verifyIfBookingHasAssignedOrders EntityAlreadyExistsException")
    void verifyIfServiceAlreadyExistsEntityAlreadyExistsException() {
        //Arrange
        when(orderDao.existsByBooking_Id(orderEntity.getBooking().getId())).thenReturn(true);

        //Act Assert
        Assertions.assertThrows(EntityAlreadyExistsException.class, () ->
                orderValidator.verifyIfBookingHasAssignedOrders(bookingEntity.getId()));
    }

    @Test
    @DisplayName("Test verifyIfBookingHasAssignedOrders False")
    void verifyIfRoleAlreadyExistsFalse() {
        //Arrange
        when(orderDao.existsByBooking_Id(orderEntity.getBooking().getId())).thenReturn(false);

        //Act Assert
        Assertions.assertDoesNotThrow(() -> orderValidator.verifyIfBookingHasAssignedOrders(bookingEntity.getId()));
    }

    @Test
    @DisplayName("Test verifyIfOrderCanBeAccepted OrderStatusInappropriateException")
    void verifyIfOrderCanBeAcceptedOrderStatusInappropriateException() {
        //Arrange
        orderEntity.setStatus(OrderStatus.FINISHED);

        //Act Assert
        Assertions.assertThrows(OrderStatusInappropriateException.class, () ->
                orderValidator.verifyIfOrderCanBeAccepted(orderEntity));
    }

    @Test
    @DisplayName("Test verifyIfOrderCanBeAccepted False")
    void verifyIfOrderCanBeAcceptedFalse() {
        //Arrange
        orderEntity.setStatus(OrderStatus.NEW);

        //Act Assert
        Assertions.assertDoesNotThrow(() -> orderValidator.verifyIfOrderCanBeAccepted(orderEntity));
    }

    @Test
    @DisplayName("Test verifyIfOrderCanBeSentToVerification OrderStatusInappropriateException")
    void verifyIfOrderCanBeSentToVerificationOrderStatusInappropriateException() {
        //Arrange
        orderEntity.setStatus(OrderStatus.FINISHED);

        //Act Assert
        Assertions.assertThrows(OrderStatusInappropriateException.class, () ->
                orderValidator.verifyIfOrderCanBeSentToVerification(orderEntity));
    }

    @Test
    @DisplayName("Test verifyIfOrderCanBeSentToVerification False")
    void verifyIfOrderCanBeSentToVerificationFalse() {
        //Arrange
        orderEntity.setStatus(OrderStatus.IN_PROGRESS);

        //Act Assert
        Assertions.assertDoesNotThrow(() -> orderValidator.verifyIfOrderCanBeSentToVerification(orderEntity));
    }

    @Test
    @DisplayName("Test verifyIfOrderCanBeFinished OrderStatusInappropriateException")
    void verifyIfOrderCanBeFinishedOrderStatusInappropriateException() {
        //Arrange
        orderEntity.setStatus(OrderStatus.NEW);

        //Act Assert
        Assertions.assertThrows(OrderStatusInappropriateException.class, () ->
                orderValidator.verifyIfOrderCanBeFinished(orderEntity));
    }

    @Test
    @DisplayName("Test verifyIfOrderCanBeFinished False")
    void verifyIfOrderCanBeFinishedFalse() {
        //Arrange
        orderEntity.setStatus(OrderStatus.TO_VERIFY);

        //Act Assert
        Assertions.assertDoesNotThrow(() -> orderValidator.verifyIfOrderCanBeFinished(orderEntity));
    }
}
