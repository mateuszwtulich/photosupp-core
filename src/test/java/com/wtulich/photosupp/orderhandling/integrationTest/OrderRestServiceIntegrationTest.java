package com.wtulich.photosupp.orderhandling.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wtulich.photosupp.general.utils.enums.MediaType;
import com.wtulich.photosupp.general.utils.enums.OrderStatus;
import com.wtulich.photosupp.orderhandling.logic.api.to.*;
import com.wtulich.photosupp.orderhandling.logic.impl.OrderHandlingImpl;
import com.wtulich.photosupp.orderhandling.logic.impl.validator.OrderValidator;
import com.wtulich.photosupp.userhandling.logic.api.to.AccountEto;
import com.wtulich.photosupp.userhandling.logic.api.to.UserEto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@SpringBootTest()
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OrderRestServiceIntegrationTest {
    private static String GET_ALL_MEDIA_CONTENT_BY_ORDER_NUMBER_URL = "/order/v1/order/{orderNumber}/mediaContent";
    private static String GET_ALL_ORDERS_URL =  "/order/v1/orders";
    private static String GET_ALL_ORDERS_BY_USER_ID_URL =  "/order/v1/orders/{userId}";
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

    @Autowired
    private OrderHandlingImpl orderHandling;

    @Autowired
    private OrderValidator orderValidator;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private OrderEto orderEto;
    private MediaContentEto mediaContentEto;
    private CommentEto commentEto;
    private UserEto userEto;
    private OrderTo orderTo;
    private CommentTo commentTo;
    private MediaContentTo mediaContentTo;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        AccountEto accountEto = new AccountEto(1L, "user1", "passw0rd", "user1@test.com", false);
        userEto = new UserEto(1L, "NAME", "SURNAME", accountEto, null);

        AccountEto accountEto2 = new AccountEto(2L, "user2", "passw0rd", "user2@test.com", true);
        UserEto userEto2 = new UserEto(2L, "NAME2", "SURNAME2", accountEto2, null);

        mediaContentEto = new MediaContentEto(1L, MediaType.IMAGE, "https://sample.com/jpg1", "INVIU_00001");
        commentEto = new CommentEto(1L, "Perfect, thanks!", "INVIU_00001", userEto2, "2020-09-11");
        orderEto = new OrderEto("INVIU_00001", userEto2, userEto, OrderStatus.NEW, null, 1000D, "2020-09-11");

        mediaContentTo = new MediaContentTo(MediaType.IMAGE, "https://sample.com/jpg1", "INVIU_00001");
        commentTo = new CommentTo("Perfect, thanks!", "INVIU_00001", 2L);
        orderTo = new OrderTo(2L, 1L, null, 1000D);
    }

    @Test
    @DisplayName("GET /order/v1/orders - Found")
    void testGetAllOrdersFound() throws Exception {
        //Arrange
        ArrayList<OrderEto> orders = new ArrayList<>();
        orders.add(orderEto);

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
        mockMvc.perform(delete(GET_ALL_MEDIA_CONTENT_BY_ORDER_NUMBER_URL, orderEto.getOrderNumber()));
        mockMvc.perform(delete(ORDER_NUMBER_URL, orderEto.getOrderNumber()));

        //Act
        mockMvc.perform(get(GET_ALL_ORDERS_URL))

                //Assert
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /order/v1/orders/{userId} - Found")
    void testGetAllOrdersByUserIdFound() throws Exception {
        //Arrange
        ArrayList<OrderEto> orders = new ArrayList<>();
        orders.add(orderEto);

        //Act
        MvcResult result = mockMvc.perform(get(GET_ALL_ORDERS_BY_USER_ID_URL, userEto.getId())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), OrderEto[].class))
                .isEqualTo(orders.toArray());
    }

    @Test
    @DisplayName("GET /order/v1/orders/{userId} - No content")
    void testGetAllOrdersByUserIdNoContent() throws Exception {
        //Arrange
        mockMvc.perform(delete(GET_ALL_MEDIA_CONTENT_BY_ORDER_NUMBER_URL, orderEto.getOrderNumber()));
        mockMvc.perform(delete(ORDER_NUMBER_URL, orderEto.getOrderNumber()));

        //Act
        mockMvc.perform(get(GET_ALL_ORDERS_BY_USER_ID_URL, userEto.getId()))

                //Assert
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /order/v1/orders/{userId} - Not found")
    void testGetAllOrdersByUserIdNotFound() throws Exception {

        //Act
        mockMvc.perform(get(GET_ALL_ORDERS_BY_USER_ID_URL, 3L))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /order/v1/order/{orderNumber}/mediaContent - Found")
    void testGetAllMediaContentByOrderFound() throws Exception {
        //Arrange
        ArrayList<MediaContentEto> mediaContentList = new ArrayList<>();
        mediaContentList.add(mediaContentEto);

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

        //Act
        mockMvc.perform(get(GET_ALL_MEDIA_CONTENT_BY_ORDER_NUMBER_URL, "INVIU_00002"))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /order/v1/order/{orderNumber}/mediaContent - No content")
    void testGetAllMediaContentByOrderNoContent() throws Exception {
        //Arrange
        mockMvc.perform(delete(GET_ALL_MEDIA_CONTENT_BY_ORDER_NUMBER_URL, orderEto.getOrderNumber()));

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
        //Act
        mockMvc.perform(get(GET_ALL_COMMENTS_ORDER_NUMBER_URL, "INVIU_00002"))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /order/v1/order/{orderNumber}/comments - No content")
    void testGetAllCommentsByOrderNoContent() throws Exception {
        //Arrange
        mockMvc.perform(delete(COMMENT_ID_URL, mediaContentEto.getId()));

        //Act
        mockMvc.perform(get(GET_ALL_COMMENTS_ORDER_NUMBER_URL, orderEto.getOrderNumber()))

                //Assert
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /order/v1/order/{orderNumber} - Found")
    void testGetOrderFound() throws Exception {
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

        //Act
        mockMvc.perform(get(ORDER_NUMBER_URL, "INVIU_00002"))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /order/v1/order/{orderNumber} - OK")
    void testDeleteOrderOk() throws Exception {
        mockMvc.perform(delete(GET_ALL_MEDIA_CONTENT_BY_ORDER_NUMBER_URL, orderEto.getOrderNumber()));

        //Act
        mockMvc.perform(delete(ORDER_NUMBER_URL, orderEto.getOrderNumber()))

                //Assert
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /order/v1/order/{orderNumber} - Not Found")
    void testDeleteOrderNotFound() throws Exception {

        //Act
        mockMvc.perform(delete(ORDER_NUMBER_URL, "INVIU_00002"))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /order/v1/order/{orderNumber} - Unprocessable Entity")
    void testDeleteOrderUnprocessableEntity() throws Exception {

        //Act
        mockMvc.perform(delete(ORDER_NUMBER_URL, orderEto.getOrderNumber()))

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

        //Act
        mockMvc.perform(delete(GET_ALL_MEDIA_CONTENT_BY_ORDER_NUMBER_URL, "INVIU_00003"))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /order/v1/order - Ok")
    void testCreateOrderOk() throws Exception {
        //Arrange
        mockMvc.perform(delete(GET_ALL_MEDIA_CONTENT_BY_ORDER_NUMBER_URL, orderEto.getOrderNumber()));
        mockMvc.perform(delete(ORDER_NUMBER_URL, orderEto.getOrderNumber()));
        orderEto.setCreatedAt(DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format(LocalDate.now()));

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
        mockMvc.perform(delete(GET_ALL_MEDIA_CONTENT_BY_ORDER_NUMBER_URL, orderEto.getOrderNumber()));
        mockMvc.perform(delete(ORDER_NUMBER_URL, orderEto.getOrderNumber()));
        orderTo.setUserId(3L);

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
        mockMvc.perform(delete(GET_ALL_MEDIA_CONTENT_BY_ORDER_NUMBER_URL, orderEto.getOrderNumber()));
        mockMvc.perform(delete(ORDER_NUMBER_URL, orderEto.getOrderNumber()));

        orderTo.setBookingId(1L);
        mockMvc.perform(post(ORDER_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(orderTo)));

        //Act
        mockMvc.perform(post(ORDER_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(orderTo)))

                //Assert
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @DisplayName("POST /order/v1/order/comment - Ok")
    void testCreateCommentOk() throws Exception {
        //Arrange
        commentEto.setCreatedAt(DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format(LocalDate.now()));
        commentEto.setId(2L);

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
        commentTo.setUserId(3L);

        //Act
        mockMvc.perform(post(COMMENT_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(commentTo)))

                //Assert
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("POST /order/v1/order/mediaContent - Ok")
    void testAddMediaContentOk() throws Exception {
        //Arrange
        mediaContentTo.setUrl("test");
        mediaContentEto.setUrl("test");
        mediaContentEto.setId(2L);

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
        mediaContentTo.setOrderNumber("test");

        //Act
        mockMvc.perform(post(MEDIA_CONTENT_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(mediaContentTo)))

                //Assert
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /order/v1/order/{orderNumber} - OK")
    void testUpdateOrderOk() throws Exception {

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
        orderTo.setBookingId(5L);

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
        mockMvc.perform(delete(GET_ALL_MEDIA_CONTENT_BY_ORDER_NUMBER_URL, orderEto.getOrderNumber()));
        mockMvc.perform(delete(ORDER_NUMBER_URL, orderEto.getOrderNumber()));

        orderTo.setBookingId(1L);
        mockMvc.perform(post(ORDER_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(orderTo)));

        //Act
        mockMvc.perform(put(ORDER_NUMBER_URL, orderEto.getOrderNumber())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(orderTo)))

                //Assert
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /order/v1/order/comment/{id} - OK")
    void testUpdateCommentOk() throws Exception {

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

        //Act
        mockMvc.perform(put(COMMENT_ID_URL, 3L)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(commentTo)))

                //Assert
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /order/v1/order/{orderNumber}/finish - OK")
    void testFinishOrderOk() throws Exception {
        //Arrange
        mockMvc.perform(put(ACCEPT_ORDER, orderEto.getOrderNumber())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE));

        mockMvc.perform(put(SENT_TO_VERIFICATION_ORDER, orderEto.getOrderNumber())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE));

        orderEto.setStatus(OrderStatus.FINISHED);

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

        //Act
        mockMvc.perform(put(FINISH_ORDER, "test")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /order/v1/order/{orderNumber}/finish - Unprocessable Entity")
    void testFinishOrderUnprocessableEntity() throws Exception {

        //Act
        mockMvc.perform(put(FINISH_ORDER, orderEto.getOrderNumber())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("PUT /order/v1/order/{orderNumber}/accept - OK")
    void testAcceptOrderOk() throws Exception {
        //Arrange
        orderEto.setStatus(OrderStatus.IN_PROGRESS);

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

        //Act
        mockMvc.perform(put(ACCEPT_ORDER, "test")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /order/v1/order/{orderNumber}/accept - Unprocessable Entity")
    void testAcceptOrderUnprocessableEntity() throws Exception {
        //Arrange
        mockMvc.perform(put(ACCEPT_ORDER, orderEto.getOrderNumber())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE));

        //Act
        mockMvc.perform(put(ACCEPT_ORDER, orderEto.getOrderNumber())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("PUT /order/v1/order/{orderNumber}/verification - OK")
    void testVerificationOrderOk() throws Exception {
        //Arrange
        mockMvc.perform(put(ACCEPT_ORDER, orderEto.getOrderNumber())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE));
        orderEto.setStatus(OrderStatus.TO_VERIFY);

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

        //Act
        mockMvc.perform(put(SENT_TO_VERIFICATION_ORDER, "test")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /order/v1/order/{orderNumber}/verification - Unprocessable Entity")
    void testVerificationOrderUnprocessableEntity() throws Exception {

        //Act
        mockMvc.perform(put(SENT_TO_VERIFICATION_ORDER, orderEto.getOrderNumber())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE))
                //Assert
                .andExpect(status().isUnprocessableEntity());
    }
}

