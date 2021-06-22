package com.android.diaryapp.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.diaryapp.AddNoteActivity;
import com.android.diaryapp.MainActivity;
import com.android.diaryapp.R;
import com.android.diaryapp.model.PostMessage;
import com.google.android.material.card.MaterialCardView;

import java.io.Serializable;
import java.util.ArrayList;

import static androidx.core.content.ContextCompat.startActivity;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    public static ArrayList<PostMessage> mPosts;
    private MainActivity context;
    public static ArrayList<String> key;


    public PostsAdapter(ArrayList<PostMessage> postMessages, MainActivity context,ArrayList<String> key) {

        this.mPosts = postMessages;
        this.context=context;
        this.key=key;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new ViewHolder(v,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        int day=today.monthDay-mPosts.get(position).getDate().getDay();
        if(day>1)   holder.tvHistoryTime.setText(String.valueOf(day)+" days ago");
        if(day==0)  holder.tvHistoryTime.setText("Today");
        if(day==1)  holder.tvHistoryTime.setText(String.valueOf(day)+" day ago");
        int hour=mPosts.get(position).getTime().getHour();
        String check="AM";
        if(hour>12) check="PM";
        holder.tvTime.setText(String.valueOf((hour>12)?hour-12:hour)+":"+String.valueOf(mPosts.get(position).getTime().getMinute())+check);
        holder.tvTitle.setText(mPosts.get(position).getTittle());
        holder.tvContent.setText(mPosts.get(position).getContent());
        holder.background.setBackgroundColor(mPosts.get(position).getBackground());
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvHistoryTime,tvTime,tvTitle,tvContent;
        private LinearLayout background;
        View v;
        MainActivity activity;

        public ViewHolder(@NonNull View view,MainActivity mainActivity) {
            super(view);
            this.activity=mainActivity;
            v=view;

            tvHistoryTime=v.findViewById(R.id.tv_history_time);
            tvTime=v.findViewById(R.id.tv_time_notes);
            tvTitle=v.findViewById(R.id.tv_tittle);
            tvContent=v.findViewById(R.id.tv_content);
            background=(LinearLayout)v.findViewById(R.id.background_main);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(activity,AddNoteActivity.class);
                    intent.putExtra("tittle",  mPosts.get(getAdapterPosition()).getTittle());
                    intent.putExtra("content",  mPosts.get(getAdapterPosition()).getContent());
                    intent.putExtra("key",  key.get(getAdapterPosition()));
                    intent.putExtra("bg",  mPosts.get(getAdapterPosition()).getBackground());
                    intent.putExtra("day",mPosts.get(getAdapterPosition()).getDate().getDay());
                    intent.putExtra("month",mPosts.get(getAdapterPosition()).getDate().getMonth());
                    intent.putExtra("year",mPosts.get(getAdapterPosition()).getDate().getYear());
                    intent.putExtra("hour",mPosts.get(getAdapterPosition()).getTime().getHour());
                    intent.putExtra("minute",mPosts.get(getAdapterPosition()).getTime().getMinute());
                    activity.startActivity(intent);
                }
            });

        }
    }
}
