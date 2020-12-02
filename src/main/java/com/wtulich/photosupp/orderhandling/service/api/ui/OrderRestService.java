package com.wtulich.photosupp.orderhandling.service.api.ui;

import com.wtulich.photosupp.general.common.api.RestService;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import com.wtulich.photosupp.general.utils.annotations.PermissionRestrict;
import com.wtulich.photosupp.orderhandling.logic.api.to.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/order/v1")
public interface OrderRestService extends RestService {

    @ApiOperation(value = "Get all orders.",
            tags = {"order"},
            response = OrderEto.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 204, message = "No content found"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @GetMapping(value = "/orders",
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_ORDERS})
    List<OrderEto> getAllOrders();


    @ApiOperation(value = "Get all orders by user.",
            tags = {"order"},
            response = OrderEto.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 204, message = "No content found"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @GetMapping(value = "/orders/{userId}",
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_ORDERS,
                        ApplicationPermissions.AUTH_USER})
    List<OrderEto> getAllOrdersByUserId(@PathVariable(name = "userId") Long userId);


    @ApiOperation(value = "Get order by number.",
            tags = {"order"},
            response = OrderEto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 204, message = "Entity not found"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @GetMapping(value = "/order/{orderNumber}",
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_ORDERS,
            ApplicationPermissions.AUTH_USER})
    ResponseEntity<OrderEto> getOrder(@PathVariable(value = "orderNumber") String orderNumber);


    @ApiOperation(value = "Get all comments by order number.",
            tags = {"comment"},
            response = CommentEto.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 204, message = "No content found"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @GetMapping(value = "/order/{orderNumber}/comments",
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_ORDERS,
            ApplicationPermissions.AUTH_USER})
    List<CommentEto> getAllCommentsByOrderId(@PathVariable(name = "orderNumber") String orderNumber);


    @ApiOperation(value = "Get all media content by order number.",
            tags = {"mediaContent"},
            response = MediaContentEto.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 204, message = "No content found"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @GetMapping(value = "/order/{orderNumber}/mediaContent",
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_ORDERS,
            ApplicationPermissions.AUTH_USER})
    List<MediaContentEto> getAllMediaContentByOrderId(@PathVariable(name = "orderNumber") String orderNumber);


    @ApiOperation(value = "Creates order",
            tags = {"order"},
            response = OrderEto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 422, message = "Could not process entity"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @PostMapping(value = "/order",
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_ORDERS })
    ResponseEntity<OrderEto> createOrder(@Validated @RequestBody OrderTo orderTo);


    @ApiOperation(value = "Creates comment",
            tags = {"comment"},
            response = CommentEto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 422, message = "Could not process entity"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @PostMapping(value = "/order/comment",
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_ORDERS,
            ApplicationPermissions.AUTH_USER })
    ResponseEntity<CommentEto> createComment(@Validated @RequestBody CommentTo commentTo);


    @ApiOperation(value = "Add mediaContent",
            tags = {"mediaContent"},
            response = MediaContentEto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 422, message = "Could not process entity"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @PostMapping(value = "/order/mediaContent",
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_ORDERS })
    ResponseEntity<MediaContentEto> addMediaContent(@Validated @RequestBody MediaContentTo mediaContentTo);


    @ApiOperation(value = "Updates order",
            tags = {"order"},
            response = OrderEto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 422, message = "Could not process entity"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @PutMapping(value = "/order/{orderNumber}",
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_ORDERS })
    ResponseEntity<OrderEto> updateOrder(@Validated @RequestBody OrderTo orderTo, @PathVariable(value = "orderNumber") String orderNumber);


    @ApiOperation(value = "Updates comment",
            tags = {"comment"},
            response = CommentEto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 422, message = "Could not process entity"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @PutMapping(value = "/order/comment/{id}",
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_ORDERS,
            ApplicationPermissions.AUTH_USER })
    ResponseEntity<CommentEto> updateComment(@Validated @RequestBody CommentTo commentTo, @PathVariable(value = "id") Long id);


    @ApiOperation(value = "Deletes order",
            tags = {"order"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @DeleteMapping(value = "/order/{orderNumber}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_ORDERS })
    ResponseEntity<?> deleteOrder(@PathVariable(value = "orderNumber") String orderNumber);


    @ApiOperation(value = "Deletes comment",
            tags = {"comment"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @DeleteMapping(value = "/order/comment/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_ORDERS,
            ApplicationPermissions.AUTH_USER })
    ResponseEntity<?> deleteComment(@PathVariable(value = "id") Long id);


    @ApiOperation(value = "Deletes mediaContent",
            tags = {"mediaContent"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @DeleteMapping(value = "/order/mediaContent/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_ORDERS })
    ResponseEntity<?> deleteMediaContent(@PathVariable(value = "id") Long id);


    @ApiOperation(value = "Deletes all mediaContent by order number",
            tags = {"mediaContent"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @DeleteMapping(value = "/order/{orderNumber}/mediaContent",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_ORDERS })
    ResponseEntity<?> deleteAllMediaContent(@PathVariable(value = "orderNumber") String orderNumber);


    @ApiOperation(value = "Finishes order",
            tags = {"order"},
            response = OrderEto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 422, message = "Could not process entity"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @PutMapping(value = "/order/{orderNumber}/finish",
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_ORDERS,
            ApplicationPermissions.AUTH_USER })
    ResponseEntity<OrderEto> finishOrder(@PathVariable(value = "orderNumber") String orderNumber);


    @ApiOperation(value = "Accepts order",
            tags = {"order"},
            response = OrderEto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 422, message = "Could not process entity"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @PutMapping(value = "/order/{orderNumber}/accept",
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_ORDERS })
    ResponseEntity<OrderEto> acceptOrder(@PathVariable(value = "orderNumber") String orderNumber);


    @ApiOperation(value = "Sends order to verification",
            tags = {"order"},
            response = OrderEto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 422, message = "Could not process entity"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @PutMapping(value = "/order/{orderNumber}/verification",
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_ORDERS })
    ResponseEntity<OrderEto> sentToVerificationOrder(@PathVariable(value = "orderNumber") String orderNumber);
}
