package com.wtulich.photosupp.orderhandling.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import com.wtulich.photosupp.general.utils.enums.MediaType;
import com.wtulich.photosupp.general.utils.enums.OrderStatus;
import com.wtulich.photosupp.orderhandling.dataaccess.api.dao.MediaContentDao;
import com.wtulich.photosupp.orderhandling.dataaccess.api.dao.OrderDao;
import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.MediaContentEntity;
import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.OrderEntity;
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

import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class UcDeleteMediaContentTest {

    @Autowired
    private UcDeleteMediaContentImpl ucDeleteMediaContent;

    @MockBean
    private MediaContentDao mediaContentDao;

    @MockBean
    private OrderDao orderDao;

    private MediaContentEntity mediaContentEntity;
    private OrderEntity orderEntity;

    @BeforeEach
    void setUp() {
        List<PermissionEntity> permissionEntities = new ArrayList<>();
        PermissionEntity permissionEntity = new PermissionEntity(ApplicationPermissions.AUTH_USER, "Standard user with no special permissions.");
        permissionEntity.setId(6L);
        permissionEntities.add(permissionEntity);

        RoleEntity roleEntity = new RoleEntity( "USER", "Standard user with no special permissions", permissionEntities);
        roleEntity.setId(2L);
        AccountEntity accountEntity = new AccountEntity( "user2", "passw0rd", "user2@test.com", true);
        accountEntity.setId(2L);

        UserEntity userEntity = new UserEntity("NAME2", "SURNAME2", roleEntity, accountEntity);
        userEntity.setId(2L);

        orderEntity = new OrderEntity("INVIU_00001", OrderStatus.IN_PROGRESS, 1000D, LocalDate.now(), userEntity, userEntity,  null );

        mediaContentEntity = new MediaContentEntity(MediaType.IMAGE, "https://sample.com/jpg1", orderEntity);
        mediaContentEntity.setId(1L);
    }

    @Test
    @DisplayName("Test deleteMediaContent Success")
    void testDeleteCommentSuccess() {
        //Arrange
        when(mediaContentDao.findById(mediaContentEntity.getId())).thenReturn(Optional.of(mediaContentEntity));

        //Act Assert
        Assertions.assertDoesNotThrow(() -> ucDeleteMediaContent.deleteMediaContent(mediaContentEntity.getId()));
    }


    @Test
    @DisplayName("Test deleteMediaContent Failure")
    void testDeleteCommentFailure() {
        //Arrange
        when(mediaContentDao.findById(mediaContentEntity.getId())).thenReturn(java.util.Optional.ofNullable(null));

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucDeleteMediaContent.deleteMediaContent(mediaContentEntity.getId()));
    }

    @Test
    @DisplayName("Test deleteAllMediaContent Success")
    void testDeleteAllCommentSuccess() {
        //Arrange
        when(orderDao.findByOrderNumber(orderEntity.getOrderNumber())).thenReturn(Optional.of(orderEntity));

        //Act Assert
        Assertions.assertDoesNotThrow(() -> ucDeleteMediaContent.deleteAllMediaContent(orderEntity.getOrderNumber()));
    }


    @Test
    @DisplayName("Test deleteAllMediaContent Failure")
    void testDeleteAllCommentFailure() {
        //Arrange
        when(orderDao.findByOrderNumber(orderEntity.getOrderNumber())).thenReturn(java.util.Optional.ofNullable(null));

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucDeleteMediaContent.deleteAllMediaContent(orderEntity.getOrderNumber()));
    }
}
