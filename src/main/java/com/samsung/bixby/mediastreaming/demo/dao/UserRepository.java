package com.samsung.bixby.mediastreaming.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String s);

    @Transactional
    void deleteByUsername(String s);

    @Transactional
    @Modifying
    @Query("update UserEntity u set u.username = :newName where u.username = :oldName")
    void updateUserName(@Param(value = "oldName") String oldName, @Param(value = "newName") String newName);

}


