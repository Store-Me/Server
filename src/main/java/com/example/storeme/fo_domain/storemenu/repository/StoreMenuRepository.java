package com.example.storeme.fo_domain.storemenu.repository;

import com.example.storeme.fo_domain.storemenu.domain.StoreMenu;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface StoreMenuRepository  extends JpaRepository<StoreMenu, Long> {
    boolean existsByIdAndStoreMenuCategory_Id(Long storeMenuId, Long storeMenuCategoryId);

    @Modifying
    @Transactional
    @Query("UPDATE StoreMenu sm SET sm.order = sm.order - 1 " +
            "WHERE sm.order > :order AND sm.storeMenuCategory.id = :storeMenuCategoryId")
    void decrementOrdersGreaterThan(@Param("order") Integer order, @Param("storeMenuCategoryId") Long storeMenuCategoryId);

    List<StoreMenu> findByStoreMenuCategory_Id(Long storeMenuCategoryId);
}
