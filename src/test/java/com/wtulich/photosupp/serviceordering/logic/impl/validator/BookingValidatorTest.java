package com.wtulich.photosupp.serviceordering.logic.impl.validator;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.general.logic.api.exception.UnprocessableEntityException;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.BookingDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.AddressEntity;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.BookingEntity;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.ServiceEntity;
import com.wtulich.photosupp.serviceordering.logic.api.to.AddressTo;
import com.wtulich.photosupp.serviceordering.logic.api.to.BookingTo;
import com.wtulich.photosupp.serviceordering.logic.api.to.PriceIndicatorTo;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class BookingValidatorTest {

    @Autowired
    private BookingValidator bookingValidator;

    @MockBean
    private BookingDao bookingDao;

    private BookingTo bookingTo;

    @BeforeEach
    void setUp() {
        AddressTo addressTo = new AddressTo("Wrocław", "Wróblewskiego", "27", null, "51-627");

        PriceIndicatorTo priceIndicatorTo = new PriceIndicatorTo(1L, 1L, 20, 0);
        List<PriceIndicatorTo> priceIndicatorToList = new ArrayList<>();
        priceIndicatorToList.add(priceIndicatorTo);

        bookingTo = new BookingTo("Film dla TestCompany", "Film produktowy z dojazdem", 1L, 1L, addressTo,
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),0)),
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),1)),
                priceIndicatorToList);
    }

    public LocalDate getCurrentDate(LocalDate currentDate, int addDays) {
        if(addDays != 0) {
            currentDate = currentDate.plusDays(addDays);
        }
        return currentDate;
    }

    @Test
    @DisplayName("Test verifyIfIndicatorAlreadyExists Success")
    void verifyIfServiceAlreadyExistsSuccess() {
        //Arrange
        when(bookingDao.existsByName(bookingTo.getName())).thenReturn(true);

        //Act Assert
        Assertions.assertThrows(EntityAlreadyExistsException.class, () ->
                bookingValidator.verifyIfBookingNameAlreadyExists(bookingTo.getName()));
    }

    @Test
    @DisplayName("Test verifyIfIndicatorAlreadyExists Failure")
    void verifyIfRoleAlreadyExistsFailure() {
        //Arrange
        when(bookingDao.existsByName(bookingTo.getName())).thenReturn(false);

        //Act Assert
        Assertions.assertDoesNotThrow(() ->  bookingValidator.verifyIfBookingNameAlreadyExists(bookingTo.getName()));
    }

    @Test
    @DisplayName("Test verifyIfBookingAlreadyCreatedAtThatDate Success")
    void verifyIfBookingAlreadyCreatedAtThatDateSuccess() {
        //Arrange
        AddressEntity addressEntity =  new AddressEntity("Wroclaw", "Wroblewskiego", "27", null, "51-627");
        ServiceEntity serviceEntity = new ServiceEntity("Film produktowy", "Film produktow na bialym tle i odpowiednim oswietleniu", 500D, "pl");

        BookingEntity bookingEntity1 = new BookingEntity("Film dla TestCompany", "Film produktowy z dojazdem", 900D,
                addressEntity, new UserEntity(), serviceEntity, false, getCurrentDate(LocalDate.now(),1),
                getCurrentDate(LocalDate.now(),2), getCurrentDate(LocalDate.now(),0));
        BookingEntity bookingEntity2 = new BookingEntity("Film dla TestCompany", "Film produktowy z dojazdem", 900D,
                addressEntity, new UserEntity(), serviceEntity, false, getCurrentDate(LocalDate.now(),0),
                getCurrentDate(LocalDate.now(),1), getCurrentDate(LocalDate.now(),0));

        bookingTo.setUserId(1L);
        List<BookingEntity> bookingEntities = new ArrayList<>();
        bookingEntities.add(bookingEntity1);
        bookingEntities.add(bookingEntity2);

        when(bookingDao.findAllByUser_Id(1L)).thenReturn(bookingEntities);

        //Act Assert
        Assertions.assertThrows(EntityAlreadyExistsException.class, () ->
                bookingValidator.verifyIfBookingAlreadyCreatedAtThatDate(bookingTo));
    }

    @Test
    @DisplayName("Test verifyIfBookingAlreadyCreatedAtThatDate Failure")
    void verifyIfBookingAlreadyCreatedAtThatDateFailure() {
        //Arrange
        AddressEntity addressEntity =  new AddressEntity("Wroclaw", "Wroblewskiego", "27", null, "51-627");
        ServiceEntity serviceEntity = new ServiceEntity("Film produktowy", "Film produktow na bialym tle i odpowiednim oswietleniu", 500D, "pl");

        BookingEntity bookingEntity1 = new BookingEntity("Film dla TestCompany", "Film produktowy z dojazdem", 900D,
                addressEntity, new UserEntity(), serviceEntity, false, getCurrentDate(LocalDate.now(),2),
                getCurrentDate(LocalDate.now(),4), getCurrentDate(LocalDate.now(),0));
        BookingEntity bookingEntity2 = new BookingEntity("Film dla TestCompany", "Film produktowy z dojazdem", 900D,
                addressEntity, new UserEntity(), serviceEntity, false, getCurrentDate(LocalDate.now(),-2),
                getCurrentDate(LocalDate.now(),-1), getCurrentDate(LocalDate.now(),0));

        bookingTo.setUserId(1L);
        List<BookingEntity> bookingEntities = new ArrayList<>();
        bookingEntities.add(bookingEntity1);
        bookingEntities.add(bookingEntity2);

        when(bookingDao.findAllByUser_Id(1L)).thenReturn(bookingEntities);

        //Act Assert
        Assertions.assertDoesNotThrow(() ->  bookingValidator.verifyIfBookingAlreadyCreatedAtThatDate(bookingTo));
    }

    @Test
    @DisplayName("Test verifyIfDatesAreValid Success")
    void verifyIfDatesAreValidSuccess() {
        //Act Assert
        Assertions.assertDoesNotThrow(() ->
                bookingValidator.
                        verifyIfDatesAreValid(getCurrentDate(LocalDate.now(),0), getCurrentDate(LocalDate.now(),0)));
    }

    @Test
    @DisplayName("Test verifyIfDatesAreValid Failure")
    void verifyIfDatesAreValidFailure() {
        //Act Assert
        Assertions.assertThrows(UnprocessableEntityException.class, () ->
                bookingValidator.
                        verifyIfDatesAreValid(getCurrentDate(LocalDate.now(),1), getCurrentDate(LocalDate.now(),0)));
    }

}
