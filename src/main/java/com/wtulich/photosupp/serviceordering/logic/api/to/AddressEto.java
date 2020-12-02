package com.wtulich.photosupp.serviceordering.logic.api.to;

import com.sun.istack.NotNull;
import com.wtulich.photosupp.general.dataaccess.api.entity.AbstractApplicationEntityTransportObject;

import java.util.Objects;

public class AddressEto extends AbstractApplicationEntityTransportObject {

    @NotNull
    private String city;

    @NotNull
    private String street;

    @NotNull
    private String buildingNumber;

    private String apartmentNumber;

    @NotNull
    private String postalCode;

    public AddressEto() {
    }

    public AddressEto(Long id, String city, String street, String buildingNumber, String apartmentNumber, String postalCode) {
        super(id);
        this.city = city;
        this.street = street;
        this.buildingNumber = buildingNumber;
        this.apartmentNumber = apartmentNumber;
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressEto)) return false;
        AddressEto that = (AddressEto) o;
        return city.equals(that.city) &&
                street.equals(that.street) &&
                buildingNumber.equals(that.buildingNumber) &&
                postalCode.equals(that.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, street, buildingNumber, postalCode);
    }
}
