package com.example.storeme.fo_domain.store.repository;

import com.example.storeme.fo_domain.store.domain.Store;
import com.example.storeme.fo_domain.store.dto.StoreInfoListResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    boolean existsByIdAndUserId(Long storeId, Long userId);

    @Query("SELECT new com.example.storeme.fo_domain.store.dto.StoreInfoListResponseDto$StoreInfoDto " +
            "(s.id, s.name, s.profileImageUrl) " +
            "FROM Store s WHERE s.user.id = :userId")
    List<StoreInfoListResponseDto.StoreInfoDto> findStoreInfoByUserId(@Param("userId") Long userId);
}
