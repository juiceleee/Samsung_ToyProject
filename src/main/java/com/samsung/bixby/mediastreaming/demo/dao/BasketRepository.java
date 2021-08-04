package com.samsung.bixby.mediastreaming.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BasketRepository extends JpaRepository<BasketEntity, Integer> {
    List<BasketEntity> findByUser(UserEntity user);
    List<BasketEntity> findByUserAndItem(UserEntity user, ItemEntity item);

    @Transactional
    void deleteByUser(UserEntity user);

    @Transactional
    void deleteByItem(ItemEntity item);

    @Transactional
    void deleteByUserAndItem(UserEntity user, ItemEntity item);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update BasketEntity b set b.itemcnt = :itemCnt where b.user = :user and b.item= :item")
    void updateItemCnt(@Param(value = "itemCnt") Integer itemCnt, @Param(value = "user") UserEntity user, @Param(value="item") ItemEntity item);
}