package com.wtulich.photosupp.serviceordering.logic.impl.validator;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.ServiceDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.ServiceEntity;
import com.wtulich.photosupp.serviceordering.logic.api.to.ServiceTo;
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

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class ServiceValidatorTest {

    @Autowired
    private ServiceValidator serviceValidator;

    @MockBean
    private ServiceDao serviceDao;

    private ServiceEntity serviceEntity;
    private ServiceTo serviceTo;

    @BeforeEach
    void setUp() {
        serviceEntity = new ServiceEntity("Film produktowy", "Film produktow na bialym tle i odpowiednim oswietleniu", 500D, "pl");
        List<Long> indicatorsIds = List.of(1L);
        serviceTo = new ServiceTo("Film produktowy", "Film produktow na bialym tle i odpowiednim oswietleniu", 500D, "pl", indicatorsIds);    }

    @Test
    @DisplayName("Test verifyIfServiceAlreadyExists Success")
    void verifyIfServiceAlreadyExistsSuccess() {
        //Arrange
        when(serviceDao.existsByName(serviceEntity.getName())).thenReturn(true);

        //Act Assert
        Assertions.assertThrows(EntityAlreadyExistsException.class, () ->
                serviceValidator.verifyIfServiceNameAlreadyExists(serviceTo.getName()));
    }

    @Test
    @DisplayName("Test verifyIfServiceAlreadyExists Failure")
    void verifyIfRoleAlreadyExistsFailure() {
        //Arrange
        when(serviceDao.existsByName(serviceEntity.getName())).thenReturn(false);

        //Act Assert
        Assertions.assertDoesNotThrow(() ->  serviceValidator.verifyIfServiceNameAlreadyExists(serviceTo.getName()));
    }
}
