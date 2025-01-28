package com.example.storeme.fo_domain.storemenu.repository;

import com.example.storeme.fo_domain.storemenu.domain.StoreMenuCategory;
import com.example.storeme.fo_domain.storemenu.dto.StoreMenuCategoryListResponseDto.StoreMenuCategoryInfoDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface StoreMenuCategoryRepository extends JpaRepository<StoreMenuCategory, Long> {
    boolean existsByIdAndStore_Id(Long storeMenuCategoryId, Long storeId);

    @Modifying
    @Transactional
    @Query("UPDATE StoreMenuCategory smc SET smc.order = smc.order - 1 " +
            "WHERE smc.order > :order AND smc.store.id = :storeId")
    void decrementOrdersGreaterThan(@Param("order") Integer order, @Param("storeId") Long storeId);

    @Query("SELECT new com.example.storeme.fo_domain.storemenu.dto.StoreMenuCategoryListResponseDto.StoreMenuCategoryInfoDto " +
            "(s.id, s.category, s.order) " +
            "FROM StoreMenuCategory s WHERE s.store.id = :storeId")
    List<StoreMenuCategoryInfoDto> findStoreMenuCategoryInfoByUserId(@Param("storeId") Long storeId);
}
