package com.wtulich.photosupp.serviceordering.logic.impl.validator;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.IndicatorDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.IndicatorEntity;
import com.wtulich.photosupp.serviceordering.logic.api.to.IndicatorTo;
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

import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class IndicatorValidatorTest {

    @Autowired
    private IndicatorValidator indicatorValidator;

    @MockBean
    private IndicatorDao indicatorDao;

    private IndicatorEntity indicatorEntity;
    private IndicatorTo indicatorTo;

    @BeforeEach
    void setUp() {
        IndicatorEntity indicatorEntity = new IndicatorEntity("Podroz sluzbowa", "Paliwo, amortyzacja", "pl", 20, 40);
        indicatorEntity.setId(1L);
        indicatorTo = new IndicatorTo("Podróż służbowa", "Paliwo, amortyzacja", "pl", 20, 40);
    }

    @Test
    @DisplayName("Test verifyIfIndicatorAlreadyExists Success")
    void verifyIfServiceAlreadyExistsSuccess() {
        //Arrange
        when(indicatorDao.existsByName(indicatorEntity.getName())).thenReturn(true);

        //Act Assert
        Assertions.assertThrows(EntityAlreadyExistsException.class, () ->
                indicatorValidator.verifyIfIndicatorNameAlreadyExists(indicatorTo.getName()));
    }

    @Test
    @DisplayName("Test verifyIfIndicatorAlreadyExists Failure")
    void verifyIfRoleAlreadyExistsFailure() {
        //Arrange
        when(indicatorDao.existsByName(indicatorEntity.getName())).thenReturn(false);

        //Act Assert
        Assertions.assertDoesNotThrow(() ->  indicatorValidator.verifyIfIndicatorNameAlreadyExists(indicatorTo.getName()));
    }
}
