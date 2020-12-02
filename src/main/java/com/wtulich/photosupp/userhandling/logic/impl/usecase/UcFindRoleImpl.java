package com.wtulich.photosupp.userhandling.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.PermissionDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.RoleDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.RoleEntity;
import com.wtulich.photosupp.userhandling.logic.api.mapper.PermissionsMapper;
import com.wtulich.photosupp.userhandling.logic.api.mapper.RoleMapper;
import com.wtulich.photosupp.userhandling.logic.api.to.PermissionEto;
import com.wtulich.photosupp.userhandling.logic.api.to.RoleEto;
import com.wtulich.photosupp.userhandling.logic.api.usecase.UcFindRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Validated
@Named
public class UcFindRoleImpl implements UcFindRole {

    private static final Logger LOG = LoggerFactory.getLogger(UcFindRoleImpl.class);
    private static final String ID_CANNOT_BE_NULL = "id cannot be a null value";
    private static final String GET_ROLE_LOG = "Get Role with id {} from database.";
    private static final String GET_ALL_ROLES_LOG = "Get all Roles from database.";

    @Inject
    private RoleDao roleDao;

    @Inject
    private PermissionDao permissionDao;

    @Inject
    private RoleMapper roleMapper;

    @Inject
    private PermissionsMapper permissionsMapper;

    @Override
    public Optional<RoleEto> findRole(Long id) throws EntityDoesNotExistException {

        Objects.requireNonNull(id, ID_CANNOT_BE_NULL);

        LOG.debug(GET_ROLE_LOG, id);
        Optional<RoleEntity> roleEntity = roleDao.findById(id);

        if(!roleEntity.isPresent()){
            throw new EntityDoesNotExistException("Role with id " + id + " does not exist.");
        }
        return Optional.of(toRoleEto(roleEntity.get()));
    }

    @Override
    public Optional<List<RoleEto>>  findAllRoles() {
        LOG.debug(GET_ALL_ROLES_LOG);

        return Optional.of(roleDao.findAll().stream()
                .map(roleEntity -> toRoleEto(roleEntity))
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<List<PermissionEto>> findAllPermissions() {
        return Optional.of(permissionDao.findAll().stream()
                .map(permissionEntity -> permissionsMapper.toPermissionEto(permissionEntity))
                .collect(Collectors.toList()));
    }

    private RoleEto toRoleEto(RoleEntity roleEntity){
        RoleEto roleEto = roleMapper.toRoleEto(roleEntity);
        roleEto.setPermissionEtoList(roleEntity.getPermissions().stream()
                .map(p -> permissionsMapper.toPermissionEto(p))
                .collect(Collectors.toList()));
        return roleEto;
    }
}
