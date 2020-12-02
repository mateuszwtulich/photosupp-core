package com.wtulich.photosupp.serviceordering.logic.api.mapper;

import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.ServiceEntity;
import com.wtulich.photosupp.serviceordering.logic.api.to.ServiceEto;
import com.wtulich.photosupp.serviceordering.logic.api.to.ServiceTo;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-12-02T17:23:02+0100",
    comments = "version: 1.4.0.Final, compiler: javac, environment: Java 1.8.0_271 (Oracle Corporation)"
)
@Component
public class ServiceMapperImpl implements ServiceMapper {

    @Override
    public ServiceEto toServiceEto(ServiceEntity serviceEntity) {
        if ( serviceEntity == null ) {
            return null;
        }

        ServiceEto serviceEto = new ServiceEto();

        serviceEto.setId( serviceEntity.getId() );
        serviceEto.setName( serviceEntity.getName() );
        serviceEto.setDescription( serviceEntity.getDescription() );
        serviceEto.setBasePrice( serviceEntity.getBasePrice() );
        serviceEto.setLocale( serviceEntity.getLocale() );

        return serviceEto;
    }

    @Override
    public ServiceEntity toServiceEntity(ServiceTo serviceTo) {
        if ( serviceTo == null ) {
            return null;
        }

        ServiceEntity serviceEntity = new ServiceEntity();

        serviceEntity.setName( serviceTo.getName() );
        serviceEntity.setDescription( serviceTo.getDescription() );
        serviceEntity.setBasePrice( serviceTo.getBasePrice() );
        serviceEntity.setLocale( serviceTo.getLocale() );

        return serviceEntity;
    }
}
