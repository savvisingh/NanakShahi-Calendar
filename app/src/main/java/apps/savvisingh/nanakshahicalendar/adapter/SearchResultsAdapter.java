package apps.savvisingh.nanakshahicalendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import apps.savvisingh.nanakshahicalendar.R;
import apps.savvisingh.nanakshahicalendar.model.Event;
import io.realm.RealmResults;

/**
 * Created by SavviSingh on 22/05/17.
 */

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {

    RealmResults<Event> results;

    public SearchResultsAdapter(RealmResults<Event> results){
        this.results = results;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.serach_results_item, parent, false);
        return new SearchResultsAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Event event = results.get(position);
        holder.eventTitle.setText(event.getTitle());
        holder.eventDescription.setText(event.getDescription());


        Calendar cal = Calendar.getInstance();
        cal.set(event.year, event.month, event.day);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(cal.getTime());

        holder.eventDate.setText(formattedDate);

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView eventTitle, eventDescription, eventDate;

        public ViewHolder(View itemView) {
            super(itemView);
            eventTitle = (TextView) itemView.findViewById(R.id.event_title);
            eventDescription = (TextView) itemView.findViewById(R.id.event_description);
            eventDate = (TextView) itemView.findViewById(R.id.event_date);
        }
    }
}
