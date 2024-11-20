package com.example.storeme.fo_domain.storeimage.domain;

import com.example.storeme.fo_domain.store.domain.Store;
import com.example.storeme.fo_domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "store_images")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class StoreImage {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "store_image_url", nullable = false, length = 2048)
    private String imageUrl;
}
