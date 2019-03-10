package com.redhatx.rbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

public class DownloadsActivity extends AppCompatActivity {

    private RecyclerView rvDownloads;
    private DownloadAdapter dlAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloads);
        rvDownloads = (RecyclerView) findViewById(R.id.rv_downloads);
        dlAdapter = new DownloadAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvDownloads.setLayoutManager(linearLayoutManager);
        rvDownloads.setHasFixedSize(true);
        rvDownloads.setAdapter(dlAdapter);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dlAdapter.onDataSetChange();
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Log.e(this.getClass().getSimpleName(), "Thread interrupted");
                    }
                }
            }
        });
        t.start();
    }
}
