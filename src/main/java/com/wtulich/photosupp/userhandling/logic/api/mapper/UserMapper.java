package com.wtulich.photosupp.userhandling.logic.api.mapper;

import com.wtulich.photosupp.userhandling.dataaccess.api.entity.UserEntity;
import com.wtulich.photosupp.userhandling.logic.api.to.UserEto;
import com.wtulich.photosupp.userhandling.logic.api.to.UserTo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toUserEntity(UserTo userTo);

    UserEto toUserEto(UserEntity userEntity);
}
