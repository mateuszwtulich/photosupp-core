package com.wtulich.photosupp.serviceordering.service.impl.ui;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.logic.api.exception.EntityHasAssignedEntitiesException;
import com.wtulich.photosupp.general.logic.api.exception.UnprocessableEntityException;
import com.wtulich.photosupp.orderhandling.logic.api.to.OrderEto;
import com.wtulich.photosupp.serviceordering.logic.api.to.*;
import com.wtulich.photosupp.serviceordering.logic.impl.ServiceOrderingImpl;
import com.wtulich.photosupp.serviceordering.service.api.ui.ServiceRestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class ServiceRestServiceImpl implements ServiceRestService {
    private static String CITIES_NOT_EXIST = "Cities do not exist.";
    private static String STREETS_NOT_EXIST = "Streets do not exist.";
    private static String BOOKINGS_NOT_EXIST = "Bookings do not exist.";
    private static String INDICATORS_NOT_EXIST = "Indicators do not exist.";
    private static String SERVICES_NOT_EXIST = "Services do not exist.";
    private static final String BASE_URL = "service/v1/";

    @Inject
    private ServiceOrderingImpl serviceOrdering;

    @Override
    public List<String> getAllCities() {
        return serviceOrdering.findAllCities().map( cities -> {
            if(cities.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, CITIES_NOT_EXIST);
            }
            return cities;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Override
    public List<String> getAllStreets() {
        return serviceOrdering.findAllStreets().map( streets -> {
            if(streets.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, STREETS_NOT_EXIST);
            }
            return streets;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Override
    public List<BookingEto> getAllBookings() {
        return serviceOrdering.findAllBookings().map( bookings -> {
            if(bookings.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, BOOKINGS_NOT_EXIST);
            }
            return bookings;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Override
    public List<BookingEto> getAllBookingsByUserId(Long userId) {
        try {
            return serviceOrdering.findAllBookingsByUserId(userId).map( bookings -> {
                if(bookings.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.NO_CONTENT, BOOKINGS_NOT_EXIST);
                }
                return bookings;
            }).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public List<IndicatorEto> getAllIndicators() {
        return serviceOrdering.findAllIndicators().map( indicators -> {
            if(indicators.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, INDICATORS_NOT_EXIST);
            }
            return indicators;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Override
    public List<ServiceEto> getAllServices() {
        return serviceOrdering.findAllServices().map( services -> {
            if(services.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, SERVICES_NOT_EXIST);
            }
            return services;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));    }

    @Override
    public ResponseEntity<BookingEto> getBooking(Long id) {
        try {
            return ResponseEntity
                    .ok()
                    .body(serviceOrdering.findBooking(id).orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<BookingEto> createBooking(BookingTo bookingTo) {
        try {
            return ResponseEntity
                    .created(new URI(BASE_URL + "/booking"))
                    .body(serviceOrdering.createBooking(bookingTo).orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
        } catch (EntityAlreadyExistsException | URISyntaxException | UnprocessableEntityException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<IndicatorEto> createIndicator(IndicatorTo indicatorTo) {
        try {
            return ResponseEntity
                    .created(new URI(BASE_URL + "/indicator"))
                    .body(serviceOrdering.createIndicator(indicatorTo).orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
        } catch (EntityAlreadyExistsException | URISyntaxException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ServiceEto> createService(ServiceTo serviceTo) {
        try {
            return ResponseEntity
                    .created(new URI(BASE_URL + "/service"))
                    .body(serviceOrdering.createService(serviceTo).orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityAlreadyExistsException | URISyntaxException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<BookingEto> updateBooking(BookingTo bookingTo, Long id) {
        try {
            return ResponseEntity
                    .ok()
                    .body(serviceOrdering.updateBooking(bookingTo, id).orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityAlreadyExistsException | UnprocessableEntityException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<IndicatorEto> updateIndicator(IndicatorTo indicatorTo, Long id) {
        try {
            return ResponseEntity
                    .ok()
                    .body(serviceOrdering.updateIndicator(indicatorTo, id).orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ServiceEto> updateService(ServiceTo serviceTo, Long id) {
        try {
            return ResponseEntity
                    .ok()
                    .body(serviceOrdering.updateService(serviceTo, id).orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> deleteBooking(Long id) {
        try {
            serviceOrdering.deleteBooking(id);
            return ResponseEntity.ok().build();
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> deleteIndicator(Long id) {
        try {
            serviceOrdering.deleteIndicator(id);
            return ResponseEntity.ok().build();
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityHasAssignedEntitiesException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> deleteService(Long id) {
        try {
            serviceOrdering.deleteService(id);
            return ResponseEntity.ok().build();
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityHasAssignedEntitiesException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<CalculateCto> calculateService(CalculateTo calculateTo) {
        try {
            return ResponseEntity
                    .ok(serviceOrdering.calculateService(calculateTo).orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnprocessableEntityException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<BookingEtoWithOrderNumber> confirmBooking(Long coordinatorId, Long id) {
        try {
            return ResponseEntity
                    .ok()
                    .body(serviceOrdering.confirmBooking(id, coordinatorId).orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
