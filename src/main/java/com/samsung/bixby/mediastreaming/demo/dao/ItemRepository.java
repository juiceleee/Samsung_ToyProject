package com.samsung.bixby.mediastreaming.demo.dao;

import com.samsung.bixby.mediastreaming.demo.dao.entitiy.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<ItemEntity, Integer>, ItemCustomRepository {
    Optional<ItemEntity> findByItemname(String s);

    List<ItemEntity> findByItemnameContaining(String s);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update ItemEntity i set i.stock = :stock where i.itemid = :itemId")
    void updateItemStock(@Param(value = "stock") Integer stock, @Param(value = "itemId") Integer itemId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update ItemEntity i set i.itemname = :newName where i.itemname = :oldName")
    void updateItemName(@Param(value = "oldName") String oldName, @Param(value = "newName") String newName);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update ItemEntity i set i.seller = :null where i.itemid = :itemId")
    void setItemSellerNull(@Param(value = "itemId") Integer itemId);

}