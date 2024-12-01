package com.example.storeme.fo_domain.storemenu.repository;

import com.example.storeme.fo_domain.storemenu.domain.StoreMenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreMenuCategoryRepository extends JpaRepository<StoreMenuCategory, Long> {
    boolean existsByIdAndStore_Id(Long storeMenuCategoryId, Long storeId);
}
