package com.archql.rssreader.ui.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.archql.rssreader.RSSItem;
import com.archql.rssreader.databinding.RssViewBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RssViewAdapter extends RecyclerView.Adapter<RssViewHolder> {

    // interface for handling item clicks
    public interface OnRssClickListener{
        void onClick(RSSItem n);
    }

    private Context ctx;
    private OnRssClickListener rssClickListener;

    public RssViewAdapter(Context context, RssViewAdapter.OnRssClickListener clickListener) {
        this.ctx = context;
        this.rssClickListener = clickListener;
    }

    public void add(RSSItem model) {
        sortedList.add(model);
    }

    public void remove(RSSItem model) {
        sortedList.remove(model);
    }

    public void add(List<RSSItem> models) {
        sortedList.addAll(models);
    }

    public void remove(List<RSSItem> models) {
        sortedList.beginBatchedUpdates();
        for (RSSItem model : models) {
            sortedList.remove(model);
        }
        sortedList.endBatchedUpdates();
    }

    public void clear() {
        sortedList.clear();
    }

    public void replaceAll(List<RSSItem> models) {
        sortedList.beginBatchedUpdates();
        for (int i = sortedList.size() - 1; i >= 0; i--) {
            final RSSItem model = sortedList.get(i);
            if (!models.contains(model)) {
                sortedList.remove(model);
            }
        }
        sortedList.addAll(models);
        sortedList.endBatchedUpdates();
    }

    @NonNull
    @NotNull
    @Override
    public RssViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        final RssViewBinding binding = RssViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RssViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RssViewHolder holder, int position) {
        final RSSItem n = sortedList.get(position);
        holder.bind(n);
        holder.binding.setNoteClickListener(rssClickListener);
    }

    @Override
    public int getItemCount() {
        return sortedList.size();
    }

    // search
    private final SortedList<RSSItem> sortedList = new SortedList<>(RSSItem.class, new SortedList.Callback<RSSItem>() {

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public int compare(RSSItem a, RSSItem b) {
            // TODO replace with comparator
            return a.compareTo(b); //mComparator.compare(a, b);
        }

        @Override
        public boolean areContentsTheSame(RSSItem oldItem, RSSItem newItem) {
            return oldItem.equals(newItem); // TODO
        }

        @Override
        public boolean areItemsTheSame(RSSItem item1, RSSItem item2) {
            return item1.equals(item2);
        }
    });

}
