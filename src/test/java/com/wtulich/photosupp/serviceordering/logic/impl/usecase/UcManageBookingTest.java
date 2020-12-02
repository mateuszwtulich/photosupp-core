package com.wtulich.photosupp.serviceordering.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.logic.api.exception.UnprocessableEntityException;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import com.wtulich.photosupp.general.utils.enums.OrderStatus;
import com.wtulich.photosupp.orderhandling.dataaccess.api.dao.OrderDao;
import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.OrderEntity;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.*;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.*;
import com.wtulich.photosupp.serviceordering.logic.api.to.*;
import com.wtulich.photosupp.serviceordering.logic.impl.validator.AddressValidator;
import com.wtulich.photosupp.serviceordering.logic.impl.validator.BookingValidator;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class UcManageBookingTest {

    @Autowired
    private UcManageBookingImpl ucManageBooking;

    @MockBean
    private BookingDao bookingDao;

    @MockBean
    private ServiceDao serviceDao;

    @MockBean
    private UserDao userDao;

    @MockBean
    private AddressDao addressDao;

    @MockBean
    private OrderDao orderDao;

    @MockBean
    private IndicatorDao indicatorDao;

    @MockBean
    private PriceIndicatorDao priceIndicatorDao;

    @MockBean
    private BookingValidator bookingValidator;

    @MockBean
    private AddressValidator addressValidator;

    private BookingEntity bookingEntity;
    private BookingEto bookingEto;
    private BookingTo bookingTo;
    private ServiceEntity serviceEntity;
    private AddressEntity addressEntity;
    private AddressEntity addressEntity2;
    private IndicatorEntity indicatorEntity;
    private PriceIndicatorEntity priceIndicatorEntity;
    private UserEntity userEntity;
    private List<PriceIndicatorEntity> priceIndicatorEntities;

    @BeforeEach
    void setUp() {
        AddressEto addressEto = new AddressEto(1L, "Wroclaw", "Wroblewskiego", "27", null, "51-627");
        IndicatorEto indicatorEto = new IndicatorEto(1L,"Podroz sluzbowa", "Paliwo, amortyzacja", "pl", 20, 30);
        List<IndicatorEto> indicatorEtos = new ArrayList<>();
        indicatorEtos.add(indicatorEto);

        ServiceEto serviceEto = new ServiceEto(1L, "Film produktowy", "Film produktow na bialym tle i odpowiednim oswietleniu", 500D, "pl", indicatorEtos);


        List<PermissionEto> permissionEtoList = new ArrayList<>();
        permissionEtoList.add(new PermissionEto(1L, ApplicationPermissions.A_CRUD_SUPER, "DESC1"));
        RoleEto roleEto = new RoleEto(1L, "ADMIN", "DESC1", permissionEtoList);
        AccountEto accountEto = new AccountEto(1L, "TEST", "PASS", "TEST@test.com", false);
        UserEto userEto = new UserEto(1L, "NAME1", "SURNAME1", accountEto, roleEto);

        List<PriceIndicatorEto> priceIndicatorEtoList = new ArrayList<>();
        bookingEto = new BookingEto(1L, "Film dla TestCompany", "Film produktowy z dojazdem", serviceEto, addressEto, userEto, false, 1400D,
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),0)),
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),1)),
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),0)),
                priceIndicatorEtoList);

        PriceIndicatorEto priceIndicatorEto = new PriceIndicatorEto(indicatorEto, bookingEto.getId(), 400,10);
        priceIndicatorEtoList.add(priceIndicatorEto);

        AddressTo addressTo = new AddressTo("Wroclaw", "Wroblewskiego", "27", null, "51-627");

        PriceIndicatorTo priceIndicatorTo = new PriceIndicatorTo(1L, 1L, 20, 0);
        List<PriceIndicatorTo> priceIndicatorToList = new ArrayList<>();
        priceIndicatorToList.add(priceIndicatorTo);

        bookingTo = new BookingTo("Film dla TestCompany", "Film produktowy z dojazdem", 1L, 1L, addressTo,
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),0)),
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),1)),
                priceIndicatorToList);

        addressEntity =  new AddressEntity("Wroclaw", "Wroblewskiego", "27", null, "51-627");
        addressEntity.setId(1L);

        addressEntity2 =  new AddressEntity("Poznan", "Wroblewskiego", "27", null, "51-627");
        addressEntity2.setId(1L);

        serviceEntity = new ServiceEntity("Film produktowy", "Film produktow na bialym tle i odpowiednim oswietleniu", 500D, "pl");
        indicatorEntity = new IndicatorEntity("Podroz sluzbowa", "Paliwo, amortyzacja", "pl", 20, 40);
        List<IndicatorEntity> indicatorEntities = new ArrayList<>();

        serviceEntity.setIndicatorList(indicatorEntities);

        PermissionEntity permissionEntity = new PermissionEntity(ApplicationPermissions.A_CRUD_SUPER, "DESC1");
        permissionEntity.setId(1L);
        List<PermissionEntity> permissionEntities = new ArrayList<>();
        permissionEntities.add(permissionEntity);

        RoleEntity roleEntity = new RoleEntity("ADMIN", "DESC1", permissionEntities);
        roleEntity.setId(1L);

        AccountEntity accountEntity = new AccountEntity("TEST", "PASS", "TEST@test.com", false);
        accountEntity.setId(1L);
        userEntity = new UserEntity("NAME1", "SURNAME1", roleEntity, accountEntity);
        userEntity.setId(1L);

        bookingEntity = new BookingEntity("Film dla TestCompany", "Film produktowy z dojazdem", 1400D,
                addressEntity, userEntity, serviceEntity, false, getCurrentDate(LocalDate.now(),0),
                getCurrentDate(LocalDate.now(),1), getCurrentDate(LocalDate.now(),0));
        bookingEntity.setId(1L);

        priceIndicatorEntity = new PriceIndicatorEntity(indicatorEntity, bookingEntity, 400, 10);
        priceIndicatorEntities = new ArrayList<>();
        priceIndicatorEntities.add(priceIndicatorEntity);
        bookingEntity.setPriceIndicatorList(priceIndicatorEntities);
    }

    public LocalDate getCurrentDate(LocalDate currentDate, int addDays) {
        if(addDays != 0) {
            currentDate = currentDate.plusDays(addDays);
        }
        return currentDate;
    }

    @Test
    @DisplayName("Test createBooking Success")
    void testCreateBookingSuccess() throws EntityAlreadyExistsException, EntityDoesNotExistException, UnprocessableEntityException {
        //Arrange
        when(serviceDao.findById(bookingTo.getServiceId())).thenReturn(Optional.of(serviceEntity));
        when(userDao.findById(bookingTo.getUserId())).thenReturn(Optional.of(userEntity));
        when(addressDao.save(any())).thenReturn(addressEntity);
        when(bookingDao.save(any())).thenReturn(bookingEntity);
        when(indicatorDao.findById(indicatorEntity.getId())).thenReturn(Optional.ofNullable(indicatorEntity));
        when(priceIndicatorDao.save(any())).thenReturn(priceIndicatorEntity);

        //Act
        Optional<BookingEto> result = ucManageBooking.createBooking(bookingTo);

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(result.get()).isEqualToIgnoringGivenFields(bookingEto, "serviceEto", "userEto", "addressEto", "priceIndicatorEtoList");
        assertThat(result.get().getAddressEto()).isEqualTo(bookingEto.getAddressEto());
        assertThat(result.get().getServiceEto()).isEqualTo(bookingEto.getServiceEto());
        assertThat(result.get().getPriceIndicatorEtoList()).isEqualTo(bookingEto.getPriceIndicatorEtoList());
        assertThat(result.get().getUserEto()).isEqualTo(bookingEto.getUserEto());
    }

    @Test
    @DisplayName("Test createBooking EntityDoesNotExistException")
    void testCreateBookingEntityDoesNotExistException() {
        //Arrange
        when(serviceDao.findById(bookingEntity.getService().getId())).thenReturn(Optional.empty());

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucManageBooking.createBooking(bookingTo));
    }

    @Test
    @DisplayName("Test createBooking EntityDoesNotExistException2")
    void testCreateBookingEntityDoesNotExistException2() throws EntityAlreadyExistsException, EntityDoesNotExistException {
        //Arrange
        when(serviceDao.findById(bookingTo.getServiceId())).thenReturn(Optional.of(serviceEntity));
        when(userDao.findById(bookingTo.getUserId())).thenReturn(Optional.of(userEntity));
        when(addressDao.save(any())).thenReturn(addressEntity);
        when(bookingDao.save(any())).thenReturn(bookingEntity);
        when(indicatorDao.findById(indicatorEntity.getId())).thenReturn(Optional.empty());

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucManageBooking.createBooking(bookingTo));
    }

    @Test
    @DisplayName("Test createBooking EntityDoesNotExistException3")
    void testCreateBookingEntityDoesNotExistException3() throws EntityAlreadyExistsException, EntityDoesNotExistException {
        //Arrange
        when(serviceDao.findById(bookingTo.getServiceId())).thenReturn(Optional.of(serviceEntity));
        when(userDao.findById(bookingTo.getUserId())).thenReturn(Optional.empty());

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucManageBooking.createBooking(bookingTo));
    }

    @Test
    @DisplayName("Test createBooking Success - AddressAlreadyExists")
    void testCreateBookingSuccessAddressAlreadyExists() throws EntityAlreadyExistsException, EntityDoesNotExistException, UnprocessableEntityException {
        //Arrange
        when(serviceDao.findById(bookingTo.getServiceId())).thenReturn(Optional.of(serviceEntity));
        when(userDao.findById(bookingTo.getUserId())).thenReturn(Optional.of(userEntity));
        when(addressValidator.isAddressAlreadyExists(any())).thenReturn(true);
        when(bookingDao.save(any())).thenReturn(bookingEntity);
        when(indicatorDao.findById(indicatorEntity.getId())).thenReturn(Optional.ofNullable(indicatorEntity));
        when(priceIndicatorDao.save(any())).thenReturn(priceIndicatorEntity);
        when(addressDao.findByCityAndStreetAndBuildingNumberAndApartmentNumberAndPostalCode(
                any(), any(), any(), any(), any()
        )).thenReturn(addressEntity);

        //Act
        Optional<BookingEto> result = ucManageBooking.createBooking(bookingTo);

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(result.get()).isEqualToIgnoringGivenFields(bookingEto, "serviceEto", "userEto", "addressEto", "priceIndicatorEtoList");
        assertThat(result.get().getAddressEto()).isEqualTo(bookingEto.getAddressEto());
        assertThat(result.get().getServiceEto()).isEqualTo(bookingEto.getServiceEto());
        assertThat(result.get().getPriceIndicatorEtoList()).isEqualTo(bookingEto.getPriceIndicatorEtoList());
        assertThat(result.get().getUserEto()).isEqualTo(bookingEto.getUserEto());
    }

    @Test
    @DisplayName("Test createBooking EntityAlreadyExistsException")
    void testCreateBookingEntityAlreadyExistsException() throws EntityAlreadyExistsException {
        //Arrange
        doThrow(EntityAlreadyExistsException.class)
                .when(bookingValidator).verifyIfBookingNameAlreadyExists(bookingEntity.getName());

        //Act Assert
        Assertions.assertThrows(EntityAlreadyExistsException.class, () ->
                ucManageBooking.createBooking(bookingTo));
    }

    @Test
    @DisplayName("Test updateBooking Success")
    void testUpdateBookingSuccess() throws EntityDoesNotExistException, EntityAlreadyExistsException, UnprocessableEntityException {
        //Arrange
        when(bookingDao.findById(bookingEntity.getId())).thenReturn(Optional.of(bookingEntity));
        when(indicatorDao.findById(indicatorEntity.getId())).thenReturn(Optional.ofNullable(indicatorEntity));
        when(priceIndicatorDao.save(any())).thenReturn(priceIndicatorEntity);

        //Act
        Optional<BookingEto> result = ucManageBooking.updateBooking(bookingTo, bookingEntity.getId());

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(result.get()).isEqualToIgnoringGivenFields(bookingEto, "serviceEto", "userEto", "addressEto", "priceIndicatorEtoList");
        assertThat(result.get().getAddressEto()).isEqualTo(bookingEto.getAddressEto());
        assertThat(result.get().getServiceEto()).isEqualTo(bookingEto.getServiceEto());
        assertThat(result.get().getPriceIndicatorEtoList()).isEqualTo(bookingEto.getPriceIndicatorEtoList());
        assertThat(result.get().getUserEto()).isEqualTo(bookingEto.getUserEto());
    }

    @Test
    @DisplayName("Test updateBooking Success2")
    void testUpdateBookingSuccess2() throws EntityDoesNotExistException, EntityAlreadyExistsException, UnprocessableEntityException {
        //Arrange
        bookingTo.setName("Nowa nazwa");
        bookingTo.setStart(DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),1)));
        bookingTo.setAddressTo(null);
        bookingTo.setPriceIndicatorToList(Collections.emptyList());
        bookingTo.setServiceId(2L);
        bookingEto.setPredictedPrice(900D);
        bookingEto.setName("Nowa nazwa");
        bookingEto.setStart(DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),1)));
        bookingEto.setAddressEto(null);
        bookingEto.getServiceEto().setId(2L);
        when(bookingDao.findById(bookingEntity.getId())).thenReturn(Optional.of(bookingEntity));
        when(indicatorDao.findById(indicatorEntity.getId())).thenReturn(Optional.ofNullable(indicatorEntity));
        when(priceIndicatorDao.save(any())).thenReturn(priceIndicatorEntity);
        when(serviceDao.findById(bookingTo.getServiceId())).thenReturn(Optional.of(serviceEntity));

        //Act
        Optional<BookingEto> result = ucManageBooking.updateBooking(bookingTo, bookingEntity.getId());

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(result.get()).isEqualToIgnoringGivenFields(bookingEto, "serviceEto", "userEto", "addressEto", "priceIndicatorEtoList");
        assertThat(result.get().getAddressEto()).isEqualTo(bookingEto.getAddressEto());
        assertThat(result.get().getServiceEto()).isEqualTo(bookingEto.getServiceEto());
        assertThat(result.get().getPriceIndicatorEtoList()).isEqualTo(bookingEto.getPriceIndicatorEtoList());
        assertThat(result.get().getUserEto()).isEqualTo(bookingEto.getUserEto());
    }

    @Test
    @DisplayName("Test updateBooking Success3")
    void testUpdateBookingSuccess3() throws EntityDoesNotExistException, EntityAlreadyExistsException, UnprocessableEntityException {
        //Arrange
        bookingTo.getAddressTo().setCity("Poznan");
        bookingEto.getAddressEto().setCity("Poznan");
        when(bookingDao.findById(bookingEntity.getId())).thenReturn(Optional.of(bookingEntity));
        when(indicatorDao.findById(indicatorEntity.getId())).thenReturn(Optional.ofNullable(indicatorEntity));
        when(priceIndicatorDao.save(any())).thenReturn(priceIndicatorEntity);

        //Act
        Optional<BookingEto> result = ucManageBooking.updateBooking(bookingTo, bookingEntity.getId());

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(result.get()).isEqualToIgnoringGivenFields(bookingEto, "serviceEto", "userEto", "addressEto", "priceIndicatorEtoList");
        assertThat(result.get().getAddressEto()).isEqualTo(bookingEto.getAddressEto());
        assertThat(result.get().getServiceEto()).isEqualTo(bookingEto.getServiceEto());
        assertThat(result.get().getPriceIndicatorEtoList()).isEqualTo(bookingEto.getPriceIndicatorEtoList());
        assertThat(result.get().getUserEto()).isEqualTo(bookingEto.getUserEto());
    }

    @Test
    @DisplayName("Test updateBooking Success4")
    void testUpdateBookingSuccess4() throws EntityDoesNotExistException, EntityAlreadyExistsException, UnprocessableEntityException {
        //Arrange
        bookingTo.getAddressTo().setCity("Poznan");
        bookingEto.getAddressEto().setCity("Poznan");

        when(bookingDao.findById(bookingEntity.getId())).thenReturn(Optional.of(bookingEntity));
        when(indicatorDao.findById(indicatorEntity.getId())).thenReturn(Optional.ofNullable(indicatorEntity));
        when(priceIndicatorDao.save(any())).thenReturn(priceIndicatorEntity);
        when(addressValidator.isAddressAlreadyExists(any())).thenReturn(true);
        when(addressDao.findByCityAndStreetAndBuildingNumberAndApartmentNumberAndPostalCode(
                any(), any(), any(), any(), any())).thenReturn(addressEntity2);

        //Act
        Optional<BookingEto> result = ucManageBooking.updateBooking(bookingTo, bookingEntity.getId());

        // Assert
        Assertions.assertTrue(result.isPresent());

        assertThat(result.get()).isEqualToIgnoringGivenFields(bookingEto, "serviceEto", "userEto", "addressEto", "priceIndicatorEtoList");
        assertThat(result.get().getAddressEto()).isEqualTo(bookingEto.getAddressEto());
        assertThat(result.get().getServiceEto()).isEqualTo(bookingEto.getServiceEto());
        assertThat(result.get().getPriceIndicatorEtoList()).isEqualTo(bookingEto.getPriceIndicatorEtoList());
        assertThat(result.get().getUserEto()).isEqualTo(bookingEto.getUserEto());
    }

    @Test
    @DisplayName("Test updateBooking EntityAlreadyExistsException")
    void testUpdateBookingEntityAlreadyExistsException() throws EntityAlreadyExistsException {
        //Arrange
        bookingTo.setName("Fotografia dla firmy");
        when(bookingDao.findById(bookingEntity.getId())).thenReturn(Optional.of(bookingEntity));
        doThrow(EntityAlreadyExistsException.class)
                .when(bookingValidator).verifyIfBookingNameAlreadyExists(any());

        //Act Assert
        Assertions.assertThrows(EntityAlreadyExistsException.class, () ->
                ucManageBooking.updateBooking(bookingTo, bookingEntity.getId()));
    }

    @Test
    @DisplayName("Test updateBooking EntityDoesNotExistException")
    void testUpdateBookingEntityDoesNotExistException() {
        //Arrange
        when(bookingDao.findById(bookingEntity.getId())).thenReturn(Optional.empty());

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucManageBooking.updateBooking(bookingTo, bookingEntity.getId()));
    }

    @Test
    @DisplayName("Test confirmBooking OK")
    void testConfirmBookingOk() throws Exception {
        //Arrange
        List<PriceIndicatorEto> priceIndicatorEtoList = new ArrayList<>();
        AddressEto addressEto = new AddressEto(1L, "Wroclaw", "Wroblewskiego", "27", null, "51-627");
        IndicatorEto indicatorEto = new IndicatorEto(1L,"Podroz sluzbowa", "Paliwo, amortyzacja", "pl", 20, 30);
        List<IndicatorEto> indicatorEtos = new ArrayList<>();
        indicatorEtos.add(indicatorEto);

        List<PermissionEto> permissionEtoList = new ArrayList<>();
        permissionEtoList.add(new PermissionEto(1L, ApplicationPermissions.A_CRUD_SUPER, "DESC1"));
        ServiceEto serviceEto = new ServiceEto(1L, "Film produktowy", "Film produktow na bialym tle i odpowiednim oswietleniu", 500D, "pl", indicatorEtos);
        RoleEto roleEto = new RoleEto(1L, "ADMIN", "DESC1", permissionEtoList);
        AccountEto accountEto = new AccountEto(1L, "TEST", "PASS", "TEST@test.com", false);
        UserEto userEto = new UserEto(1L, "NAME1", "SURNAME1", accountEto, roleEto);

        BookingEtoWithOrderNumber bookingEtoWithOrderNumber = new BookingEtoWithOrderNumber(1L, "Film dla TestCompany",
                "Film produktowy z dojazdem", serviceEto, addressEto, userEto, true, 1400D,
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),0)),
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),1)),
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),0)),
                priceIndicatorEtoList, "INVIU_00001");

        PriceIndicatorEto priceIndicatorEto = new PriceIndicatorEto(indicatorEto, bookingEto.getId(), 400, 10);
        priceIndicatorEtoList.add(priceIndicatorEto);

        OrderEntity orderEntity = new OrderEntity("INVIU_00001", OrderStatus.NEW, 1400D, LocalDate.now(), userEntity, userEntity,  bookingEntity);

        when(bookingDao.findById(bookingEntity.getId())).thenReturn(Optional.ofNullable(bookingEntity));
        when(userDao.findById(userEntity.getId())).thenReturn(Optional.ofNullable(userEntity));
        when(orderDao.save(any())).thenReturn(orderEntity);

        //Act
        Optional<BookingEtoWithOrderNumber> result = ucManageBooking.confirmBooking(bookingEto.getId(), userEntity.getId());

        // Assert
        Assertions.assertTrue(result.isPresent());

        assertThat(result.get()).isEqualToIgnoringGivenFields(bookingEtoWithOrderNumber, "serviceEto", "addressEto", "priceIndicatorEtoList");
        assertThat(result.get().getAddressEto()).isEqualTo(bookingEtoWithOrderNumber.getAddressEto());
        assertThat(result.get().getServiceEto()).isEqualTo(bookingEtoWithOrderNumber.getServiceEto());
        assertThat(result.get().getPriceIndicatorEtoList()).isEqualTo(bookingEtoWithOrderNumber.getPriceIndicatorEtoList());
    }


    @Test
    @DisplayName("Test confirmBooking EntityDoesNotExistException")
    void testConfirmBookingEntityDoesNotExistException() {
        //Arrange
        when(bookingDao.findById(bookingEntity.getId())).thenReturn(Optional.empty());

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucManageBooking.confirmBooking(bookingEntity.getId(), userEntity.getId()));
    }
}
