package com.redhatx.rbook;

import android.os.AsyncTask;

import java.net.URL;

public class GetResponseAsync extends AsyncTask<URL, Void, String> {


    @Override
    protected String doInBackground(URL... urls) {
        URL url = urls[0];
        return NetworkHandler.getResponesString(url);
    }


}
