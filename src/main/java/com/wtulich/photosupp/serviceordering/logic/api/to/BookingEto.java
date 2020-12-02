package com.wtulich.photosupp.serviceordering.logic.api.to;

import com.sun.istack.NotNull;
import com.wtulich.photosupp.general.dataaccess.api.entity.AbstractApplicationEntityTransportObject;
import com.wtulich.photosupp.userhandling.logic.api.to.UserEto;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class BookingEto extends AbstractApplicationEntityTransportObject {

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

    public BookingEto() {
    }

    public BookingEto(Long id, String name, String description, ServiceEto serviceEto, AddressEto addressEto,
                      UserEto userEto, boolean isConfirmed, Double predictedPrice, String start, String end,
                      String modificationDate, List<PriceIndicatorEto> priceIndicatorEtoList) {
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

    public UserEto getUserEto() {
        return userEto;
    }

    public void setUserEto(UserEto userEto) {
        this.userEto = userEto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingEto)) return false;
        if (!super.equals(o)) return false;
        BookingEto that = (BookingEto) o;
        return isConfirmed == that.isConfirmed &&
                name.equals(that.name) &&
                Objects.equals(description, that.description) &&
                serviceEto.equals(that.serviceEto) &&
                Objects.equals(addressEto, that.addressEto) &&
                Objects.equals(predictedPrice, that.predictedPrice) &&
                start.equals(that.start) &&
                end.equals(that.end) &&
                modificationDate.equals(that.modificationDate) &&
                Objects.equals(priceIndicatorEtoList, that.priceIndicatorEtoList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, description, serviceEto, addressEto, isConfirmed, predictedPrice, start, end, modificationDate, priceIndicatorEtoList);
    }
}
