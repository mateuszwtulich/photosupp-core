package com.wtulich.photosupp.userhandling.dataaccess.api.entity;

import com.sun.istack.NotNull;
import com.wtulich.photosupp.general.dataaccess.api.entity.AbstractApplicationPersistenceEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "ROLE")
public class RoleEntity extends AbstractApplicationPersistenceEntity {
    private static final long serialVersionUID = 1L;

    public RoleEntity() {
    }

    public RoleEntity(String name, String description, List<PermissionEntity> permissions) {
        this.name = name;
        this.description = description;
        this.permissions = permissions;
    }

    @NotNull
    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToMany(fetch = EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "ROLE_PERMISSIONS",
            inverseJoinColumns = {@JoinColumn(
                    name = "PERMISSION_ID", nullable = false, updatable = false
            )},
            joinColumns = {@JoinColumn(
                    name = "ROLE_ID", nullable = false, updatable = false
            )}
    )
    private List<PermissionEntity> permissions = new ArrayList<>();

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

    public List<PermissionEntity> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionEntity> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleEntity)) return false;
        RoleEntity that = (RoleEntity) o;
        return name.equals(that.name) &&
                Objects.equals(description, that.description) &&
                permissions.equals(that.permissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, permissions);
    }
}
