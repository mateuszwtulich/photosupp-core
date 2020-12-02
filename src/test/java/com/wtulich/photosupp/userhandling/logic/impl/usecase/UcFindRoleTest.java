package com.wtulich.photosupp.userhandling.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.RoleDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.PermissionEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.RoleEntity;
import com.wtulich.photosupp.userhandling.logic.api.mapper.PermissionsMapper;
import com.wtulich.photosupp.userhandling.logic.api.mapper.RoleMapper;
import com.wtulich.photosupp.userhandling.logic.api.to.RoleEto;
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
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class UcFindRoleTest {

    @Autowired
    private UcFindRoleImpl ucFindRole;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionsMapper permissionsMapper;

    @MockBean
    private RoleDao roleDao;

    private RoleEntity roleEntity;
    private List<RoleEntity> roleEntities;
    private List<RoleEto> roleEtoList;
    private List<PermissionEntity> permissionEntities;
    private RoleEto roleEto;

    @BeforeEach
    void setUp() {
        PermissionEntity permissionEntity = new PermissionEntity(ApplicationPermissions.A_CRUD_SUPER, "DESC");
        permissionEntity.setId(1L);
        permissionEntities = new ArrayList<>();
        permissionEntities.add(permissionEntity);
        roleEntity = new RoleEntity("ADMIN", "DESC1", permissionEntities);
        roleEntity.setId(1L);

        roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);

        roleEtoList = new ArrayList<>();
        roleEto = roleMapper.toRoleEto(roleEntity);
        roleEto.setPermissionEtoList(permissionEntities.stream().map(p ->
                permissionsMapper.toPermissionEto(p)).collect(Collectors.toList()));
        roleEtoList.add(roleEto);
    }

    @Test
    @DisplayName("Test findAllRoles Success")
    void testFindAllRolesSuccess() {
        //Arrange
        when(roleDao.findAll()).thenReturn(roleEntities);

        //Act
        Optional<List<RoleEto>> result = ucFindRole.findAllRoles();

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(roleEtoList.get(0)).isEqualToComparingFieldByField(result.get().get(0));
    }

    @Test
    @DisplayName("Test findAllRoles No content")
    void testFindAllRolesNoContent() {
        //Arrange
        when(roleDao.findAll()).thenReturn(new ArrayList<>());

        //Act
        Optional<List<RoleEto>> result = ucFindRole.findAllRoles();

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertTrue(result.get().size() == 0);
    }

    @Test
    @DisplayName("Test findRoleById Success")
    void testFindRoleByIdSuccess() throws EntityDoesNotExistException {
        //Arrange
        when(roleDao.findById(roleEntity.getId())).thenReturn(Optional.of(roleEntity));

        //Act
        Optional<RoleEto> result = ucFindRole.findRole(roleEntity.getId());

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(roleEto).isEqualToComparingFieldByField(result.get());
    }

    @Test
    @DisplayName("Test findRoleById Failure")
    void testFindRoleByIdFailure() {
        //Arrange
        when(roleDao.findById(roleEntity.getId())).thenReturn(Optional.ofNullable(null));

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucFindRole.findRole(roleEntity.getId()));
    }
}
