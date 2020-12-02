package com.wtulich.photosupp.userhandling.logic.api.to;

import com.sun.istack.NotNull;

import java.util.List;
import java.util.Objects;

public class RoleTo {

    public RoleTo() {
    }

    public RoleTo(String name, String description, List<Long> permissionIds) {
        this.name = name;
        this.description = description;
        this.permissionIds = permissionIds;
    }

    @NotNull
    private String name;

    private String description;

    @NotNull
    private List<Long> permissionIds;

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

    public List<Long> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<Long> permissionIds) {
        this.permissionIds = permissionIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleTo)) return false;
        RoleTo roleTo = (RoleTo) o;
        return name.equals(roleTo.name) &&
                Objects.equals(description, roleTo.description) &&
                permissionIds.equals(roleTo.permissionIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, permissionIds);
    }
}
