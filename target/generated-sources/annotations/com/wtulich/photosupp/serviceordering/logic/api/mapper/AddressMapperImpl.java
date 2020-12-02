package com.wtulich.photosupp.serviceordering.logic.api.mapper;

import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.AddressEntity;
import com.wtulich.photosupp.serviceordering.logic.api.to.AddressEto;
import com.wtulich.photosupp.serviceordering.logic.api.to.AddressTo;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-11-30T19:18:31+0100",
    comments = "version: 1.4.0.Final, compiler: javac, environment: Java 15 (Oracle Corporation)"
)
@Component
public class AddressMapperImpl implements AddressMapper {

    @Override
    public AddressEto toAddressEto(AddressEntity addressEntity) {
        if ( addressEntity == null ) {
            return null;
        }

        AddressEto addressEto = new AddressEto();

        addressEto.setId( addressEntity.getId() );
        addressEto.setCity( addressEntity.getCity() );
        addressEto.setStreet( addressEntity.getStreet() );
        addressEto.setBuildingNumber( addressEntity.getBuildingNumber() );
        addressEto.setApartmentNumber( addressEntity.getApartmentNumber() );
        addressEto.setPostalCode( addressEntity.getPostalCode() );

        return addressEto;
    }

    @Override
    public AddressEntity toAddressEntity(AddressTo addressTo) {
        if ( addressTo == null ) {
            return null;
        }

        AddressEntity addressEntity = new AddressEntity();

        addressEntity.setCity( addressTo.getCity() );
        addressEntity.setStreet( addressTo.getStreet() );
        addressEntity.setBuildingNumber( addressTo.getBuildingNumber() );
        addressEntity.setApartmentNumber( addressTo.getApartmentNumber() );
        addressEntity.setPostalCode( addressTo.getPostalCode() );

        return addressEntity;
    }
}
