package com.wtulich.photosupp.serviceordering.logic.impl.usecase;

import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.ServiceDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.ServiceEntity;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.ServiceMapper;
import com.wtulich.photosupp.serviceordering.logic.api.to.ServiceEto;
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
public class UcFindServiceTest {

    @Autowired
    private UcFindServiceImpl ucFindService;

    @Autowired
    private ServiceMapper serviceMapper;

    @MockBean
    private ServiceDao serviceDao;

    private ServiceEntity serviceEntity;
    private List<ServiceEntity> serviceEntities;
    private List<ServiceEto> serviceEtoList;

    @BeforeEach
    void setUp() {
        serviceEntity = new ServiceEntity("Film produktowy", "Film produktow na bialym tle i odpowiednim oswietleniu", 500D, "pl");
        serviceEntity.setId(1L);

        serviceEntities = new ArrayList<>();
        serviceEntities.add(serviceEntity);
        serviceEtoList = new ArrayList<>();
    }

    @Test
    @DisplayName("Test findAllServices Success")
    void testFindAllServicesSuccess() {
        //Arrange
        when(serviceDao.findAll()).thenReturn(serviceEntities);
        serviceEtoList.add(serviceMapper.toServiceEto(serviceEntity));

        //Act
        Optional<List<ServiceEto>> result = ucFindService.findAllServices();

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(result.get()).hasSize(serviceEtoList.size());
        assertThat(serviceEtoList.get(0)).isEqualTo(result.get().get(0));
    }

    @Test
    @DisplayName("Test findAllServices No content")
    void testFindAllServicesNoContent() {
        //Arrange
        when(serviceDao.findAll()).thenReturn(new ArrayList<>());

        //Act
        Optional<List<ServiceEto>> result = ucFindService.findAllServices();

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertTrue(result.get().size() == 0);
    }
}
