package com.wtulich.photosupp.userhandling.logic.api.to;

import com.sun.istack.NotNull;
import com.wtulich.photosupp.general.dataaccess.api.entity.AbstractApplicationEntityTransportObject;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;

import java.util.Objects;

public class PermissionEto extends AbstractApplicationEntityTransportObject {

    @NotNull
    private ApplicationPermissions name;

    private String description;

    public ApplicationPermissions getName() {
        return name;
    }

    public void setName(ApplicationPermissions name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PermissionEto() {
    }

    public PermissionEto(Long id, ApplicationPermissions name, String description) {
        super(id);
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PermissionEto)) return false;
        if (!super.equals(o)) return false;
        PermissionEto that = (PermissionEto) o;
        return name == that.name &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, description);
    }
}
