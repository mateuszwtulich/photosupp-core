package com.wtulich.photosupp.userhandling.logic.api.mapper;

import com.wtulich.photosupp.userhandling.dataaccess.api.entity.RoleEntity;
import com.wtulich.photosupp.userhandling.logic.api.to.RoleEto;
import com.wtulich.photosupp.userhandling.logic.api.to.RoleTo;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-12-02T17:23:02+0100",
    comments = "version: 1.4.0.Final, compiler: javac, environment: Java 1.8.0_271 (Oracle Corporation)"
)
@Component
public class RoleMapperImpl implements RoleMapper {

    @Override
    public RoleEntity toRoleEntity(RoleTo roleTo) {
        if ( roleTo == null ) {
            return null;
        }

        RoleEntity roleEntity = new RoleEntity();

        roleEntity.setName( roleTo.getName() );
        roleEntity.setDescription( roleTo.getDescription() );

        return roleEntity;
    }

    @Override
    public RoleEto toRoleEto(RoleEntity roleEntity) {
        if ( roleEntity == null ) {
            return null;
        }

        RoleEto roleEto = new RoleEto();

        roleEto.setId( roleEntity.getId() );
        roleEto.setName( roleEntity.getName() );
        roleEto.setDescription( roleEntity.getDescription() );

        return roleEto;
    }
}
