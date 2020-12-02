package com.wtulich.photosupp.serviceordering.dataaccess.api.entity;

import com.sun.istack.NotNull;
import com.wtulich.photosupp.general.dataaccess.api.entity.AbstractApplicationPersistenceEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.PermissionEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "SERVICE")
public class ServiceEntity extends AbstractApplicationPersistenceEntity {
    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @NotNull
    @Column(name = "LOCALE")
    private String locale;

    @NotNull
    @Column(name = "BASE_PRICE", nullable = false)
    private Double basePrice;

    @ManyToMany(fetch = LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "SERVICE_INDICATORS",
            inverseJoinColumns = {@JoinColumn(
                    name = "INDICATOR_ID", nullable = false, updatable = false
            )},
            joinColumns = {@JoinColumn(
                    name = "SERVICE_ID", nullable = false, updatable = false
            )}
    )
    private List<IndicatorEntity> indicatorList = new ArrayList<>();

    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY, targetEntity = BookingEntity.class, orphanRemoval = true)
    private List<BookingEntity> bookingList;

    public ServiceEntity() {
    }

    public ServiceEntity(String name, String description, Double basePrice, String locale) {
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        this.locale = locale;
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

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public List<BookingEntity> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<BookingEntity> bookingList) {
        this.bookingList = bookingList;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public List<IndicatorEntity> getIndicatorList() {
        return indicatorList;
    }

    public void setIndicatorList(List<IndicatorEntity> indicatorList) {
        this.indicatorList = indicatorList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ServiceEntity that = (ServiceEntity) o;
        return name.equals(that.name) &&
                Objects.equals(description, that.description) &&
                locale.equals(that.locale) &&
                basePrice.equals(that.basePrice) &&
                indicatorList.equals(that.indicatorList) &&
                Objects.equals(bookingList, that.bookingList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, description, locale, basePrice, indicatorList, bookingList);
    }
}
