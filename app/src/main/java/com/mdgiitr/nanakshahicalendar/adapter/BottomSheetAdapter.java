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
import io.realm.RealmResults;

import static com.mdgiitr.nanakshahicalendar.util.AppConstants.GOVERNMENT_HOLIDAY;
import static com.mdgiitr.nanakshahicalendar.util.AppConstants.GURUPURAB;
import static com.mdgiitr.nanakshahicalendar.util.AppConstants.HISTORICAL_DAYS;
import static com.mdgiitr.nanakshahicalendar.util.AppConstants.MASYA;
import static com.mdgiitr.nanakshahicalendar.util.AppConstants.PURANMASHI;
import static com.mdgiitr.nanakshahicalendar.util.AppConstants.SAGRANDH;

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
                case MASYA: imageView.setImageResource(R.drawable.khanda_black);
                    break;
                case SAGRANDH: imageView.setImageResource(R.drawable.khanda_violet);
                    break;
                case GURUPURAB: imageView.setImageResource(R.drawable.khanda_red);
                    break;
                case PURANMASHI: imageView.setImageResource(R.drawable.khanda_yellow);
                    break;
                case HISTORICAL_DAYS: imageView.setImageResource(R.drawable.khanda_blue);
                    break;
                case GOVERNMENT_HOLIDAY: imageView.setImageResource(R.drawable.khanda_blue);
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
