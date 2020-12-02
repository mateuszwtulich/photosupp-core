package com.wtulich.photosupp.userhandling.logic.api.mapper;

import com.wtulich.photosupp.userhandling.dataaccess.api.entity.PermissionEntity;
import com.wtulich.photosupp.userhandling.logic.api.to.PermissionEto;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-12-02T17:23:02+0100",
    comments = "version: 1.4.0.Final, compiler: javac, environment: Java 1.8.0_271 (Oracle Corporation)"
)
@Component
public class PermissionsMapperImpl implements PermissionsMapper {

    @Override
    public PermissionEto toPermissionEto(PermissionEntity permissionEntity) {
        if ( permissionEntity == null ) {
            return null;
        }

        PermissionEto permissionEto = new PermissionEto();

        permissionEto.setId( permissionEntity.getId() );
        permissionEto.setName( permissionEntity.getName() );
        permissionEto.setDescription( permissionEntity.getDescription() );

        return permissionEto;
    }
}
