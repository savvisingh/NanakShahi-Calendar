package apps.savvisingh.nanakshahicalendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import apps.savvisingh.nanakshahicalendar.R;
import apps.savvisingh.nanakshahicalendar.classes.Event;
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
        TextView textView;
        ImageView imageView;

        public ItemHolder(View itemView, BottomSheetAdapter parent) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.adapter = parent;

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }

        public void bind(Event item) {
            textView.setText(item.getTitle());
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
