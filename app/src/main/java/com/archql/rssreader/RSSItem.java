package com.archql.rssreader;

import android.util.Log;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class RSSItem implements Comparable<RSSItem>{
    public String title;
    public String link;
    public String description;
    public String datePublished;
    public String guid;

    public boolean clickable;

    public RSSItem(String title, String link, String description, String datePublished, String guid) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.datePublished = datePublished;
        this.guid = guid;
        this.clickable = true;
    }
    public RSSItem(String title, String description) {
        this.title = title;
        this.link = "";
        this.description = description;
        this.datePublished = "";
        this.guid = "";
        this.clickable = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RSSItem rssItem = (RSSItem) o;
        return Objects.equals(title, rssItem.title) &&
                Objects.equals(link, rssItem.link) &&
                Objects.equals(description, rssItem.description) &&
                Objects.equals(datePublished, rssItem.datePublished) &&
                Objects.equals(guid, rssItem.guid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, link, description, datePublished, guid);
    }

    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss Z"); // TODO
    @Override
    public int compareTo(RSSItem o) {
        ZonedDateTime parsedDate2;
        ZonedDateTime parsedDate1;
        try {
            parsedDate2 = ZonedDateTime.parse(o.datePublished, formatter);
        } catch (Exception e) {
            parsedDate2 = null;
        }
        try {
            parsedDate1 = ZonedDateTime.parse(datePublished, formatter);
        } catch (Exception e) {
            parsedDate1 = null;
        }
        if (parsedDate1 == null && parsedDate2 == null) {
            return 0;
        } else if (parsedDate1 == null) {
            return -1;
        } else if (parsedDate2 == null) {
            return 1;
        } else {
            //Log.i("cmp", parsedDate1 + ((parsedDate1.isAfter(parsedDate2)) ? ">" : "<") + '\n' + parsedDate2 + '\n');
            return parsedDate1.isAfter(parsedDate2) ? -1 : 1;
        }
    }
}
