package com.wtulich.photosupp.orderhandling.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import com.wtulich.photosupp.general.utils.enums.OrderStatus;
import com.wtulich.photosupp.orderhandling.dataaccess.api.dao.CommentDao;
import com.wtulich.photosupp.orderhandling.dataaccess.api.dao.OrderDao;
import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.CommentEntity;
import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.OrderEntity;
import com.wtulich.photosupp.orderhandling.logic.api.mapper.CommentMapper;
import com.wtulich.photosupp.orderhandling.logic.api.to.CommentEto;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.AccountEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.PermissionEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.RoleEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.UserEntity;
import com.wtulich.photosupp.userhandling.logic.api.to.AccountEto;
import com.wtulich.photosupp.userhandling.logic.api.to.PermissionEto;
import com.wtulich.photosupp.userhandling.logic.api.to.RoleEto;
import com.wtulich.photosupp.userhandling.logic.api.to.UserEto;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class UcFindCommentTest {

    @Autowired
    private UcFindCommentImpl ucFindComment;

    @Autowired
    private CommentMapper commentMapper;

    @MockBean
    private CommentDao commentDao;

    @MockBean
    private OrderDao orderDao;

    private List<CommentEntity> commentEntities;
    private List<CommentEto> commentEtos;
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

        CommentEntity commentEntity = new CommentEntity("Perfect, thanks!", userEntity, orderEntity, LocalDate.now());
        commentEntity.setId(1L);
        commentEntities = new ArrayList<>();
        commentEntities.add(commentEntity);

        AccountEto accountEto2 = new AccountEto(2L, "user2", "passw0rd", "user2@test.com", true);
        UserEto userEto2 = new UserEto(2L, "NAME2", "SURNAME2", accountEto2, null);

        CommentEto commentEto = new CommentEto(1L, "Perfect, thanks!", "INVIU_00001", userEto2,
                DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format(LocalDateTime.now()));

        commentEtos = new ArrayList<>();
        commentEtos.add(commentEto);
    }

    @Test
    @DisplayName("Test findAllComments Success")
    void testFindAllCommentsSuccess() throws EntityDoesNotExistException {
        //Arrange
        when(orderDao.findByOrderNumber(orderEntity.getOrderNumber())).thenReturn(Optional.ofNullable(orderEntity));
        when(commentDao.findAllByOrder_OrderNumber(orderEntity.getOrderNumber())).thenReturn(commentEntities);

        //Act
        Optional<List<CommentEto>> result = ucFindComment.findAllCommentsByOrderNumber(orderEntity.getOrderNumber());

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(result.get()).hasSize(commentEtos.size());
        assertThat(commentEtos.get(0)).isEqualTo(result.get().get(0));

    }

    @Test
    @DisplayName("Test findAllComments No content")
    void testFindAllCommentsNoContent() throws EntityDoesNotExistException {
        //Arrange
        when(orderDao.findByOrderNumber(orderEntity.getOrderNumber())).thenReturn(Optional.ofNullable(orderEntity));
        when(commentDao.findAllByOrder_OrderNumber(orderEntity.getOrderNumber())).thenReturn(new ArrayList<>());

        //Act
        Optional<List<CommentEto>> result = ucFindComment.findAllCommentsByOrderNumber(orderEntity.getOrderNumber());

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertTrue(result.get().size() == 0);
    }

    @Test
    @DisplayName("Test findAllComments Not Found")
    void testFindAllCommentsNotFound() {
        //Arrange
        when(orderDao.findByOrderNumber(orderEntity.getOrderNumber())).thenReturn(Optional.ofNullable(null));

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucFindComment.findAllCommentsByOrderNumber(orderEntity.getOrderNumber()));
    }
}
