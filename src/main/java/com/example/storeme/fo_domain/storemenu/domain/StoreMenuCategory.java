package com.example.storeme.fo_domain.storemenu.domain;

import com.example.storeme.fo_domain.store.domain.Store;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "store_menu_categorys")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class StoreMenuCategory {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_infos_id")
    private Store store;

    @OneToMany(mappedBy = "storeMenuCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<StoreMenu> storeMenuList = new ArrayList<>();

    @Column(name = "menu_category", nullable = false, length = 60, unique = true)
    private String category;

    @Column(name = "menu_category_order", nullable = false)
    private Integer order;

    public void addStoreMenu(StoreMenu storeMenu){
        if (storeMenu.getStoreMenuCategory() != this) {
            storeMenuList.add(storeMenu);
            storeMenu.setStoreMenuCategory(this);
        }
    }
}
