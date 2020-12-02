package com.wtulich.photosupp.userhandling.dataaccess.api.entity;

import com.sun.istack.NotNull;
import com.wtulich.photosupp.general.dataaccess.api.entity.AbstractApplicationPersistenceEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "VERIFICATION_TOKEN")
public class VerificationTokenEntity extends AbstractApplicationPersistenceEntity {
    private static final long serialVersionUID = 1L;
    private static final int EXPIRATION = 60 * 24;

    public VerificationTokenEntity() {
    }

    public VerificationTokenEntity(String token, AccountEntity account) {
        this.token = token;
        this.account = account;
    }

    @NotNull
    @Column(name = "TOKEN", nullable = false)
    private String token;

    @NotNull
    @OneToOne(targetEntity = AccountEntity.class, fetch = FetchType.EAGER, cascade = CascadeType.MERGE, orphanRemoval = true)
    @JoinColumn(name = "ACCOUNT_ID", nullable = false)
    private AccountEntity account;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AccountEntity getAccount() {
        return account;
    }

    public void setAccount(AccountEntity account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VerificationTokenEntity)) return false;
        VerificationTokenEntity that = (VerificationTokenEntity) o;
        return token.equals(that.token) &&
                account.equals(that.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
