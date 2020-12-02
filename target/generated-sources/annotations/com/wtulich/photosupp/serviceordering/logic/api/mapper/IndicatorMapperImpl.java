package com.wtulich.photosupp.serviceordering.logic.api.mapper;

import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.IndicatorEntity;
import com.wtulich.photosupp.serviceordering.logic.api.to.IndicatorEto;
import com.wtulich.photosupp.serviceordering.logic.api.to.IndicatorTo;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-11-30T19:18:31+0100",
    comments = "version: 1.4.0.Final, compiler: javac, environment: Java 15 (Oracle Corporation)"
)
@Component
public class IndicatorMapperImpl implements IndicatorMapper {

    @Override
    public IndicatorEto toIndicatorEto(IndicatorEntity indicatorEntity) {
        if ( indicatorEntity == null ) {
            return null;
        }

        IndicatorEto indicatorEto = new IndicatorEto();

        indicatorEto.setId( indicatorEntity.getId() );
        indicatorEto.setName( indicatorEntity.getName() );
        indicatorEto.setDescription( indicatorEntity.getDescription() );
        indicatorEto.setBaseAmount( indicatorEntity.getBaseAmount() );
        indicatorEto.setLocale( indicatorEntity.getLocale() );
        indicatorEto.setDoublePrice( indicatorEntity.getDoublePrice() );

        return indicatorEto;
    }

    @Override
    public IndicatorEntity toIndicatorEntity(IndicatorTo indicatorTo) {
        if ( indicatorTo == null ) {
            return null;
        }

        IndicatorEntity indicatorEntity = new IndicatorEntity();

        indicatorEntity.setName( indicatorTo.getName() );
        indicatorEntity.setDescription( indicatorTo.getDescription() );
        indicatorEntity.setBaseAmount( indicatorTo.getBaseAmount() );
        indicatorEntity.setLocale( indicatorTo.getLocale() );
        indicatorEntity.setDoublePrice( indicatorTo.getDoublePrice() );

        return indicatorEntity;
    }
}
