package com.wtulich.photosupp.orderhandling.logic.api.mapper;

import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.OrderEntity;
import com.wtulich.photosupp.orderhandling.logic.api.to.OrderEto;
import com.wtulich.photosupp.orderhandling.logic.api.to.OrderTo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderEntity toOrderEntity(OrderTo orderTo);

    @Mappings({
            @Mapping(target = "createdAt", source = "createdAt", dateFormat = "yyyy-MM-dd")})
    OrderEto toOrderEto(OrderEntity orderEntity);
}
