package com.wtulich.photosupp.orderhandling.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import com.wtulich.photosupp.general.utils.enums.MediaType;
import com.wtulich.photosupp.general.utils.enums.OrderStatus;
import com.wtulich.photosupp.orderhandling.dataaccess.api.dao.MediaContentDao;
import com.wtulich.photosupp.orderhandling.dataaccess.api.dao.OrderDao;
import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.MediaContentEntity;
import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.OrderEntity;
import com.wtulich.photosupp.orderhandling.logic.api.mapper.MediaContentMapper;
import com.wtulich.photosupp.orderhandling.logic.api.to.MediaContentEto;
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
public class UcFindMediaContentTest {

    @Autowired
    private UcFindMediaContentImpl ucFindMediaContent;

    @Autowired
    private MediaContentMapper mediaContentMapper;

    @MockBean
    private MediaContentDao mediaContentDao;

    @MockBean
    private OrderDao orderDao;

    private List<MediaContentEntity> mediaContentEntities;
    private List<MediaContentEto> mediaContentEtos;
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

        MediaContentEntity mediaContentEntity = new MediaContentEntity(MediaType.IMAGE, "https://sample.com/jpg1", orderEntity);
        mediaContentEntity.setId(1L);
        mediaContentEntities = new ArrayList<>();
        mediaContentEntities.add(mediaContentEntity);

        MediaContentEto mediaContentEto = new MediaContentEto(1L, MediaType.IMAGE, "https://sample.com/jpg1", "INVIU_00001");

        mediaContentEtos = new ArrayList<>();
        mediaContentEtos.add(mediaContentEto);
    }

    @Test
    @DisplayName("Test findAllMediaContent Success")
    void testFindAllMediaContentSuccess() throws EntityDoesNotExistException {
        //Arrange
        when(orderDao.findByOrderNumber(orderEntity.getOrderNumber())).thenReturn(Optional.ofNullable(orderEntity));
        when(mediaContentDao.findAllByOrder_OrderNumber(orderEntity.getOrderNumber())).thenReturn(mediaContentEntities);

        //Act
        Optional<List<MediaContentEto>> result = ucFindMediaContent.findAllMediaContentByOrderNumber(orderEntity.getOrderNumber());

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(result.get()).hasSize(mediaContentEtos.size());
        assertThat(mediaContentEtos.get(0)).isEqualTo(result.get().get(0));
    }

    @Test
    @DisplayName("Test findAllMediaContent No content")
    void testFindAllMediaContentNoContent() throws EntityDoesNotExistException {
        //Arrange
        when(orderDao.findByOrderNumber(orderEntity.getOrderNumber())).thenReturn(Optional.ofNullable(orderEntity));
        when(mediaContentDao.findAllByOrder_OrderNumber(orderEntity.getOrderNumber())).thenReturn(new ArrayList<>());

        //Act
        Optional<List<MediaContentEto>> result = ucFindMediaContent.findAllMediaContentByOrderNumber(orderEntity.getOrderNumber());

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertTrue(result.get().size() == 0);
    }

    @Test
    @DisplayName("Test findAllMediaContent Not Found")
    void testFindAllMediaContentNotFound() {
        //Arrange
        when(orderDao.findByOrderNumber(orderEntity.getOrderNumber())).thenReturn(Optional.ofNullable(null));

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucFindMediaContent.findAllMediaContentByOrderNumber(orderEntity.getOrderNumber()));
    }
}
