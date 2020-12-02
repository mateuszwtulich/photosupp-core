package com.wtulich.photosupp.orderhandling.service.impl.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.logic.api.exception.EntityHasAssignedEntitiesException;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import com.wtulich.photosupp.general.utils.enums.MediaType;
import com.wtulich.photosupp.general.utils.enums.OrderStatus;
import com.wtulich.photosupp.orderhandling.logic.api.exception.OrderStatusInappropriateException;
import com.wtulich.photosupp.orderhandling.logic.api.to.*;
import com.wtulich.photosupp.orderhandling.logic.impl.OrderHandlingImpl;
import com.wtulich.photosupp.serviceordering.logic.api.to.BookingTo;
import com.wtulich.photosupp.userhandling.logic.api.to.AccountEto;
import com.wtulich.photosupp.userhandling.logic.api.to.PermissionEto;
import com.wtulich.photosupp.userhandling.logic.api.to.RoleEto;
import com.wtulich.photosupp.userhandling.logic.api.to.UserEto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class OrderRestServiceTest {
    private static String GET_ALL_MEDIA_CONTENT_BY_ORDER_NUMBER_URL = "/order/v1/order/{orderNumber}/mediaContent";
    private static String GET_ALL_ORDERS_URL =  "/order/v1/orders";
    private static String GET_ALL_COMMENTS_ORDER_NUMBER_URL =  "/order/v1/order/{orderNumber}/comments";
    private static String MEDIA_CONTENT_ID_URL = "/order/v1/order/mediaContent/{id}";
    private static String ORDER_NUMBER_URL = "/order/v1/order/{orderNumber}";
    private static String COMMENT_ID_URL = "/order/v1/order/comment/{id}";
    private static String MEDIA_CONTENT_URL = "/order/v1/order/mediaContent";
    private static String ORDER_URL = "/order/v1/order";
    private static String COMMENT_URL = "/order/v1/order/comment";
    private static String SENT_TO_VERIFICATION_ORDER = "/order/v1/order/{orderNumber}/verification";
    private static String FINISH_ORDER = "/order/v1/order/{orderNumber}/finish";
    private static String ACCEPT_ORDER = "/order/v1/order/{orderNumber}/accept";

    @MockBean
    private OrderHandlingImpl orderHandling;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private OrderEto orderEto;
    private MediaContentEto mediaContentEto;
    private CommentEto commentEto;
    private OrderTo orderTo;
    private CommentTo commentTo;
    private MediaContentTo mediaContentTo;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        List<PermissionEto> permissionEtoList = new ArrayList<>();
        permissionEtoList.add(new PermissionEto(1L, ApplicationPermissions.A_CRUD_SUPER, "User has possibility to use CRUD operations one every functionality."));
        RoleEto roleEto = new RoleEto(1L, "MANAGER", "Manager with all permissions in order management", permissionEtoList);
        AccountEto accountEto = new AccountEto(1L, "user1", "passw0rd", "user1@test.com", false);
        UserEto userEto = new UserEto(1L, "NAME", "SURNAME", accountEto, roleEto);

        List<PermissionEto> permissionEtoList2 = new ArrayList<>();
        permissionEtoList2.add(new PermissionEto(6L, ApplicationPermissions.AUTH_USER, "Standard user with no special permissions."));
        RoleEto roleEto2 = new RoleEto(2L, "USER", "Standard user with no special permissions", permissionEtoList2);
        AccountEto accountEto2 = new AccountEto(2L, "user2", "passw0rd", "user2@test.com", true);
        UserEto userEto2 = new UserEto(2L, "NAME2", "SURNAME2", accountEto2, roleEto2);

        mediaContentEto = new MediaContentEto(1L, MediaType.IMAGE, "https://sample.com/jpg1", "INVIU_00001");
        commentEto = new CommentEto(1L, "Perfect, thanks!", "INVIU_00001", userEto2,
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format(LocalDate.now()));
        orderEto = new OrderEto("INVIU_00001", userEto, userEto2, OrderStatus.IN_PROGRESS, null, 1000D,
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format(LocalDate.now()));

        mediaContentTo = new MediaContentTo(MediaType.IMAGE, "https://sample.com/jpg1", "INVIU_00001");
        commentTo = new CommentTo("Perfect, thanks!", "INVIU_00001", 2L);
        orderTo = new OrderTo(1L, 2L, 1L, 1000D);
    }

    @Test
    @DisplayName("GET /order/v1/orders - Found")
    void testGetAllOrdersFound() throws Exception {
        //Arrange
        ArrayList<OrderEto> orders = new ArrayList<>();
        orders.add(orderEto);
        when(orderHandling.findAllOrders()).thenReturn(Optional.of(orders));

        //Act
        MvcResult result = mockMvc.perform(get(GET_ALL_ORDERS_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), OrderEto[].class))
                .isEqualTo(orders.toArray());
    }

    @Test
    @DisplayName("GET /order/v1/orders - No content")
    void testGetAllOrdersNoContent() throws Exception {
        //Arrange
        when(orderHandling.findAllOrders()).thenReturn(Optional.of(new ArrayList<>()));

        //Act
        mockMvc.perform(get(GET_ALL_ORDERS_URL))

                //Assert
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /order/v1/order/{orderNumber}/mediaContent - Found")
    void testGetAllMediaContentByOrderFound() throws Exception {
        //Arrange
        ArrayList<MediaContentEto> mediaContentList = new ArrayList<>();
        mediaContentList.add(mediaContentEto);
        when(orderHandling.findAllMediaContentByOrderNumber(orderEto.getOrderNumber())).thenReturn(Optional.of(mediaContentList));

        //Act
        MvcResult result = mockMvc.perform(get(GET_ALL_MEDIA_CONTENT_BY_ORDER_NUMBER_URL, orderEto.getOrderNumber())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), MediaContentEto[].class))
                .isEqualTo(mediaContentList.toArray());
    }

    @Test
    @DisplayName("GET /order/v1/order/{orderNumber}/mediaContent - Not Found")
    void testGetAllMediaContentByOrderNotFound() throws Exception {
        //Arrange
        ArrayList<MediaContentEto> mediaContentList = new ArrayList<>();
        mediaContentList.add(mediaContentEto);
        doThrow(EntityDoesNotExistException.class).when(orderHandling).findAllMediaContentByOrderNumber("INVIU_00002");

        //Act
        mockMvc.perform(get(GET_ALL_MEDIA_CONTENT_BY_ORDER_NUMBER_URL, "INVIU_00002"))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /order/v1/order/{orderNumber}/mediaContent - No content")
    void testGetAllMediaContentByOrderNoContent() throws Exception {
        //Arrange
        when(orderHandling.findAllMediaContentByOrderNumber(orderEto.getOrderNumber())).thenReturn(Optional.of(new ArrayList<>()));

        //Act
        mockMvc.perform(get(GET_ALL_MEDIA_CONTENT_BY_ORDER_NUMBER_URL, orderEto.getOrderNumber()))

                //Assert
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /order/v1/order/{orderNumber}/comments - Found")
    void testGetAllCommentsByOrderFound() throws Exception {
        //Arrange
        ArrayList<CommentEto> comments = new ArrayList<>();
        comments.add(commentEto);
        when(orderHandling.findAllCommentsByOrderNumber(orderEto.getOrderNumber())).thenReturn(Optional.of(comments));

        //Act
        MvcResult result = mockMvc.perform(get(GET_ALL_COMMENTS_ORDER_NUMBER_URL, orderEto.getOrderNumber())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), CommentEto[].class))
                .isEqualTo(comments.toArray());
    }

    @Test
    @DisplayName("GET /order/v1/order/{orderNumber}/comments - Not Found")
    void testGetAllCommentsByOrderNotFound() throws Exception {
        //Arrange
        ArrayList<MediaContentEto> mediaContentList = new ArrayList<>();
        mediaContentList.add(mediaContentEto);
        doThrow(EntityDoesNotExistException.class).when(orderHandling).findAllCommentsByOrderNumber("INVIU_00002");

        //Act
        mockMvc.perform(get(GET_ALL_COMMENTS_ORDER_NUMBER_URL, "INVIU_00002"))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /order/v1/order/{orderNumber}/comments - No content")
    void testGetAllCommentsByOrderNoContent() throws Exception {
        //Arrange
        when(orderHandling.findAllCommentsByOrderNumber(orderEto.getOrderNumber())).thenReturn(Optional.of(new ArrayList<>()));

        //Act
        mockMvc.perform(get(GET_ALL_COMMENTS_ORDER_NUMBER_URL, orderEto.getOrderNumber()))

                //Assert
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /order/v1/order/{orderNumber} - Found")
    void testGetOrderFound() throws Exception {
        //Arrange
        when(orderHandling.findOrder(orderEto.getOrderNumber())).thenReturn(Optional.of(orderEto));

        //Act
        MvcResult result = mockMvc.perform(get(ORDER_NUMBER_URL, orderEto.getOrderNumber())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), OrderEto.class))
                .isEqualToComparingFieldByField(orderEto);
    }

    @Test
    @DisplayName("GET /order/v1/order/{orderNumber}  - Not Found")
    void testGetOrderNotFound() throws Exception {
        //Arrange
        when(orderHandling.findOrder("INVIU_00002"))
                .thenThrow(EntityDoesNotExistException.class);

        //Act
        mockMvc.perform(get(ORDER_NUMBER_URL, "INVIU_00002"))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /order/v1/order/{orderNumber} - ISE")
    void testGetOrderISE() throws Exception {
        //Arrange
        when(orderHandling.findOrder("INVIU_00002"))
                .thenReturn(Optional.empty());

        //Act
        mockMvc.perform(get(ORDER_NUMBER_URL, "INVIU_00002"))

                //Assert
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("DELETE /order/v1/order/{orderNumber} - OK")
    void testDeleteOrderOk() throws Exception {

        //Act
        mockMvc.perform(delete(ORDER_NUMBER_URL, orderEto.getOrderNumber()))

                //Assert
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /order/v1/order/{orderNumber} - Not Found")
    void testDeleteOrderNotFound() throws Exception {
        //Arrange
        doThrow(EntityDoesNotExistException.class).when(orderHandling).deleteOrder("INVIU_00002");

        //Act
        mockMvc.perform(delete(ORDER_NUMBER_URL, "INVIU_00002"))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /order/v1/order/{orderNumber} - Unprocessable Entity")
    void testDeleteOrderUnprocessableEntity() throws Exception {
        //Arrange
        doThrow(EntityHasAssignedEntitiesException.class).when(orderHandling).deleteOrder("INVIU_00002");

        //Act
        mockMvc.perform(delete(ORDER_NUMBER_URL, "INVIU_00002"))

                //Assert
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("DELETE /order/v1/order/mediaContent/{id}- OK")
    void testDeleteMediaContentOk() throws Exception {

        //Act
        mockMvc.perform(delete(MEDIA_CONTENT_ID_URL, mediaContentEto.getId()))

                //Assert
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /order/v1/order/mediaContent/{id} - Not Found")
    void testDeleteMediaContentNotFound() throws Exception {
        //Arrange
        doThrow(EntityDoesNotExistException.class).when(orderHandling).deleteMediaContent(3L);

        //Act
        mockMvc.perform(delete(MEDIA_CONTENT_ID_URL, 3L))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /order/v1/order/comment/{id} - OK")
    void testDeleteCommentOk() throws Exception {

        //Act
        mockMvc.perform(delete(COMMENT_ID_URL, mediaContentEto.getId()))

                //Assert
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /order/v1/order/comment/{id} - Not Found")
    void testDeleteCommentNotFound() throws Exception {
        //Arrange
        doThrow(EntityDoesNotExistException.class).when(orderHandling).deleteComment(3L);

        //Act
        mockMvc.perform(delete(COMMENT_ID_URL, 3L))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /order/v1/order/{orderNumber}/mediaContent - OK")
    void testDeleteAllMediaContentOk() throws Exception {

        //Act
        mockMvc.perform(delete(GET_ALL_MEDIA_CONTENT_BY_ORDER_NUMBER_URL, orderEto.getOrderNumber()))

                //Assert
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /order/v1/order/{orderNumber}/mediaContent - Not Found")
    void testDeleteAllMediaContentNotFound() throws Exception {
        //Arrange
        doThrow(EntityDoesNotExistException.class).when(orderHandling).deleteAllMediaContent("INVIU_00003");

        //Act
        mockMvc.perform(delete(GET_ALL_MEDIA_CONTENT_BY_ORDER_NUMBER_URL, "INVIU_00003"))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /order/v1/order - Ok")
    void testCreateOrderOk() throws Exception {
        //Arrange
        when(orderHandling.createOrder(orderTo)).thenReturn(Optional.of(orderEto));

        //Act
        MvcResult result = mockMvc.perform(post(ORDER_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(orderTo)))

                //Assert
                .andExpect(status().isCreated())
                .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), OrderEto.class))
                .isEqualToComparingFieldByField(orderEto);
    }

    @Test
    @DisplayName("POST /order/v1/order - Not Found")
    void testCreateOrderNotFound() throws Exception {
        //Arrange
        when(orderHandling.createOrder(orderTo)).thenThrow(EntityDoesNotExistException.class);

        //Act
        mockMvc.perform(post(ORDER_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(orderTo)))

                //Assert
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("POST /order/v1/order - Unprocessable Entity")
    void testCreateOrderUnprocessableEntity() throws Exception {
        //Arrange
        when(orderHandling.createOrder(orderTo)).thenThrow(EntityAlreadyExistsException.class);

        //Act
        mockMvc.perform(post(ORDER_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(orderTo)))

                //Assert
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @DisplayName("POST /order/v1/order - ISE")
    void testCreateOrderISE() throws Exception {
        //Arrange
        when(orderHandling.createOrder(orderTo)).thenReturn(Optional.empty());

        //Act
        mockMvc.perform(post(ORDER_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(orderTo)))

                //Assert
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    @DisplayName("POST /order/v1/order/comment - Ok")
    void testCreateCommentOk() throws Exception {
        //Arrange
        when(orderHandling.createComment(commentTo)).thenReturn(Optional.of(commentEto));

        //Act
        MvcResult result = mockMvc.perform(post(COMMENT_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(commentTo)))

                //Assert
                .andExpect(status().isCreated())
                .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), CommentEto.class))
                .isEqualToComparingFieldByField(commentEto);
    }

    @Test
    @DisplayName("POST /order/v1/order/comment - Not Found")
    void testCreateCommentNotFound() throws Exception {
        //Arrange
        when(orderHandling.createComment(commentTo)).thenThrow(EntityDoesNotExistException.class);

        //Act
        mockMvc.perform(post(COMMENT_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(commentTo)))

                //Assert
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("POST /order/v1/order/comment - ISE")
    void testCreateCommentISE() throws Exception {
        //Arrange
        when(orderHandling.createComment(commentTo)).thenReturn(Optional.empty());

        //Act
        mockMvc.perform(post(COMMENT_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(commentTo)))

                //Assert
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    @DisplayName("POST /order/v1/order/mediaContent - Ok")
    void testAddMediaContentOk() throws Exception {
        //Arrange
        when(orderHandling.addMediaContent(mediaContentTo)).thenReturn(Optional.of(mediaContentEto));

        //Act
        MvcResult result = mockMvc.perform(post(MEDIA_CONTENT_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(mediaContentTo)))

                //Assert
                .andExpect(status().isCreated())
                .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), MediaContentEto.class))
                .isEqualToComparingFieldByField(mediaContentEto);
    }

    @Test
    @DisplayName("POST /order/v1/order/mediaContent - Not Found")
    void testAddMediaContentNotFound() throws Exception {
        //Arrange
        when(orderHandling.addMediaContent(mediaContentTo)).thenThrow(EntityDoesNotExistException.class);

        //Act
        mockMvc.perform(post(MEDIA_CONTENT_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(mediaContentTo)))

                //Assert
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("POST /order/v1/order/mediaContent - ISE")
    void testAddMediaContentISE() throws Exception {
        //Arrange
        when(orderHandling.addMediaContent(mediaContentTo)).thenReturn(Optional.empty());

        //Act
        mockMvc.perform(post(MEDIA_CONTENT_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(mediaContentTo)))

                //Assert
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /order/v1/order/{orderNumber} - OK")
    void testUpdateOrderOk() throws Exception {
        //Arrange
        when(orderHandling.updateOrder(orderTo, orderEto.getOrderNumber())).thenReturn(Optional.of(orderEto));

        //Act
        MvcResult result = mockMvc.perform(put(ORDER_NUMBER_URL, orderEto.getOrderNumber())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(orderTo)))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), OrderEto.class))
                .isEqualToComparingFieldByField(orderEto);
    }

    @Test
    @DisplayName("PUT /order/v1/order/{orderNumber} - Not Found")
    void testUpdateOrderNotFound() throws Exception {
        //Arrange
        when(orderHandling.updateOrder(orderTo, orderEto.getOrderNumber())).thenThrow(EntityDoesNotExistException.class);

        //Act
        MvcResult result = mockMvc.perform(put(ORDER_NUMBER_URL, orderEto.getOrderNumber())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(orderTo)))

                //Assert
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /order/v1/order/{orderNumber} - Unprocessable Entity")
    void testUpdateOrderUnprocessableEntity() throws Exception {
        //Arrange
        when(orderHandling.updateOrder(orderTo, orderEto.getOrderNumber())).thenThrow(EntityAlreadyExistsException.class);

        //Act
        mockMvc.perform(put(ORDER_NUMBER_URL, orderEto.getOrderNumber())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(orderTo)))

                //Assert
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /order/v1/order/{orderNumber} - ISE")
    void testUpdateOrderISE() throws Exception {
        //Arrange
        when(orderHandling.updateOrder(orderTo, orderEto.getOrderNumber())).thenReturn(Optional.empty());

        //Act
        mockMvc.perform(put(ORDER_NUMBER_URL, orderEto.getOrderNumber())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(orderTo)))

                //Assert
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /order/v1/order/comment/{id} - OK")
    void testUpdateCommentOk() throws Exception {
        //Arrange
        when(orderHandling.updateComment(commentTo, commentEto.getId())).thenReturn(Optional.of(commentEto));

        //Act
        MvcResult result = mockMvc.perform(put(COMMENT_ID_URL, commentEto.getId())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(commentTo)))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), CommentEto.class))
                .isEqualToComparingFieldByField(commentEto);
    }

    @Test
    @DisplayName("PUT /order/v1/order/comment/{id} - Not Found")
    void testUpdateCommentNotFound() throws Exception {
        //Arrange
        when(orderHandling.updateComment(commentTo, commentEto.getId())).thenThrow(EntityDoesNotExistException.class);

        //Act
        mockMvc.perform(put(COMMENT_ID_URL, commentEto.getId())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(commentTo)))

                //Assert
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /order/v1/order/comment/{id} - ISE")
    void testUpdateCommentISE() throws Exception {
        //Arrange
        when(orderHandling.updateComment(commentTo, commentEto.getId())).thenReturn(Optional.empty());

        //Act
        mockMvc.perform(put(COMMENT_ID_URL, commentEto.getId())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(commentTo)))

                //Assert
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /order/v1/order/{orderNumber}/finish - OK")
    void testFinishOrderOk() throws Exception {
        //Arrange
        when(orderHandling.finishOrder(orderEto.getOrderNumber())).thenReturn(Optional.ofNullable(orderEto));

        //Act
        MvcResult result = mockMvc.perform(put(FINISH_ORDER, orderEto.getOrderNumber())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), OrderEto.class))
                .isEqualToComparingFieldByField(orderEto);
    }

    @Test
    @DisplayName("PUT /order/v1/order/{orderNumber}/finish - Not Found")
    void testFinishOrderNotFound() throws Exception {
        //Arrange
        when(orderHandling.finishOrder(orderEto.getOrderNumber())).thenThrow(EntityDoesNotExistException.class);

        //Act
        mockMvc.perform(put(FINISH_ORDER, orderEto.getOrderNumber())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /order/v1/order/{orderNumber}/finish - Unprocessable Entity")
    void testFinishOrderUnprocessableEntity() throws Exception {
        //Arrange
        when(orderHandling.finishOrder(orderEto.getOrderNumber())).thenThrow(OrderStatusInappropriateException.class);

        //Act
        mockMvc.perform(put(FINISH_ORDER, orderEto.getOrderNumber())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("PUT /order/v1/order/{orderNumber}/finish - ISE")
    void testFinishOrderISE() throws Exception {
        //Arrange
        when(orderHandling.finishOrder(orderEto.getOrderNumber())).thenReturn(Optional.empty());

        //Act
        mockMvc.perform(put(FINISH_ORDER, orderEto.getOrderNumber())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE))
                //Assert
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("PUT /order/v1/order/{orderNumber}/accept - OK")
    void testAcceptOrderOk() throws Exception {
        //Arrange
        when(orderHandling.acceptOrder(orderEto.getOrderNumber())).thenReturn(Optional.ofNullable(orderEto));

        //Act
        MvcResult result = mockMvc.perform(put(ACCEPT_ORDER, orderEto.getOrderNumber())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), OrderEto.class))
                .isEqualToComparingFieldByField(orderEto);
    }

    @Test
    @DisplayName("PUT /order/v1/order/{orderNumber}/accept - Not Found")
    void testAcceptOrderNotFound() throws Exception {
        //Arrange
        when(orderHandling.acceptOrder(orderEto.getOrderNumber())).thenThrow(EntityDoesNotExistException.class);

        //Act
        mockMvc.perform(put(ACCEPT_ORDER, orderEto.getOrderNumber())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /order/v1/order/{orderNumber}/accept - Unprocessable Entity")
    void testAcceptOrderUnprocessableEntity() throws Exception {
        //Arrange
        when(orderHandling.acceptOrder(orderEto.getOrderNumber())).thenThrow(OrderStatusInappropriateException.class);

        //Act
        mockMvc.perform(put(ACCEPT_ORDER, orderEto.getOrderNumber())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("PUT /order/v1/order/{orderNumber}/accept  - ISE")
    void testAcceptOrderISE() throws Exception {
        //Arrange
        when(orderHandling.acceptOrder(orderEto.getOrderNumber())).thenReturn(Optional.empty());

        //Act
        mockMvc.perform(put(ACCEPT_ORDER, orderEto.getOrderNumber())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE))
                //Assert
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("PUT /order/v1/order/{orderNumber}/verification - OK")
    void testVerificationOrderOk() throws Exception {
        //Arrange
        when(orderHandling.sendOrderToVerification(orderEto.getOrderNumber())).thenReturn(Optional.of(orderEto));

        //Act
        MvcResult result = mockMvc.perform(put(SENT_TO_VERIFICATION_ORDER, orderEto.getOrderNumber())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), OrderEto.class))
                .isEqualToComparingFieldByField(orderEto);
    }

    @Test
    @DisplayName("PUT /order/v1/order/{orderNumber}/verification - Not Found")
    void testVerificationOrderNotFound() throws Exception {
        //Arrange
        when(orderHandling.sendOrderToVerification(orderEto.getOrderNumber())).thenThrow(EntityDoesNotExistException.class);

        //Act
        mockMvc.perform(put(SENT_TO_VERIFICATION_ORDER, orderEto.getOrderNumber())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /order/v1/order/{orderNumber}/verification - Unprocessable Entity")
    void testVerificationOrderUnprocessableEntity() throws Exception {
        //Arrange
        when(orderHandling.sendOrderToVerification(orderEto.getOrderNumber())).thenThrow(OrderStatusInappropriateException.class);

        //Act
        mockMvc.perform(put(SENT_TO_VERIFICATION_ORDER, orderEto.getOrderNumber())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE))
                //Assert
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("PUT /order/v1/order/{orderNumber}/verification  - ISE")
    void testVerificationOrderISE() throws Exception {
        //Arrange
        when(orderHandling.sendOrderToVerification(orderEto.getOrderNumber())).thenReturn(Optional.empty());

        //Act
        mockMvc.perform(put(SENT_TO_VERIFICATION_ORDER, orderEto.getOrderNumber())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE))
                //Assert
                .andExpect(status().isInternalServerError());
    }
}
