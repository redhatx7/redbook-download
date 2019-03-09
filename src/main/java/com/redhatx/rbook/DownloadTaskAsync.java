package com.redhatx.rbook;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadTaskAsync extends AsyncTask<String,Integer,Boolean> {


    private final String TAG = this.getClass().getSimpleName();
    private final String FOLDER_NAME = "RBook";
    private String bookname = null;
    private Context context = null;
    public DownloadTaskAsync(Context context){
        this.context = context;
    }
    @Override
    protected Boolean doInBackground(String... strings)  {
        InputStream in = null;
        OutputStream out = null;
        HttpURLConnection connection = null;
        URL url;
        try {
            File dlFolder = new File(Environment.getExternalStorageDirectory() + File.separator + FOLDER_NAME);
            if(!dlFolder.exists()){
                dlFolder.mkdirs();

            }
            url = new URL(strings[0]);
            String name = strings[1];
            String exp = strings[2];
            bookname = name;
            DownloadData.updateDownload(bookname,0);
            connection  = (HttpURLConnection) url.openConnection();
            connection.connect();
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                Log.e(TAG,"Unable to download file...");
                return false;
            }
            int fileLength = connection.getContentLength();
            in = connection.getInputStream();
            byte buffer[] = new byte[1 << 12];
            File fileToDownload = new File(dlFolder + "/" + name + "." + exp);

            out = new FileOutputStream(fileToDownload);
            long total = 0;
            int count;
            while((count = in.read(buffer)) != -1){
                total += count;
                if(fileLength > 0){
                    publishProgress((int)((100 * total) / fileLength));
                }
                out.write(buffer,0,count);
            }

        }
        catch (MalformedURLException ex){
            Log.e(TAG,ex.getMessage());
        }
        catch (IOException ex){
            Log.e(TAG,"IO Error " + ex.getMessage());
        }
        finally {
            if (connection != null)
                connection.disconnect();
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        Log.i(TAG,"download Compelet");
        Toast.makeText(context,"دانلود با موفیت به اتمام رسید",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(context,"دانلود کتاب شروع شد",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

        int progress = values[0];
        //Log.i(TAG,"Download progress " + progress + "%" );
        DownloadData.updateDownload(bookname,progress);

    }
}
