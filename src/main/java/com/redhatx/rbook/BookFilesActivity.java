package com.redhatx.rbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

public class BookFilesActivity extends AppCompatActivity {

    private FileListAdapter mAdapter;
    private RecyclerView rvFileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_files);
        rvFileList = (RecyclerView) findViewById(R.id.rv_file_list);
        mAdapter = new FileListAdapter(this);
        rvFileList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvFileList.setLayoutManager(layoutManager);
        rvFileList.setAdapter(mAdapter);
    }
}
