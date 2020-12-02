package com.wtulich.photosupp.serviceordering.dataaccess.api.dao;

import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressDao extends JpaRepository<AddressEntity, Long> {

    boolean existsByCityAndStreetAndBuildingNumberAndApartmentNumberAndPostalCode
            (String city, String street, String buildingNumber, String apartmentNumber, String postalCode);
    AddressEntity findByCityAndStreetAndBuildingNumberAndApartmentNumberAndPostalCode
            (String city, String street, String buildingNumber, String apartmentNumber, String postalCode);
}
