package com.wtulich.photosupp.userhandling.logic.api.mapper;

import com.wtulich.photosupp.userhandling.dataaccess.api.entity.PermissionEntity;
import com.wtulich.photosupp.userhandling.logic.api.to.PermissionEto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-11-30T19:18:32+0100",
    comments = "version: 1.4.0.Final, compiler: javac, environment: Java 15 (Oracle Corporation)"
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
