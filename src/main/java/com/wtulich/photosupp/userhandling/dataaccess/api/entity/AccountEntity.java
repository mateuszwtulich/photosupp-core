package com.wtulich.photosupp.userhandling.dataaccess.api.entity;

import com.sun.istack.NotNull;
import com.wtulich.photosupp.general.dataaccess.api.entity.AbstractApplicationPersistenceEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "ACCOUNT")
public class AccountEntity extends AbstractApplicationPersistenceEntity {
    private static final long serialVersionUID = 1L;

    public AccountEntity() {
    }

    public AccountEntity(String username, String password, String email, boolean isActivated) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isActivated = isActivated;
    }

    @NotNull
    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    @NotNull
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @NotNull
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(name = "IS_ACTIVATED", nullable = false)
    private boolean isActivated = false;

    @OneToOne(targetEntity = UserEntity.class, mappedBy = "account", fetch = FetchType.LAZY, orphanRemoval = true)
    private UserEntity user;

    @OneToOne(targetEntity = VerificationTokenEntity.class, mappedBy = "account", cascade = CascadeType.MERGE, orphanRemoval = true)
    private VerificationTokenEntity verificationToken;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public VerificationTokenEntity getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(VerificationTokenEntity verificationToken) {
        this.verificationToken = verificationToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountEntity)) return false;
        AccountEntity that = (AccountEntity) o;
        return isActivated == that.isActivated &&
                username.equals(that.username) &&
                password.equals(that.password) &&
                email.equals(that.email) &&
                Objects.equals(user, that.user) &&
                Objects.equals(verificationToken, that.verificationToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, email, isActivated, verificationToken);
    }
}
