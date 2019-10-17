package com.jc.allnewjcclass.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jc.allnewjcclass.R;
import com.jc.allnewjcclass.customs.CustomDialog;
import com.jc.allnewjcclass.customs.CustomSound;

public class KanaRecyclerAdapter extends RecyclerView.Adapter<KanaRecyclerAdapter.ViewHolder> {

    private CustomDialog dialog;
    private CustomSound sound;

    private Context context;
    private LayoutInflater inflater;
    private String[] kanaList;
    private String[] strokeList;
    private String[] romajiList;
    private int[] animationList;
    private int[] soundList;

    public KanaRecyclerAdapter() {
    }

    public KanaRecyclerAdapter(Context context, String[] kanaList, String[] strokeList, String[] romajiList, int[] animationList, int[] soundList) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.kanaList = kanaList;
        this.strokeList = strokeList;
        this.romajiList = romajiList;
        this.animationList = animationList;
        this.soundList = soundList;
        dialog = new CustomDialog(context);
        sound = new CustomSound(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_kana, parent, false);
        return new KanaRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final String kana = kanaList[position];
        holder.setKana(kana);
        holder.setRomaji(romajiList[position]);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                if (!kana.equals(" ")) {
                    dialog.showDialogKana(kana, strokeList[position], romajiList[position], animationList[position], soundList[position]);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return kanaList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        void setKana(String string) {
            TextView textView = mView.findViewById(R.id.single_kana);
            textView.setText(string);
        }

        void setRomaji(String string) {
            TextView textView = mView.findViewById(R.id.single_romaji);
            textView.setText(string);
        }
    }
}
