package com.wtulich.photosupp.userhandling.dataaccess.api.dao;

import com.wtulich.photosupp.userhandling.dataaccess.api.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountDao extends JpaRepository<AccountEntity, Long> {

    boolean existsByEmail(String email);

    Optional<AccountEntity> findByUsername(String username);

    Optional<AccountEntity> findByEmail(String email);
}
