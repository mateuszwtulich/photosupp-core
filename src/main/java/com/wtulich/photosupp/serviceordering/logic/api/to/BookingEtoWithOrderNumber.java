package com.wtulich.photosupp.serviceordering.logic.api.to;

import com.sun.istack.NotNull;
import com.wtulich.photosupp.general.dataaccess.api.entity.AbstractApplicationEntityTransportObject;
import com.wtulich.photosupp.userhandling.logic.api.to.UserEto;

import java.util.List;
import java.util.Objects;

public class BookingEtoWithOrderNumber extends AbstractApplicationEntityTransportObject {

    @NotNull
    private String name;

    private String description;

    @NotNull
    private ServiceEto serviceEto;

    private AddressEto addressEto;

    @NotNull
    private UserEto userEto;

    @NotNull
    private boolean isConfirmed;

    private Double predictedPrice;

    @NotNull
    private String start;

    @NotNull
    private String end;

    @NotNull
    private String modificationDate;

    private List<PriceIndicatorEto> priceIndicatorEtoList;

    @NotNull
    private String orderNumber;

    public BookingEtoWithOrderNumber() {
    }

    public BookingEtoWithOrderNumber(Long id, String name, String description, ServiceEto serviceEto, AddressEto addressEto, UserEto userEto, boolean isConfirmed, Double predictedPrice, String start, String end, String modificationDate, List<PriceIndicatorEto> priceIndicatorEtoList, String orderNumber) {
        super(id);
        this.name = name;
        this.description = description;
        this.serviceEto = serviceEto;
        this.addressEto = addressEto;
        this.userEto = userEto;
        this.isConfirmed = isConfirmed;
        this.predictedPrice = predictedPrice;
        this.start = start;
        this.end = end;
        this.modificationDate = modificationDate;
        this.priceIndicatorEtoList = priceIndicatorEtoList;
        this.orderNumber = orderNumber;
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

    public ServiceEto getServiceEto() {
        return serviceEto;
    }

    public void setServiceEto(ServiceEto serviceEto) {
        this.serviceEto = serviceEto;
    }

    public AddressEto getAddressEto() {
        return addressEto;
    }

    public void setAddressEto(AddressEto addressEto) {
        this.addressEto = addressEto;
    }

    public UserEto getUserEto() {
        return userEto;
    }

    public void setUserEto(UserEto userEto) {
        this.userEto = userEto;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public Double getPredictedPrice() {
        return predictedPrice;
    }

    public void setPredictedPrice(Double predictedPrice) {
        this.predictedPrice = predictedPrice;
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

    public String getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(String modificationDate) {
        this.modificationDate = modificationDate;
    }

    public List<PriceIndicatorEto> getPriceIndicatorEtoList() {
        return priceIndicatorEtoList;
    }

    public void setPriceIndicatorEtoList(List<PriceIndicatorEto> priceIndicatorEtoList) {
        this.priceIndicatorEtoList = priceIndicatorEtoList;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BookingEtoWithOrderNumber that = (BookingEtoWithOrderNumber) o;
        return isConfirmed == that.isConfirmed &&
                name.equals(that.name) &&
                Objects.equals(description, that.description) &&
                serviceEto.equals(that.serviceEto) &&
                addressEto.equals(that.addressEto) &&
                userEto.equals(that.userEto) &&
                predictedPrice.equals(that.predictedPrice) &&
                start.equals(that.start) &&
                end.equals(that.end) &&
                modificationDate.equals(that.modificationDate) &&
                priceIndicatorEtoList.equals(that.priceIndicatorEtoList) &&
                orderNumber.equals(that.orderNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, description, serviceEto, addressEto, userEto, isConfirmed, predictedPrice, start, end, modificationDate, priceIndicatorEtoList, orderNumber);
    }
}
