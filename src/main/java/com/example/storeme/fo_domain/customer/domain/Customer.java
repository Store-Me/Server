package com.example.storeme.fo_domain.customer.domain;

import com.example.storeme.fo_domain.store.domain.Store;
import com.example.storeme.fo_domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer_infos")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Customer {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "customer")
    private User user;

    @Column(name = "nickname", nullable = false, length = 30)
    private String nickname;

    @Column(name = "profile_image_url", length = 2048)
    private String profileImageUrl;
}
