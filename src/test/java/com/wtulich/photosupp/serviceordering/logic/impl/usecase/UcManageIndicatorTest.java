package com.wtulich.photosupp.serviceordering.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.IndicatorDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.IndicatorEntity;
import com.wtulich.photosupp.serviceordering.logic.api.to.IndicatorEto;
import com.wtulich.photosupp.serviceordering.logic.api.to.IndicatorTo;
import com.wtulich.photosupp.serviceordering.logic.impl.validator.IndicatorValidator;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class UcManageIndicatorTest {

    @Autowired
    private UcManageIndicatorImpl ucManageIndicator;

    @MockBean
    private IndicatorDao indicatorDao;

    @MockBean
    private IndicatorValidator indicatorValidator;

    private IndicatorEntity indicatorEntity;
    private IndicatorEto indicatorEto;
    private IndicatorTo indicatorTo;

    @BeforeEach
    void setUp() {
        IndicatorEntity indicatorEntity = new IndicatorEntity("Podroz sluzbowa", "Paliwo, amortyzacja", "pl", 20, 40);
        indicatorEntity.setId(1L);

        indicatorEto = new IndicatorEto(1L, "Podroz sluzbowa", "Paliwo, amortyzacja", "pl", 20, 40);

        indicatorTo = new IndicatorTo("Podroz sluzbowa", "Paliwo, amortyzacja", "pl", 20, 40);
    }

    @Test
    @DisplayName("Test createIndicator Success")
    void testCreateIndicatorSuccess() throws EntityAlreadyExistsException {
        //Arrange
        when(indicatorDao.save(indicatorEntity)).thenReturn(indicatorEntity);

        //Act
        Optional<IndicatorEto> result = ucManageIndicator.createIndicator(indicatorTo);

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(indicatorEto).isEqualToComparingFieldByField(result.get());
    }

    @Test
    @DisplayName("Test createIndicator EntityAlreadyExistsException")
    void testCreateIndicatorEntityAlreadyExistsException() throws EntityAlreadyExistsException {
        //Arrange
        doThrow(EntityAlreadyExistsException.class)
                .when(indicatorValidator).verifyIfIndicatorNameAlreadyExists(indicatorEntity.getName());

        //Act Assert
        Assertions.assertThrows(EntityAlreadyExistsException.class, () ->
                ucManageIndicator.createIndicator(indicatorTo));
    }

    @Test
    @DisplayName("Test updateIndicator Success")
    void testUpdateIndicatorSuccess() throws EntityDoesNotExistException, EntityAlreadyExistsException {
        //Arrange
        indicatorTo.setName("Nowy");
        indicatorEto.setName("Nowy");
        when(indicatorDao.findById(indicatorEntity.getId())).thenReturn(Optional.of(indicatorEntity));

        //Act
        Optional<IndicatorEto> result = ucManageIndicator.updateIndicator(indicatorTo, indicatorEntity.getId());

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(indicatorEto).isEqualToComparingFieldByField(result.get());
    }

    @Test
    @DisplayName("Test updateIndicator EntityAlreadyExistsException")
    void testUpdateIndicatorEntityAlreadyExistsException() throws EntityAlreadyExistsException {
        //Arrange
        indicatorTo.setName("Dodatkowy sprzet");
        when(indicatorDao.findById(indicatorEntity.getId())).thenReturn(Optional.of(indicatorEntity));
        doThrow(EntityAlreadyExistsException.class)
                .when(indicatorValidator).verifyIfIndicatorNameAlreadyExists(indicatorTo.getName());

        //Act Assert
        Assertions.assertThrows(EntityAlreadyExistsException.class, () ->
                ucManageIndicator.updateIndicator(indicatorTo, indicatorEntity.getId()));
    }

    @Test
    @DisplayName("Test updateIndicator EntityDoesNotExistException")
    void testUpdateIndicatorEntityDoesNotExistException() {
        //Arrange
        when(indicatorDao.findById(indicatorEntity.getId())).thenReturn(Optional.empty());

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucManageIndicator.updateIndicator(indicatorTo, indicatorEntity.getId()));
    }
}
