package com.redhatx.rbook;

import android.util.Pair;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class DownloadData {
    private static HashMap<String, Integer> downloads = new HashMap<>();

    public static HashMap<String, Integer> getDownloads() {
        return downloads;
    }

    public static void updateDownload(String bookName, int progress) {
        downloads.put(bookName, progress);
    }

    public static List<Pair<String, Integer>> getDownloadsAsList() {

        List<Pair<String, Integer>> list = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : downloads.entrySet()) {
            list.add(new Pair<String, Integer>(entry.getKey(), entry.getValue()));
        }
        return list;
    }
}
