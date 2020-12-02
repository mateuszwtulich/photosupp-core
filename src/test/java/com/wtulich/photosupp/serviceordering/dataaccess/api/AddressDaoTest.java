package com.wtulich.photosupp.serviceordering.dataaccess.api;

import com.wtulich.photosupp.config.H2JpaConfig;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.AddressDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.*;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({SpringExtension.class})
@SpringBootTest(classes = {H2JpaConfig.class})
public class AddressDaoTest {

    @Autowired
    private AddressDao addressDao;

    @Test
    void exists_TRUE() {
        //Act Assert
        Assertions.assertTrue(addressDao.existsByCityAndStreetAndBuildingNumberAndApartmentNumberAndPostalCode
                ("Wroclaw", "Wroblewskiego", "27", null, "51-627"));
    }

    @Test
    void exists_FALSE() {
        //Act Assert
        Assertions.assertFalse(addressDao.existsByCityAndStreetAndBuildingNumberAndApartmentNumberAndPostalCode
                ("Wroclaw", "Wroblewskiego", "28", null, "51-627"));
    }

    @Test
    void findBy_SUCCESS() {
        //Arrange
        AddressEntity addressEntity =  new AddressEntity("Wroclaw", "Wroblewskiego", "27", null, "51-627");
        addressEntity.setId(1L);

        //Act
        AddressEntity result = addressDao.findByCityAndStreetAndBuildingNumberAndApartmentNumberAndPostalCode(
                addressEntity.getCity(), addressEntity.getStreet(), addressEntity.getBuildingNumber(),
                addressEntity.getApartmentNumber(), addressEntity.getPostalCode());

        // Assert
        assertThat(result).isEqualToIgnoringGivenFields(addressEntity, "bookingList");
    }

    @Test
    void findBy_FAILURE() {
        //Arrange
        AddressEntity addressEntity =  new AddressEntity("Poznan", "Wroblewskiego", "27", null, "51-627");
        addressEntity.setId(1L);

        //Act
        AddressEntity result = addressDao.findByCityAndStreetAndBuildingNumberAndApartmentNumberAndPostalCode(
                addressEntity.getCity(), addressEntity.getStreet(), addressEntity.getBuildingNumber(),
                addressEntity.getApartmentNumber(), addressEntity.getPostalCode());

        // Assert
                assertThat(result).isNotEqualTo(addressEntity);
    }
}
