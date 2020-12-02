package com.wtulich.photosupp.userhandling.logic.api.to;

import com.sun.istack.NotNull;
import com.wtulich.photosupp.general.dataaccess.api.entity.AbstractApplicationEntityTransportObject;

import java.util.List;
import java.util.Objects;

public class RoleEto extends AbstractApplicationEntityTransportObject {

    @NotNull
    private String name;

    private String description;

    @NotNull
    private List<PermissionEto> permissionEtoList;

    public RoleEto() {
        super();
    }

    public RoleEto(Long id, String name, String description, List<PermissionEto> permissionEtoList) {
        super(id);
        this.name = name;
        this.description = description;
        this.permissionEtoList = permissionEtoList;
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

    public List<PermissionEto> getPermissionEtoList() {
        return permissionEtoList;
    }

    public void setPermissionEtoList(List<PermissionEto> permissionEtoList) {
        this.permissionEtoList = permissionEtoList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleEto)) return false;
        if (!super.equals(o)) return false;
        RoleEto roleEto = (RoleEto) o;
        return name.equals(roleEto.name) &&
                Objects.equals(description, roleEto.description) &&
                permissionEtoList.equals(roleEto.permissionEtoList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, description, permissionEtoList);
    }
}
