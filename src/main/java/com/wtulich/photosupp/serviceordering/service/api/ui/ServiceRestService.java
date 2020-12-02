package com.wtulich.photosupp.serviceordering.service.api.ui;

import com.wtulich.photosupp.general.common.api.RestService;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import com.wtulich.photosupp.general.utils.annotations.PermissionRestrict;
import com.wtulich.photosupp.orderhandling.logic.api.to.OrderEto;
import com.wtulich.photosupp.serviceordering.logic.api.to.*;
import com.wtulich.photosupp.userhandling.logic.api.to.RoleEto;
import com.wtulich.photosupp.userhandling.logic.api.to.RoleTo;
import com.wtulich.photosupp.userhandling.logic.api.to.UserEto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/service/v1")
public interface ServiceRestService extends RestService {

    @ApiOperation(value = "Get all cities.",
            tags = {"address"},
            response = String.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 204, message = "No content found"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @GetMapping(value = "/address/cities",
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    List<String> getAllCities();


    @ApiOperation(value = "Get all streets.",
            tags = {"address"},
            response = String.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 204, message = "No content found"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @GetMapping(value = "/address/streets",
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    List<String> getAllStreets();


    @ApiOperation(value = "Get all bookings.",
            tags = {"booking"},
            response = BookingEto.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 204, message = "No content found"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @GetMapping(value = "/bookings",
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_BOOKINGS,
            ApplicationPermissions.AUTH_USER })
    List<BookingEto> getAllBookings();


    @ApiOperation(value = "Get all bookings by user.",
            tags = {"booking"},
            response = OrderEto.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 204, message = "No content found"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @GetMapping(value = "/bookings/{userId}",
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_BOOKINGS,
            ApplicationPermissions.AUTH_USER })
    List<BookingEto> getAllBookingsByUserId(@PathVariable(name = "userId") Long userId);



    @ApiOperation(value = "Get all indicators.",
            tags = {"indicator"},
            response = IndicatorEto.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 204, message = "No content found"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @GetMapping(value = "/indicators",
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    List<IndicatorEto> getAllIndicators();


    @ApiOperation(value = "Get all services.",
            tags = {"service"},
            response = ServiceEto.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 204, message = "No content found"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @GetMapping(value = "/services",
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    List<ServiceEto> getAllServices();


    @ApiOperation(value = "Get booking by id.",
            tags = {"booking"},
            response = BookingEto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 204, message = "Entity not found"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @GetMapping(value = "/booking/{id}",
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_BOOKINGS,
            ApplicationPermissions.AUTH_USER })
    ResponseEntity<BookingEto> getBooking(@PathVariable(value = "id") Long id);


    @ApiOperation(value = "Creates booking",
            tags = {"booking"},
            response = BookingEto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 422, message = "Could not process entity"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @PostMapping(value = "/booking",
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_BOOKINGS,
            ApplicationPermissions.AUTH_USER })
    ResponseEntity<BookingEto> createBooking(@Validated @RequestBody BookingTo bookingTo);


    @ApiOperation(value = "Creates indicator",
            tags = {"indicator"},
            response = IndicatorEto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 422, message = "Could not process entity"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @PostMapping(value = "/indicator",
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_INDICATORS})
    ResponseEntity<IndicatorEto> createIndicator(@Validated @RequestBody IndicatorTo indicatorTo);


    @ApiOperation(value = "Creates service",
            tags = {"service"},
            response = ServiceEto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 422, message = "Could not process entity"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @PostMapping(value = "/service",
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_SERVICES})
    ResponseEntity<ServiceEto> createService(@Validated @RequestBody ServiceTo serviceTo);


    @ApiOperation(value = "Updates booking",
            tags = {"booking"},
            response = BookingEto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 422, message = "Could not process entity"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @PutMapping(value = "/booking/{id}",
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_BOOKINGS,
                        ApplicationPermissions.AUTH_USER})
    ResponseEntity<BookingEto> updateBooking(@Validated @RequestBody BookingTo bookingTo, @PathVariable(value = "id") Long id);


    @ApiOperation(value = "Updates indicator",
            tags = {"indicator"},
            response = IndicatorEto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 422, message = "Could not process entity"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @PutMapping(value = "/indicator/{id}",
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_INDICATORS})
    ResponseEntity<IndicatorEto> updateIndicator(@Validated @RequestBody IndicatorTo indicatorTo, @PathVariable(value = "id") Long id);


    @ApiOperation(value = "Updates service",
            tags = {"service"},
            response = ServiceEto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 422, message = "Could not process entity"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @PutMapping(value = "/service/{id}",
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_SERVICES})
    ResponseEntity<ServiceEto> updateService(@Validated @RequestBody ServiceTo serviceTo, @PathVariable(value = "id") Long id);


    @ApiOperation(value = "Deletes booking",
            tags = {"booking"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @DeleteMapping(value = "/booking/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_BOOKINGS,
                        ApplicationPermissions.AUTH_USER })
    ResponseEntity<?> deleteBooking(@PathVariable(value = "id") Long id);


    @ApiOperation(value = "Deletes indicator",
            tags = {"indicator"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @DeleteMapping(value = "/indicator/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_INDICATORS})
    ResponseEntity<?> deleteIndicator(@PathVariable(value = "id") Long id);


    @ApiOperation(value = "Deletes service",
            tags = {"service"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @DeleteMapping(value = "/service/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_SERVICES})
    ResponseEntity<?> deleteService(@PathVariable(value = "id") Long id);

    @ApiOperation(value = "Calculates service",
            tags = {"service"},
            response = ServiceEto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 422, message = "Could not process entity"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @PostMapping(value = "/service/calculate",
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CalculateCto> calculateService(@Validated @RequestBody CalculateTo calculateTo);


    @ApiOperation(value = "Confirms booking",
            tags = {"booking"},
            response = BookingEtoWithOrderNumber.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 422, message = "Could not process entity"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @PutMapping(value = "/booking/{id}/confirm",
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_BOOKINGS})
    ResponseEntity<BookingEtoWithOrderNumber> confirmBooking(@Validated @RequestBody Long coordinatorId,
                                                             @PathVariable(value = "id") Long id);
}
