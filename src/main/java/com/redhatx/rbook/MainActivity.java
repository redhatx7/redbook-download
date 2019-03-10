package com.redhatx.rbook;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    RecyclerView rBookView;
    BookAdapter mAdapter;
    private Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        if (isNetworkConnected()) {
            rBookView = (RecyclerView) findViewById(R.id.rv_book);
            mAdapter = new BookAdapter(context, NetworkHandler.REQUEST_TYPE.NEW_RELEASES);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            rBookView.setHasFixedSize(true);
            rBookView.setLayoutManager(linearLayoutManager);
            Toast.makeText(context, "در حال دریافت اطلاعات لطفا صبر کنید", Toast.LENGTH_LONG).show();
            rBookView.setAdapter(mAdapter);
            isStoragePermissionGranted();
        } else {
            Toast.makeText(context, "لطفا اتصال اینترنت را بررسی کنید", Toast.LENGTH_LONG).show();
        }


    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("INFO", "Permission is granted");
                return true;
            } else {

                Log.v("ERR", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            Log.v("INFO", "Permission is granted");
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.m_download_action) {

            Intent intent = new Intent(this, DownloadsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.m_search_action) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            builder.setTitle("Search");

            View view = inflater.inflate(R.layout.search_dialog, null);
            final TextView searchText = view.findViewById(R.id.et_search_book);
            builder.setView(view)
                    .setPositiveButton("جست و جو!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "در حال جست و جو! لطفا صبر کنید " + searchText.getText(), Toast.LENGTH_LONG).show();
                            mAdapter.onChangeQueryMode(NetworkHandler.REQUEST_TYPE.SEARCH, searchText.getText().toString());
                        }
                    }).setNegativeButton("بستن", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create();
            builder.show();
            return true;
        } else if (id == R.id.m_show_downloaded) {
            Intent intent = new Intent(context, BookFilesActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
