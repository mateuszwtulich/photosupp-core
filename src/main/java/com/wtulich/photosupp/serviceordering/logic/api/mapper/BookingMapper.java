package com.wtulich.photosupp.serviceordering.logic.api.mapper;

import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.BookingEntity;
import com.wtulich.photosupp.serviceordering.logic.api.to.BookingEto;
import com.wtulich.photosupp.serviceordering.logic.api.to.BookingEtoWithOrderNumber;
import com.wtulich.photosupp.serviceordering.logic.api.to.BookingTo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mappings({
    @Mapping(target = "start", source = "start", dateFormat = "yyyy-MM-dd"),
    @Mapping(target = "end", source = "end", dateFormat = "yyyy-MM-dd")})
    BookingEto toBookingEto(BookingEntity bookingEntity);

    BookingEntity toBookingEntity(BookingTo bookingTo);

    BookingEtoWithOrderNumber toBookingEtoWithOrderId(BookingEto bookingEto);
}
