package com.example.storeme.fo_domain.storeimage.repository;

import com.example.storeme.fo_domain.storeimage.domain.StoreImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreImageRepository extends JpaRepository<StoreImage, Long> {
}
