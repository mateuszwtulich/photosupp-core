package com.wtulich.photosupp.orderhandling.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import com.wtulich.photosupp.general.utils.enums.MediaType;
import com.wtulich.photosupp.general.utils.enums.OrderStatus;
import com.wtulich.photosupp.orderhandling.dataaccess.api.dao.MediaContentDao;
import com.wtulich.photosupp.orderhandling.dataaccess.api.dao.OrderDao;
import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.MediaContentEntity;
import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.OrderEntity;
import com.wtulich.photosupp.orderhandling.logic.api.to.MediaContentEto;
import com.wtulich.photosupp.orderhandling.logic.api.to.MediaContentTo;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.AccountEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.PermissionEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.RoleEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.UserEntity;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class UcManageMediaContentTest {

    @Autowired
    private UcManageMediaContentImpl ucManageMediaContent;

    @MockBean
    private MediaContentDao mediaContentDao;

    @MockBean
    private OrderDao orderDao;

    private MediaContentEntity mediaContentEntity;
    private MediaContentEto mediaContentEto;
    private MediaContentTo mediaContentTo;
    private OrderEntity orderEntity;

    @BeforeEach
    void setUp() {
        List<PermissionEntity> permissionEntities = new ArrayList<>();
        PermissionEntity permissionEntity = new PermissionEntity(ApplicationPermissions.AUTH_USER, "User has possibility to use CRUD operations one every functionality.");
        permissionEntity.setId(1L);
        permissionEntities.add(permissionEntity);

        RoleEntity roleEntity = new RoleEntity( "MANAGER", "Manager with all permissions in order management", permissionEntities);
        roleEntity.setId(1L);
        AccountEntity accountEntity = new AccountEntity( "user1", "passw0rd", "user1@test.com", true);
        accountEntity.setId(1L);

        UserEntity userEntity = new UserEntity("NAME", "SURNAME", roleEntity, accountEntity);
        userEntity.setId(1L);

        List<PermissionEntity> permissionEntities2 = new ArrayList<>();
        PermissionEntity permissionEntity2 = new PermissionEntity(ApplicationPermissions.AUTH_USER, "Standard user with no special permissions.");
        permissionEntity2.setId(6L);
        permissionEntities2.add(permissionEntity2);

        RoleEntity roleEntity2 = new RoleEntity( "USER", "Standard user with no special permissions", permissionEntities2);
        roleEntity2.setId(2L);
        AccountEntity accountEntity2 = new AccountEntity( "user2", "passw0rd", "user2@test.com", true);
        accountEntity2.setId(2L);

        UserEntity userEntity2 = new UserEntity("NAME2", "SURNAME2", roleEntity2, accountEntity2);
        userEntity2.setId(2L);

        orderEntity = new OrderEntity("INVIU_00001", OrderStatus.IN_PROGRESS, 1000D, LocalDate.now(), userEntity2, userEntity,  null );

        mediaContentEntity = new MediaContentEntity(MediaType.IMAGE, "https://sample.com/jpg1", orderEntity);
        mediaContentEntity.setId(1L);

        mediaContentEto = new MediaContentEto(1L, MediaType.IMAGE, "https://sample.com/jpg1", "INVIU_00001");

        mediaContentTo = new MediaContentTo(MediaType.IMAGE, "https://sample.com/jpg1", "INVIU_00001");
    }

    @Test
    @DisplayName("Test createMediaContent Success")
    void testCreateMediaContentSuccess() throws EntityDoesNotExistException {
        //Arrange
        when(orderDao.findByOrderNumber(orderEntity.getOrderNumber())).thenReturn(Optional.ofNullable(orderEntity));
        when(mediaContentDao.save(mediaContentEntity)).thenReturn(mediaContentEntity);

        //Act
        Optional<MediaContentEto> result = ucManageMediaContent.addMediaContent(mediaContentTo);

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(mediaContentEto).isEqualToComparingFieldByField(result.get());
    }

    @Test
    @DisplayName("Test createMediaContent EntityDoesNotExistException")
    void testCreateMediaContentEntityDoesNotExistException() throws EntityDoesNotExistException {
        //Arrange
        when(orderDao.findByOrderNumber(orderEntity.getOrderNumber())).thenReturn(Optional.ofNullable(null));

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucManageMediaContent.addMediaContent(mediaContentTo));
    }
}
