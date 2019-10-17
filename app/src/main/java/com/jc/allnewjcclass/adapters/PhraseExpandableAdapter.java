package com.jc.allnewjcclass.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.jc.allnewjcclass.R;
import com.jc.allnewjcclass.customs.CustomSound;
import com.jc.allnewjcclass.entities.Phrase;
import com.jc.allnewjcclass.entities.PhraseHeader;

import java.util.ArrayList;

import io.paperdb.Paper;

public class PhraseExpandableAdapter extends BaseExpandableListAdapter {

    private CustomSound sound;

    private Context context;
    private ArrayList<PhraseHeader> phraseHeadersList;

    public PhraseExpandableAdapter(Context context, ArrayList<PhraseHeader> phraseHeadersList) {
        this.context = context;
        this.phraseHeadersList = phraseHeadersList;
        sound = new CustomSound(context);
    }

    @Override
    public int getGroupCount() {
        return phraseHeadersList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<Phrase> phraseList = phraseHeadersList.get(groupPosition).getPhraseList();
        return phraseList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return phraseHeadersList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<Phrase> phraseList = phraseHeadersList.get(groupPosition).getPhraseList();
        return phraseList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        PhraseHeader phraseHeader = (PhraseHeader) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.list_phrases_heading, null);
        }

        TextView heading = convertView.findViewById(R.id.heading_phrases_name);
        heading.setText(phraseHeader.getName());

        return convertView;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Phrase phrase = (Phrase) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_phrases_child, null);
        }

        TextView japan = convertView.findViewById(R.id.child_phrases_japan);
        TextView romaji = convertView.findViewById(R.id.child_phrases_romaji);
        TextView desc = convertView.findViewById(R.id.child_phrases_desc);

        Paper.init(context);
        String language = Paper.book().read("language");
        if (language.equals("en")) {
            japan.setText("Japanese : " + phrase.getJapan());
            romaji.setText("Romaji : " + phrase.getRomaji());
            desc.setText("Description : " + phrase.getDescription());
        } else if (language.equals("in")) {
            japan.setText("Bahasa Jepang : " + phrase.getJapan());
            romaji.setText("Romaji : " + phrase.getRomaji());
            desc.setText("Deskripsi : " + phrase.getDescription());
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
