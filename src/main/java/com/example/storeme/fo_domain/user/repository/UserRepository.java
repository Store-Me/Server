package com.example.storeme.fo_domain.user.repository;

import com.example.storeme.fo_domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByAccountId(String accountId);

    Optional<User> findByKakaoId(String kakaoId);

    boolean existsByAccountId(String accountId);


}
