package com.jc.allnewjcclass.entities;

import java.util.ArrayList;

public class PhraseHeader {

    private String name;
    private ArrayList<Phrase> phraseList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Phrase> getPhraseList() {
        return phraseList;
    }

    public void setPhraseList(ArrayList<Phrase> phraseList) {
        this.phraseList = phraseList;
    }
}
