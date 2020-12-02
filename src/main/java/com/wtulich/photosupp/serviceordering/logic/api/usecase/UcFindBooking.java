package com.wtulich.photosupp.serviceordering.logic.api.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.serviceordering.logic.api.to.BookingEto;

import java.util.List;
import java.util.Optional;

public interface UcFindBooking {

    Optional<BookingEto> findBooking(Long id) throws EntityDoesNotExistException;

    Optional<List<BookingEto>> findAllBookings();

    Optional<List<BookingEto>> findAllBookingsByUserId(Long userId) throws EntityDoesNotExistException;
}
