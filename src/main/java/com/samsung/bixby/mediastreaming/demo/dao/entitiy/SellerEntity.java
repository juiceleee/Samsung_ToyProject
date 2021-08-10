package com.samsung.bixby.mediastreaming.demo.dao.entitiy;

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
public class SellerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private String password;

    @OneToMany
    @JoinColumn
    @Builder.Default private List<ItemEntity> items = new ArrayList<>();

    public void addItem(ItemEntity item){
        this.items.add(item);
    }

    public void removeItem(ItemEntity item){
        this.items.remove(item);
    }

    public void nullItem(){this.items=new ArrayList<>();}
}
