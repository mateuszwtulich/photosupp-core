package com.wtulich.photosupp.orderhandling.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import com.wtulich.photosupp.general.utils.enums.OrderStatus;
import com.wtulich.photosupp.orderhandling.dataaccess.api.dao.OrderDao;
import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.OrderEntity;
import com.wtulich.photosupp.orderhandling.logic.api.mapper.OrderMapper;
import com.wtulich.photosupp.orderhandling.logic.api.to.OrderEto;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.*;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.AddressMapper;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.BookingMapper;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.IndicatorMapper;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.ServiceMapper;
import com.wtulich.photosupp.serviceordering.logic.api.to.BookingEto;
import com.wtulich.photosupp.serviceordering.logic.api.to.PriceIndicatorEto;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.AccountEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.PermissionEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.RoleEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.UserEntity;
import com.wtulich.photosupp.userhandling.logic.api.mapper.AccountMapper;
import com.wtulich.photosupp.userhandling.logic.api.mapper.UserMapper;
import com.wtulich.photosupp.userhandling.logic.api.to.AccountEto;
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
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class UcFindOrderTest {

    @Autowired
    private UcFindOrderImpl ucFindOrder;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private ServiceMapper serviceMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IndicatorMapper indicatorMapper;

    @Autowired
    private AccountMapper accountMapper;

    @MockBean
    private OrderDao orderDao;

    private List<OrderEntity> orderEntities;
    private List<OrderEto> orderEtoList;
    private OrderEntity orderEntity;
    private OrderEto orderEto;

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

        UserEntity userEntity = new UserEntity("NAME", "SURNAME", roleEntity, accountEntity);
        userEntity.setId(1L);

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

        AddressEntity addressEntity =  new AddressEntity("Wroclaw", "Wroblewskiego", "27", null, "51-627");
        ServiceEntity serviceEntity = new ServiceEntity("Film produktowy", "Film produktow na bialym tle i odpowiednim oswietleniu", 500D, "pl");
        IndicatorEntity indicatorEntity = new IndicatorEntity("Podroz sluzbowa", "Paliwo, amortyzacja", "pl", 20, 40);
        ArrayList<IndicatorEntity> indicatorEntities = new ArrayList<>();
        indicatorEntities.add(indicatorEntity);
        serviceEntity.setIndicatorList(indicatorEntities);

        BookingEntity bookingEntity = new BookingEntity("Film dla TestCompany", "Film produktowy z dojazdem", 900D,
                addressEntity, userEntity2, serviceEntity, false, LocalDate.now(), LocalDate.now(), LocalDate.now());
        bookingEntity.setId(1L);

        PriceIndicatorEntity priceIndicatorEntity = new PriceIndicatorEntity(indicatorEntity, bookingEntity, 400, 10);
        List<PriceIndicatorEntity> priceIndicatorEntities = new ArrayList<>();
        priceIndicatorEntities.add(priceIndicatorEntity);
        bookingEntity.setPriceIndicatorList(priceIndicatorEntities);

        orderEntity = new OrderEntity("INVIU_00001", OrderStatus.IN_PROGRESS, 1000D, LocalDate.now(), userEntity2, userEntity,  bookingEntity );

        orderEntities = new ArrayList<>();
        orderEntities.add(orderEntity);

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

        orderEtoList = new ArrayList<>();
        orderEtoList.add(orderEto);
    }

    @Test
    @DisplayName("Test findAllOrders Success")
    void testFindAllOrdersSuccess() throws EntityDoesNotExistException {
        //Arrange
        when(orderDao.findAll()).thenReturn(orderEntities);

        //Act
        Optional<List<OrderEto>> result = ucFindOrder.findAllOrders();

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(result.get()).hasSize(orderEtoList.size());
        assertThat(orderEtoList.get(0)).isEqualTo(result.get().get(0));
    }

    @Test
    @DisplayName("Test findAllOrders No content")
    void testFindAllOrdersNoContent() throws EntityDoesNotExistException {
        //Arrange
        when(orderDao.findAll()).thenReturn(new ArrayList<>());

        //Act
        Optional<List<OrderEto>> result = ucFindOrder.findAllOrders();

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertTrue(result.get().size() == 0);
    }

    @Test
    @DisplayName("Test findOrderById Success")
    void testFindOrderByIdSuccess() throws EntityDoesNotExistException {
        //Arrange
        when(orderDao.findByOrderNumber(orderEntity.getOrderNumber())).thenReturn(Optional.of(orderEntity));

        //Act
        Optional<OrderEto> result = ucFindOrder.findOrder(orderEntity.getOrderNumber());

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(result.get()).isEqualTo(orderEto);
    }

    @Test
    @DisplayName("Test findOrderById Failure")
    void testFindOrderByIdFailure() {
        //Arrange
        when(orderDao.findByOrderNumber(orderEntity.getOrderNumber())).thenReturn(Optional.ofNullable(null));

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucFindOrder.findOrder(orderEntity.getOrderNumber()));
    }
}
