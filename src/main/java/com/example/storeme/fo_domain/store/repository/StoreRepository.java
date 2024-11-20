package com.example.storeme.fo_domain.store.repository;

import com.example.storeme.fo_domain.store.domain.Store;
import com.example.storeme.fo_domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
