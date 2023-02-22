package com.archql.rssreader.ui.helpers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.archql.rssreader.RSSItem;

import java.util.List;

public class RssViewModel extends androidx.lifecycle.ViewModel {
    // MutableLiveData allows its value to be changed
    private final MutableLiveData<RSSItem> selectedRssItem = new MutableLiveData<>();
    private final MutableLiveData<List<RSSItem>> listOfRssItems = new MutableLiveData<>();

    public void setSelectedRssItem(RSSItem n) {
        selectedRssItem.setValue(n);
    }
    public LiveData<RSSItem> getSelectedRssItem() {
        return selectedRssItem; // LiveData is a lifecycle-aware observable data holder class
    }

    public void setListOfRssItems(List<RSSItem> list) {
        this.listOfRssItems.setValue(list);
    }
    public LiveData<List<RSSItem>> getListOfRssItems() {
        return listOfRssItems;
    }
}
