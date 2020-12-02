package com.wtulich.photosupp.serviceordering.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.logic.api.exception.UnprocessableEntityException;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.IndicatorDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.ServiceDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.IndicatorEntity;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.ServiceEntity;
import com.wtulich.photosupp.serviceordering.logic.api.to.*;
import com.wtulich.photosupp.serviceordering.logic.impl.validator.BookingValidator;
import com.wtulich.photosupp.userhandling.logic.api.to.PermissionEto;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class UcCalculateServiceTest {

    @Autowired
    private UcCalculateServiceImpl ucCalculateService;

    @MockBean
    private ServiceDao serviceDao;

    @MockBean
    private IndicatorDao indicatorDao;

    @MockBean
    private BookingValidator bookingValidator;

    private CalculateCto calculateCto;
    private CalculateTo calculateTo;
    private ServiceEntity serviceEntity;
    private IndicatorEntity indicatorEntity;

    @BeforeEach
    void setUp() {
        IndicatorEto indicatorEto = new IndicatorEto(1L,"Podroz sluzbowa", "Paliwo, amortyzacja", "pl", 20, 30);
        List<IndicatorEto> indicatorEtos = List.of(indicatorEto);

        ServiceEto serviceEto = new ServiceEto(1L, "Film produktowy", "Film produktow na bialym tle i odpowiednim oswietleniu", 500D, "pl", indicatorEtos);
        List<PriceIndicatorEto> priceIndicatorEtoList = new ArrayList<>();

        PriceIndicatorEto priceIndicatorEto = new PriceIndicatorEto(indicatorEto, null, 400, 10);
        priceIndicatorEtoList.add(priceIndicatorEto);

        calculateCto = new CalculateCto(serviceEto, 1400D,
                DateTimeFormatter.ofPattern("yyyy-MM-dd").format(getCurrentDate(LocalDate.now(), 0)),
                DateTimeFormatter.ofPattern("yyyy-MM-dd").format(getCurrentDate(LocalDate.now(), 1)),
                priceIndicatorEtoList);

        PriceIndicatorTo priceIndicatorTo = new PriceIndicatorTo(1L, 1L, 20, 0);
        List<PriceIndicatorTo> priceIndicatorToList = new ArrayList<>();
        priceIndicatorToList.add(priceIndicatorTo);

        calculateTo = new CalculateTo(1L,
                DateTimeFormatter.ofPattern("yyyy-MM-dd").format(getCurrentDate(LocalDate.now(), 0)),
                DateTimeFormatter.ofPattern("yyyy-MM-dd").format(getCurrentDate(LocalDate.now(), 1)),
                priceIndicatorToList);

        ServiceEntity serviceEntity = new ServiceEntity("Film produktowy", "Film produktow na bialym tle i odpowiednim oswietleniu", 500D, "pl");
        IndicatorEntity indicatorEntity = new IndicatorEntity("Podroz sluzbowa", "Paliwo, amortyzacja", "pl", 20, 40);
        serviceEntity.setIndicatorList(List.of(indicatorEntity));

    }

    public LocalDate getCurrentDate(LocalDate currentDate, int addDays) {
        if(addDays != 0) {
            currentDate = currentDate.plusDays(addDays);
        }
        return currentDate;
    }

    @Test
    @DisplayName("Test calculateService Success")
    void testCalculateServiceSuccess() throws EntityDoesNotExistException, UnprocessableEntityException {
        //Arrange
        when(serviceDao.findById(calculateTo.getServiceId())).thenReturn(Optional.of(serviceEntity));
        when(indicatorDao.findById(1L)).thenReturn(Optional.of(indicatorEntity));

        //Act
        Optional<CalculateCto> result = ucCalculateService.calculateService(calculateTo);

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(calculateCto).isEqualToComparingFieldByField(result.get());
    }

    @Test
    @DisplayName("Test calculateService EntityDoesNotExistException")
    void testCalculateServiceEntityDoesNotExistException() {
        //Arrange
        when(serviceDao.findById(calculateTo.getServiceId())).thenReturn(Optional.empty());

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucCalculateService.calculateService(calculateTo));
    }

    @Test
    @DisplayName("Test calculateService EntityDoesNotExistException2")
    void testCalculateServiceEntityDoesNotExistException2() {
        //Arrange
        when(serviceDao.findById(calculateTo.getServiceId())).thenReturn(Optional.of(serviceEntity));
        when(indicatorDao.findById(1L)).thenReturn(Optional.empty());

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucCalculateService.calculateService(calculateTo));
    }

    @Test
    @DisplayName("Test calculateService UnprocessableEntityException")
    void testCalculateServiceUnprocessableEntityException() throws UnprocessableEntityException {
        //Arrange
        when(serviceDao.findById(calculateTo.getServiceId())).thenReturn(Optional.of(serviceEntity));
        when(indicatorDao.findById(1L)).thenReturn(Optional.of(indicatorEntity));

        doThrow(UnprocessableEntityException.class)
                .when(bookingValidator).verifyIfDatesAreValid(any(), any());

        //Act Assert
        Assertions.assertThrows(UnprocessableEntityException.class, () ->
                ucCalculateService.calculateService(calculateTo));
    }
}
