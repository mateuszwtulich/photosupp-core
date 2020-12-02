package com.wtulich.photosupp.serviceordering.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.logic.api.exception.EntityHasAssignedEntitiesException;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.IndicatorDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.PriceIndicatorDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.IndicatorEntity;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.PriceIndicatorEntity;
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
public class UcDeleteIndicatorTest {

    @Autowired
    private UcDeleteIndicatorImpl ucDeleteIndicator;

    @MockBean
    private IndicatorDao indicatorDao;

    @MockBean
    private PriceIndicatorDao priceIndicatorDao;

    private IndicatorEntity indicatorEntity;

    @BeforeEach
    void setUp() {
        IndicatorEntity indicatorEntity = new IndicatorEntity("Podroz sluzbowa", "Paliwo, amortyzacja", "pl", 20, 40);
        indicatorEntity.setId(1L);
    }

    @Test
    @DisplayName("Test deleteIndicator Success")
    void testDeleteIndicatorSuccess() {
        //Arrange
        when(indicatorDao.findById(indicatorEntity.getId())).thenReturn(Optional.of(indicatorEntity));
        when(priceIndicatorDao.findAllByIndicator_Id(indicatorEntity.getId())).thenReturn(Collections.emptyList());

        //Act Assert
        Assertions.assertDoesNotThrow(() -> ucDeleteIndicator.deleteIndicator(indicatorEntity.getId()));
    }


    @Test
    @DisplayName("Test deleteIndicator Failure")
    void testDeleteIndicatorFailure() {
        //Arrange
        when(indicatorDao.findById(indicatorEntity.getId())).thenReturn(java.util.Optional.ofNullable(null));

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucDeleteIndicator.deleteIndicator(indicatorEntity.getId()));
    }

    @Test
    @DisplayName("Test deleteService Failure2")
    void testDeleteIndicatorFailure2() {
        //Arrange
        List<PriceIndicatorEntity> priceIndicatorEntityList = new ArrayList<>();
        priceIndicatorEntityList.add(new PriceIndicatorEntity());
        when(indicatorDao.findById(indicatorEntity.getId())).thenReturn(Optional.of(indicatorEntity));
        when(priceIndicatorDao.findAllByIndicator_Id(indicatorEntity.getId())).thenReturn(priceIndicatorEntityList);

        //Act Assert
        Assertions.assertThrows(EntityHasAssignedEntitiesException.class, () ->
                ucDeleteIndicator.deleteIndicator(indicatorEntity.getId()));
    }
}
