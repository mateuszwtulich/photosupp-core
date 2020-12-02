package com.wtulich.photosupp.serviceordering.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.IndicatorDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.ServiceDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.IndicatorEntity;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.ServiceEntity;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.IndicatorMapper;
import com.wtulich.photosupp.serviceordering.logic.api.to.IndicatorEto;
import com.wtulich.photosupp.serviceordering.logic.api.to.IndicatorTo;
import com.wtulich.photosupp.serviceordering.logic.api.to.ServiceEto;
import com.wtulich.photosupp.serviceordering.logic.api.to.ServiceTo;
import com.wtulich.photosupp.serviceordering.logic.api.usecase.UcManageIndicator;
import com.wtulich.photosupp.serviceordering.logic.impl.validator.IndicatorValidator;
import com.wtulich.photosupp.serviceordering.logic.impl.validator.ServiceValidator;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.PermissionDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.RoleDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.PermissionEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.RoleEntity;
import com.wtulich.photosupp.userhandling.logic.api.mapper.PermissionsMapper;
import com.wtulich.photosupp.userhandling.logic.api.mapper.RoleMapper;
import com.wtulich.photosupp.userhandling.logic.api.to.RoleEto;
import com.wtulich.photosupp.userhandling.logic.api.to.RoleTo;
import com.wtulich.photosupp.userhandling.logic.impl.usecase.UcManageRoleImpl;
import com.wtulich.photosupp.userhandling.logic.impl.validator.RoleValidator;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class UcManageServiceTest {

    @Autowired
    private UcManageServiceImpl ucManageService;

    @MockBean
    private ServiceDao serviceDao;

    @MockBean
    private ServiceValidator serviceValidator;

    private ServiceEntity serviceEntity;
    private ServiceEto serviceEto;
    private ServiceTo serviceTo;

    @BeforeEach
    void setUp() {
        serviceEntity = new ServiceEntity("Film produktowy", "Film produktow na bialym tle i odpowiednim oswietleniu", 500D, "pl");
        serviceEntity.setId(1L);

        List<Long> indicatorsIds = new ArrayList<>();
        indicatorsIds.add(1L);

        IndicatorEto indicatorEto = new IndicatorEto(1L,"Podroz sluzbowa", "Paliwo, amortyzacja", "pl", 20, 30);
        List<IndicatorEto> indicatorEtos = new ArrayList<>();
        indicatorEtos.add(indicatorEto);

        serviceEto = new ServiceEto(1L, "Film produktowy", "Film produktow na bialym tle i odpowiednim oswietleniu", 500D, "pl", indicatorEtos);

        serviceTo = new ServiceTo("Film produktowy", "Film produktow na bialym tle i odpowiednim oswietleniu", 500D, "pl", indicatorsIds);
    }

    @Test
    @DisplayName("Test createService Success")
    void testCreateServiceSuccess() throws EntityAlreadyExistsException, EntityDoesNotExistException {
        //Arrange
        when(serviceDao.save(serviceEntity)).thenReturn(serviceEntity);

        //Act
        Optional<ServiceEto> result = ucManageService.createService(serviceTo);

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(serviceEto).isEqualToComparingFieldByField(result.get());
    }

    @Test
    @DisplayName("Test createService EntityAlreadyExistsException")
    void testCreateServiceEntityAlreadyExistsException() throws EntityAlreadyExistsException {
        //Arrange
        doThrow(EntityAlreadyExistsException.class)
                .when(serviceValidator).verifyIfServiceNameAlreadyExists(serviceEntity.getName());

        //Act Assert
        Assertions.assertThrows(EntityAlreadyExistsException.class, () ->
                ucManageService.createService(serviceTo));
    }

    @Test
    @DisplayName("Test updateService Success")
    void testUpdateServiceSuccess() throws EntityDoesNotExistException, EntityAlreadyExistsException {
        //Arrange
        serviceTo.setName("Nowy");
        serviceEto.setName("Nowy");
        when(serviceDao.findById(serviceEntity.getId())).thenReturn(Optional.of(serviceEntity));

        //Act
        Optional<ServiceEto> result = ucManageService.updateService(serviceTo, serviceEntity.getId());

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(serviceEto).isEqualToComparingFieldByField(result.get());
    }

    @Test
    @DisplayName("Test updateService EntityAlreadyExistsException")
    void testUpdateServiceEntityAlreadyExistsException() throws EntityAlreadyExistsException {
        //Arrange
        serviceTo.setName("Fotografia");
        when(serviceDao.findById(serviceEntity.getId())).thenReturn(Optional.of(serviceEntity));
        doThrow(EntityAlreadyExistsException.class)
                .when(serviceValidator).verifyIfServiceNameAlreadyExists(serviceTo.getName());

        //Act Assert
        Assertions.assertThrows(EntityAlreadyExistsException.class, () ->
                ucManageService.updateService(serviceTo, serviceEntity.getId()));
    }

    @Test
    @DisplayName("Test updateService EntityDoesNotExistException")
    void testUpdateServiceEntityDoesNotExistException() {
        //Arrange
        when(serviceDao.findById(serviceEntity.getId())).thenReturn(Optional.empty());

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucManageService.updateService(serviceTo, serviceEntity.getId()));
    }
}
