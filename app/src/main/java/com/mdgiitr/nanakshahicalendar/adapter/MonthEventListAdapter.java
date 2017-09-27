package com.mdgiitr.nanakshahicalendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mdgiitr.nanakshahicalendar.model.CalenderEvent;
import com.mdgiitr.nanakshahicalendar.util.AppConstants;

import java.util.ArrayList;

import apps.savvisingh.nanakshahicalendar.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Created by SavviSingh on 17/09/17.
 */

public class MonthEventListAdapter extends RecyclerView.Adapter<MonthEventListAdapter.ItemHolder> {

    private ArrayList<CalenderEvent> calenderEvents;

    public MonthEventListAdapter(ArrayList<CalenderEvent> calenderEvents){
        this.calenderEvents = calenderEvents;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.month_list_item, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {

        CalenderEvent event = calenderEvents.get(position);

        holder.textViewDate.setText(String.valueOf(event.getDay()));
        holder.textViewTitle.setText(event.getDescription());

        switch (event.getEvent_type()){
            case AppConstants.MASYA:
                holder.imageView.setImageResource(R.drawable.circle_black);
                break;
            case AppConstants.SAGRANDH:
                holder.imageView.setImageResource(R.drawable.circle_orange_dark);
                break;
            case AppConstants.GURUPURAB:
                holder.imageView.setImageResource(R.drawable.circle_red);
                break;
            case AppConstants.PURANMASHI:
                holder.imageView.setImageResource(R.drawable.circle_orange);
                break;
            case AppConstants.HISTORICAL_DAYS:
                holder.imageView.setImageResource(R.drawable.circle_blue);
                break;
            case AppConstants.GOVERNMENT_HOLIDAY:
                holder.imageView.setImageResource(R.drawable.circle_purple);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return calenderEvents.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.event_title)
        TextView textViewTitle;

        @BindView(R.id.event_date)
        TextView textViewDate;

        @BindView(R.id.imageView)
        ImageView imageView;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
