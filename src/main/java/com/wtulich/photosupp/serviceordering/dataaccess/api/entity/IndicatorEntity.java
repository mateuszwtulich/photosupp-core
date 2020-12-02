package com.wtulich.photosupp.serviceordering.dataaccess.api.entity;

import com.sun.istack.NotNull;
import com.wtulich.photosupp.general.dataaccess.api.entity.AbstractApplicationPersistenceEntity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "INDICATOR")
public class IndicatorEntity extends AbstractApplicationPersistenceEntity {
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
    @Column(name = "BASE_AMOUNT", nullable = false)
    private Integer baseAmount;

    @NotNull
    @Column(name = "DOUBLE_PRICE", nullable = false)
    private Integer doublePrice;

    @OneToMany(mappedBy = "indicator", fetch = FetchType.LAZY, targetEntity = PriceIndicatorEntity.class, orphanRemoval = true)
    private List<PriceIndicatorEntity> priceIndicatorEntityList;


    public IndicatorEntity() {
    }

    public IndicatorEntity(String name, String description, String locale, Integer baseAmount, Integer doublePrice) {
        this.name = name;
        this.description = description;
        this.locale = locale;
        this.baseAmount = baseAmount;
        this.doublePrice = doublePrice;
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

    public Integer getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(Integer baseAmount) {
        this.baseAmount = baseAmount;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Integer getDoublePrice() {
        return doublePrice;
    }

    public void setDoublePrice(Integer doublePrice) {
        this.doublePrice = doublePrice;
    }

    public List<PriceIndicatorEntity> getPriceIndicatorEntityList() {
        return priceIndicatorEntityList;
    }

    public void setPriceIndicatorEntityList(List<PriceIndicatorEntity> priceIndicatorEntityList) {
        this.priceIndicatorEntityList = priceIndicatorEntityList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        IndicatorEntity that = (IndicatorEntity) o;
        return name.equals(that.name) &&
                Objects.equals(description, that.description) &&
                locale.equals(that.locale) &&
                baseAmount.equals(that.baseAmount) &&
                doublePrice.equals(that.doublePrice) &&
                Objects.equals(priceIndicatorEntityList, that.priceIndicatorEntityList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, description, locale, baseAmount, doublePrice, priceIndicatorEntityList);
    }
}
