package com.wtulich.photosupp.serviceordering.dataaccess.api;

import com.wtulich.photosupp.config.H2JpaConfig;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.PriceIndicatorDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.*;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.UserEntity;
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
public class PriceIndicatorMapperDaoTest {

    @Autowired
    private PriceIndicatorDao priceIndicatorDao;


    @Test
    void findAllByIndicator_Id_TRUE() {
        //Arrange
        AddressEntity addressEntity =  new AddressEntity("Wroclaw", "Wroblewskiego", "27", null, "51-627");
        addressEntity.setId(1L);
        ServiceEntity serviceEntity = new ServiceEntity("Film produktowy", "Film produktow na bialym tle i odpowiednim oswietleniu", 500D, "pl");
        serviceEntity.setId(1L);
        IndicatorEntity indicatorEntity = new IndicatorEntity("Podroz sluzbowa", "Paliwo, amortyzacja", "pl", 20, 30);
        indicatorEntity.setId(1L);

        BookingEntity bookingEntity = new BookingEntity("Film dla TestCompany", "Film produktowy z dojazdem", 900D,
                addressEntity, new UserEntity(), serviceEntity, false, LocalDate.now(), LocalDate.now(), LocalDate.now());
        bookingEntity.setId(1L);
        PriceIndicatorEntity priceIndicatorEntity = new PriceIndicatorEntity(indicatorEntity, bookingEntity, 400, 10);

        //Act
        List<PriceIndicatorEntity> priceIndicatorEntities = priceIndicatorDao.findAllByIndicator_Id(1L);

        // Assert
        assertThat(priceIndicatorEntities).hasSize(1);
        assertThat(priceIndicatorEntity).isEqualToIgnoringGivenFields(priceIndicatorEntities.get(0), "id", "indicator", "booking");
        assertThat(priceIndicatorEntity.getIndicator().getId()).isEqualTo(priceIndicatorEntities.get(0).getIndicator().getId());
        assertThat(priceIndicatorEntity.getBooking().getId()).isEqualTo(priceIndicatorEntities.get(0).getBooking().getId());
    }

    @Test
    void findAllByIndicator_Id_FALSE() {

        //Act
        List<PriceIndicatorEntity> priceIndicatorEntities = priceIndicatorDao.findAllByIndicator_Id(2L);

        // Assert
        assertThat(priceIndicatorEntities).hasSize(0);
    }
}
