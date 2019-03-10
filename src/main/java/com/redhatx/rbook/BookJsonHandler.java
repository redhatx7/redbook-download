package com.redhatx.rbook;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BookJsonHandler {

    private final static String TAG = BookJsonHandler.class.getName();

    public static List<Book> getNewReleaseBooks() {
        String newBooks = NetworkHandler.getNewReleasesBooksJSONString();
        List<Book> books = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(newBooks);
            JSONArray booksArray = jsonObject.getJSONArray("books");

            for (int i = 0; i < booksArray.length(); i++) {
                JSONObject obj = booksArray.getJSONObject(i);
                String isbn13 = obj.getString("isbn13");
                Book book = getBookByIsbn(isbn13);
                books.add(book);
            }
        } catch (JSONException ex) {
            Log.e(TAG, "Error while parsing json " + ex.getMessage());
        }
        return books;
    }

    public static Book getBookByIsbn(String isbn13) {
        String bookStr = NetworkHandler.getBookByIsbnJSONString(isbn13);
        Book book = null;
        try {
            JSONObject bookObject = new JSONObject(bookStr);
            book = new Book(bookObject);

        } catch (JSONException ex) {
            Log.e(TAG, "Error while parsing json " + ex.getMessage());
        }
        return book;
    }

    public static List<Book> searchBook(String searchQuery, int pNumber) {
        String searchBookStr;
        JSONObject jsonObject;
        List<Book> books = new ArrayList<>();
        try {

            searchBookStr = NetworkHandler.searchBookJSONString(searchQuery, pNumber);
            jsonObject = new JSONObject(searchBookStr);
            JSONArray booksArray = jsonObject.getJSONArray("books");
            for (int i = 0; i < booksArray.length(); i++) {
                String isbn13 = booksArray.getJSONObject(i).getString("isbn13");
                Book book = getBookByIsbn(isbn13);
                books.add(book);
            }

        } catch (JSONException ex) {
            Log.e(TAG, "Error while parsing json " + ex.getMessage());
        }

        return books;
    }

    public static int getPageNumber(String param) {
        int pageNumber = 0;
        try {
            String searchBookStr = NetworkHandler.searchBookJSONString(param, 1);
            JSONObject jsonObject = new JSONObject(searchBookStr);
            pageNumber = (int) Math.ceil(jsonObject.getInt("total") / 10.0);
        } catch (JSONException e) {
            Log.e(TAG, "Unable to parse json");
        }
        return pageNumber;
    }
}
