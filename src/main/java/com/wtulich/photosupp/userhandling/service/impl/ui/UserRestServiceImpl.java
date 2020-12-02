package com.wtulich.photosupp.userhandling.service.impl.ui;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.userhandling.logic.api.exception.AccountAlreadyExistsException;
import com.wtulich.photosupp.userhandling.logic.api.exception.RoleHasAssignedUsersException;
import com.wtulich.photosupp.userhandling.logic.api.to.*;
import com.wtulich.photosupp.userhandling.logic.impl.UserHandlingImpl;
import com.wtulich.photosupp.userhandling.service.api.ui.UserRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import javax.inject.Inject;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class UserRestServiceImpl implements UserRestService {
    private static String USERS_NOT_EXIST = "Users do not exist.";
    private static String ACCOUNTS_NOT_EXIST = "Accounts do not exist.";
    private static String ROLES_NOT_EXIST = "Roles do not exist.";
    private static final String BASE_URL = "user/v1/";

    @Inject
    private UserHandlingImpl userHandling;

    @Override
    public ResponseEntity<UserEto> getUser(Long id) {
        try {
            return ResponseEntity
                    .ok()
                    .body(userHandling.findUser(id).orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public List<UserEto> getAllUsers() {
        return userHandling.findAllUsers().map( userEtos -> {
            if(userEtos.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, USERS_NOT_EXIST);
            }
            return userEtos;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Override
    public List<AccountEto> getAllAccounts() {
        return userHandling.findAllAccounts().map( accountEtos -> {
            if(accountEtos.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, ACCOUNTS_NOT_EXIST);
            }
            return accountEtos;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Override
    public List<UserEto> getAllUsersByRoleId(Long roleId) {
        return userHandling.findAllUsersByRoleId(roleId).map( userEtos -> {
            if(userEtos.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Users with role id " + roleId + " do not exist.");
            }
            return userEtos;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Override
    public ResponseEntity<UserEto> createUser(UserTo userTo, HttpServletRequest request, Errors errors) {
        try {
            return ResponseEntity
                    .created(new URI(BASE_URL + "user"))
                    .body(userHandling.createUserAndAccountEntities(userTo, request, errors).orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
        } catch (AccountAlreadyExistsException | AddressException | URISyntaxException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public RedirectView confirmRegistration(String token) {
        try {
            return userHandling.confirmRegistration(token).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        } catch (EntityDoesNotExistException e) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<UserEto> updateUser(Long id, UserTo userTo) {
        try {
            return ResponseEntity
                    .ok()
                    .body(userHandling.updateUser(userTo, id).orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<AccountEto> updateUserAccount(Long userId, AccountTo accountTo) {
        try {
            return ResponseEntity
                    .ok()
                    .body(userHandling.updateUserAccount(accountTo, userId).orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
        } catch (AccountAlreadyExistsException | AddressException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> updateAccountPassword(AccountTo accountTo) {
        try {
            userHandling.updatePassword(accountTo);
            return ResponseEntity.ok().build();
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> deleteUser(Long id) {
        try {
            userHandling.deleteUserAndAllRelatedEntities(id);
            return ResponseEntity.ok().build();
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<RoleEto> getRole(Long id) {
        try {
            return ResponseEntity
                    .ok()
                    .body(userHandling.findRole(id).orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public List<RoleEto> getAllRoles() {
        return userHandling.findAllRoles().map(roleEtos -> {
            if (roleEtos.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, ROLES_NOT_EXIST);
            }
            return roleEtos;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Override
    public List<PermissionEto> getAllPermissions() {
        return userHandling.findAllPermissions().map(permissionEtos -> {
            if (permissionEtos.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, ROLES_NOT_EXIST);
            }
            return permissionEtos;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));    }

    @Override
    public ResponseEntity<RoleEto> createRole(RoleTo roleTo) {
        try {
            return ResponseEntity
                    .created(new URI(BASE_URL + "/role"))
                    .body(userHandling.createRole(roleTo).orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
        } catch (EntityAlreadyExistsException | URISyntaxException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<RoleEto> updateRole(Long id, RoleTo roleTo) {
        try {
            return ResponseEntity
                    .ok()
                    .body(userHandling.updateRole(roleTo, id).orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> deleteRole(Long id) {
        try {
            userHandling.deleteRole(id);
            return ResponseEntity.ok().build();
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (RoleHasAssignedUsersException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }
}
