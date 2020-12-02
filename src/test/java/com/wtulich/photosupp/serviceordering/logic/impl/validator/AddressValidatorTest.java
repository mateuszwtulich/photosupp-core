package com.wtulich.photosupp.serviceordering.logic.impl.validator;

import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.AddressDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.AddressEntity;
import com.wtulich.photosupp.serviceordering.logic.api.to.AddressTo;
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
public class AddressValidatorTest {

    @Autowired
    private AddressValidator addressValidator;

    @MockBean
    private AddressDao addressDao;

    private AddressEntity addressEntity;
    private AddressTo addressTo;

    @BeforeEach
    void setUp() {
        addressEntity = new AddressEntity("Wroclaw", "Wroblewskiego", "27", null, "51-627");
        addressTo = new AddressTo("Wroclaw", "Wroblewskiego", "27", null, "51-627");
    }

    @Test
    @DisplayName("Test verifyIfIndicatorAlreadyExists True")
    void verifyIfServiceAlreadyExistsTrue() {
        //Arrange
        when(addressDao.existsByCityAndStreetAndBuildingNumberAndApartmentNumberAndPostalCode(
                addressTo.getCity(), addressTo.getStreet(), addressTo.getBuildingNumber(),
                addressTo.getApartmentNumber(), addressTo.getPostalCode())).thenReturn(true);

        //Act
        Boolean iaAddressAlreadyExists =  addressValidator.isAddressAlreadyExists(addressTo);

        // Assert
        Assertions.assertTrue(iaAddressAlreadyExists);
    }

    @Test
    @DisplayName("Test verifyIfIndicatorAlreadyExists False")
    void verifyIfRoleAlreadyExistsFalse() {
        //Arrange
        when(addressDao.existsByCityAndStreetAndBuildingNumberAndApartmentNumberAndPostalCode(
                addressTo.getCity(), addressTo.getStreet(), addressTo.getBuildingNumber(),
                addressTo.getApartmentNumber(), addressTo.getPostalCode())).thenReturn(false);

        //Act
        Boolean iaAddressAlreadyExists =  addressValidator.isAddressAlreadyExists(addressTo);

        // Assert
        Assertions.assertFalse(iaAddressAlreadyExists);
    }
}
