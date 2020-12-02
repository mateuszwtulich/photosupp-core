package com.wtulich.photosupp.orderhandling.logic.api.mapper;

import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.OrderEntity;
import com.wtulich.photosupp.orderhandling.logic.api.to.OrderEto;
import com.wtulich.photosupp.orderhandling.logic.api.to.OrderTo;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.BookingEntity;
import com.wtulich.photosupp.serviceordering.logic.api.to.BookingEto;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.UserEntity;
import com.wtulich.photosupp.userhandling.logic.api.to.UserEto;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-11-30T19:18:32+0100",
    comments = "version: 1.4.0.Final, compiler: javac, environment: Java 15 (Oracle Corporation)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderEntity toOrderEntity(OrderTo orderTo) {
        if ( orderTo == null ) {
            return null;
        }

        OrderEntity orderEntity = new OrderEntity();

        orderEntity.setPrice( orderTo.getPrice() );

        return orderEntity;
    }

    @Override
    public OrderEto toOrderEto(OrderEntity orderEntity) {
        if ( orderEntity == null ) {
            return null;
        }

        OrderEto orderEto = new OrderEto();

        if ( orderEntity.getCreatedAt() != null ) {
            orderEto.setCreatedAt( DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( orderEntity.getCreatedAt() ) );
        }
        orderEto.setOrderNumber( orderEntity.getOrderNumber() );
        orderEto.setCoordinator( userEntityToUserEto( orderEntity.getCoordinator() ) );
        orderEto.setUser( userEntityToUserEto( orderEntity.getUser() ) );
        orderEto.setStatus( orderEntity.getStatus() );
        orderEto.setBooking( bookingEntityToBookingEto( orderEntity.getBooking() ) );
        orderEto.setPrice( orderEntity.getPrice() );

        return orderEto;
    }

    protected UserEto userEntityToUserEto(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        UserEto userEto = new UserEto();

        userEto.setId( userEntity.getId() );
        userEto.setName( userEntity.getName() );
        userEto.setSurname( userEntity.getSurname() );

        return userEto;
    }

    protected BookingEto bookingEntityToBookingEto(BookingEntity bookingEntity) {
        if ( bookingEntity == null ) {
            return null;
        }

        BookingEto bookingEto = new BookingEto();

        bookingEto.setId( bookingEntity.getId() );
        bookingEto.setName( bookingEntity.getName() );
        bookingEto.setDescription( bookingEntity.getDescription() );
        bookingEto.setConfirmed( bookingEntity.isConfirmed() );
        bookingEto.setPredictedPrice( bookingEntity.getPredictedPrice() );
        if ( bookingEntity.getStart() != null ) {
            bookingEto.setStart( DateTimeFormatter.ISO_LOCAL_DATE.format( bookingEntity.getStart() ) );
        }
        if ( bookingEntity.getEnd() != null ) {
            bookingEto.setEnd( DateTimeFormatter.ISO_LOCAL_DATE.format( bookingEntity.getEnd() ) );
        }
        if ( bookingEntity.getModificationDate() != null ) {
            bookingEto.setModificationDate( DateTimeFormatter.ISO_LOCAL_DATE.format( bookingEntity.getModificationDate() ) );
        }

        return bookingEto;
    }
}
