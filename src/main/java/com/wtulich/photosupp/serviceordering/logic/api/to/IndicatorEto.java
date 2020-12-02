package com.wtulich.photosupp.serviceordering.logic.api.to;

import com.sun.istack.NotNull;
import com.wtulich.photosupp.general.dataaccess.api.entity.AbstractApplicationEntityTransportObject;

import java.util.Objects;

public class IndicatorEto extends AbstractApplicationEntityTransportObject {

    @NotNull
    private String name;

    private String description;

    @NotNull
    private String locale;

    @NotNull
    private Integer baseAmount;

    @NotNull
    private Integer doublePrice;

    public IndicatorEto() {
    }

    public IndicatorEto(Long id, String name, String description, String locale, Integer baseAmount, Integer doublePrice) {
        super(id);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IndicatorEto)) return false;
        IndicatorEto that = (IndicatorEto) o;
        return  name.equals(that.name) &&
                Objects.equals(description, that.description) &&
                baseAmount.equals(that.baseAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, baseAmount);
    }
}
