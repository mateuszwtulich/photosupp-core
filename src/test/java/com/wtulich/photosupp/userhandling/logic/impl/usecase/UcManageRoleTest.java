package com.wtulich.photosupp.userhandling.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.PermissionDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.RoleDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.PermissionEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.RoleEntity;
import com.wtulich.photosupp.userhandling.logic.api.mapper.PermissionsMapper;
import com.wtulich.photosupp.userhandling.logic.api.mapper.RoleMapper;
import com.wtulich.photosupp.userhandling.logic.api.to.RoleEto;
import com.wtulich.photosupp.userhandling.logic.api.to.RoleTo;
import com.wtulich.photosupp.userhandling.logic.impl.validator.RoleValidator;
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
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class UcManageRoleTest {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionsMapper permissionsMapper;

    @Autowired
    private UcManageRoleImpl ucManageRole;

    @MockBean
    private RoleDao roleDao;

    @MockBean
    private PermissionDao permissionDao;

    @MockBean
    private RoleValidator roleValidator;

    private RoleEntity roleEntity;
    private List<PermissionEntity> permissionEntities;
    private RoleEto roleEto;
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
        roleEntity.setId(1L);

        roleEto = roleMapper.toRoleEto(roleEntity);
        roleEto.setPermissionEtoList(permissionEntities.stream().map(p ->
                permissionsMapper.toPermissionEto(p)).collect(Collectors.toList()));
    }

    @Test
    @DisplayName("Test createRole Success")
    void testCreateRoleSuccess() throws EntityDoesNotExistException, EntityAlreadyExistsException {
        //Arrange
        when(permissionDao.findAllById(permissionsIds)).thenReturn(permissionEntities);
        when(roleDao.save(roleEntity)).thenReturn(roleEntity);

        //Act
        Optional<RoleEto> result = ucManageRole.createRole(roleTo);

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(roleEto).isEqualToComparingFieldByField(result.get());
    }

    @Test
    @DisplayName("Test createRole EntityAlreadyExistsException")
    void testCreateRoleEntityAlreadyExistsException() throws EntityAlreadyExistsException {
        //Arrange
        doThrow(EntityAlreadyExistsException.class).when(roleValidator).verifyIfRoleNameAlreadyExists(roleTo.getName());

        //Act Assert
        Assertions.assertThrows(EntityAlreadyExistsException.class, () ->
                ucManageRole.createRole(roleTo));
    }

    @Test
    @DisplayName("Test createRole EntityDoesNotExistException")
    void testCreateRoleEntityDoesNotExistException() {
        //Arrange
        when(permissionDao.findAllById(permissionsIds)).thenReturn(new ArrayList<>());
        when(roleDao.save(roleEntity)).thenReturn(roleEntity);

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucManageRole.createRole(roleTo));
    }

    @Test
    @DisplayName("Test updateRole Success")
    void testUpdateRoleSuccess() throws EntityDoesNotExistException, EntityAlreadyExistsException {
        //Arrange
        when(roleDao.findById(roleEntity.getId())).thenReturn(Optional.of(roleEntity));
        when(permissionDao.findAllById(permissionsIds)).thenReturn(permissionEntities);

        //Act
        Optional<RoleEto> result = ucManageRole.updateRole(roleTo, roleEntity.getId());

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(roleEto).isEqualToComparingFieldByField(result.get());
    }

    @Test
    @DisplayName("Test updateRole EntityAlreadyExistsException")
    void testUpdateRoleEntityAlreadyExistsException() throws EntityAlreadyExistsException {
        //Arrange
        roleTo.setName("USER");
        when(roleDao.findById(roleEntity.getId())).thenReturn(Optional.of(roleEntity));
        doThrow(EntityAlreadyExistsException.class).when(roleValidator).verifyIfRoleNameAlreadyExists(roleTo.getName());

        //Act Assert
        Assertions.assertThrows(EntityAlreadyExistsException.class, () ->
                ucManageRole.updateRole(roleTo, roleEntity.getId()));
    }

    @Test
    @DisplayName("Test updateRole EntityDoesNotExistException")
    void testUpdateRoleEntityDoesNotExistException() {
        //Arrange
        when(roleDao.findById(roleEntity.getId())).thenReturn(Optional.ofNullable(null));
        when(permissionDao.findAllById(permissionsIds)).thenReturn(new ArrayList<>());

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucManageRole.updateRole(roleTo, roleEntity.getId()));
    }
}
