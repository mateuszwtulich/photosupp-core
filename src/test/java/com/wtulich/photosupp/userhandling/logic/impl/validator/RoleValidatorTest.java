package com.wtulich.photosupp.userhandling.logic.impl.validator;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.PermissionDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.RoleDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.PermissionEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.RoleEntity;
import com.wtulich.photosupp.userhandling.logic.api.to.RoleTo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class RoleValidatorTest {

    @Autowired
    private RoleValidator roleValidator;

    @MockBean
    private RoleDao roleDao;

    @MockBean
    private PermissionDao permissionDao;

    private RoleEntity roleEntity;
    private List<PermissionEntity> permissionEntities;
    private List<Long> permissionsIds;
    private RoleTo roleTo;

    @BeforeEach
    void setUp() {
        PermissionEntity permissionEntity = new PermissionEntity(ApplicationPermissions.A_CRUD_SUPER, "DESC");
        permissionEntity.setId(1L);
        permissionEntities = new ArrayList<>();
        permissionEntities.add(permissionEntity);
        roleEntity = new RoleEntity("ADMIN", "DESC1", permissionEntities);

        permissionsIds = new ArrayList<>();
        permissionsIds.add(1L);
        roleTo = new RoleTo("ADMIN", "DESC1", permissionsIds);
    }

    @Test
    @DisplayName("Test verifyIfRoleAlreadyExists Success")
    void verifyIfRoleAlreadyExistsSuccess() {
        //Arrange
        when(permissionDao.findAllById(permissionsIds)).thenReturn(permissionEntities);
        when(roleDao.existsByName(roleEntity.getName())).thenReturn(true);

        //Act Assert
        Assertions.assertThrows(EntityAlreadyExistsException.class, () ->
                        roleValidator.verifyIfRoleNameAlreadyExists(roleTo.getName()));
    }

    @Test
    @DisplayName("Test verifyIfRoleAlreadyExists Failure")
    void verifyIfRoleAlreadyExistsFailure() {
        //Arrange
        when(permissionDao.findAllById(permissionsIds)).thenReturn(permissionEntities);
        when(roleDao.existsByName(roleEntity.getName())).thenReturn(false);
        when(roleDao.existsByPermissionsIn(permissionEntities)).thenReturn(false);

        //Act Assert
        Assertions.assertDoesNotThrow(() ->  roleValidator.verifyIfRoleNameAlreadyExists(roleTo.getName()));
    }
}
