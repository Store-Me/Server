package com.example.storeme.fo_domain.store.domain;

import com.example.storeme.fo_domain.storemenu.domain.StoreMenuCategory;
import com.example.storeme.fo_domain.storeimage.domain.StoreImage;
import com.example.storeme.fo_domain.user.constant.StoreCategory;
import com.example.storeme.fo_domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "store_infos")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Store {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreImage> storeImageList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreMenuCategory> storeMenuCategoryList = new ArrayList<>();

    @Column(name = "store_name", nullable = false, length = 60, unique = true)
    private String name;

    @Column(name = "store_profile_image_url", length = 2048)
    private String profileImageUrl;

    @Column(name = "store_featured_image_url", length = 2048)
    private String featuredImageUrl;

    @Column(name = "store_banner_image_url", length = 2048)
    private String bannerImageUrl;

    @Lob
    @Column(name = "store_description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "store_category", nullable = false, length = 20)
    private StoreCategory category;

    @Column(name = "store_detail_category", length = 45)
    private String detailCategory;

    @Column(name = "store_location", nullable = false, length = 60)
    private String location;

    @Column(name = "store_location_code", nullable = false)
    private Long locationCode;

    @Column(name = "store_location_address", length = 300)
    private String locationAddress;

    @Column(name = "store_location_detail", length = 300)
    private String locationDetail;

    @Column(name = "store_lat")
    private Double lat;

    @Column(name = "store_lng")
    private Double lng;

    @Column(name = "store_phone_number", length = 13)
    private String phoneNumber;

    @Column(name = "store_notice", length = 300)
    private String notice;

    @Column(name = "store_intro", length = 300)
    private String intro;

    public void addStoreImage(StoreImage storeImage){
        if (storeImage.getStore() != this) {
            storeImageList.add(storeImage);
            storeImage.setStore(this);
        }
    }

    public void addStoreImageList(List<StoreImage> storeImageList){
        if(storeImageList != null)
            storeImageList.forEach(this::addStoreImage);
    }

    public void addStoreMenuCategory(StoreMenuCategory storeMenuCategory){
        if (storeMenuCategory.getStore() != this) {
            storeMenuCategoryList.add(storeMenuCategory);
            storeMenuCategory.setStore(this);
        }
    }
}
