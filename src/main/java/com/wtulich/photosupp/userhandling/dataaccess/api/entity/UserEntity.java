package com.wtulich.photosupp.userhandling.dataaccess.api.entity;

import com.sun.istack.NotNull;
import com.wtulich.photosupp.general.dataaccess.api.entity.AbstractApplicationPersistenceEntity;
import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.OrderEntity;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.BookingEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "PHOTOSUPP_USER")
public class UserEntity extends AbstractApplicationPersistenceEntity {
    private static final long serialVersionUID = 1L;

    public UserEntity() {
    }

    public UserEntity(String name, String surname, RoleEntity role, AccountEntity account) {
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.account = account;
    }

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotNull
    @Column(name = "SURNAME", nullable = false)
    private String surname;

    @NotNull
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "ROLE_ID", nullable = false, referencedColumnName = "id")
    private RoleEntity role;

    @NotNull
    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "ACCOUNT_ID", nullable = false, referencedColumnName = "id", unique = true)
    private AccountEntity account;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, targetEntity = OrderEntity.class, orphanRemoval = true)
    private List<OrderEntity> orderList;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, targetEntity = BookingEntity.class, orphanRemoval = true)
    private List<BookingEntity> bookingList;


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

    public RoleEntity getRole() {
        return role;
    }

    public void setRole(RoleEntity role) {
        this.role = role;
    }

    public AccountEntity getAccount() {
        return account;
    }

    public void setAccount(AccountEntity account) {
        this.account = account;
    }

    public List<OrderEntity> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderEntity> orderList) {
        this.orderList = orderList;
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
        if (!(o instanceof UserEntity)) return false;
        UserEntity that = (UserEntity) o;
        return name.equals(that.name) &&
                role.equals(that.role) &&
                surname.equals(that.surname) &&
                account.equals(that.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, role, surname, account);
    }
}
