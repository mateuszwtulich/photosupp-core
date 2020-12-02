package com.wtulich.photosupp.userhandling.service.impl.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import com.wtulich.photosupp.userhandling.logic.api.exception.AccountAlreadyExistsException;
import com.wtulich.photosupp.userhandling.logic.api.exception.RoleHasAssignedUsersException;
import com.wtulich.photosupp.userhandling.logic.api.to.*;
import com.wtulich.photosupp.userhandling.logic.impl.UserHandlingImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.view.RedirectView;

import javax.mail.internet.AddressException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserRestServiceTest {
    private static String GET_USER_BY_ID_URL = "/user/v1/user/{id}";
    private static String GET_ALL_USERS_URL = "/user/v1/users";
    private static String GET_USERS_BY_ROLE_URL = "/user/v1/users/role/{id}";
    private static String GET_ROLE_BY_ID_URL = "/user/v1/role/{id}";
    private static String GET_ALL_ROLES_URL = "/user/v1/roles";
    private static String GET_ALL_ACCOUNTS_URL = "/user/v1/users/accounts";
    private static String ROLE_ID_URL = "/user/v1/role/{id}";
    private static String USER_ID_URL = "/user/v1/user/{id}";
    private static String ROLE_URL = "/user/v1/role";
    private static String USER_URL = "/user/v1/user";
    private static String ACCOUNT_REGISTRATION_URL = "/user/v1/user/account/registrationConfirm";

    @MockBean
    private UserHandlingImpl userHandling;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private RoleEto roleEto;
    private AccountEto accountEto;
    private UserEto userEto;
    private List<PermissionEto> permissionEtoList;
    private RoleTo roleTo;
    private AccountTo accountTo;
    private UserTo userTo;
    private List<Long> permissionsIds;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        permissionEtoList = new ArrayList<>();
        permissionEtoList.add(new PermissionEto(1L, ApplicationPermissions.A_CRUD_SUPER, "DESC1"));
        roleEto = new RoleEto(1L, "ADMIN", "DESC1", permissionEtoList);
        accountEto = new AccountEto(1L, "TEST", "PASS", "TEST@test.com", false);
        userEto = new UserEto(1L, "NAME1", "SURNAME1", accountEto, roleEto);

        permissionsIds = new ArrayList<>();
        permissionsIds.add(1L);
        roleTo = new RoleTo("ADMIN", "DESC1", permissionsIds);
        accountTo = new AccountTo("PASS", "TEST@test.com");
        userTo = new UserTo("NAME1", "SURNAME1", accountTo, 1L);
    }

    @Test
    @DisplayName("GET /user/v1/user/1 - Found")
    void testGetUserByIdFound() throws Exception {
        //Arrange
        when(userHandling.findUser(userEto.getId())).thenReturn(Optional.of(userEto));

        //Act
        MvcResult result = mockMvc.perform(get(GET_USER_BY_ID_URL, userEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), UserEto.class))
                .isEqualToComparingFieldByField(userEto);
    }

    @Test
    @DisplayName("GET /user/v1/user/1 - Not Found")
    void testGetUserByIdNotFound() throws Exception {
        //Arrange
        when(userHandling.findUser(userEto.getId()))
                .thenThrow(EntityDoesNotExistException.class);

        //Act
        mockMvc.perform(get(GET_USER_BY_ID_URL, userEto.getId()))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /user/v1/user/1 - ISE")
    void testGetUserByIdISE() throws Exception {
        //Arrange
        when(userHandling.findUser(userEto.getId()))
                .thenReturn(Optional.empty());

        //Act
        mockMvc.perform(get(GET_USER_BY_ID_URL, userEto.getId()))

                //Assert
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("GET /user/v1/users - Found")
    void testGetAllUsersFound() throws Exception {
        //Arrange
        ArrayList<UserEto> users = new ArrayList<>();
        users.add(userEto);
        when(userHandling.findAllUsers()).thenReturn(Optional.of(users));

        //Act
        MvcResult result = mockMvc.perform(get(GET_ALL_USERS_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), UserEto[].class))
                .isEqualTo(users.toArray());
    }

    @Test
    @DisplayName("GET /user/v1/users - No content")
    void testGetAllUsersNoContent() throws Exception {
        //Arrange
        when(userHandling.findAllUsers())
                .thenReturn(Optional.of(new ArrayList<>()));


        //Act
        mockMvc.perform(get(GET_ALL_USERS_URL))

                //Assert
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /user/v1/users/role/{id} - Found")
    void testGetUsersByRoleFound() throws Exception {
        //Arrange
        ArrayList<UserEto> users = new ArrayList<>();
        users.add(userEto);
        when(userHandling.findAllUsersByRoleId(roleEto.getId())).thenReturn(Optional.of(users));

        //Act
        MvcResult result = mockMvc.perform(get(GET_USERS_BY_ROLE_URL, roleEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), UserEto[].class))
                .isEqualTo(users.toArray());
    }

    @Test
    @DisplayName("GET /user/v1/users/role/{id} - No content")
    void testGetUsersByRoleNoContent() throws Exception {
        //Arrange
        when(userHandling.findAllUsersByRoleId(roleEto.getId()))
                .thenReturn(Optional.of(new ArrayList<>()));

        //Act
        MvcResult result = mockMvc.perform(get(GET_USERS_BY_ROLE_URL, roleEto.getId()))

                //Assert
                .andExpect(status().isNoContent())
                .andReturn();

        assertThat(result.getResponse().getErrorMessage()).isEqualTo("Users with role id " + roleEto.getId() + " do not exist.");
    }

    @Test
    @DisplayName("GET /user/v1/role/1 - Found")
    void testGetRoleByIdFound() throws Exception {
        //Arrange
        when(userHandling.findRole(roleEto.getId())).thenReturn(Optional.of(roleEto));

        //Act
        MvcResult result = mockMvc.perform(get(GET_ROLE_BY_ID_URL, roleEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), RoleEto.class))
                .isEqualToComparingFieldByField(roleEto);
    }

    @Test
    @DisplayName("GET /user/v1/role/1 - Not found")
    void testGetRoleByIdNotFound() throws Exception {
        //Arrange
        when(userHandling.findRole(roleEto.getId()))
                .thenThrow(EntityDoesNotExistException.class);

        //Act
        mockMvc.perform(get(GET_ROLE_BY_ID_URL, roleEto.getId()))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /user/v1/role/1 - ISE")
    void testGetRoleByIdISE() throws Exception {
        //Arrange
        when(userHandling.findRole(roleEto.getId()))
                .thenReturn(Optional.empty());

        //Act
        mockMvc.perform(get(GET_ROLE_BY_ID_URL, roleEto.getId()))

                //Assert
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("GET /user/v1/roles - Found")
    void testGetAllRolesFound() throws Exception {
        //Arrange
        ArrayList<RoleEto> roles = new ArrayList<>();
        roles.add(roleEto);
        when(userHandling.findAllRoles()).thenReturn(Optional.of(roles));

        //Act
        MvcResult result = mockMvc.perform(get(GET_ALL_ROLES_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), RoleEto[].class))
                .isEqualTo(roles.toArray());
    }

    @Test
    @DisplayName("GET /user/v1/roles - No content")
    void testGetAllRolesNoContent() throws Exception {
        //Arrange
        when(userHandling.findAllRoles()).thenReturn(Optional.of(new ArrayList<>()));

        //Act
        MvcResult result = mockMvc.perform(get(GET_ALL_ROLES_URL))

                //Assert
                .andExpect(status().isNoContent())
                .andReturn();

        assertThat(result.getResponse().getErrorMessage()).isEqualTo("Roles do not exist.");
    }

    @Test
    @DisplayName("GET /user/v1/users/accounts - Found")
    void testGetAllAccountsFound() throws Exception {
        //Arrange
        ArrayList<AccountEto> accounts = new ArrayList<>();
        accounts.add(accountEto);
        when(userHandling.findAllAccounts()).thenReturn(Optional.of(accounts));

        //Act
        MvcResult result = mockMvc.perform(get(GET_ALL_ACCOUNTS_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), AccountEto[].class))
                .isEqualTo(accounts.toArray());
    }

    @Test
    @DisplayName("GET /user/v1/users/accounts - No content")
    void testGetAllAccountsNoContent() throws Exception {
        //Arrange
        when(userHandling.findAllAccounts()).thenReturn(Optional.of(new ArrayList<>()));

        //Act
        MvcResult result = mockMvc.perform(get(GET_ALL_ACCOUNTS_URL))

                //Assert
                .andExpect(status().isNoContent())
                .andReturn();

        assertThat(result.getResponse().getErrorMessage()).isEqualTo("Accounts do not exist.");
    }

    @Test
    @DisplayName("DELETE /user/v1/role/{id} - OK")
    void testDeleteRoleOk() throws Exception {
        //Act
        mockMvc.perform(delete(ROLE_ID_URL, roleEto.getId()))

                //Assert
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /user/v1/role/{id} - Not Found")
    void testDeleteRoleNotFound() throws Exception {
        //Arrange
        doThrow(EntityDoesNotExistException.class).when(userHandling).deleteRole(roleEto.getId());

        //Act
        mockMvc.perform(delete(ROLE_ID_URL, roleEto.getId()))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /user/v1/role/{id} - Has Users")
    void testDeleteRoleHasUsers() throws Exception {
        //Arrange
        doThrow(RoleHasAssignedUsersException.class).when(userHandling).deleteRole(roleEto.getId());

        //Act
        mockMvc.perform(delete(ROLE_ID_URL, roleEto.getId()))

                //Assert
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("DELETE /user/v1/user/{id} - OK")
    void testDeleteUserOk() throws Exception {
        //Act
        mockMvc.perform(delete(USER_ID_URL, userEto.getId()))

                //Assert
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /user/v1/user/{id} - Not Found")
    void testDeleteUserNotFound() throws Exception {
        //Arrange
        doThrow(EntityDoesNotExistException.class).when(userHandling).deleteUserAndAllRelatedEntities(userEto.getId());

        //Act
        mockMvc.perform(delete(USER_ID_URL, userEto.getId()))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /user/v1/role - Ok")
    void testCreateRoleOk() throws Exception {
        //Arrange
        when(userHandling.createRole(roleTo)).thenReturn(Optional.of(roleEto));

        //Act
        MvcResult result = mockMvc.perform(post(ROLE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(roleTo)))

                //Assert
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), RoleEto.class))
                .isEqualToComparingFieldByField(roleEto);
    }

    @Test
    @DisplayName("POST /user/v1/role - Not Found")
    void testCreateRoleNotFound() throws Exception {
        //Arrange
        when(userHandling.createRole(roleTo)).thenThrow(EntityDoesNotExistException.class);

        //Act
        mockMvc.perform(post(ROLE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(roleTo)))

                //Assert
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("POST /user/v1/role - Unprocessable Entity")
    void testCreateRoleUnprocessableEntity() throws Exception {
        //Arrange
        when(userHandling.createRole(roleTo)).thenThrow(EntityAlreadyExistsException.class);

        //Act
        mockMvc.perform(post(ROLE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(roleTo)))

                //Assert
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @DisplayName("POST /user/v1/role - ISE")
    void testCreateRoleISE() throws Exception {
        //Arrange
        when(userHandling.createRole(roleTo)).thenReturn(Optional.empty());

        //Act
        mockMvc.perform(post(ROLE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(roleTo)))

                //Assert
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    @DisplayName("POST /user/v1/user - Created")
    void testCreateUserOk() throws Exception {
        //Arrange
        when(userHandling.createUserAndAccountEntities(any(), any(), any())).thenReturn(Optional.of(userEto));

        //Act
        MvcResult result = mockMvc.perform(post(USER_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userTo)))

                //Assert
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), UserEto.class))
                .isEqualToComparingFieldByField(userEto);
    }

    @Test
    @DisplayName("POST /user/v1/user - Not Found")
    void testCreateUserNotFound() throws Exception {
        //Arrange
        when(userHandling.createUserAndAccountEntities(any(), any(), any())).thenThrow(EntityDoesNotExistException.class);

        //Act
        mockMvc.perform(post(USER_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userTo)))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /user/v1/user - Unprocessable Entity")
    void testCreateUserUnprocessableEntity() throws Exception {
        //Arrange
        when(userHandling.createUserAndAccountEntities(any(), any(), any())).thenThrow(AccountAlreadyExistsException.class);

        //Act
        mockMvc.perform(post(USER_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userTo)))

                //Assert
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @DisplayName("POST /user/v1/user - Unprocessable Entity2")
    void testCreateUserUnprocessableEntity2() throws Exception {
        //Arrange
        when(userHandling.createUserAndAccountEntities(any(), any(), any())).thenThrow(AddressException.class);

        //Act
        mockMvc.perform(post(USER_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userTo)))

                //Assert
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @DisplayName("POST /user/v1/user - ISE")
    void testCreateUserISE() throws Exception {
        //Arrange
        when(userHandling.createUserAndAccountEntities(any(), any(), any())).thenReturn(Optional.empty());

        //Act
        mockMvc.perform(post(USER_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userTo)))

                //Assert
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /user/v1/role/{id} - OK")
    void testUpdateRoleOk() throws Exception {
        //Arrange
        when(userHandling.updateRole(roleTo, roleEto.getId())).thenReturn(Optional.of(roleEto));

        //Act
        MvcResult result = mockMvc.perform(put(ROLE_ID_URL, roleEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(roleTo)))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), RoleEto.class))
                .isEqualToComparingFieldByField(roleEto);
    }

    @Test
    @DisplayName("PUT /user/v1/role/{id} - Not Found")
    void testUpdateRoleNotFound() throws Exception {
        //Arrange
        when(userHandling.updateRole(roleTo, roleEto.getId())).thenThrow(EntityDoesNotExistException.class);

        //Act
        mockMvc.perform(put(ROLE_ID_URL, roleEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(roleTo)))

                //Assert
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /user/v1/role/{id} - Unprocessable Entity")
    void testUpdateRoleUnprocessableEntity() throws Exception {
        //Arrange
        when(userHandling.updateRole(roleTo, roleEto.getId())).thenThrow(EntityAlreadyExistsException.class);

        //Act
        mockMvc.perform(put(ROLE_ID_URL, roleEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(roleTo)))

                //Assert
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /user/v1/role/{id} - ISE")
    void testUpdateRoleISE() throws Exception {
        //Arrange
        when(userHandling.updateRole(roleTo, roleEto.getId())).thenReturn(Optional.empty());

        //Act
        mockMvc.perform(put(ROLE_ID_URL, roleEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(roleTo)))

                //Assert
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /user/v1/user/{id} - OK")
    void testUpdateUserOk() throws Exception {
        //Arrange
        when(userHandling.updateUser(userTo, userEto.getId())).thenReturn(Optional.of(userEto));

        //Act
        MvcResult result = mockMvc.perform(put(USER_ID_URL, userEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userTo)))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), UserEto.class))
                .isEqualToComparingFieldByField(userEto);
    }

    @Test
    @DisplayName("PUT /user/v1/user/{id} - Not Found")
    void testUpdateUserNotFound() throws Exception {
        //Arrange
        when(userHandling.updateUser(userTo, userEto.getId())).thenThrow(EntityDoesNotExistException.class);

        //Act
        mockMvc.perform(put(USER_ID_URL, userEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userTo)))

                //Assert
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /user/v1/user/{id} - ISE")
    void testUpdateUserISE() throws Exception {
        //Arrange
        when(userHandling.updateUser(userTo, userEto.getId())).thenReturn(Optional.empty());

        //Act
        mockMvc.perform(put(USER_ID_URL, userEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userTo)))

                //Assert
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /user/v1/user/{id}/account - OK")
    void testUpdateUserAccountOk() throws Exception {
        //Arrange
        when(userHandling.updateUserAccount(accountTo, userEto.getId())).thenReturn(Optional.of(accountEto));

        //Act
        MvcResult result = mockMvc.perform(put(USER_ID_URL + "/account", userEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(accountTo)))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), AccountEto.class))
                .isEqualToComparingFieldByField(accountEto);
    }

    @Test
    @DisplayName("PUT /user/v1/user/{id}/account - Not Found")
    void testUpdateUserAccountNotFound() throws Exception {
        //Arrange
        when(userHandling.updateUserAccount(accountTo, userEto.getId())).thenThrow(EntityDoesNotExistException.class);

        //Act
        mockMvc.perform(put(USER_ID_URL + "/account", userEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(accountTo)))

                //Assert
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /user/v1/user/{id}/account - Unprocessable Entity")
    void testUpdateUserAccountUnprocessableEntity() throws Exception {
        //Arrange
        when(userHandling.updateUserAccount(accountTo, userEto.getId())).thenThrow(AccountAlreadyExistsException.class);

        //Act
        mockMvc.perform(put(USER_ID_URL + "/account", userEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(accountTo)))

                //Assert
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /user/v1/user/{id}/account - Unprocessable Entity")
    void testUpdateUserAccountUnprocessableEntity2() throws Exception {
        //Arrange
        when(userHandling.updateUserAccount(accountTo, userEto.getId())).thenThrow(AddressException.class);

        //Act
        mockMvc.perform(put(USER_ID_URL + "/account", userEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(accountTo)))

                //Assert
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /user/v1/user/{id}/account - ISE")
    void testUpdateUserAccountISE() throws Exception {
        //Arrange
        when(userHandling.updateUserAccount(accountTo, userEto.getId())).thenReturn(Optional.empty());

        //Act
        mockMvc.perform(put(USER_ID_URL + "/account", userEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(accountTo)))

                //Assert
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    @DisplayName("GET /user/v1/user/account/registrationConfirm/{token} - Redirection")
    void testConfirmRegistrationRedirection() throws Exception {
        //Arrange
        RedirectView redirectView = new RedirectView("sth");
        String token = "token";
        when(userHandling.confirmRegistration(token)).thenReturn(Optional.of(redirectView));

        //Act
        mockMvc.perform(get(ACCOUNT_REGISTRATION_URL)
                .param("token", token)
                .contentType(MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("GET /user/v1/user/account/registrationConfirm/{token} - Not Found")
    void testConfirmRegistrationNotFound() throws Exception {
        //Arrange
        String token = "token";
        when(userHandling.confirmRegistration(token)).thenThrow(EntityDoesNotExistException.class);

        //Act
        mockMvc.perform(get(ACCOUNT_REGISTRATION_URL)
                .param("token", token)
                .contentType(MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /user/v1/user/account/registrationConfirm/{token} - ISE")
    void testConfirmRegistrationISE() throws Exception {
        //Arrange
        String token = "sth";
        when(userHandling.confirmRegistration(token)).thenReturn(Optional.empty());

        //Act
        mockMvc.perform(get(ACCOUNT_REGISTRATION_URL)
                .param("token", token)
                .contentType(MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isInternalServerError());
    }
}
