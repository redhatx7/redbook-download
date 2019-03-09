package com.redhatx.rbook;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BookJsonHandler {

    private final static String TAG = BookJsonHandler.class.getName();

    public static List<Book> getNewReleaseBooks(){
        String newBooks = NetworkHandler.getNewReleasesBooksJSONString();
        List<Book> books = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(newBooks);
            JSONArray booksArray  = jsonObject.getJSONArray("books");

            for (int i=0;i< booksArray.length();i++){
                 JSONObject obj = booksArray.getJSONObject(i);
                 String isbn13 = obj.getString("isbn13");
                 Book book = getBookByIsbn(isbn13);
                 books.add(book);
            }
        }
        catch (JSONException ex){
            Log.e(TAG,"Error while parsing json " + ex.getMessage());
        }
        return books;
    }

    public static Book getBookByIsbn(String isbn13){
        String bookStr = NetworkHandler.getBookByIsbnJSONString(isbn13);
        Book book = null;
        try {
            JSONObject bookObject = new JSONObject(bookStr);
            book = new Book(bookObject);

        }
        catch (JSONException ex){
            Log.e(TAG,"Error while parsing json " + ex.getMessage());
        }
        return book;
    }

    public static List<Book> searchBook(String searchQuery){
        String searchBookStr = NetworkHandler.searchBookJSONString(searchQuery,1);

        List<Book> books = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(searchBookStr);
            int pageNumber = (int)Math.ceil(jsonObject.getInt("total") / 10.0);
            for(int pNum = 1;pNum <= pageNumber;pNum++){
                searchBookStr = NetworkHandler.searchBookJSONString(searchQuery,pNum);
                jsonObject = new JSONObject(searchBookStr);
                JSONArray booksArray = jsonObject.getJSONArray("books");
                for(int i = 0;i<booksArray.length();i++){
                    books.add(new Book(booksArray.getJSONObject(i)));
                }
            }
        }
        catch (JSONException ex){
            Log.e(TAG,"Error while parsing json " + ex.getMessage());
        }

        return books;
    }
}
