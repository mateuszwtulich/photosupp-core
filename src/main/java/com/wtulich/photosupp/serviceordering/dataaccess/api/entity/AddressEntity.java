package com.wtulich.photosupp.serviceordering.dataaccess.api.entity;

import com.sun.istack.NotNull;
import com.wtulich.photosupp.general.dataaccess.api.entity.AbstractApplicationPersistenceEntity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ADDRESS")
public class AddressEntity extends AbstractApplicationPersistenceEntity {
    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "CITY", nullable = false)
    private String city;

    @NotNull
    @Column(name = "STREET", nullable = false)
    private String street;

    @NotNull
    @Column(name = "BUILDING_NUMBER", nullable = false)
    private String buildingNumber;

    @Column(name = "APARTMENT_NUMBER")
    private String apartmentNumber;

    @NotNull
    @Column(name = "POSTAL_CODE", nullable = false)
    private String postalCode;

    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY, targetEntity = BookingEntity.class, orphanRemoval = true)
    private List<BookingEntity> bookingList;

    public AddressEntity(String city, String street, String buildingNumber, String apartmentNumber, String postalCode) {
        this.city = city;
        this.street = street;
        this.buildingNumber = buildingNumber;
        this.apartmentNumber = apartmentNumber;
        this.postalCode = postalCode;
    }

    public AddressEntity() {
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

    public List<BookingEntity> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<BookingEntity> bookingList) {
        this.bookingList = bookingList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AddressEntity that = (AddressEntity) o;
        return city.equals(that.city) &&
                street.equals(that.street) &&
                buildingNumber.equals(that.buildingNumber) &&
                Objects.equals(apartmentNumber, that.apartmentNumber) &&
                postalCode.equals(that.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), city, street, buildingNumber, apartmentNumber, postalCode);
    }
}
