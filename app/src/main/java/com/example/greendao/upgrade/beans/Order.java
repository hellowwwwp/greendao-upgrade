package com.example.greendao.upgrade.beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/5/19
 */
@Entity(nameInDb = "T_ORDER")
public class Order {

    @Id
    private String id;

    @Generated(hash = 1806380022)
    public Order(String id) {
        this.id = id;
    }

    @Generated(hash = 1105174599)
    public Order() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
