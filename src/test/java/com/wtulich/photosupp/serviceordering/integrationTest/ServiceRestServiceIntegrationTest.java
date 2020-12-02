package com.wtulich.photosupp.serviceordering.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import com.wtulich.photosupp.serviceordering.logic.api.to.*;
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
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@SpringBootTest()
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ServiceRestServiceIntegrationTest {
    private static String GET_ALL_MEDIA_CONTENT_BY_ORDER_NUMBER_URL = "/order/v1/order/{orderNumber}/mediaContent";
    private static String ORDER_NUMBER_URL = "/order/v1/order/{orderNumber}";
    private static String GET_ALL_CITIES_URL = "/service/v1/address/cities";
    private static String GET_ALL_STREETS_URL = "/service/v1/address/streets";
    private static String GET_ALL_BOOKINGS_URL =  "/service/v1/bookings";
    private static String GET_ALL_BOOKINGS_BY_USER_ID_URL =  "/service/v1/bookings/{userId}";
    private static String GET_ALL_SERVICES_URL =  "/service/v1/services";
    private static String GET_ALL_INDICATORS_URL =  "/service/v1/indicators";
    private static String GET_BOOKING_BY_ID_URL = "/service/v1/booking/{id}";
    private static String INDICATOR_ID_URL = "/service/v1/indicator/{id}";
    private static String SERVICE_ID_URL = "/service/v1/service/{id}";
    private static String BOOKING_ID_URL = "/service/v1/booking/{id}";
    private static String SERVICE_URL = "/service/v1/service";
    private static String BOOKING_URL = "/service/v1/booking";
    private static String INDICATOR_URL = "/service/v1/indicator";
    private static String CONFIRM_BOOKING = "/service/v1/booking/{id}/confirm";
    private static String CALCULATE_SERVICE_URL = "/service/v1/service/calculate";

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private ServiceEto serviceEto;
    private ServiceEto serviceEto2;
    private AddressEto addressEto;
    private BookingEto bookingEto;
    private UserEto userEto;
    private IndicatorEto indicatorEto;
    private List<PriceIndicatorEto> priceIndicatorEtoList;
    private AddressTo addressTo;
    private ServiceTo serviceTo;
    private BookingTo bookingTo;
    private IndicatorTo indicatorTo;
    private List<PriceIndicatorTo> priceIndicatorToList;
    private CalculateTo calculateTo;
    private CalculateCto calculateCto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        addressEto = new AddressEto(1L, "Wroclaw", "Wroblewskiego", "27", null, "51-627");


        List<IndicatorEto> indicatorEtos = new ArrayList<>();
        indicatorEtos.add(indicatorEto);
        indicatorEto = new IndicatorEto(1L,"Podroz sluzbowa", "Paliwo, amortyzacja", "pl", 20, 30);
        serviceEto2 = new ServiceEto(2L, "FOTOGRAFIA", "PRODUKTOWA", 1500D, "pl", indicatorEtos);
        serviceEto = new ServiceEto(1L, "Film produktowy", "Film produktow na bialym tle i odpowiednim oswietleniu", 500D, "pl", indicatorEtos);

        List<PermissionEto> permissionEtoList = new ArrayList<>();
        permissionEtoList.add(new PermissionEto(6L, ApplicationPermissions.AUTH_USER, "Standard user with no special permissions."));
        RoleEto roleEto = new RoleEto(2L, "USER", "Standard user with no special permissions", permissionEtoList);
        AccountEto accountEto = new AccountEto(1L, "user1", "passw0rd", "user1@test.com", false);
        userEto = new UserEto(1L, "NAME", "SURNAME", accountEto, roleEto);

        priceIndicatorEtoList = new ArrayList<>();
        bookingEto = new BookingEto(1L, "Film dla TestCompany", "Film produktowy z dojazdem", serviceEto, addressEto, userEto, false, 1400D,
                "2020-04-11", "2020-04-12", "2020-04-11", priceIndicatorEtoList);

        PriceIndicatorEto priceIndicatorEto = new PriceIndicatorEto(indicatorEto, bookingEto.getId(), 400, 10);
        priceIndicatorEtoList.add(priceIndicatorEto);

        addressTo = new AddressTo("Wroclaw", "Wroblewskiego", "27", null, "51-627");
        List<Long> indicatorsIds = new ArrayList<>();
        indicatorsIds.add(1L);
        serviceTo = new ServiceTo("Film produktowy", "Film produktow na bialym tle i odpowiednim oswietleniu", 500D, "pl", indicatorsIds);
        indicatorTo = new IndicatorTo("Podróż służbowa", "Paliwo, amortyzacja", "pl", 20, 30);

        PriceIndicatorTo priceIndicatorTo = new PriceIndicatorTo(1L, 1L, 20, 0);
        priceIndicatorToList = new ArrayList<>();
        priceIndicatorToList.add(priceIndicatorTo);
        bookingTo = new BookingTo("Film dla TestCompany", "Film produktowy z dojazdem", 1L, 1L, addressTo,
                "2020-04-11", "2020-04-12", priceIndicatorToList);
    }

    public LocalDate getCurrentDate(LocalDate currentDate, int addDays) {
        if(addDays != 0) {
            currentDate = currentDate.plusDays(addDays);
        }
        return currentDate;
    }

    @Test
    @DisplayName("GET /service/v1/address/cities - Found")
    void testGetAllCitiesFound() throws Exception {
        //Arrange
        ArrayList<String> cities = new ArrayList<>();
        cities.add("Wroclaw");

        //Act
        MvcResult result = mockMvc.perform(get(GET_ALL_CITIES_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), String[].class))
                .isEqualTo(cities.toArray());
    }

    @Test
    @DisplayName("GET /service/v1/address/streets - Found")
    void testGetAllStreetsFound() throws Exception {
        //Arrange
        ArrayList<String> streets = new ArrayList<>();
        streets.add("Wroblewskiego");

        //Act
        MvcResult result = mockMvc.perform(get(GET_ALL_STREETS_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), String[].class))
                .isEqualTo(streets.toArray());
    }

    @Test
    @DisplayName("GET /service/v1/bookings - Found")
    void testGetAllBookingsFound() throws Exception {
        //Arrange
        ArrayList<BookingEto> bookings = new ArrayList<>();
        bookings.add(bookingEto);

        //Act
        MvcResult result = mockMvc.perform(get(GET_ALL_BOOKINGS_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        BookingEto[] bookingEtos = objectMapper.readValue(result.getResponse().getContentAsString(), BookingEto[].class);
        assertThat(bookingEtos).isEqualTo(bookings.toArray());
    }

    @Test
    @DisplayName("GET /service/v1/bookings - No content")
    void testGetAllBookingsNoContent() throws Exception {
        //Arrange
        mockMvc.perform(delete(BOOKING_ID_URL, bookingEto.getId()));

        //Act
        mockMvc.perform(get(GET_ALL_BOOKINGS_URL))

                //Assert
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /service/v1/bookings/{userId} - Found")
    void testGetAllBookingsByUserFound() throws Exception {
        //Arrange
        ArrayList<BookingEto> bookings = new ArrayList<>();
        bookings.add(bookingEto);

        //Act
        MvcResult result = mockMvc.perform(get(GET_ALL_BOOKINGS_BY_USER_ID_URL, userEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        BookingEto[] bookingEtos = objectMapper.readValue(result.getResponse().getContentAsString(), BookingEto[].class);
        assertThat(bookingEtos).isEqualTo(bookings.toArray());
    }

    @Test
    @DisplayName("GET /service/v1/bookings/{userId} - No Content")
    void testGetAllBookingsByUserNoContent() throws Exception {
        //Arrange
        mockMvc.perform(delete(BOOKING_ID_URL, bookingEto.getId()));

        //Act
        mockMvc.perform(get(GET_ALL_BOOKINGS_BY_USER_ID_URL, 1L))

                //Assert
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /service/v1/bookings/{userId} - Not Found")
    void testGetAllBookingsByUserNotFound() throws Exception {

        //Act
        mockMvc.perform(get(GET_ALL_BOOKINGS_BY_USER_ID_URL, 3L))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /service/v1/services - Found")
    void testGetAllServicesFound() throws Exception {
        //Arrange
        ArrayList<ServiceEto> services = new ArrayList<>();
        services.add(serviceEto);
        services.add(serviceEto2);

        //Act
        MvcResult result = mockMvc.perform(get(GET_ALL_SERVICES_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        ServiceEto[] serviceEtos = objectMapper.readValue(result.getResponse().getContentAsString(), ServiceEto[].class);
        assertThat(serviceEtos).isEqualTo(services.toArray());
    }

    @Test
    @DisplayName("GET /service/v1/services - No content")
    void testGetAllServicesNoContent() throws Exception {
        //Arrange
        mockMvc.perform(delete(BOOKING_ID_URL, bookingEto.getId()));
        mockMvc.perform(delete(SERVICE_ID_URL, serviceEto.getId()));
        mockMvc.perform(delete(SERVICE_ID_URL, serviceEto2.getId()));

        //Act
        mockMvc.perform(get(GET_ALL_SERVICES_URL))

                //Assert
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /service/v1/indicators - Found")
    void testGetAllIndicatorsFound() throws Exception {
        //Arrange
        ArrayList<IndicatorEto> indicators = new ArrayList<>();
        indicators.add(indicatorEto);

        //Act
        MvcResult result = mockMvc.perform(get(GET_ALL_INDICATORS_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), IndicatorEto[].class))
                .isEqualTo(indicators.toArray());
    }

    @Test
    @DisplayName("GET /service/v1/indicators - No content")
    void testGetAllIndicatorsNoContent() throws Exception {
        //Arrange
        mockMvc.perform(delete(BOOKING_ID_URL, bookingEto.getId()));
        mockMvc.perform(delete(INDICATOR_ID_URL, indicatorEto.getId()));

        //Act
        mockMvc.perform(get(GET_ALL_INDICATORS_URL))

                //Assert
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /service/v1/booking/1 - Found")
    void testGetBookingByIdFound() throws Exception {
        //Act
        MvcResult result = mockMvc.perform(get(GET_BOOKING_BY_ID_URL, bookingEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), BookingEto.class))
                .isEqualToComparingFieldByField(bookingEto);
    }

    @Test
    @DisplayName("GET /service/v1/booking/3 - No Found")
    void testGetBookingByIdNotFound() throws Exception {

        //Act
        mockMvc.perform(get(GET_BOOKING_BY_ID_URL, 3L))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /service/v1/indicator/ - OK")
    void testDeleteIndicatorOk() throws Exception {
        //Act
        mockMvc.perform(delete(BOOKING_ID_URL, bookingEto.getId()));
        mockMvc.perform(delete(INDICATOR_ID_URL, indicatorEto.getId()))

                //Assert
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /service/v1/indicator/2 - Not Found")
    void testDeleteIndicatorNotFound() throws Exception {
        //Act
        mockMvc.perform(delete(INDICATOR_ID_URL, 2))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /service/v1/indicator/1 - Unprocessable Entity")
    void testDeleteIndicatorUnprocessableEntity() throws Exception {
        //Act
        mockMvc.perform(delete(INDICATOR_ID_URL, indicatorEto.getId()))

                //Assert
                .andExpect(status().isUnprocessableEntity());
    }


    @Test
    @DisplayName("DELETE /service/v1/service/1 - OK")
    void testDeleteServiceOk() throws Exception {
        //Act
        mockMvc.perform(delete(BOOKING_ID_URL, bookingEto.getId()));
        mockMvc.perform(delete(SERVICE_ID_URL, serviceEto.getId()))

                //Assert
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /service/v1/role/1 - Unprocessable Entity")
    void testDeleteServiceUnprocessableEntity() throws Exception {
        //Act
        mockMvc.perform(delete(SERVICE_ID_URL, serviceEto.getId()))

                //Assert
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("DELETE /service/v1/role/3 - Not Found")
    void testDeleteServiceNotFound() throws Exception {
        //Act
        mockMvc.perform(delete(SERVICE_ID_URL, 3))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /service/v1/booking/1 - OK")
    void testDeleteBookingOk() throws Exception {
        //Act
        mockMvc.perform(delete(BOOKING_ID_URL, bookingEto.getId()))

                //Assert
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /service/v1/booking/1 - Not Found")
    void testDeleteBookingNotFound() throws Exception {
        //Act
        mockMvc.perform(delete(BOOKING_ID_URL, 2L))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /service/v1/indicator - Created")
    void testCreateIndicatorOk() throws Exception {
        //Arrange
        mockMvc.perform(delete(BOOKING_ID_URL, bookingEto.getId()));
        mockMvc.perform(delete(INDICATOR_ID_URL, indicatorEto.getId()));
        indicatorEto.setId(2L);

        //Act
        MvcResult result = mockMvc.perform(post(INDICATOR_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(indicatorTo)))

                //Assert
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), IndicatorEto.class))
                .isEqualToComparingFieldByField(indicatorEto);
    }

    @Test
    @DisplayName("POST /service/v1/indicator - Unprocessable Entity")
    void testCreateIndicatorUnprocessableEntity() throws Exception {
        //Act
        mockMvc.perform(post(INDICATOR_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(indicatorTo)))

                //Assert
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @DisplayName("POST /service/v1/service - Created")
    void testCreateServiceOk() throws Exception {
        //Arrange
        mockMvc.perform(delete(BOOKING_ID_URL, bookingEto.getId()));
        mockMvc.perform(delete(SERVICE_ID_URL, serviceEto.getId()));
        serviceEto.setId(3L);

        //Act
        MvcResult result = mockMvc.perform(post(SERVICE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(serviceTo)))

                //Assert
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), ServiceEto.class))
                .isEqualToComparingFieldByField(serviceEto);
    }

    @Test
    @DisplayName("POST /service/v1/service - Unprocessable Entity")
    void testCreateServiceUnprocessableEntity() throws Exception {
        //Act
        mockMvc.perform(post(SERVICE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(serviceTo)))

                //Assert
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @DisplayName("POST /service/v1/booking - Created")
    void testCreateBookingOk() throws Exception {
        //Arrange
        bookingTo.setUserId(1L);
        bookingEto.setModificationDate( DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( LocalDate.now()) );
        mockMvc.perform(delete(BOOKING_ID_URL, bookingEto.getId()));
        bookingEto.setId(2L);
        priceIndicatorEtoList.get(0).setBookingId(2L);

        //Act
        MvcResult result = mockMvc.perform(post(BOOKING_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(bookingTo)))

                //Assert
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(bookingEto)
                .isEqualToComparingFieldByField(objectMapper.readValue(result.getResponse().getContentAsString(), BookingEto.class));
    }

    @Test
    @DisplayName("POST /service/v1/booking - Not Found")
    void testCreateBookingNotFound() throws Exception {
        //Arrange
        mockMvc.perform(delete(BOOKING_ID_URL, bookingEto.getId()));
        bookingTo.setUserId(3L);

        //Act
        mockMvc.perform(post(BOOKING_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(bookingTo)))

                //Assert
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("POST /service/v1/booking - Unprocessable Entity")
    void testCreateBookingUnprocessableEntity() throws Exception {
        //Arrange
        bookingTo.setUserId(1L);

        //Act
        mockMvc.perform(post(BOOKING_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(bookingTo)))

                //Assert
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }


    @Test
    @DisplayName("PUT /service/v1/indicator/1 - OK")
    void testUpdateIndicatorOk() throws Exception {
        //Act
        MvcResult result = mockMvc.perform(put(INDICATOR_ID_URL, indicatorEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(indicatorTo)))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), IndicatorEto.class))
                .isEqualToComparingFieldByField(indicatorEto);
    }

    @Test
    @DisplayName("PUT /service/v1/indicator/1 - Not Found")
    void testUpdateIndicatorNotFound() throws Exception {
        //Act
        mockMvc.perform(put(INDICATOR_ID_URL, 3)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(indicatorTo)))

                //Assert
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /service/v1/indicator/1 - Unprocessable Entity")
    void testUpdateIndicatorUnprocessableEntity() throws Exception {
        //Arrange
        IndicatorTo indicatorTo2 = new IndicatorTo("Sprzet foto", "aparat", "pl", 30, 200);
        mockMvc.perform(post(INDICATOR_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(indicatorTo2)));

        //Act
        mockMvc.perform(put(INDICATOR_ID_URL, indicatorEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(indicatorTo2)))

                //Assert
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /service/v1/service/1 - OK")
    void testUpdateServiceOk() throws Exception {
        //Act
        MvcResult result = mockMvc.perform(put(SERVICE_ID_URL, serviceEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(serviceTo)))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), ServiceEto.class))
                .isEqualToComparingFieldByField(serviceEto);
    }

    @Test
    @DisplayName("PUT /service/v1/service/1 - Not Found")
    void testUpdateServiceNotFound() throws Exception {
        //Act
        mockMvc.perform(put(SERVICE_ID_URL, 4L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(serviceTo)))

                //Assert
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /service/v1/service/1 - Unprocessable Entity")
    void testUpdateServiceUnprocessableEntity() throws Exception {
        //Arrange
        List<Long> indicatorsIds = new ArrayList<>();
        indicatorsIds.add(1L);
        ServiceTo serviceTo2 = new ServiceTo("FOTOGRAFIA", "PRODUKTOWA", 500D, "pl", indicatorsIds);

        //Act
        mockMvc.perform(put(SERVICE_ID_URL, serviceEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(serviceTo2)))

                //Assert
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /service/v1/booking/1 - OK")
    void testUpdateBookingOk() throws Exception {
        //Act
        bookingEto.setModificationDate( DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( LocalDate.now()) );

        MvcResult result = mockMvc.perform(put(BOOKING_ID_URL, bookingEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(bookingTo)))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), BookingEto.class))
                .isEqualToComparingFieldByField(bookingEto);
    }

    @Test
    @DisplayName("PUT /service/v1/booking/1 - Not Found")
    void testUpdateBookingNotFound() throws Exception {
        //Act
        mockMvc.perform(put(BOOKING_ID_URL, 3L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(bookingTo)))

                //Assert
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /service/v1/booking/1 - Unprocessable Entity")
    void testUpdateBookingUnprocessableEntity() throws Exception {
        //Arrange
        BookingTo bookingTo2 = new BookingTo("Film dla TestCompany2", "Film produktowy z dojazdem", 1L, 1L, addressTo,
                "2020-04-11", "2020-04-12", priceIndicatorToList);
        bookingTo2.setUserId(1L);

        mockMvc.perform(post(BOOKING_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(bookingTo2)));

        //Act
        mockMvc.perform(put(BOOKING_ID_URL, bookingEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(bookingTo2)))

                //Assert
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @DisplayName("POST /service/v1/service/calculate - OK")
    void testCalculateServiceOk() throws Exception {
        //Arrange
        priceIndicatorEtoList.forEach(priceIndicatorEto -> priceIndicatorEto.setBookingId(null));

        calculateCto = new CalculateCto(serviceEto, 1400D,
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),0)),
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),1)),
                priceIndicatorEtoList);

        calculateTo = new CalculateTo(1L,
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),0)),
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),1)),
                priceIndicatorToList);

        //Act
        MvcResult result = mockMvc.perform(post(CALCULATE_SERVICE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(calculateTo)))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), CalculateCto.class))
                .isEqualToComparingFieldByField(calculateCto);
    }

    @Test
    @DisplayName("POST /service/v1/service/calculate - Unprocessable Entity")
    void testCalculateServiceUnprocessableEntity() throws Exception {
        //Arrange
        calculateTo = new CalculateTo(1L,
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),1)),
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),0)),
                priceIndicatorToList);

        //Act
        mockMvc.perform(post(CALCULATE_SERVICE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(calculateTo)))

                //Assert
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @DisplayName("POST /service/v1/service/calculate - Not Found")
    void testCalculateServiceNotFound() throws Exception {
        //Arrange
        calculateTo = new CalculateTo(3L,
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),0)),
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),1)),
                priceIndicatorToList);

        //Act
        mockMvc.perform(post(CALCULATE_SERVICE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(calculateTo)))

                //Assert
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /service/v1/booking/1/confirm - OK")
    void testConfirmBookingOk() throws Exception {
        //Arrange
        mockMvc.perform(delete(GET_ALL_MEDIA_CONTENT_BY_ORDER_NUMBER_URL, "INVIU_00001"));
        mockMvc.perform(delete(ORDER_NUMBER_URL, "INVIU_00001"));

        BookingEtoWithOrderNumber bookingEtoWithOrderNumber =
                new BookingEtoWithOrderNumber(1L, "Film dla TestCompany", "Film produktowy z dojazdem",
                        serviceEto, addressEto, userEto, true, 1400D, "2020-04-11", "2020-04-12",
                        "2020-04-11", priceIndicatorEtoList, "INVIU_00001");

        //Act
        MvcResult result = mockMvc.perform(put(CONFIRM_BOOKING, bookingEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(1L)))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), BookingEtoWithOrderNumber.class))
                .isEqualToComparingFieldByField(bookingEtoWithOrderNumber);
    }

    @Test
    @DisplayName("PUT /service/v1/booking/1/confirm - Not Found")
    void testConfirmBookingNotFound() throws Exception {
        //Act
        mockMvc.perform(put(CONFIRM_BOOKING, 3L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(1L)))

                //Assert
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
