package com.wtulich.photosupp.serviceordering.logic.impl.validator;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.general.logic.api.exception.UnprocessableEntityException;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.BookingDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.BookingEntity;
import com.wtulich.photosupp.serviceordering.logic.api.to.BookingTo;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Named
public class BookingValidator {

    @Inject
    private BookingDao bookingDao;

    public void verifyIfBookingNameAlreadyExists(String name) throws EntityAlreadyExistsException {
        if (bookingDao.existsByName(name)) {
            throw new EntityAlreadyExistsException("Booking with name " + name + " already exists");
        }
    }

    public void verifyIfBookingAlreadyCreatedAtThatDate(BookingTo bookingTo) throws EntityAlreadyExistsException {
        List<BookingEntity> bookingEntityList = bookingDao.findAllByUser_Id(bookingTo.getUserId());

        if (bookingEntityList.stream().anyMatch(bookingEntity ->
                !bookingEntity.getStart().isAfter(LocalDate.parse(bookingTo.getStart()))  &&
                        !bookingEntity.getEnd().isBefore(LocalDate.parse(bookingTo.getEnd())))) {
            throw new EntityAlreadyExistsException("Booking of that user already exists in this period of time.");
        }
    }

    public void verifyIfDatesAreValid(LocalDate start, LocalDate end) throws UnprocessableEntityException {
        if(start.isAfter(end)){
            throw new UnprocessableEntityException("Start date was greater than end date.");
        }
    }
}
