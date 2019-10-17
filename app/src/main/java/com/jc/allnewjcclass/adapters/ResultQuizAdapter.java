package com.jc.allnewjcclass.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jc.allnewjcclass.R;
import com.jc.allnewjcclass.customs.CustomDialog;
import com.jc.allnewjcclass.customs.CustomSound;

import java.util.ArrayList;
import java.util.Map;

public class ResultQuizAdapter extends RecyclerView.Adapter<ResultQuizAdapter.ResultViewHolder> {

    private CustomDialog dialog;
    private CustomSound sound;

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Map> resultList;

    public ResultQuizAdapter() {
    }

    public ResultQuizAdapter(Context context, ArrayList<Map> resultList) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.resultList = resultList;
        dialog = new CustomDialog(context);
        sound = new CustomSound(context);
    }

    @NonNull
    @Override
    public ResultQuizAdapter.ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_quiz_result, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultQuizAdapter.ResultViewHolder holder, int position) {

        String correct = resultList.get(position).get("CORRECT").toString();
        String question_id = resultList.get(position).get("QUESTION_ID").toString();
        final String question = resultList.get(position).get("QUESTION").toString();
        final String type = resultList.get(position).get("QUESTION_TYPE").toString();
        final String answer = resultList.get(position).get("RIGHT_ANSWER").toString();
        final String chosen = resultList.get(position).get("CHOSEN_ANSWER").toString();
        final String correction = resultList.get(position).get("CORRECTION").toString();

        if (type.equals("boolean")) {
            holder.setAnswers(answer.toUpperCase());
            holder.setChosens(chosen.toUpperCase());
        } else {
            holder.setAnswers(answer);
            holder.setChosens(chosen);
        }

        if (type.equals("image")) {
            holder.setQuestions("Image Question");
        } else {
            holder.setQuestions(question);
        }

        holder.setTypes(type);
        holder.setBgColors(correct);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                dialog.showDialogResultQuiz(type, question, answer, chosen, correction);
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {

        View mView;

        ResultViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        void setBgColors(String string) {
            CardView cardView = itemView.findViewById(R.id.result_container);
            if (string.equals("correct")) {
                cardView.setBackgroundColor(context.getResources().getColor(R.color.correct));
            } else {
                cardView.setBackgroundColor(context.getResources().getColor(R.color.wrong));
            }
        }

        void setQuestions(String string) {

            if (string.length() >= 30) {
                string = string.substring(0, 30) + " ...";
            }

            TextView textView = itemView.findViewById(R.id.result_question);
            textView.setText(string);
        }

        void setAnswers(String string) {
            TextView textView = itemView.findViewById(R.id.result_answer);
            textView.setText(string);
        }

        void setChosens(String string) {
            TextView textView = itemView.findViewById(R.id.result_chosen);
            textView.setText(string);
        }

        void setTypes(String string) {
            TextView textView = itemView.findViewById(R.id.result_type);
            textView.setText(string);
        }
    }
}
