package com.wtulich.photosupp.userhandling.dataaccess.api;

import com.wtulich.photosupp.config.H2JpaConfig;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.RoleDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.PermissionEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith({SpringExtension.class})
@SpringBootTest(classes = {H2JpaConfig.class})
public class RoleDaoTest {

    @Autowired
    private RoleDao roleDao;

    @Test
    void existsByName_TRUE() {
        //Act Assert
        Assertions.assertTrue(roleDao.existsByName("USER"));
    }

    @Test
    void existsByName_FALSE() {
        //Act Assert
        Assertions.assertFalse(roleDao.existsByName("NAME"));
    }

    @Test
    void existsByPermissions_TRUE() {
        //Arrange
        PermissionEntity permissionEntity = new PermissionEntity(ApplicationPermissions.AUTH_USER,
                "Standard user with no special permissions.");
        permissionEntity.setId(6L);

        List<PermissionEntity> permissionEntityList = new ArrayList<>();
        permissionEntityList.add(permissionEntity);

        //Act Assert
        Assertions.assertTrue(roleDao.existsByPermissionsIn(permissionEntityList));
    }

    @Test
    void existsByPermissions_FALSE() {
        //Arrange
        PermissionEntity permissionEntity2 = new PermissionEntity(ApplicationPermissions.A_CRUD_SUPER,
                "User has possibility to use CRUD operations one every functionality.");
        permissionEntity2.setId(0L);

        List<PermissionEntity> permissionEntityList = new ArrayList<>();
        permissionEntityList.add(permissionEntity2);

        //Act Assert
        Assertions.assertFalse(roleDao.existsByPermissionsIn(permissionEntityList));
    }
}
