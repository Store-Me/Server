package com.example.storeme.fo_domain.user.domain;

import com.example.storeme.fo_domain.user.constant.RoleType;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "account_id", length = 20, unique = true)
    private String accountId;

    @Column(name = "kakao_id", length = 20, unique = true)
    private String kakaoId;

    @Column(name = "password", length = 60)
    private String password;

    @Column(name = "phone_number", nullable = false, length = 13, unique = true)
    private String phoneNumber;

    @Column(name = "nickname", nullable = false, length = 30)
    private String nickname;

    @Column(name = "profile_image_url", length = 2048)
    private String profileImageUrl;

    @Column(name = "privacy_consent", nullable = false)
    private Boolean privacyConsent;

    @Column(name = "marketing_consent", nullable = false)
    private Boolean marketingConsent;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false, length = 20)
    private RoleType roleType;

}
