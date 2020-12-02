package com.wtulich.photosupp.serviceordering.service.impl.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.logic.api.exception.EntityHasAssignedEntitiesException;
import com.wtulich.photosupp.general.logic.api.exception.UnprocessableEntityException;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import com.wtulich.photosupp.serviceordering.logic.api.to.*;
import com.wtulich.photosupp.serviceordering.logic.impl.ServiceOrderingImpl;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ServiceRestServiceTest {
    private static String GET_ALL_CITIES_URL = "/service/v1/address/cities";
    private static String GET_ALL_STREETS_URL = "/service/v1/address/streets";
    private static String GET_ALL_BOOKINGS_URL =  "/service/v1/bookings";
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

    @MockBean
    private ServiceOrderingImpl serviceOrdering;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private ServiceEto serviceEto;
    private AddressEto addressEto;
    private BookingEto bookingEto;
    private IndicatorEto indicatorEto;
    private List<PriceIndicatorEto> priceIndicatorEtoList;
    private AddressTo addressTo;
    private ServiceTo serviceTo;
    private BookingTo bookingTo;
    private IndicatorTo indicatorTo;
    private List<PriceIndicatorTo> priceIndicatorToList;
    private CalculateTo calculateTo;
    private CalculateCto calculateCto;
    private UserEto userEto;
    private BookingEtoWithOrderNumber bookingEtoWithOrderNumber;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        addressEto = new AddressEto(1L, "Wroclaw", "Wroblewskiego", "27", null, "51-627");
        indicatorEto = new IndicatorEto(1L,"Podroz sluzbowa", "Paliwo, amortyzacja", "pl", 20, 30);
        List<IndicatorEto> indicatorEtos = List.of(indicatorEto);

        serviceEto = new ServiceEto(1L, "Film produktowy", "Film produktow na bialym tle i odpowiednim oswietleniu", 500D, "pl", indicatorEtos);

        List<PermissionEto> permissionEtoList = new ArrayList<>();
        permissionEtoList.add(new PermissionEto(1L, ApplicationPermissions.A_CRUD_SUPER, "DESC1"));
        RoleEto roleEto = new RoleEto(1L, "ADMIN", "DESC1", permissionEtoList);
        AccountEto accountEto = new AccountEto(1L, "TEST", "PASS", "TEST@test.com", false);
        userEto = new UserEto(1L, "NAME1", "SURNAME1", accountEto, roleEto);

        bookingEto = new BookingEto(1L, "Film dla TestCompany", "Film produktowy z dojazdem", serviceEto, addressEto, userEto, false, 900D,
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),0)),
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),1)),
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),0)),
                priceIndicatorEtoList);

        PriceIndicatorEto priceIndicatorEto = new PriceIndicatorEto(indicatorEto, bookingEto.getId(), 400, 10);
        priceIndicatorEtoList = new ArrayList<>();
        priceIndicatorEtoList.add(priceIndicatorEto);

        addressTo = new AddressTo("Wrocław", "Wróblewskiego", "27", null, "51-627");

        List<Long> indicatorsIds = List.of(1L);
        serviceTo = new ServiceTo("Film produktowy", "Film produktow na bialym tle i odpowiednim oswietleniu", 500D, "pl", indicatorsIds);
        indicatorTo = new IndicatorTo("Podróż służbowa", "Paliwo, amortyzacja", "pl", 20, 30);

        PriceIndicatorTo priceIndicatorTo = new PriceIndicatorTo(1L, 1L, 20, 0);
        priceIndicatorToList = new ArrayList<>();
        priceIndicatorToList.add(priceIndicatorTo);
        bookingTo = new BookingTo("Film dla TestCompany", "Film produktowy z dojazdem", 1L, 1L, addressTo,
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),0)),
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),1)),
                priceIndicatorToList);
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
        cities.add("Czestochowa");
        when(serviceOrdering.findAllCities()).thenReturn(Optional.of(cities));

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
    @DisplayName("GET /service/v1/address/cities - No content")
    void testGetAllCitiesNoContent() throws Exception {
        //Arrange
        when(serviceOrdering.findAllCities()).thenReturn(Optional.of(new ArrayList<>()));

        //Act
        mockMvc.perform(get(GET_ALL_CITIES_URL))

                //Assert
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /service/v1/address/streets - Found")
    void testGetAllStreetsFound() throws Exception {
        //Arrange
        ArrayList<String> streets = new ArrayList<>();
        streets.add("Kosciuszki");
        streets.add("Wroblewskiego");
        when(serviceOrdering.findAllStreets()).thenReturn(Optional.of(streets));

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
    @DisplayName("GET /service/v1/address/streets - No content")
    void testGetAllStreetsNoContent() throws Exception {
        //Arrange
        when(serviceOrdering.findAllStreets()).thenReturn(Optional.of(new ArrayList<>()));

        //Act
        mockMvc.perform(get(GET_ALL_STREETS_URL))

                //Assert
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /service/v1/bookings - Found")
    void testGetAllBookingsFound() throws Exception {
        //Arrange
        ArrayList<BookingEto> bookings = new ArrayList<>();
        bookings.add(bookingEto);
        when(serviceOrdering.findAllBookings()).thenReturn(Optional.of(bookings));

        //Act
        MvcResult result = mockMvc.perform(get(GET_ALL_BOOKINGS_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), BookingEto[].class))
                .isEqualTo(bookings.toArray());
    }

    @Test
    @DisplayName("GET /service/v1/bookings - No content")
    void testGetAllBookingsNoContent() throws Exception {
        //Arrange
        when(serviceOrdering.findAllBookings()).thenReturn(Optional.of(new ArrayList<>()));

        //Act
        mockMvc.perform(get(GET_ALL_BOOKINGS_URL))

                //Assert
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /service/v1/services - Found")
    void testGetAllServicesFound() throws Exception {
        //Arrange
        ArrayList<ServiceEto> services = new ArrayList<>();
        services.add(serviceEto);
        when(serviceOrdering.findAllServices()).thenReturn(Optional.of(services));

        //Act
        MvcResult result = mockMvc.perform(get(GET_ALL_SERVICES_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), ServiceEto[].class))
                .isEqualTo(services.toArray());
    }

    @Test
    @DisplayName("GET /service/v1/services - No content")
    void testGetAllServicesNoContent() throws Exception {
        //Arrange
        when(serviceOrdering.findAllServices()).thenReturn(Optional.of(new ArrayList<>()));

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
        when(serviceOrdering.findAllIndicators()).thenReturn(Optional.of(indicators));

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
        when(serviceOrdering.findAllIndicators()).thenReturn(Optional.of(new ArrayList<>()));

        //Act
        mockMvc.perform(get(GET_ALL_INDICATORS_URL))

                //Assert
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /service/v1/booking/1 - Found")
    void testGetBookingByIdFound() throws Exception {
        //Arrange
        when(serviceOrdering.findBooking(bookingEto.getId())).thenReturn(Optional.of(bookingEto));

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
    @DisplayName("GET /service/v1/booking/1 - Not Found")
    void testGetBookingByIdNotFound() throws Exception {
        //Arrange
        when(serviceOrdering.findBooking(bookingEto.getId()))
                .thenThrow(EntityDoesNotExistException.class);

        //Act
        mockMvc.perform(get(GET_BOOKING_BY_ID_URL, bookingEto.getId()))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /service/v1/booking/1 - ISE")
    void testGetBookingByIdISE() throws Exception {
        //Arrange
        when(serviceOrdering.findBooking(bookingEto.getId())).thenReturn(Optional.empty());


        //Act
        mockMvc.perform(get(GET_BOOKING_BY_ID_URL, bookingEto.getId()))

                //Assert
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("DELETE /service/v1/indicator/ - OK")
    void testDeleteIndicatorOk() throws Exception {

        //Act
        mockMvc.perform(delete(INDICATOR_ID_URL, indicatorEto.getId()))

                //Assert
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /service/v1/indicator/1 - Not Found")
    void testDeleteIndicatorNotFound() throws Exception {
        //Arrange
        doThrow(EntityDoesNotExistException.class).when(serviceOrdering).deleteIndicator(indicatorEto.getId());

        //Act
        mockMvc.perform(delete(INDICATOR_ID_URL, indicatorEto.getId()))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /service/v1/indicator/1 - Unprocessable Entity")
    void testDeleteIndicatorUnprocessableEntity() throws Exception {
        //Arrange
        doThrow(EntityHasAssignedEntitiesException.class).when(serviceOrdering).deleteIndicator(indicatorEto.getId());

        //Act
        mockMvc.perform(delete(INDICATOR_ID_URL, indicatorEto.getId()))

                //Assert
                .andExpect(status().isUnprocessableEntity());
    }


    @Test
    @DisplayName("DELETE /service/v1/service/1 - OK")
    void testDeleteServiceOk() throws Exception {
        //Act
        mockMvc.perform(delete(SERVICE_ID_URL, serviceEto.getId()))

                //Assert
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /service/v1/role/1 - Unprocessable Entity")
    void testDeleteServiceUnprocessableEntity() throws Exception {
        //Arrange
        doThrow(EntityHasAssignedEntitiesException.class).when(serviceOrdering).deleteService(serviceEto.getId());

        //Act
        mockMvc.perform(delete(SERVICE_ID_URL, serviceEto.getId()))

                //Assert
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("DELETE /service/v1/role/1 - Not Found")
    void testDeleteServiceNotFound() throws Exception {
        //Arrange
        doThrow(EntityDoesNotExistException.class).when(serviceOrdering).deleteService(serviceEto.getId());

        //Act
        mockMvc.perform(delete(SERVICE_ID_URL, serviceEto.getId()))

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
        //Arrange
        doThrow(EntityDoesNotExistException.class).when(serviceOrdering).deleteBooking(serviceEto.getId());

        //Act
        mockMvc.perform(delete(BOOKING_ID_URL, bookingEto.getId()))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /service/v1/indicator - Created")
    void testCreateIndicatorOk() throws Exception {
        //Arrange
        when(serviceOrdering.createIndicator(indicatorTo)).thenReturn(Optional.of(indicatorEto));

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
        //Arrange
        when(serviceOrdering.createIndicator(indicatorTo)).thenThrow(EntityAlreadyExistsException.class);

        //Act
        mockMvc.perform(post(INDICATOR_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(indicatorTo)))

                //Assert
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @DisplayName("POST /service/v1/indicator - ISE")
    void testCreateIndicatorISE() throws Exception {
        //Arrange
        when(serviceOrdering.createIndicator(indicatorTo)).thenReturn(Optional.empty());

        //Act
        mockMvc.perform(post(INDICATOR_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(indicatorTo)))

                //Assert
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    @DisplayName("POST /service/v1/service - Created")
    void testCreateServiceOk() throws Exception {
        //Arrange
        when(serviceOrdering.createService(serviceTo)).thenReturn(Optional.of(serviceEto));

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
        //Arrange
        when(serviceOrdering.createService(serviceTo)).thenThrow(EntityAlreadyExistsException.class);

        //Act
        mockMvc.perform(post(SERVICE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(serviceTo)))

                //Assert
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }


    @Test
    @DisplayName("POST /service/v1/service - ISE")
    void testCreateServiceISE() throws Exception {
        //Arrange
        when(serviceOrdering.createService(serviceTo)).thenReturn(Optional.empty());

        //Act
        mockMvc.perform(post(SERVICE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(serviceTo)))

                //Assert
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    @DisplayName("POST /service/v1/booking - Created")
    void testCreateBookingOk() throws Exception {
        //Arrange
        bookingTo.setUserId(1L);
        when(serviceOrdering.createBooking(bookingTo)).thenReturn(Optional.of(bookingEto));

        //Act
        MvcResult result = mockMvc.perform(post(BOOKING_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(bookingTo)))

                //Assert
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), BookingEto.class))
                .isEqualToComparingFieldByField(bookingEto);
    }

    @Test
    @DisplayName("POST /service/v1/booking - Not Found")
    void testCreateBookingNotFound() throws Exception {
        //Arrange
        bookingTo.setUserId(1L);
        when(serviceOrdering.createBooking(bookingTo)).thenThrow(EntityDoesNotExistException.class);

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
        when(serviceOrdering.createBooking(bookingTo)).thenThrow(EntityAlreadyExistsException.class);

        //Act
        mockMvc.perform(post(BOOKING_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(bookingTo)))

                //Assert
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @DisplayName("POST /service/v1/booking - ISE")
    void testCreateBookingISE() throws Exception {
        //Arrange
        bookingTo.setUserId(1L);
        when(serviceOrdering.createBooking(bookingTo)).thenReturn(Optional.empty());

        //Act
        mockMvc.perform(post(BOOKING_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(bookingTo)))

                //Assert
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /service/v1/indicator/1 - OK")
    void testUpdateIndicatorOk() throws Exception {
        //Arrange
        when(serviceOrdering.updateIndicator(indicatorTo, indicatorEto.getId())).thenReturn(Optional.of(indicatorEto));

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
        //Arrange
        when(serviceOrdering.updateIndicator(indicatorTo, indicatorEto.getId())).thenThrow(EntityDoesNotExistException.class);

        //Act
        mockMvc.perform(put(INDICATOR_ID_URL, indicatorEto.getId())
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
        when(serviceOrdering.updateIndicator(indicatorTo, indicatorEto.getId())).thenThrow(EntityAlreadyExistsException.class);

        //Act
        mockMvc.perform(put(INDICATOR_ID_URL, indicatorEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(indicatorTo)))

                //Assert
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /service/v1/indicator/1 - ISE")
    void testUpdateIndicatorISE() throws Exception {
        //Arrange
        when(serviceOrdering.updateIndicator(indicatorTo, indicatorEto.getId())).thenReturn(Optional.empty());

        //Act
        mockMvc.perform(put(INDICATOR_ID_URL, indicatorEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(indicatorTo)))

                //Assert
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /service/v1/service/1 - OK")
    void testUpdateServiceOk() throws Exception {
        //Arrange
        when(serviceOrdering.updateService(serviceTo, serviceEto.getId())).thenReturn(Optional.of(serviceEto));

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
        //Arrange
        when(serviceOrdering.updateService(serviceTo, serviceEto.getId())).thenThrow(EntityDoesNotExistException.class);

        //Act
        mockMvc.perform(put(SERVICE_ID_URL, serviceEto.getId())
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
        when(serviceOrdering.updateService(serviceTo, serviceEto.getId())).thenThrow(EntityAlreadyExistsException.class);

        //Act
        mockMvc.perform(put(SERVICE_ID_URL, serviceEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(serviceTo)))

                //Assert
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /service/v1/service/1 - ISE")
    void testUpdateServiceISE() throws Exception {
        //Arrange
        when(serviceOrdering.updateService(serviceTo, serviceEto.getId())).thenReturn(Optional.empty());

        //Act
        mockMvc.perform(put(SERVICE_ID_URL, serviceEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(serviceTo)))

                //Assert
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /service/v1/booking/1 - OK")
    void testUpdateBookingOk() throws Exception {
        //Arrange
        bookingTo.setUserId(1L);
        when(serviceOrdering.updateBooking(bookingTo, bookingEto.getId())).thenReturn(Optional.of(bookingEto));

        //Act
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
        //Arrange
        bookingTo.setUserId(1L);
        when(serviceOrdering.updateBooking(bookingTo, bookingEto.getId())).thenThrow(EntityDoesNotExistException.class);

        //Act
        mockMvc.perform(put(BOOKING_ID_URL, bookingEto.getId())
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
        bookingTo.setUserId(1L);
        when(serviceOrdering.updateBooking(bookingTo, bookingEto.getId())).thenThrow(EntityAlreadyExistsException.class);

        //Act
        mockMvc.perform(put(BOOKING_ID_URL, bookingEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(bookingTo)))

                //Assert
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /service/v1/booking/1 - ISE")
    void testUpdateBookingISE() throws Exception {
        //Arrange
        bookingTo.setUserId(1L);
        when(serviceOrdering.updateBooking(bookingTo, bookingEto.getId())).thenReturn(Optional.empty());

        //Act
        mockMvc.perform(put(BOOKING_ID_URL, bookingEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(bookingTo)))

                //Assert
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    @DisplayName("POST /service/v1/service/calculate - OK")
    void testCalculateServiceOk() throws Exception {
        //Arrange
        calculateCto = new CalculateCto(serviceEto, 900D,
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),0)),
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),1)),
                priceIndicatorEtoList);

        calculateTo = new CalculateTo(1L,
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),0)),
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),1)),
                priceIndicatorToList);

        when(serviceOrdering.calculateService(calculateTo)).thenReturn(Optional.of(calculateCto));

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
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),0)),
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),1)),
                priceIndicatorToList);

        when(serviceOrdering.calculateService(calculateTo)).thenThrow(UnprocessableEntityException.class);

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
        calculateTo = new CalculateTo(1L,
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),0)),
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),1)),
                priceIndicatorToList);

        when(serviceOrdering.calculateService(calculateTo)).thenThrow(EntityDoesNotExistException.class);

        //Act
        mockMvc.perform(post(CALCULATE_SERVICE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(calculateTo)))

                //Assert
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("POST /service/v1/service/calculate  - ISE")
    void testCalculateServiceISE() throws Exception {
        //Arrange
        calculateTo = new CalculateTo(1L,
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),0)),
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),1)),
                priceIndicatorToList);

        when(serviceOrdering.calculateService(calculateTo)).thenReturn(Optional.empty());

        //Act
        mockMvc.perform(post(CALCULATE_SERVICE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(calculateTo)))

                //Assert
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /service/v1/booking/1/confirm - OK")
    void testConfirmBookingOk() throws Exception {
        //Arrange
        bookingEtoWithOrderNumber = new BookingEtoWithOrderNumber(1L, "Film dla TestCompany", "Film produktowy z dojazdem", serviceEto, addressEto, userEto, false, 900D,
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),0)),
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),1)),
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( getCurrentDate(LocalDate.now(),0)),
                priceIndicatorEtoList, "INVIU_00001");

        PriceIndicatorEto priceIndicatorEto = new PriceIndicatorEto(indicatorEto, bookingEto.getId(), 400, 10);
        priceIndicatorEtoList = new ArrayList<>();
        priceIndicatorEtoList.add(priceIndicatorEto);


        when(serviceOrdering.confirmBooking(bookingEto.getId(), userEto.getId())).thenReturn(Optional.of(bookingEtoWithOrderNumber));

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
        //Arrange
        when(serviceOrdering.confirmBooking(bookingEto.getId(), userEto.getId())).thenThrow(EntityDoesNotExistException.class);

        //Act
        mockMvc.perform(put(CONFIRM_BOOKING, bookingEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(1L)))

                //Assert
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
