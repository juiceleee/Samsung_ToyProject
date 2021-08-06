package com.samsung.bixby.mediastreaming.demo.dao;

import com.samsung.bixby.mediastreaming.demo.dao.entitiy.SellerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<SellerEntity, Integer>, SellerCustomRepository {
    Optional<SellerEntity> findByName(String sellerName);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update SellerEntity u set u.name = :newName where u.name = :oldName")
    void updateSellerName(@Param(value = "oldName") String oldName, @Param(value = "newName") String newName);
}
