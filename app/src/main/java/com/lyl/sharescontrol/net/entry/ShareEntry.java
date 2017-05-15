package com.lyl.sharescontrol.net.entry;

import com.litesuits.orm.db.annotation.Default;
import com.litesuits.orm.db.annotation.Ignore;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by lyl on 2017/5/15.
 */

@Table("ShareEntry")
public class ShareEntry {

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;

    private String code;
    private String name;

    @Ignore
    private String nowPrice;
    private String buyPrice;
    private String shareTallRatio;
    private String shareLowRatio;
    private String shareTall;
    private String shareLow;
    private String createDate;
    @Default("false")
    private Boolean isDelete;

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public String getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(String nowPrice) {
        this.nowPrice = nowPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getShareTallRatio() {
        return shareTallRatio;
    }

    public void setShareTallRatio(String shareTallRatio) {
        this.shareTallRatio = shareTallRatio;
    }

    public String getShareLowRatio() {
        return shareLowRatio;
    }

    public void setShareLowRatio(String shareLowRatio) {
        this.shareLowRatio = shareLowRatio;
    }

    public String getShareTall() {
        return shareTall;
    }

    public void setShareTall(String shareTall) {
        this.shareTall = shareTall;
    }

    public String getShareLow() {
        return shareLow;
    }

    public void setShareLow(String shareLow) {
        this.shareLow = shareLow;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
