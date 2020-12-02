package com.wtulich.photosupp.serviceordering.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.logic.api.exception.EntityHasAssignedEntitiesException;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.BookingDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.ServiceDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.BookingEntity;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.ServiceEntity;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class UcDeleteServiceTest {

    @Autowired
    private UcDeleteServiceImpl ucDeleteService;

    @MockBean
    private ServiceDao serviceDao;

    @MockBean
    private BookingDao bookingDao;

    private ServiceEntity serviceEntity;

    @BeforeEach
    void setUp() {
        serviceEntity = new ServiceEntity("Film produktowy", "Film produktow na bialym tle i odpowiednim oswietleniu", 500D, "pl");
        serviceEntity.setId(1L);
    }

    @Test
    @DisplayName("Test deleteService Success")
    void testDeleteServiceSuccess() {
        //Arrange
        when(serviceDao.findById(serviceEntity.getId())).thenReturn(Optional.of(serviceEntity));
        when(bookingDao.findAllByService_Id(serviceEntity.getId())).thenReturn(Collections.emptyList());

        //Act Assert
        Assertions.assertDoesNotThrow(() -> ucDeleteService.deleteService(serviceEntity.getId()));
    }


    @Test
    @DisplayName("Test deleteService Failure")
    void testDeleteIndicatorFailure() {
        //Arrange
        when(serviceDao.findById(serviceEntity.getId())).thenReturn(java.util.Optional.ofNullable(null));

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucDeleteService.deleteService(serviceEntity.getId()));
    }

    @Test
    @DisplayName("Test deleteService Failure2")
    void testDeleteIndicatorFailure2() {
        //Arrange
        List<BookingEntity> bookingEntities = new ArrayList<>();
        bookingEntities.add(new BookingEntity());
        when(serviceDao.findById(serviceEntity.getId())).thenReturn(Optional.of(serviceEntity));
        when(bookingDao.findAllByService_Id(serviceEntity.getId())).thenReturn(bookingEntities);

        //Act Assert
        Assertions.assertThrows(EntityHasAssignedEntitiesException.class, () ->
                ucDeleteService.deleteService(serviceEntity.getId()));
    }
}
