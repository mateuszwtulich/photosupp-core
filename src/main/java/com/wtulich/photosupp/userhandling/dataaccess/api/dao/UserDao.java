package com.wtulich.photosupp.userhandling.dataaccess.api.dao;

import com.wtulich.photosupp.userhandling.dataaccess.api.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserDao extends JpaRepository<UserEntity, Long> {

    List<UserEntity> findAllByRole_Id(Long roleId);

    Optional<UserEntity> findByAccount_Username(String username);
}
