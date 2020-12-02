package com.wtulich.photosupp.userhandling.logic.api.to;

import com.sun.istack.NotNull;

import java.util.Objects;

public class UserTo {

    public UserTo() {
    }

    public UserTo(String name, String surname, AccountTo accountTo, Long roleId) {
        this.name = name;
        this.surname = surname;
        this.accountTo = accountTo;
        this.roleId = roleId;
    }

    @NotNull
    private String name;

    @NotNull
    private String surname;

    @NotNull
    private AccountTo accountTo;

    @NotNull
    private Long roleId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public AccountTo getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(AccountTo accountTo) {
        this.accountTo = accountTo;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserTo)) return false;
        UserTo userTo = (UserTo) o;
        return name.equals(userTo.name) &&
                surname.equals(userTo.surname) &&
                accountTo.equals(userTo.accountTo) &&
                roleId.equals(userTo.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, accountTo, roleId);
    }
}
