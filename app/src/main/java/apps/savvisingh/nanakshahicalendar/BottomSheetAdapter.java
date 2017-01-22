package apps.savvisingh.nanakshahicalendar;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

/**
 * Created by Günhan on 28.02.2016.
 */
public class BottomSheetAdapter extends RecyclerView.Adapter<BottomSheetAdapter.ItemHolder> {
    private List<SampleModel> list;
    private OnItemClickListener onItemClickListener;

    public BottomSheetAdapter(List<SampleModel> list) {
        this.list = list;
    }

    @Override
    public BottomSheetAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sheet_main, parent, false);
        return new ItemHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(BottomSheetAdapter.ItemHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
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

        public void bind(SampleModel item) {
            textView.setText(item.getTitleId());
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
