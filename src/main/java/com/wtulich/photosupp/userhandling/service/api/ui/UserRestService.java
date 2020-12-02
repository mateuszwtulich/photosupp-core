package com.wtulich.photosupp.userhandling.service.api.ui;

import com.wtulich.photosupp.general.common.api.RestService;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import com.wtulich.photosupp.general.utils.annotations.PermissionRestrict;
import com.wtulich.photosupp.userhandling.logic.api.to.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/user/v1")
public interface UserRestService extends RestService {

    @ApiOperation(value = "Get user by id.",
            tags = {"user"},
            response = UserEto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 204, message = "Entity not found"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @GetMapping(value = "/user/{id}",
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.AUTH_USER, ApplicationPermissions.A_CRUD_USERS})
    ResponseEntity<UserEto> getUser(@PathVariable(value = "id") Long id);


    @ApiOperation(value = "Get all users.",
            tags = {"user"},
            response = UserEto.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 204, message = "No content found"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @GetMapping(value = "/users",
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_USERS})
    List<UserEto> getAllUsers();


    @ApiOperation(value = "Get all accounts.",
            tags = {"account"},
            response = AccountEto.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 204, message = "No content found"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @GetMapping(value = "/users/accounts",
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_USERS})
    List<AccountEto> getAllAccounts();


    @ApiOperation(value = "Get all users of role.",
            tags = {"user"},
            response = UserEto.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 204, message = "No content found"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @GetMapping(value = "/users/role/{id}",
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_USERS})
    List<UserEto> getAllUsersByRoleId(@PathVariable(value = "id") Long roleId);


    @ApiOperation(value = "Creates user",
            tags = {"user"},
            response = UserEto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 422, message = "Could not process entity"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @PostMapping(value = "/user",
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserEto> createUser(@Validated @RequestBody UserTo userTo, HttpServletRequest request, Errors errors);


    @ApiOperation(value = "Confirm registration.",
            tags = {"registration", "account"},
            response = RedirectView.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @GetMapping(value = "user/account/registrationConfirm")
    RedirectView confirmRegistration(@Param(value = "token") String token);


    @ApiOperation(value = "Updates user",
            tags = {"user"},
            response = UserEto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 422, message = "Could not process entity"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @PutMapping(value = "/user/{id}",
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_USERS,
                        ApplicationPermissions.AUTH_USER})
    ResponseEntity<UserEto> updateUser(@PathVariable(value = "id") Long id, @Validated @RequestBody  UserTo userTo);


    @ApiOperation(value = "Updates user account",
            tags = {"account"},
            response = AccountEto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 422, message = "Could not process entity"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @PutMapping(value = "/user/{id}/account",
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_USERS,
            ApplicationPermissions.AUTH_USER})
    ResponseEntity<AccountEto> updateUserAccount(@PathVariable(value = "id") Long userId, @Validated @RequestBody AccountTo accountTo);


    @ApiOperation(value = "Updates account password",
            tags = {"account"},
            response = AccountEto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 422, message = "Could not process entity"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @PutMapping(value = "/user/account",
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updateAccountPassword(@Validated @RequestBody AccountTo accountTo);


    @ApiOperation(value = "Deletes User",
            tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @DeleteMapping(value = "/user/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_USERS })
    ResponseEntity<?> deleteUser(@PathVariable(value = "id") Long id);


    @ApiOperation(value = "Get role by id.",
            tags = {"role"},
            response = RoleEto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 204, message = "Entity not found"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @GetMapping(value = "/role/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_ROLES })
    ResponseEntity<RoleEto> getRole(@PathVariable(value = "id") Long id);


    @ApiOperation(value = "Get all roles.",
            tags = {"role"},
            response = RoleEto.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 204, message = "No content found"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @GetMapping(value = "/roles",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_ROLES })
    List<RoleEto> getAllRoles();


    @ApiOperation(value = "Get all permissions.",
            tags = {"role"},
            response = PermissionEto.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 204, message = "No content found"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @GetMapping(value = "/permissions",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_ROLES })
    List<PermissionEto> getAllPermissions();


    @ApiOperation(value = "Creates role",
            tags = {"role"},
            response = RoleEto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 422, message = "Could not process entity"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @PostMapping(value = "/role",
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_ROLES })
    ResponseEntity<RoleEto> createRole(@Validated @RequestBody RoleTo roleTo);


    @ApiOperation(value = "Updates role",
            tags = {"role"},
            response = RoleEto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 422, message = "Could not process entity"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @PutMapping(value = "/role/{id}",
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_ROLES })
    ResponseEntity<RoleEto> updateRole(@PathVariable(value = "id") Long id, @Validated @RequestBody RoleTo roleTo);


    @ApiOperation(value = "Deletes Role",
            tags = {"role"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 401, message = "Unauthorized request"),
            @ApiResponse(code = 403, message = "You dont have permissions for this action!"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 429, message = "Too many requests"),
    })
    @DeleteMapping(value = "/role/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PermissionRestrict(permissions = { ApplicationPermissions.A_CRUD_SUPER, ApplicationPermissions.A_CRUD_ROLES })
    ResponseEntity<?> deleteRole(@PathVariable(value = "id") Long id);
}
