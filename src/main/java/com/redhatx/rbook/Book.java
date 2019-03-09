

package com.redhatx.rbook;
import android.util.Log;

import org.json.*;

import java.io.Serializable;
import java.nio.channels.GatheringByteChannel;

public class Book implements Serializable {

    private final String TAG = this.getClass().getName();

    private String title;
    private String subTitle;
    private String authors;
    private String publisher;
    private String isbn13;
    private String isbn10;
    private int pages;
    private int year;
    private int rating;
    private String desc;
    private String price;
    private String image;
    private String url;



    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getAuthors() {
        return authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public int getPages() {
        return pages;
    }

    public int getYear() {
        return year;
    }

    public int getRating() {
        return rating;
    }

    public String getDesc() {
        return desc;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getUrl() {
        return url;
    }

    public Book(JSONObject object){
        try {
            title = object.getString("title");
            authors = object.getString("authors");
            desc = object.getString("desc");
            subTitle = object.getString("subtitle");
            publisher = object.getString("publisher");
            isbn10 = object.getString("isbn10");
            isbn13 = object.getString("isbn13");
            pages = object.getInt("pages");
            year = object.getInt("year");
            rating = object.getInt("rating");
            price = object.getString("price");
            image = object.getString("image");
            url = object.getString("url");
            Log.i(TAG,"Json parsed successfully");

        }
        catch (JSONException ex){
            Log.e(TAG,ex.getMessage());
        }

    }

}
