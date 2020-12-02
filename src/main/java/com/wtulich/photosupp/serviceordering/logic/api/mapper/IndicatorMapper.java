package com.wtulich.photosupp.serviceordering.logic.api.mapper;

import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.IndicatorEntity;
import com.wtulich.photosupp.serviceordering.logic.api.to.IndicatorEto;
import com.wtulich.photosupp.serviceordering.logic.api.to.IndicatorTo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IndicatorMapper {

    IndicatorEto toIndicatorEto(IndicatorEntity indicatorEntity);

    IndicatorEntity toIndicatorEntity(IndicatorTo indicatorTo);
}
