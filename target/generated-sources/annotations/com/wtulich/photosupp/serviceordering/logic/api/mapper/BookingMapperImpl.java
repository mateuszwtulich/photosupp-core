package com.wtulich.photosupp.serviceordering.logic.api.mapper;

import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.BookingEntity;
import com.wtulich.photosupp.serviceordering.logic.api.to.BookingEto;
import com.wtulich.photosupp.serviceordering.logic.api.to.BookingEtoWithOrderNumber;
import com.wtulich.photosupp.serviceordering.logic.api.to.BookingTo;
import com.wtulich.photosupp.serviceordering.logic.api.to.PriceIndicatorEto;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-12-02T17:23:02+0100",
    comments = "version: 1.4.0.Final, compiler: javac, environment: Java 1.8.0_271 (Oracle Corporation)"
)
@Component
public class BookingMapperImpl implements BookingMapper {

    @Override
    public BookingEto toBookingEto(BookingEntity bookingEntity) {
        if ( bookingEntity == null ) {
            return null;
        }

        BookingEto bookingEto = new BookingEto();

        if ( bookingEntity.getStart() != null ) {
            bookingEto.setStart( DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( bookingEntity.getStart() ) );
        }
        if ( bookingEntity.getEnd() != null ) {
            bookingEto.setEnd( DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( bookingEntity.getEnd() ) );
        }
        bookingEto.setId( bookingEntity.getId() );
        bookingEto.setName( bookingEntity.getName() );
        bookingEto.setDescription( bookingEntity.getDescription() );
        bookingEto.setConfirmed( bookingEntity.isConfirmed() );
        bookingEto.setPredictedPrice( bookingEntity.getPredictedPrice() );
        if ( bookingEntity.getModificationDate() != null ) {
            bookingEto.setModificationDate( DateTimeFormatter.ISO_LOCAL_DATE.format( bookingEntity.getModificationDate() ) );
        }

        return bookingEto;
    }

    @Override
    public BookingEntity toBookingEntity(BookingTo bookingTo) {
        if ( bookingTo == null ) {
            return null;
        }

        BookingEntity bookingEntity = new BookingEntity();

        bookingEntity.setName( bookingTo.getName() );
        bookingEntity.setDescription( bookingTo.getDescription() );
        if ( bookingTo.getStart() != null ) {
            bookingEntity.setStart( LocalDate.parse( bookingTo.getStart() ) );
        }
        if ( bookingTo.getEnd() != null ) {
            bookingEntity.setEnd( LocalDate.parse( bookingTo.getEnd() ) );
        }

        return bookingEntity;
    }

    @Override
    public BookingEtoWithOrderNumber toBookingEtoWithOrderId(BookingEto bookingEto) {
        if ( bookingEto == null ) {
            return null;
        }

        BookingEtoWithOrderNumber bookingEtoWithOrderNumber = new BookingEtoWithOrderNumber();

        bookingEtoWithOrderNumber.setId( bookingEto.getId() );
        bookingEtoWithOrderNumber.setName( bookingEto.getName() );
        bookingEtoWithOrderNumber.setDescription( bookingEto.getDescription() );
        bookingEtoWithOrderNumber.setServiceEto( bookingEto.getServiceEto() );
        bookingEtoWithOrderNumber.setAddressEto( bookingEto.getAddressEto() );
        bookingEtoWithOrderNumber.setUserEto( bookingEto.getUserEto() );
        bookingEtoWithOrderNumber.setConfirmed( bookingEto.isConfirmed() );
        bookingEtoWithOrderNumber.setPredictedPrice( bookingEto.getPredictedPrice() );
        bookingEtoWithOrderNumber.setStart( bookingEto.getStart() );
        bookingEtoWithOrderNumber.setEnd( bookingEto.getEnd() );
        bookingEtoWithOrderNumber.setModificationDate( bookingEto.getModificationDate() );
        List<PriceIndicatorEto> list = bookingEto.getPriceIndicatorEtoList();
        if ( list != null ) {
            bookingEtoWithOrderNumber.setPriceIndicatorEtoList( new ArrayList<PriceIndicatorEto>( list ) );
        }

        return bookingEtoWithOrderNumber;
    }
}
