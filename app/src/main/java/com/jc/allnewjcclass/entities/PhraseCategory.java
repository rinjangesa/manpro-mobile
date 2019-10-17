package com.jc.allnewjcclass.entities;

import java.util.ArrayList;

public class PhraseCategory {
    private String id;
    private String english;
    private String indonesia;
    private String japan;
    private String romaji;
    private String img;

    public PhraseCategory() {
    }

    public PhraseCategory(String id, String english, String indonesia, String japan, String romaji, String img) {
        this.id = id;
        this.english = english;
        this.indonesia = indonesia;
        this.japan = japan;
        this.romaji = romaji;
        this.img = img;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
