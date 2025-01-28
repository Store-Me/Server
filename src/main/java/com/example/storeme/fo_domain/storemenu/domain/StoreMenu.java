package com.example.storeme.fo_domain.storemenu.domain;

import com.example.storeme.fo_domain.storemenu.constant.MenuPriceType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "store_menus")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class StoreMenu {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_menu_categorys_id")
    private StoreMenuCategory storeMenuCategory;

    @Column(name = "menu_name", nullable = false, length = 15*3, unique = true)
    private String name;

    @Column(name = "menu_order", nullable = false)
    private Integer order;

    @Enumerated(EnumType.STRING)
    @Column(name = "price_type", nullable = false)
    private MenuPriceType priceType;

    @Column(name = "fixed_price")
    private Integer fixedPrice;

    @Column(name = "rangeMaxPrice")
    private Integer rangeMaxPrice;

    @Column(name = "rangeMinPrice")
    private Integer rangeMinPrice;

    @Column(name = "menu_image_url", length = 2048)
    private String imageUrl;

    @Column(name = "description", length = 150)
    private String description;

    @Column(name = "is_signature", nullable = false)
    private Boolean isSignature;

    @Column(name = "is_popular", nullable = false)
    private Boolean isPopular;

    @Column(name = "is_recommended", nullable = false)
    private Boolean isRecommended;
}
