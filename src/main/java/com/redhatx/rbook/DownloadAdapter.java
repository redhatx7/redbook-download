package com.redhatx.rbook;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.DownloadViewHolder> {

    private List<Pair<String, Integer>> downloads;


    private Context context;

    public DownloadAdapter(Context ctx) {
        downloads = DownloadData.getDownloadsAsList();
        this.context = ctx;
        if (this.downloads.size() == 0) {
            Toast.makeText(context, "کتابی جهت دانلود وجود ندارد.", Toast.LENGTH_LONG).show();
        }
    }

    public void onDataSetChange() {
        downloads = DownloadData.getDownloadsAsList();
        notifyDataSetChanged();
    }

    class DownloadViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar pDownloadBar;
        private TextView tBookname;

        public DownloadViewHolder(@NonNull View itemView) {
            super(itemView);
            pDownloadBar = (ProgressBar) itemView.findViewById(R.id.dl_progress);
            tBookname = (TextView) itemView.findViewById(R.id.tv_dl_book_name);

        }

        public void bind(int i) {
            Pair<String, Integer> pair = downloads.get(i);
            String bookName = pair.first;
            int progress = pair.second;
            pDownloadBar.setProgress(progress);
            tBookname.setText(bookName);
        }
    }


    @NonNull
    @Override
    public DownloadAdapter.DownloadViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutId = R.layout.download_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, viewGroup, false);
        DownloadViewHolder downloadViewHolder = new DownloadViewHolder(view);
        return downloadViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadAdapter.DownloadViewHolder downloadViewHolder, int i) {
        downloadViewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        return downloads.size();
    }
}
