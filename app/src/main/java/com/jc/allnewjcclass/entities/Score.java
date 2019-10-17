package com.jc.allnewjcclass.entities;

public class Score {
    private String id;
    private int bestkanji;
    private int lastkanji;
    private int bestphrases;
    private int lastphrases;
    private int bestgrammar;
    private int lastgrammar;

    public Score() {
    }

    public Score(String id, int bestkanji, int lastkanji, int bestphrases, int lastphrases, int bestgrammar, int lastgrammar) {
        this.id = id;
        this.bestkanji = bestkanji;
        this.lastkanji = lastkanji;
        this.bestphrases = bestphrases;
        this.lastphrases = lastphrases;
        this.bestgrammar = bestgrammar;
        this.lastgrammar = lastgrammar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBestkanji() {
        return bestkanji;
    }

    public void setBestkanji(int bestkanji) {
        this.bestkanji = bestkanji;
    }

    public int getLastkanji() {
        return lastkanji;
    }

    public void setLastkanji(int lastkanji) {
        this.lastkanji = lastkanji;
    }

    public int getBestphrases() {
        return bestphrases;
    }

    public void setBestphrases(int bestphrases) {
        this.bestphrases = bestphrases;
    }

    public int getLastphrases() {
        return lastphrases;
    }

    public void setLastphrases(int lastphrases) {
        this.lastphrases = lastphrases;
    }

    public int getBestgrammar() {
        return bestgrammar;
    }

    public void setBestgrammar(int bestgrammar) {
        this.bestgrammar = bestgrammar;
    }

    public int getLastgrammar() {
        return lastgrammar;
    }

    public void setLastgrammar(int lastgrammar) {
        this.lastgrammar = lastgrammar;
    }
}
