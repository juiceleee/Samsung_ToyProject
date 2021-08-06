package com.samsung.bixby.mediastreaming.demo.dao;

import com.samsung.bixby.mediastreaming.demo.dao.entitiy.BasketEntity;
import com.samsung.bixby.mediastreaming.demo.dao.entitiy.ItemEntity;
import com.samsung.bixby.mediastreaming.demo.dao.entitiy.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BasketRepository extends JpaRepository<BasketEntity, Integer>, BasketCustomRepository {
    List<BasketEntity> findByUser(UserEntity user);
    List<BasketEntity> findByUserAndItemAAndIsbought(UserEntity user, ItemEntity item, boolean isbought);

    @Transactional
    void deleteByUser(UserEntity user);

    @Transactional
    void deleteByItem(ItemEntity item);

    @Transactional
    void deleteByUserAndItemAAndIsbought(UserEntity user, ItemEntity item, boolean isbought);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update BasketEntity b set b.itemcnt = :itemCnt where b.user = :user and b.item= :item")
    void updateItemCnt(@Param(value = "itemCnt") Integer itemCnt, @Param(value = "user") UserEntity user, @Param(value="item") ItemEntity item);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update BasketEntity b set b.isbought = true where b.rownum = :rownum")
    void updateItemStatus(@Param(value = "rownum") Integer rownum);
}