package com.wtulich.photosupp.userhandling.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import com.wtulich.photosupp.userhandling.logic.api.to.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@SpringBootTest()
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserRestServiceIntegrationTest {

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

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private RoleEto roleEto;
    private AccountEto accountEto1;
    private AccountEto accountEto2;
    private AccountEto accountCreated;
    private UserEto userEto;
    private UserEto userCreated;
    private List<PermissionEto> permissionEtoList;
    private RoleTo roleTo;
    private RoleEto roleCreated;
    private AccountTo accountTo;
    private UserTo userTo;
    private List<Long> permissionsIds;

    @BeforeEach
    void setUp() {
        //Arrange
        objectMapper = new ObjectMapper();

        permissionEtoList = new ArrayList<>();
        permissionEtoList.add(new PermissionEto(6L, ApplicationPermissions.AUTH_USER, "Standard user with no special permissions."));
        roleEto = new RoleEto(2L, "USER", "Standard user with no special permissions", permissionEtoList);
        accountEto1 = new AccountEto(1L, "user1", "passw0rd", "user1@test.com", false);
        accountEto2 = new AccountEto(2L, "user2", "passw0rd", "user2@test.com", true);
        userEto = new UserEto(1L, "NAME", "SURNAME", accountEto1, roleEto);

        permissionsIds = new ArrayList<>();
        permissionsIds.add(6L);
        roleTo = new RoleTo("ADMIN2", "DESC1", permissionsIds);
        accountTo = new AccountTo("PASS", "TEST@test.com");
        userTo = new UserTo("NAME1", "SURNAME1", accountTo, 2L);
        roleCreated = new RoleEto(3L, "ADMIN2", "DESC1", permissionEtoList);
        accountCreated = new AccountEto(3L, "TEST", "PASS", "TEST@test.com", false);
        userCreated = new UserEto(3L, "NAME1", "SURNAME1", accountCreated, roleCreated);
    }

    @Test
    @DisplayName("GET /user/v1/user/1 - Found")
    void testGetUserByIdFound() throws Exception {
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
        //Act
        mockMvc.perform(get(GET_USER_BY_ID_URL, 4L))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /user/v1/users - Found")
    void testGetAllUsersFound() throws Exception {
        //Arrange
        ArrayList<UserEto> users = new ArrayList<>();
        users.add(userEto);

        //Act
        MvcResult result = mockMvc.perform(get(GET_ALL_USERS_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<UserEto> userEtos = Arrays.stream(objectMapper.readValue(result.getResponse().getContentAsString(), UserEto[].class))
                .collect(Collectors.toList());
        assertThat(userEtos).hasSize(2);
        assertThat(userEtos.get(0)).isEqualToComparingFieldByField(userEto);
    }

    @Test
    @DisplayName("GET /user/v1/users/role/{id} - Found")
    void testGetUsersByRoleFound() throws Exception {
        //Arrange
        ArrayList<UserEto> users = new ArrayList<>();
        users.add(userEto);

        //Act
        MvcResult result = mockMvc.perform(get(GET_USERS_BY_ROLE_URL, roleEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<UserEto> userEtos = Arrays.stream(objectMapper.readValue(result.getResponse().getContentAsString(), UserEto[].class))
                .collect(Collectors.toList());
        assertThat(userEtos).hasSize(2);
        assertThat(userEtos.get(0)).isEqualToComparingFieldByField(userEto);
    }

    @Test
    @DisplayName("GET /user/v1/users/role/{id} - No content")
    void testGetUsersByRoleNoContent() throws Exception {
        //Act
        MvcResult result = mockMvc.perform(get(GET_USERS_BY_ROLE_URL, 1L))

                //Assert
                .andExpect(status().isNoContent())
                .andReturn();

        assertThat(result.getResponse().getErrorMessage()).isEqualTo("Users with role id " + 1L + " do not exist.");
    }

    @Test
    @DisplayName("GET /user/v1/role/1 - Found")
    void testGetRoleByIdFound() throws Exception {
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
    @DisplayName("GET /user/v1/role/1 - Not Found")
    void testGetRoleByIdNotFound() throws Exception {
        //Act
        mockMvc.perform(get(GET_ROLE_BY_ID_URL, 10L))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /user/v1/roles - Found")
    void testGetAllRolesFound() throws Exception {
        //Act
        MvcResult result = mockMvc.perform(get(GET_ALL_ROLES_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();


        List<RoleEto> roleEtos = Arrays.stream(objectMapper.readValue(result.getResponse().getContentAsString(), RoleEto[].class))
                .collect(Collectors.toList());

        assertThat(roleEtos).hasSize(2);
        assertThat(roleEtos.get(1)).isEqualToComparingFieldByField(roleEto);
    }

    @Test
    @DisplayName("GET /user/v1/users/accounts - Found")
    void testGetAllAccountsFound() throws Exception {
        //Arrange
        ArrayList<AccountEto> accounts = new ArrayList<>();
        accounts.add(accountEto1);
        accounts.add(accountEto2);

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
    @DisplayName("POST /user/v1/user - Created")
    void testCreateUserOk() throws Exception {
        //Arrange
        userCreated.setRoleEto(roleEto);

        //Act
        MvcResult result = mockMvc.perform(post(USER_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userTo)))

                //Assert
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), UserEto.class))
                .isEqualToIgnoringGivenFields(userCreated, "accountEto");
    }

    @Test
    @DisplayName("POST /user/v1/user - Not Found")
    void testCreateUserNotFound() throws Exception {
        //Arrange
        userTo.setRoleId(8L);

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
        accountTo.setEmail("user1@test.com");

        //Act
        mockMvc.perform(post(USER_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userTo)))

                //Assert
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @DisplayName("POST /user/v1/role - Ok")
    void testCreateRoleOk() throws Exception {
        //Act
        MvcResult result = mockMvc.perform(post(ROLE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(roleTo)))

                //Assert
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), RoleEto.class))
                .isEqualToComparingFieldByField(roleCreated);
    }

    @Test
    @DisplayName("POST /user/v1/role - Not Found")
    void testCreateRoleNotFound() throws Exception {
        //Arrange
        roleTo.getPermissionIds().clear();
        roleTo.getPermissionIds().add(12L);

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
        roleTo.setName("USER");

        //Act
        mockMvc.perform(post(ROLE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(roleTo)))

                //Assert
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @DisplayName("POST /user/v1/user - Unprocessable Entity2")
    void testCreateUserUnprocessableEntity2() throws Exception {
        //Arrange
        accountTo.setEmail("asadas");

        //Act
        mockMvc.perform(post(USER_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userTo)))

                //Assert
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @DisplayName("DELETE /user/v1/role/{id} - OK")
    void testDeleteRoleOk() throws Exception {

        //Act
        mockMvc.perform(delete(ROLE_ID_URL, 1L))

                //Assert
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /user/v1/role/{id} - Has Users")
    void testDeleteRoleHasUsers() throws Exception {
        //Act
        mockMvc.perform(delete(ROLE_ID_URL, roleEto.getId()))

                //Assert
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("DELETE /user/v1/role/{id} - Not Found")
    void testDeleteRoleNotFound() throws Exception {
        //Act
        mockMvc.perform(delete(ROLE_ID_URL, 4L))

                //Assert
                .andExpect(status().isNotFound());
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
        //Act
        mockMvc.perform(delete(USER_ID_URL, 3L))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /user/v1/role/{id} - OK")
    void testUpdateRoleOk() throws Exception {
        //Arrange
        roleCreated.setId(2L);

        //Act
        MvcResult result = mockMvc.perform(put(ROLE_ID_URL, roleEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(roleTo)))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), RoleEto.class))
                .isEqualToComparingFieldByField(roleCreated);
    }

    @Test
    @DisplayName("PUT /user/v1/role/{id} - Not Found")
    void testUpdateRoleNotFound() throws Exception {
        //Act
        mockMvc.perform(put(ROLE_ID_URL, 5L)
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
        roleTo.setName("USER");

        //Act
        mockMvc.perform(put(ROLE_ID_URL, 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(roleTo)))

                //Assert
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /user/v1/user/{id} - OK")
    void testUpdateUserOk() throws Exception {
        userCreated.setAccountEto(accountEto1);
        userCreated.setRoleEto(roleEto);
        userCreated.setId(1L);

        //Act
        MvcResult result = mockMvc.perform(put(USER_ID_URL, userEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userTo)))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), UserEto.class))
                .isEqualToComparingFieldByField(userCreated);
    }

    @Test
    @DisplayName("PUT /user/v1/user/{id} - Not Found")
    void testUpdateUserNotFound() throws Exception {
        //Act
        mockMvc.perform(put(USER_ID_URL, 5L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userTo)))

                //Assert
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("PUT /user/v1/user/{id}/account - OK")
    void testUpdateUserAccountOk() throws Exception {
        //Arrange
        accountCreated.setId(1L);

        //Act
        MvcResult result = mockMvc.perform(put(USER_ID_URL + "/account", userEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(accountTo)))

                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), AccountEto.class))
                .isEqualToIgnoringGivenFields(accountCreated, "password");
    }

    @Test
    @DisplayName("PUT /user/v1/user/{id}/account - Not Found")
    void testUpdateUserAccountNotFound() throws Exception {
        //Act
        mockMvc.perform(put(USER_ID_URL + "/account", 5L)
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
        accountTo.setEmail("user2@test.com");

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
        accountTo.setEmail("invalid");

        //Act
        mockMvc.perform(put(USER_ID_URL + "/account", userEto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(accountTo)))

                //Assert
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @DisplayName("GET /user/v1/user/account/registrationConfirm/{token} - Redirection")
    void testConfirmRegistrationRedirection() throws Exception {
        //Arrange
        String token = "token";

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
        String token = "sth";

        //Act
        mockMvc.perform(get(ACCOUNT_REGISTRATION_URL)
                .param("token", token)
                .contentType(MediaType.APPLICATION_JSON_VALUE))

                //Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /user/v1/users - No content")
    void testGetAllUsersNoContent() throws Exception {
        //Arrange
        mockMvc.perform(delete(USER_ID_URL, userEto.getId()));
        mockMvc.perform(delete(USER_ID_URL, 2L));

        //Act
        mockMvc.perform(get(GET_ALL_USERS_URL))

                //Assert
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /user/v1/roles - No content")
    void testGetAllRolesNoContent() throws Exception {
        //Arrange
        mockMvc.perform(delete(USER_ID_URL, userEto.getId()));
        mockMvc.perform(delete(USER_ID_URL, 2L));
        mockMvc.perform(delete(ROLE_ID_URL, 1L));
        mockMvc.perform(delete(ROLE_ID_URL, 2L));

        //Act
        MvcResult result = mockMvc.perform(get(GET_ALL_ROLES_URL))

                //Assert
                .andExpect(status().isNoContent())
                .andReturn();

        assertThat(result.getResponse().getErrorMessage()).isEqualTo("Roles do not exist.");
    }

    @Test
    @DisplayName("GET /user/v1/users/accounts - No content")
    void testGetAllAccountsNoContent() throws Exception {
        //Arrange
        mockMvc.perform(delete(USER_ID_URL, userEto.getId()));
        mockMvc.perform(delete(USER_ID_URL, 2L));

        //Act
        MvcResult result = mockMvc.perform(get(GET_ALL_ACCOUNTS_URL))

                //Assert
                .andExpect(status().isNoContent())
                .andReturn();

        assertThat(result.getResponse().getErrorMessage()).isEqualTo("Accounts do not exist.");
    }
}
