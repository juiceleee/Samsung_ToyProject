package com.samsung.bixby.mediastreaming.demo.dao.entitiy;


import com.samsung.bixby.mediastreaming.demo.dao.entitiy.BasketEntity;
import com.samsung.bixby.mediastreaming.demo.dao.entitiy.ItemEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userid;

    @Column
    private String username;

    @Column
    private String password;

    @OneToMany
    private List<BasketEntity> baskets = new ArrayList<>();

    public void addBasket(BasketEntity basket){
        this.baskets.add(basket);
    }

    public void removeBasket(BasketEntity basket){
        this.baskets.remove(basket);
    }
}
