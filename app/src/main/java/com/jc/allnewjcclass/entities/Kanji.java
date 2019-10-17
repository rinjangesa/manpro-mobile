package com.jc.allnewjcclass.entities;

public class Kanji {

    private String id;
    private String grade;
    private String kanji;
    private String onyomi;
    private String kunyomi;
    private int stroke;
    private String english;
    private String indonesia;
    private String example;
    private String animation;
    private String zJson;

    public Kanji() {
    }

    public Kanji(String id, String grade, String kanji, String onyomi, String kunyomi, int stroke, String english, String indonesia, String example, String animation, String zJson) {
        this.id = id;
        this.grade = grade;
        this.kanji = kanji;
        this.onyomi = onyomi;
        this.kunyomi = kunyomi;
        this.stroke = stroke;
        this.english = english;
        this.indonesia = indonesia;
        this.example = example;
        this.animation = animation;
        this.zJson = zJson;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getKanji() {
        return kanji;
    }

    public void setKanji(String kanji) {
        this.kanji = kanji;
    }

    public String getOnyomi() {
        return onyomi;
    }

    public void setOnyomi(String onyomi) {
        this.onyomi = onyomi;
    }

    public String getKunyomi() {
        return kunyomi;
    }

    public void setKunyomi(String kunyomi) {
        this.kunyomi = kunyomi;
    }

    public int getStroke() {
        return stroke;
    }

    public void setStroke(int stroke) {
        this.stroke = stroke;
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

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getAnimation() {
        return animation;
    }

    public void setAnimation(String animation) {
        this.animation = animation;
    }

    public String getzJson() {
        return zJson;
    }

    public void setzJson(String zJson) {
        this.zJson = zJson;
    }
}
