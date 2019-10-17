package com.jc.allnewjcclass.entities;

import java.util.Date;

public class Character {

    private String id;
    private String name;
    private String japanname;
    private int price;
    private int exp;
    private int money;
    private String oriimg;
    private String fullimg;
    private String thumbimg;
    private String descriptionEN;
    private String descriptionID;
    private String descriptionJP;
    private Date createdAt;

    public Character() {
    }

    public Character(String id, String name, String japanname, int price, int exp, int money, String oriimg, String fullimg, String thumbimg, String descriptionEN, String descriptionID, String descriptionJP, Date createdAt) {
        this.id = id;
        this.name = name;
        this.japanname = japanname;
        this.price = price;
        this.exp = exp;
        this.money = money;
        this.oriimg = oriimg;
        this.fullimg = fullimg;
        this.thumbimg = thumbimg;
        this.descriptionEN = descriptionEN;
        this.descriptionID = descriptionID;
        this.descriptionJP = descriptionJP;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJapanname() {
        return japanname;
    }

    public void setJapanname(String japanname) {
        this.japanname = japanname;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getOriimg() {
        return oriimg;
    }

    public void setOriimg(String oriimg) {
        this.oriimg = oriimg;
    }

    public String getFullimg() {
        return fullimg;
    }

    public void setFullimg(String fullimg) {
        this.fullimg = fullimg;
    }

    public String getThumbimg() {
        return thumbimg;
    }

    public void setThumbimg(String thumbimg) {
        this.thumbimg = thumbimg;
    }

    public String getDescriptionEN() {
        return descriptionEN;
    }

    public void setDescriptionEN(String descriptionEN) {
        this.descriptionEN = descriptionEN;
    }

    public String getDescriptionID() {
        return descriptionID;
    }

    public void setDescriptionID(String descriptionID) {
        this.descriptionID = descriptionID;
    }

    public String getDescriptionJP() {
        return descriptionJP;
    }

    public void setDescriptionJP(String descriptionJP) {
        this.descriptionJP = descriptionJP;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
