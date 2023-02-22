package com.archql.rssreader.ui.helpers;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.archql.rssreader.RSSItem;
import com.archql.rssreader.databinding.RssViewBinding;

import org.jetbrains.annotations.NotNull;

public class RssViewHolder extends RecyclerView.ViewHolder {

    protected RssViewBinding binding;

    public RssViewHolder(@NonNull @NotNull RssViewBinding rssViewBinding) {
        super(rssViewBinding.getRoot());

        binding = rssViewBinding;
    }

    public void bind(RSSItem n) {
        //binding.setVariable(BR.note, n);
        binding.setRss(n);
        binding.executePendingBindings();
    }
}
