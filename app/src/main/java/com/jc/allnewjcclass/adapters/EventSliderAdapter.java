package com.jc.allnewjcclass.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jc.allnewjcclass.R;
import com.jc.allnewjcclass.entities.Message;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventSliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<String> nameList;
    ArrayList<String> imgList;
    ArrayList<String> dateList;

    public EventSliderAdapter(Context context, ArrayList<String> nameList, ArrayList<String> imgList,
                              ArrayList<String> dateList) {
        this.context = context;
        this.nameList = nameList;
        this.imgList = imgList;
        this.dateList = dateList;
    }

    @Override
    public int getCount() {
        return nameList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.slide_event, container, false);

        final TextView name = view.findViewById(R.id.event_name);
        final ImageView img = view.findViewById(R.id.event_image);
        final TextView date = view.findViewById(R.id.event_date);

        name.setText(nameList.get(position));
        Glide.with(context).load(imgList.get(position)).into(img);
        date.setText(dateList.get(position));

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View) object);
    }
}
