package com.wtulich.photosupp.serviceordering.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.BookingDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.BookingEntity;
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

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class UcDeleteBookingTest {

    @Autowired
    private UcDeleteBookingImpl ucDeleteBooking;

    @MockBean
    private BookingDao bookingDao;

    private BookingEntity bookingEntity;

    @BeforeEach
    void setUp() {
        bookingEntity = new BookingEntity();
    }

    @Test
    @DisplayName("Test deleteBooking Success")
    void testDeleteBookingSuccess() {
        //Arrange
        when(bookingDao.findById(1L)).thenReturn(Optional.of(bookingEntity));

        //Act Assert
        Assertions.assertDoesNotThrow(() -> ucDeleteBooking.deleteBooking(1L));
    }


    @Test
    @DisplayName("Test deleteBooking Failure")
    void testDeleteBookingFailure() {
        //Arrange
        when(bookingDao.findById(1L)).thenReturn(java.util.Optional.ofNullable(null));

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucDeleteBooking.deleteBooking(1L));
    }
}
