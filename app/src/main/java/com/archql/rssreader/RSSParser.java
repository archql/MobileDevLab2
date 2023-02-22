package com.archql.rssreader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ProgressBar;

import com.archql.rssreader.ui.helpers.RssViewAdapter;
import com.archql.rssreader.ui.helpers.RssViewModel;

import org.w3c.dom.Node;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.UnsupportedEncodingException;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RSSParser {

    // RSS XML document CHANNEL tag
    private static String TAG_CHANNEL = "channel";
    private static String TAG_TITLE = "title";
    private static String TAG_LINK = "link";
    private static String TAG_DESCRIPTION = "description";
    private static String TAG_ITEM = "item";
    private static String TAG_PUB_DATE = "pubDate";
    private static String TAG_GUID = "guid";

    RssViewAdapter adapter;
    ProgressBar progressBar;
    Context ctx;
    RssViewModel viewModel;

    public RSSParser(RssViewAdapter adapter, ProgressBar progressBar, Context ctx, RssViewModel viewModel) {
        this.adapter = adapter;
        this.ctx = ctx;
        this.progressBar = progressBar;
        this.viewModel = viewModel;
    }

    public void getRssItemsUri(Uri uriFrom) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        progressBar.setVisibility(ProgressBar.VISIBLE);

        executor.execute(() -> {
            //Background work here
            List<RSSItem> res;
            if (uriFrom != null) {
                res = getRSSFeedItems(getUriContent(uriFrom, ctx));
                if (res.size() == 0 || uriFrom == null) {
                    res.add(new RSSItem("Error", "An error occurred"));
                }
            } else {
                res = new ArrayList<>();
                res.add(new RSSItem("Error", "An error occurred"));
            }
            // post update
            handler.post(() -> {
                //UI Thread work here
                adapter.replaceAll(res);
                viewModel.setListOfRssItems(res);
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            });
        });
    }

    public void getRssItems(String urlFrom) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        progressBar.setVisibility(ProgressBar.VISIBLE);

        executor.execute(() -> {
            //Background work here
            List<RSSItem> res = getRSSFeedItems(getUrlContent(urlFrom));
            if (res.size() == 0) {
                res.add(new RSSItem("Error", "An error occurred"));
            }
            // post update
            handler.post(() -> {
                //UI Thread work here
                adapter.replaceAll(res);
                viewModel.setListOfRssItems(res);
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            });
        });
    }

    private String getUrlContent(String url) {
        StringBuilder content = new StringBuilder();
        try {
            URL siteUrl = new URL(url);

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(siteUrl.openStream()))) {

                String line;
                while ((line = in.readLine()) != null) {
                    content.append(line).append("");
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    private String getUriContent(Uri uri, Context ctx) {
        BufferedReader reader = null;
        try {
            // open the user-picked file for reading:
            InputStream in = ctx.getContentResolver().openInputStream(uri);
            // now read the content:
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null){
                builder.append(line);
            }
            // Do something with the content in
            return builder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    private List<RSSItem> getRSSFeedItems(String contents) {
        List<RSSItem> itemsList = new ArrayList<>();
        try {
            Document doc = getDomElement(contents);
            NodeList nodeList = doc.getElementsByTagName(TAG_CHANNEL);
            Element e = (Element) nodeList.item(0);

            NodeList items = e.getElementsByTagName(TAG_ITEM);
            for (int i = 0; i < items.getLength(); i++) {
                Element e1 = (Element) items.item(i);

                String title = getValue(e1, TAG_TITLE);
                String link = getValue(e1, TAG_LINK);
                String description = getValue(e1, TAG_DESCRIPTION);
                String datePublished = getValue(e1, TAG_PUB_DATE);
                String guid = getValue(e1, TAG_GUID);

                RSSItem rssItem = new RSSItem(title, link, description, datePublished, guid);
                // adding item to list
                itemsList.add(rssItem);
            }
        } catch (Exception e) {
            // Check log for errors
            e.printStackTrace();
        }
        return itemsList;
    }

    private Document getDomElement(String xml) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
            e.printStackTrace();
            return null;
        }
        return doc;
    }

    private final String getElementValue(Node elem) {
        Node child;
        if (elem != null) {
            if (elem.hasChildNodes()) {
                for (child = elem.getFirstChild(); child != null; child = child
                        .getNextSibling()) {
                    if (child.getNodeType() == Node.TEXT_NODE || (child.getNodeType() == Node.CDATA_SECTION_NODE)) {
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    private String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return getElementValue(n.item(0));
    }
}
