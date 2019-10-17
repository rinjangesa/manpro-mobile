package com.jc.allnewjcclass.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jc.allnewjcclass.R;
import com.jc.allnewjcclass.entities.Grammar;

import java.util.ArrayList;

public class GrammarListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Grammar> grammars;

    public GrammarListAdapter(Context context, LayoutInflater layoutInflater, ArrayList<Grammar> grammars) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.grammars = grammars;
    }

    @Override
    public int getCount() {
        return grammars.size();
    }

    @Override
    public Object getItem(int position) {
        return grammars.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return layoutInflater.inflate(R.layout.list_grammar, parent, false);
    }
}
