package com.redhatx.rbook;


import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class LibgenNetwork {
    private static final String TAG = LibgenNetwork.class.getSimpleName();

    private static final String LIBGEN_URL = "http://libgen.io/search.php";
    private static final String SORT_PARAM = "sort";
    private static final String TOPIC = "lg_topic";
    private static final String TOPIC_NAME = "libgen";
    private static final String REQUEST = "req";
    private static final String VIEW_TYPE = "simple";
    private static final String VIEW = "view";

    private static final String DOWNLOAD_URL = "http://booksdl.org/get.php";

    private URL generateUrl(String isbn) {
        Uri uri = Uri.parse(LIBGEN_URL).buildUpon()
                .appendQueryParameter(REQUEST, isbn)
                .appendQueryParameter(TOPIC, TOPIC_NAME)
                .appendQueryParameter(VIEW, VIEW_TYPE)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException ex) {
            Log.e(TAG, "Error while parsing url " + ex.getMessage());
        }
        return url;

    }


    public boolean getFileToDownload(String isbn, Context ctx) {
        URL url = this.generateUrl(isbn);
        String body;
        Document document = null;
        try {
            body = new GetResponseAsync().execute(url).get();
            document = Jsoup.parse(body);
            Element table = document.select("table.c").first();
            Elements tr = table.select("tr");
            if (tr.size() <= 1) {
                Log.i(TAG, "Book not found ! ISBN : " + isbn);
                return false;
            }
            Log.i(TAG, "Size is " + tr.size());
            boolean pdfFound = false;
            for (int i = 1; i < tr.size(); i++) {
                Element element = tr.get(i);
                Elements td = element.select("td");
                if (td.size() <= 9 && i < (tr.size() - 1)) continue;
                else {
                    pdfFound = true;
                    i = 1;
                }
                String ext = td.get(8).ownText();
                if (ext == "pdf") pdfFound = true;
                String bookName = td.get(2).selectFirst("a").ownText();
                String md5 = td.get(2).selectFirst("a").attr("href");
                if (!md5.startsWith("book")) {
                    md5 = td.get(2).select("a").get(1).attr("href");
                    bookName = td.get(2).select("a").get(1).ownText();
                }
                int index = md5.indexOf("md5=");
                md5 = md5.substring(index + 4).trim();
                if (ext != null && !md5.equals("") && pdfFound) {
                    downloadFromLibgen(md5, bookName, ext, ctx);
                    return true;
                } else if (!pdfFound && i == (tr.size() - 1)) {
                    i = 1;
                    pdfFound = true;
                }


            }
        } catch (InterruptedException exx) {
            Log.e(TAG, "Async task Failed..." + exx.getMessage());
        } catch (ExecutionException exx) {
            Log.e(TAG, "Async task Failed..." + exx.getMessage());
        }
        return false;
    }

    public void downloadFromLibgen(String md5, String name, String exp, Context ctx) {
        Uri uri = Uri.parse(DOWNLOAD_URL).buildUpon()
                .appendQueryParameter("md5", md5)
                .build();
        new DownloadTaskAsync(ctx).execute(uri.toString(), name, exp);
    }

}
