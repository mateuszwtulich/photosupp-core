package com.wtulich.photosupp.userhandling.logic.api.mapper;

import com.wtulich.photosupp.userhandling.dataaccess.api.entity.UserEntity;
import com.wtulich.photosupp.userhandling.logic.api.to.UserEto;
import com.wtulich.photosupp.userhandling.logic.api.to.UserTo;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-11-30T19:18:31+0100",
    comments = "version: 1.4.0.Final, compiler: javac, environment: Java 15 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserEntity toUserEntity(UserTo userTo) {
        if ( userTo == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setName( userTo.getName() );
        userEntity.setSurname( userTo.getSurname() );

        return userEntity;
    }

    @Override
    public UserEto toUserEto(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        UserEto userEto = new UserEto();

        userEto.setId( userEntity.getId() );
        userEto.setName( userEntity.getName() );
        userEto.setSurname( userEntity.getSurname() );

        return userEto;
    }
}
