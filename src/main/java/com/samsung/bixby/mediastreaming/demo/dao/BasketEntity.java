package com.samsung.bixby.mediastreaming.demo.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class BasketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rownum;

    @Column
    private Integer userid;

    @Column
    private Integer itemid;

    @Column
    private Integer itemcnt;
}
