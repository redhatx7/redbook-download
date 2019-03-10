package com.redhatx.rbook;

import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import static android.content.Intent.ACTION_VIEW;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.FileListViewHolder> {


    public static String getMimeString(File file) {
        String filename = file.getName();
        int index = filename.lastIndexOf('.');
        if (index == -1) {
            return null;
        }
        String ext = filename.substring(index + 1);
        String mimeType = null;
        switch (ext) {
            case "pdf":
                mimeType = "application/pdf";
                break;
            case "azw3":
                mimeType = "application/vnd.amazon.mobi8-ebook";
                break;
            case "epub":
                mimeType = "application/epub+zip";
                break;
        }
        return mimeType;
    }

    private final String FOLDER_NAME = "RBook";
    private File[] files = null;
    private Context context;

    public FileListAdapter(Context context) {
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + FOLDER_NAME);
        File[] filesInDir = file.listFiles();
        this.files = filesInDir;
        this.context = context;
        if (this.files.length == 0) {
            Toast.makeText(context, "شما هنوز کتابی دانلود نکرده اید", Toast.LENGTH_LONG).show();
        }
    }

    @NonNull
    @Override
    public FileListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context ctx = viewGroup.getContext();
        int layoutId = R.layout.file_item;
        boolean attachMd = false;
        View view = LayoutInflater.from(context).inflate(layoutId, viewGroup, attachMd);
        FileListViewHolder viewHolder = new FileListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FileListViewHolder fileListViewHolder, int i) {
        fileListViewHolder.bind(i);

    }

    @Override
    public int getItemCount() {
        return files.length;
    }

    class FileListViewHolder extends RecyclerView.ViewHolder {
        private ImageView extImage;
        private TextView tvInf0BookName;
        private Button btnRunPdf;

        public FileListViewHolder(@NonNull final View itemView) {
            super(itemView);
            extImage = (ImageView) itemView.findViewById(R.id.img_info);
            tvInf0BookName = (TextView) itemView.findViewById(R.id.tv_info_book_name);
            btnRunPdf = (Button) itemView.findViewById(R.id.btn_info_run_pdf);

            btnRunPdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, files[getAdapterPosition()]);
                    String mimeType = getMimeString(files[getAdapterPosition()]);
                    intent.setDataAndType(uri, mimeType);
                    try {
                        context.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context, "برنامه ای برای نمایش این فایل وجود ندارد", Toast.LENGTH_LONG).show();
                    }

                }
            });

        }

        public void bind(int pos) {
            String ext = files[pos].getName();
            int findex = ext.lastIndexOf('.');
            ext = ext.substring(findex + 1);
            switch (ext) {
                case "pdf":
                    extImage.setImageResource(R.drawable.pdf);
                    break;
                case "azw3":
                    extImage.setImageResource(R.drawable.azw3);
                    break;
                case "epub":
                    extImage.setImageResource(R.drawable.epub);
                default:
                    extImage.setImageResource(R.mipmap.ic_pdf);
                    break;
            }
            tvInf0BookName.setText(files[pos].getName());

        }
    }

}
