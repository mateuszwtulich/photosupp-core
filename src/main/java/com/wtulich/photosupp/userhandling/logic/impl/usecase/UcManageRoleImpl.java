package com.wtulich.photosupp.userhandling.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.PermissionDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.RoleDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.PermissionEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.RoleEntity;
import com.wtulich.photosupp.userhandling.logic.api.mapper.PermissionsMapper;
import com.wtulich.photosupp.userhandling.logic.api.mapper.RoleMapper;
import com.wtulich.photosupp.userhandling.logic.api.to.RoleEto;
import com.wtulich.photosupp.userhandling.logic.api.to.RoleTo;
import com.wtulich.photosupp.userhandling.logic.api.usecase.UcManageRole;
import com.wtulich.photosupp.userhandling.logic.impl.validator.RoleValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Validated
@Named
public class UcManageRoleImpl implements UcManageRole {

    private static final Logger LOG = LoggerFactory.getLogger(UcManageRoleImpl.class);
    private static final String ID_CANNOT_BE_NULL = "id cannot be a null value";
    private static final String CREATE_ROLE_LOG = "Create Role with name {} in database.";
    private static final String UPDATE_ROLE_LOG = "Update Role with id {} from database.";
    private static final String PERMISSION_DOES_NOT_EXIST = "Permissions do not exist.";

    @Inject
    private RoleDao roleDao;

    @Inject
    private PermissionDao permissionDao;

    @Inject
    private RoleMapper roleMapper;

    @Inject
    private PermissionsMapper permissionsMapper;

    @Inject
    private RoleValidator roleValidator;

    @Override
    public Optional<RoleEto> createRole(RoleTo roleTo) throws EntityAlreadyExistsException, EntityDoesNotExistException {
        roleValidator.verifyIfRoleNameAlreadyExists(roleTo.getName());
        LOG.debug(CREATE_ROLE_LOG, roleTo.getName());

        RoleEntity roleEntity = roleMapper.toRoleEntity(roleTo);
        roleEntity.setPermissions(getPermissionsByIds(roleTo.getPermissionIds()));
        return toRoleEto(roleDao.save(roleEntity));
    }

    @Override
    public Optional<RoleEto> updateRole(RoleTo roleTo, Long id) throws EntityAlreadyExistsException, EntityDoesNotExistException {

        Objects.requireNonNull(id, ID_CANNOT_BE_NULL);

        RoleEntity roleEntity = roleDao.findById(id).orElseThrow(() ->
                new EntityDoesNotExistException("Role with id " + id + " does not exist."));

        if(!roleEntity.getName().equals(roleTo.getName())){
            roleValidator.verifyIfRoleNameAlreadyExists(roleTo.getName());
        }
        LOG.debug(UPDATE_ROLE_LOG, id);

        return toRoleEto(mapRoleEntity(roleEntity, roleTo));
    }

    private RoleEntity mapRoleEntity(RoleEntity roleEntity, RoleTo roleTo) throws EntityDoesNotExistException {
        roleEntity.setName(roleTo.getName());
        roleEntity.setDescription(roleTo.getDescription());
        roleEntity.setPermissions(getPermissionsByIds(roleTo.getPermissionIds()));
        return roleEntity;
    }

    private Optional<RoleEto> toRoleEto(RoleEntity roleEntity){
        RoleEto roleEto = roleMapper.toRoleEto(roleEntity);
        roleEto.setPermissionEtoList(roleEntity.getPermissions().stream()
                .map(p -> permissionsMapper.toPermissionEto(p))
                .collect(Collectors.toList()));
        return Optional.of(roleEto);
    }

    private List<PermissionEntity> getPermissionsByIds(List<Long> permissionIds) throws EntityDoesNotExistException {
         List<PermissionEntity> permissionEntities = permissionDao.findAllById(permissionIds);

         if(permissionEntities.size() == 0){
             throw new EntityDoesNotExistException(PERMISSION_DOES_NOT_EXIST);
         }
         return permissionEntities;
    }
}
