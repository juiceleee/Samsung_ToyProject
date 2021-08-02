package com.samsung.bixby.mediastreaming.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BasketRepository extends JpaRepository<BasketEntity, Integer> {
    List<BasketEntity> findByUserid(Integer i);
    List<BasketEntity> findByUseridAndItemid(Integer userId, Integer itemId);
    @Transactional
    void deleteByUserid(Integer userId);
    @Transactional
    void deleteByItemidAndUserid(Integer itemId, Integer userId);

    @Transactional
    @Modifying
    @Query("update BasketEntity b set b.itemcnt = :itemCnt where b.userid = :userId and b.itemid= :itemId")
    void updateItemCnt(@Param(value = "itemCnt") Integer itemCnt, @Param(value = "userId") Integer userId, @Param(value="itemId") Integer itemId);
}