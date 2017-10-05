package com.mdgiitr.nanakshahicalendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import apps.savvisingh.nanakshahicalendar.R;

import com.mdgiitr.nanakshahicalendar.model.Event;
import com.mdgiitr.nanakshahicalendar.util.AppConstants;

import io.realm.RealmResults;

/**
 * Created by Günhan on 28.02.2016.
 */
public class BottomSheetAdapter extends RecyclerView.Adapter<BottomSheetAdapter.ItemHolder> {
    private OnItemClickListener onItemClickListener;
    private RealmResults<Event> results;

    public BottomSheetAdapter(RealmResults<Event> results) {
        this.results = results;
    }

    @Override
    public BottomSheetAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sheet_main, parent, false);
        return new ItemHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(BottomSheetAdapter.ItemHolder holder, int position) {
        holder.bind(results.get(position));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(ItemHolder item, int position);
    }

    public static class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private BottomSheetAdapter adapter;
        TextView eventTitle, eventDescription;
        ImageView imageView;

        public ItemHolder(View itemView, BottomSheetAdapter parent) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.adapter = parent;

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            eventTitle = (TextView) itemView.findViewById(R.id.event_title);
            eventDescription = (TextView) itemView.findViewById(R.id.event_description);
        }

        public void bind(Event item) {
            eventTitle.setText(item.getTitle());
            if(TextUtils.isEmpty(item.getDescription()))
                eventDescription.setVisibility(View.GONE);
            else
                eventDescription.setText(item.getDescription());
            switch (item.getEvent_type()){
                case AppConstants.MASYA:
                    imageView.setImageResource(R.drawable.circle_black);
                    break;
                case AppConstants.SAGRANDH:
                    imageView.setImageResource(R.drawable.circle_orange_dark);
                    break;
                case AppConstants.GURUPURAB:
                    imageView.setImageResource(R.drawable.circle_red);
                    break;
                case AppConstants.PURANMASHI:
                    imageView.setImageResource(R.drawable.circle_orange);
                    break;
                case AppConstants.HISTORICAL_DAYS:
                    imageView.setImageResource(R.drawable.circle_blue);
                    break;
                case AppConstants.GOVERNMENT_HOLIDAY:
                    imageView.setImageResource(R.drawable.circle_purple);
                    break;
            }
        }

        @Override
        public void onClick(View v) {
            final OnItemClickListener listener = adapter.getOnItemClickListener();
            if (listener != null) {
                listener.onItemClick(this, getAdapterPosition());
            }
        }
    }
}
