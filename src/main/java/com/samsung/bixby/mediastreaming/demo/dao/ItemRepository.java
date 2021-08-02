package com.samsung.bixby.mediastreaming.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {
    Optional<ItemEntity> findByItemname(String s);

    @Transactional
    @Modifying
    @Query("update ItemEntity i set i.itemcnt = :itemCnt where i.itemid = :itemId")
    void deleteItemCnt(@Param(value = "itemCnt") Integer itemCnt, @Param(value = "itemId") Integer itemId);

    @Transactional
    @Modifying
    @Query("update ItemEntity i set i.itemname = :newName where i.itemname = :oldName")
    void updateItemName(@Param(value = "oldName") String oldName, @Param(value = "newName") String newName);
}