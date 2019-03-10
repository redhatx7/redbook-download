package com.redhatx.rbook;

import android.net.Uri;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkHandler {
    private final static String TAG = NetworkHandler.class.getName();
    private final static String API_MAIN_URL = "https://api.itbook.store/1.0";
    private final static String SEARCH_PATH = "search";
    private final static String BOOK_PATH = "books";
    private final static String NEW_BOOK_PATH = "new";

    public enum REQUEST_TYPE {
        SINGLE_BOOK,
        NEW_RELEASES,
        SEARCH
    }

    public static URL generateUrl(REQUEST_TYPE rtype, String... query) {
        Uri uri = null;

        switch (rtype) {
            case SINGLE_BOOK:
                uri = Uri.parse(API_MAIN_URL).buildUpon()
                        .appendEncodedPath(BOOK_PATH)
                        .appendEncodedPath(query[0])
                        .build();
                break;
            case SEARCH:
                String removeStr = query[0].toLowerCase();
                if (removeStr.indexOf("#") != -1) {
                    removeStr = removeStr.replace("#", " sharp");
                }
                if (removeStr.indexOf('+') != -1) {
                    removeStr = removeStr.replace("+", " plus");
                }
                uri = Uri.parse(API_MAIN_URL).buildUpon()
                        .appendEncodedPath(SEARCH_PATH)
                        .appendEncodedPath(removeStr)
                        .appendEncodedPath(query[1])
                        .build();
                break;
            case NEW_RELEASES:
                uri = Uri.parse(API_MAIN_URL).buildUpon()
                        .appendEncodedPath(NEW_BOOK_PATH)
                        .build();

        }
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException ex) {
            Log.e(TAG, ex.getMessage());
        }
        return url;
    }

    public static String getResponesString(URL requestUrl) {
        String response = null;
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) requestUrl.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");
            boolean hasNext = scanner.hasNext();
            if (hasNext)
                response = scanner.next();

        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            httpURLConnection.disconnect();
        }
        return response;

    }

    public static String getNewReleasesBooksJSONString() {
        URL newBooksUrl = generateUrl(REQUEST_TYPE.NEW_RELEASES, "");
        String response = getResponesString(newBooksUrl);
        return response;
    }

    public static String getBookByIsbnJSONString(String isbn13) {
        URL bookByIsbn = generateUrl(REQUEST_TYPE.SINGLE_BOOK, isbn13);
        String response = getResponesString(bookByIsbn);
        return response;
    }

    public static String searchBookJSONString(String query, int page) {
        URL searchBook = generateUrl(REQUEST_TYPE.SEARCH, query, String.valueOf(page));
        String response = getResponesString(searchBook);
        return response;
    }


}
