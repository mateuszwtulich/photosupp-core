package com.wtulich.photosupp.serviceordering.logic.api.to;

import com.sun.istack.NotNull;

import java.util.Objects;

public class AddressTo {

    @NotNull
    private String city;

    @NotNull
    private String street;

    @NotNull
    private String buildingNumber;

    private String apartmentNumber;

    @NotNull
    private String postalCode;

    public AddressTo() {
    }

    public AddressTo(String city, String street, String buildingNumber, String apartmentNumber, String postalCode) {
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
        if (!(o instanceof AddressTo)) return false;
        AddressTo addressTo = (AddressTo) o;
        return city.equals(addressTo.city) &&
                street.equals(addressTo.street) &&
                buildingNumber.equals(addressTo.buildingNumber) &&
                postalCode.equals(addressTo.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, street, buildingNumber, postalCode);
    }
}
