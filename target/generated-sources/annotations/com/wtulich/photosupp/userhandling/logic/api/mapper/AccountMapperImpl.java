package com.wtulich.photosupp.userhandling.logic.api.mapper;

import com.wtulich.photosupp.userhandling.dataaccess.api.entity.AccountEntity;
import com.wtulich.photosupp.userhandling.logic.api.to.AccountEto;
import com.wtulich.photosupp.userhandling.logic.api.to.AccountTo;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-12-02T17:23:02+0100",
    comments = "version: 1.4.0.Final, compiler: javac, environment: Java 1.8.0_271 (Oracle Corporation)"
)
@Component
public class AccountMapperImpl implements AccountMapper {

    @Override
    public AccountEntity toAccountEntity(AccountTo accountTo) {
        if ( accountTo == null ) {
            return null;
        }

        AccountEntity accountEntity = new AccountEntity();

        accountEntity.setPassword( accountTo.getPassword() );
        accountEntity.setEmail( accountTo.getEmail() );

        return accountEntity;
    }

    @Override
    public AccountEto toAccountEto(AccountEntity accountEntity) {
        if ( accountEntity == null ) {
            return null;
        }

        AccountEto accountEto = new AccountEto();

        accountEto.setId( accountEntity.getId() );
        accountEto.setUsername( accountEntity.getUsername() );
        accountEto.setPassword( accountEntity.getPassword() );
        accountEto.setEmail( accountEntity.getEmail() );
        accountEto.setActivated( accountEntity.isActivated() );

        return accountEto;
    }
}
