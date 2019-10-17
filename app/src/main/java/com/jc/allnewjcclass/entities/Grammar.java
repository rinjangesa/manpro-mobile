package com.jc.allnewjcclass.entities;

public class Grammar {

    private String id;
    private String title;
    private String tier;
    private String description;

    public Grammar() {
    }

    public Grammar(String id, String title, String tier, String description) {
        this.id = id;
        this.title = title;
        this.tier = tier;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
