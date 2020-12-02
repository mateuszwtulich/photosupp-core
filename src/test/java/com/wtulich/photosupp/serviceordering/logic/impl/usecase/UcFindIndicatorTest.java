package com.wtulich.photosupp.serviceordering.logic.impl.usecase;

import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.IndicatorDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.IndicatorEntity;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.IndicatorMapper;
import com.wtulich.photosupp.serviceordering.logic.api.to.IndicatorEto;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class UcFindIndicatorTest {

    @Autowired
    private UcFindIndicatorImpl ucFindIndicator;

    @Autowired
    private IndicatorMapper indicatorMapper;

    @MockBean
    private IndicatorDao indicatorDao;

    private IndicatorEntity indicatorEntity;
    private List<IndicatorEntity> indicatorEntities;
    private List<IndicatorEto> indicatorEtoList;

    @BeforeEach
    void setUp() {
        IndicatorEntity indicatorEntity = new IndicatorEntity("Podroz sluzbowa", "Paliwo, amortyzacja", "pl", 20, 40);

        indicatorEntities = new ArrayList<>();
        indicatorEntities.add(indicatorEntity);
        indicatorEtoList = new ArrayList<>();
    }

    @Test
    @DisplayName("Test findAllIndicators Success")
    void testFindAllIndicatorsSuccess() {
        //Arrange
        when(indicatorDao.findAll()).thenReturn(indicatorEntities);
        indicatorEtoList.add(indicatorMapper.toIndicatorEto(indicatorEntity));

        //Act
        Optional<List<IndicatorEto>> result = ucFindIndicator.findAllIndicators();

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(result.get()).hasSize(indicatorEtoList.size());
        assertThat(indicatorEtoList.get(0)).isEqualTo(result.get().get(0));
    }

    @Test
    @DisplayName("Test findAllIndicators No content")
    void testFindAllIndicatorsNoContent() {
        //Arrange
        when(indicatorDao.findAll()).thenReturn(new ArrayList<>());

        //Act
        Optional<List<IndicatorEto>> result = ucFindIndicator.findAllIndicators();

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertTrue(result.get().size() == 0);
    }
}
