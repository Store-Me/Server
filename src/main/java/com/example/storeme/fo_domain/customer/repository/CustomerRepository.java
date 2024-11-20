package com.example.storeme.fo_domain.customer.repository;

import com.example.storeme.fo_domain.customer.domain.Customer;
import com.example.storeme.fo_domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
