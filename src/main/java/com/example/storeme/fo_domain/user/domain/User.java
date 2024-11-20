package com.example.storeme.fo_domain.user.domain;

import com.example.storeme.fo_domain.customer.domain.Customer;
import com.example.storeme.fo_domain.store.domain.Store;
import com.example.storeme.fo_domain.user.constant.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class User{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Store> storeList = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "customer_id", unique = true)
    private Customer customer;

    @Column(name = "account_id", length = 20, unique = true)
    private String accountId;

    @Column(name = "kakao_id", length = 20, unique = true)
    private String kakaoId;

    @Column(name = "password", length = 60)
    private String password;

    @Column(name = "phone_number", nullable = false, length = 13, unique = true)
    private String phoneNumber;

    @Column(name = "privacy_consent", nullable = false)
    private Boolean privacyConsent;

    @Column(name = "marketing_consent", nullable = false)
    private Boolean marketingConsent;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false, length = 20)
    private RoleType roleType;

    public void setCustomer(Customer customer){
        if(this.customer != null){
            customer.setUser(null);
        }
        this.customer = customer;
        customer.setUser(this);
    }

    public void addStore(Store store){
        if (store.getUser() != this) {
            storeList.add(store);
            store.setUser(this);
        }
    }

}
