package com.wtulich.photosupp.serviceordering.logic.api.mapper;

import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.AddressEntity;
import com.wtulich.photosupp.serviceordering.logic.api.to.AddressEto;
import com.wtulich.photosupp.serviceordering.logic.api.to.AddressTo;
import net.bytebuddy.implementation.bind.annotation.Default;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressEto toAddressEto(AddressEntity addressEntity);

    AddressEntity toAddressEntity(AddressTo addressTo);
}
