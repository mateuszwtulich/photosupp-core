package com.wtulich.photosupp.serviceordering.logic.api.to;

import com.sun.istack.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class BookingTo {

    @NotNull
    private String name;

    private String description;

    @NotNull
    private Long serviceId;

    @NotNull
    private Long userId;

    private AddressTo addressTo;

    @NotNull
    private String start;

    @NotNull
    private String end;

    private List<PriceIndicatorTo> priceIndicatorToList;

    public BookingTo() {
    }

    public BookingTo(String name, String description, Long serviceId, Long userId, AddressTo addressTo,
                     String start, String end, List<PriceIndicatorTo> priceIndicatorToList) {
        this.name = name;
        this.description = description;
        this.serviceId = serviceId;
        this.addressTo = addressTo;
        this.start = start;
        this.end = end;
        this.priceIndicatorToList = priceIndicatorToList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public AddressTo getAddressTo() {
        return addressTo;
    }

    public void setAddressTo(AddressTo addressTo) {
        this.addressTo = addressTo;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public List<PriceIndicatorTo> getPriceIndicatorToList() {
        return priceIndicatorToList;
    }

    public void setPriceIndicatorToList(List<PriceIndicatorTo> priceIndicatorToList) {
        this.priceIndicatorToList = priceIndicatorToList;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingTo bookingTo = (BookingTo) o;
        return name.equals(bookingTo.name) &&
                Objects.equals(description, bookingTo.description) &&
                serviceId.equals(bookingTo.serviceId) &&
                userId.equals(bookingTo.userId) &&
                addressTo.equals(bookingTo.addressTo) &&
                start.equals(bookingTo.start) &&
                end.equals(bookingTo.end) &&
                priceIndicatorToList.equals(bookingTo.priceIndicatorToList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, serviceId, userId, addressTo, start, end, priceIndicatorToList);
    }
}
