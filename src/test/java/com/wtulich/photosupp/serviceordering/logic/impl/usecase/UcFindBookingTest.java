package com.wtulich.photosupp.serviceordering.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.BookingDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.*;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.AddressMapper;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.IndicatorMapper;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.ServiceMapper;
import com.wtulich.photosupp.serviceordering.logic.api.to.BookingEto;
import com.wtulich.photosupp.serviceordering.logic.api.to.PriceIndicatorEto;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class UcFindBookingTest {

    @Autowired
    private UcFindBookingImpl ucFindBooking;

    @Autowired
    private ServiceMapper serviceMapper;

    @Autowired
    private IndicatorMapper indicatorMapper;

    @Autowired
    private AddressMapper addressMapper;

    @MockBean
    private BookingDao bookingDao;

    private BookingEntity bookingEntity;
    private List<BookingEntity> bookingEntities;
    private BookingEto bookingEto;
    private List<BookingEto> bookingEtoList;

    @BeforeEach
    void setUp() {
        AddressEntity addressEntity =  new AddressEntity("Wroclaw", "Wroblewskiego", "27", null, "51-627");
        ServiceEntity serviceEntity = new ServiceEntity("Film produktowy", "Film produktow na bialym tle i odpowiednim oswietleniu", 500D, "pl");
        IndicatorEntity indicatorEntity = new IndicatorEntity("Podroz sluzbowa", "Paliwo, amortyzacja", "pl", 20, 40);
        ArrayList<IndicatorEntity> indicatorEntities = new ArrayList<>();
        indicatorEntities.add(indicatorEntity);
        serviceEntity.setId(1L);
        indicatorEntity.setId(1L);

        PermissionEntity permissionEntity = new PermissionEntity(ApplicationPermissions.A_CRUD_SUPER, "DESC1");
        permissionEntity.setId(1L);
        List<PermissionEntity> permissionEntities = new ArrayList<>();
        permissionEntities.add(permissionEntity);

        RoleEntity roleEntity = new RoleEntity("ADMIN", "DESC1", permissionEntities);
        roleEntity.setId(1L);

        AccountEntity accountEntity = new AccountEntity("TEST", "PASS", "TEST@test.com", false);
        accountEntity.setId(1L);
        UserEntity userEntity = new UserEntity("NAME1", "SURNAME1", roleEntity, accountEntity);
        userEntity.setId(1L);

        bookingEntity = new BookingEntity("Film dla TestCompany", "Film produktowy z dojazdem", 900D,
                addressEntity, userEntity, serviceEntity, false, getCurrentDate(LocalDate.now(),0),
                getCurrentDate(LocalDate.now(),1), getCurrentDate(LocalDate.now(),0));
        bookingEntity.setId(1L);

        PriceIndicatorEntity priceIndicatorEntity = new PriceIndicatorEntity(indicatorEntity, bookingEntity, 400, 10);
        List<PriceIndicatorEntity> priceIndicatorEntities = new ArrayList<>();
        priceIndicatorEntities.add(priceIndicatorEntity);
        bookingEntity.setPriceIndicatorList(priceIndicatorEntities);

        bookingEntities = new ArrayList<>();
        bookingEntities.add(bookingEntity);

        List<PermissionEto> permissionEtoList = new ArrayList<>();
        permissionEtoList.add(new PermissionEto(1L, ApplicationPermissions.A_CRUD_SUPER, "DESC1"));
        RoleEto roleEto = new RoleEto(1L, "ADMIN", "DESC1", permissionEtoList);
        AccountEto accountEto = new AccountEto(1L, "TEST", "PASS", "TEST@test.com", false);
        UserEto userEto = new UserEto(1L, "NAME1", "SURNAME1", accountEto, roleEto);

        bookingEto = new BookingEto(1L, "Film dla TestCompany", "Film produktowy z dojazdem", serviceMapper.toServiceEto(serviceEntity),
                addressMapper.toAddressEto(addressEntity), userEto, false, 900D,
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),0)),
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),1)),
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),0)),
                null);

        PriceIndicatorEto priceIndicatorEto = new PriceIndicatorEto(indicatorMapper.toIndicatorEto(indicatorEntity), bookingEto.getId(), 400, 10);
        List<PriceIndicatorEto> priceIndicatorEtoList = new ArrayList<>();
        priceIndicatorEtoList.add(priceIndicatorEto);

        bookingEto.setPriceIndicatorEtoList(priceIndicatorEtoList);

        bookingEtoList = new ArrayList<>();
        bookingEtoList.add(bookingEto);
    }

    public LocalDate getCurrentDate(LocalDate currentDate, int addDays) {
        if(addDays != 0) {
            currentDate = currentDate.plusDays(addDays);
        }
        return currentDate;
    }

    @Test
    @DisplayName("Test findAllBookings Success")
    void testFindAllBookingsSuccess() {
        //Arrange
        when(bookingDao.findAll()).thenReturn(bookingEntities);

        //Act
        Optional<List<BookingEto>> result = ucFindBooking.findAllBookings();

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(result.get()).hasSize(bookingEtoList.size());
        assertThat(bookingEtoList.get(0)).isEqualTo(result.get().get(0));
    }

    @Test
    @DisplayName("Test findAllBookings No content")
    void testFindAllBookingsNoContent() {
        //Arrange
        when(bookingDao.findAll()).thenReturn(new ArrayList<>());

        //Act
        Optional<List<BookingEto>> result = ucFindBooking.findAllBookings();

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertTrue(result.get().size() == 0);
    }

    @Test
    @DisplayName("Test findBookingById Success")
    void testFindBookingByIdSuccess() throws EntityDoesNotExistException {
        //Arrange
        when(bookingDao.findById(bookingEntity.getId())).thenReturn(Optional.of(bookingEntity));

        //Act
        Optional<BookingEto> result = ucFindBooking.findBooking(bookingEntity.getId());

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(result.get()).isEqualTo(bookingEto);
    }

    @Test
    @DisplayName("Test findBookingById Failure")
    void testFindBookingByIdFailure() {
        //Arrange
        when(bookingDao.findById(bookingEntity.getId())).thenReturn(Optional.ofNullable(null));

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucFindBooking.findBooking(bookingEntity.getId()));
    }
}
