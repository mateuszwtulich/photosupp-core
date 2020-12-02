package com.wtulich.photosupp.serviceordering.logic.impl.usecase;

import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.AddressDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.AddressEntity;
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
public class UcFindAddressTest {

    @Autowired
    private UcFindAddressImpl ucFindAddress;

    @MockBean
    private AddressDao addressDao;

    private AddressEntity addressEntity;
    private List<AddressEntity> addressEntities;
    private List<String> cities;
    private List<String> streets;

    @BeforeEach
    void setUp() {
        addressEntity = new AddressEntity("Wroclaw", "Wroblewskiego", "27", null, "51-627");

        addressEntities = new ArrayList<>();
        addressEntities.add(addressEntity);
        cities = new ArrayList<>();
        streets = new ArrayList<>();
    }

    @Test
    @DisplayName("Test findAllCities Success")
    void testFindAllCitiesSuccess() {
        //Arrange
        when(addressDao.findAll()).thenReturn(addressEntities);
        cities.add("Wroclaw");

        //Act
        Optional<List<String>> result = ucFindAddress.findAllCities();

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(result.get()).hasSize(cities.size());
        assertThat(cities).isEqualTo(result.get());
    }

    @Test
    @DisplayName("Test findAllCities No content")
    void testFindAllCitiesNoContent() {
        //Arrange
        when(addressDao.findAll()).thenReturn(new ArrayList<>());

        //Act
        Optional<List<String>> result = ucFindAddress.findAllCities();

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertTrue(result.get().size() == 0);
    }

    @Test
    @DisplayName("Test findAllStreets Success")
    void testFindAllStreetsSuccess() {
        //Arrange
        when(addressDao.findAll()).thenReturn(addressEntities);
        streets.add("Wroblewskiego");

        //Act
        Optional<List<String>> result = ucFindAddress.findAllStreets();

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(result.get()).hasSize(streets.size());
        assertThat(streets).isEqualTo(result.get());
    }

    @Test
    @DisplayName("Test findAllStreets No content")
    void testFindAllStreetsNoContent() {
        //Arrange
        when(addressDao.findAll()).thenReturn(new ArrayList<>());

        //Act
        Optional<List<String>> result = ucFindAddress.findAllStreets();

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertTrue(result.get().size() == 0);
    }
}
