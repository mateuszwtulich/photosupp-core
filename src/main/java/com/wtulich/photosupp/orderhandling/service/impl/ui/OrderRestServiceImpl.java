package com.wtulich.photosupp.orderhandling.service.impl.ui;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.logic.api.exception.EntityHasAssignedEntitiesException;
import com.wtulich.photosupp.orderhandling.logic.api.exception.OrderStatusInappropriateException;
import com.wtulich.photosupp.orderhandling.logic.api.to.*;
import com.wtulich.photosupp.orderhandling.logic.impl.OrderHandlingImpl;
import com.wtulich.photosupp.orderhandling.service.api.ui.OrderRestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class OrderRestServiceImpl implements OrderRestService {
    private static String ORDERS_NOT_EXIST = "Orders do not exist.";
    private static String MEDIA_CONTENT_NOT_EXIST = "Media content does not exist.";
    private static String COMMENT_NOT_EXIST = "Comments do not exist.";
    private static final String BASE_URL = "order/v1/";

    @Inject
    private OrderHandlingImpl orderHandling;

    @Override
    public List<OrderEto> getAllOrders() {
        return orderHandling.findAllOrders().map( orders -> {
            if(orders.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, ORDERS_NOT_EXIST);
            }
            return orders;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Override
    public List<OrderEto> getAllOrdersByUserId(Long userId) {
        try {
            return orderHandling.findAllOrdersByUserId(userId).map( orders -> {
            if(orders.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, ORDERS_NOT_EXIST);
            }
            return orders;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<OrderEto> getOrder(String orderNumber) {
        try {
            return ResponseEntity
                    .ok()
                    .body(orderHandling.findOrder(orderNumber).orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public List<CommentEto> getAllCommentsByOrderId(String orderNumber) {
        try {
            return orderHandling.findAllCommentsByOrderNumber(orderNumber).map( comments -> {
                if(comments.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.NO_CONTENT, COMMENT_NOT_EXIST);
                }
                return comments;
            }).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public List<MediaContentEto> getAllMediaContentByOrderId(String orderNumber) {
        try {
            return orderHandling.findAllMediaContentByOrderNumber(orderNumber).map( mediaContentList -> {
                if(mediaContentList.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.NO_CONTENT, MEDIA_CONTENT_NOT_EXIST);
                }
                return mediaContentList;
            }).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<OrderEto> createOrder(OrderTo orderTo) {
        try {
            return ResponseEntity
                    .created(new URI(BASE_URL + "/order"))
                    .body(orderHandling.createOrder(orderTo).orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
        } catch (EntityAlreadyExistsException | URISyntaxException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<CommentEto> createComment(CommentTo commentTo) {
        try {
            return ResponseEntity
                    .created(new URI(BASE_URL + "/order/comment"))
                    .body(orderHandling.createComment(commentTo).orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
        } catch (URISyntaxException | EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<MediaContentEto> addMediaContent(MediaContentTo mediaContentTo) {
        try {
            return ResponseEntity
                    .created(new URI(BASE_URL + "/order/mediaContent"))
                    .body(orderHandling.addMediaContent(mediaContentTo).orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
        } catch (URISyntaxException | EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<OrderEto> updateOrder(OrderTo orderTo, String orderNumber) {
        try {
            return ResponseEntity
                    .ok()
                    .body(orderHandling.updateOrder(orderTo, orderNumber).orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<CommentEto> updateComment(CommentTo commentTo, Long id) {
        try {
            return ResponseEntity
                    .ok()
                    .body(orderHandling.updateComment(commentTo, id).orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> deleteOrder(String orderNumber) {
        try {
            orderHandling.deleteOrder(orderNumber);
            return ResponseEntity.ok().build();
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityHasAssignedEntitiesException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> deleteComment(Long id) {
        try {
            orderHandling.deleteComment(id);
            return ResponseEntity.ok().build();
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }    }

    @Override
    public ResponseEntity<?> deleteMediaContent(Long id) {
        try {
            orderHandling.deleteMediaContent(id);
            return ResponseEntity.ok().build();
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> deleteAllMediaContent(String orderNumber) {
        try {
            orderHandling.deleteAllMediaContent(orderNumber);
            return ResponseEntity.ok().build();
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<OrderEto> finishOrder(String orderNumber) {
        try {
            return ResponseEntity
                    .ok()
                    .body(orderHandling.finishOrder(orderNumber).orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (OrderStatusInappropriateException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<OrderEto> acceptOrder(String orderNumber) {
        try {
            return ResponseEntity
                    .ok()
                    .body(orderHandling.acceptOrder(orderNumber).orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (OrderStatusInappropriateException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }        }

    @Override
    public ResponseEntity<OrderEto> sentToVerificationOrder(String orderNumber) {
        try {
            return ResponseEntity
                    .ok()
                    .body(orderHandling.sendOrderToVerification(orderNumber).orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (OrderStatusInappropriateException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }        }
}
