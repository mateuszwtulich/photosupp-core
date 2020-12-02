package com.wtulich.photosupp.serviceordering.logic.api.mapper;

import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.ServiceEntity;
import com.wtulich.photosupp.serviceordering.logic.api.to.ServiceEto;
import com.wtulich.photosupp.serviceordering.logic.api.to.ServiceTo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    ServiceEto toServiceEto(ServiceEntity serviceEntity);

    ServiceEntity toServiceEntity(ServiceTo serviceTo);
}
