package com.jc.allnewjcclass.entities;

public class Phrase {
    private String id;
    private String english;
    private String indonesia;
    private String japan;
    private String romaji;
    private String category;
    private String description;

    public Phrase() {
    }

    public Phrase(String id, String english, String indonesia, String japan, String romaji, String category, String description) {
        this.id = id;
        this.english = english;
        this.indonesia = indonesia;
        this.japan = japan;
        this.romaji = romaji;
        this.category = category;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getIndonesia() {
        return indonesia;
    }

    public void setIndonesia(String indonesia) {
        this.indonesia = indonesia;
    }

    public String getJapan() {
        return japan;
    }

    public void setJapan(String japan) {
        this.japan = japan;
    }

    public String getRomaji() {
        return romaji;
    }

    public void setRomaji(String romaji) {
        this.romaji = romaji;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
