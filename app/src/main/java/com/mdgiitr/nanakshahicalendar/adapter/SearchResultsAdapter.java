package com.mdgiitr.nanakshahicalendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import apps.savvisingh.nanakshahicalendar.R;
import com.mdgiitr.nanakshahicalendar.model.CalenderEvent;
import io.realm.RealmResults;

/**
 * Created by SavviSingh on 22/05/17.
 */

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {

    RealmResults<CalenderEvent> results;

    private OnItemClickListener listener;

    public SearchResultsAdapter(RealmResults<CalenderEvent> results, OnItemClickListener listener){
        this.results = results;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.serach_results_item, parent, false);
        return new SearchResultsAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        CalenderEvent calenderEvent = results.get(position);
        holder.eventTitle.setText(calenderEvent.getTitle());
        holder.eventDescription.setText(calenderEvent.getDescription());


        Calendar cal = Calendar.getInstance();
        cal.set(calenderEvent.year, calenderEvent.month, calenderEvent.day);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(cal.getTime());

        holder.eventDate.setText(formattedDate);

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView eventTitle, eventDescription, eventDate;

        public ViewHolder(View itemView) {
            super(itemView);
            eventTitle = (TextView) itemView.findViewById(R.id.event_title);
            eventDescription = (TextView) itemView.findViewById(R.id.event_description);
            eventDate = (TextView) itemView.findViewById(R.id.event_date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(listener!=null){
                CalenderEvent calenderEvent = results.get(getAdapterPosition());
                listener.onClick(calenderEvent.getId());
            }
        }
    }

    public interface OnItemClickListener{
        void onClick(int eventId);
    }
}
