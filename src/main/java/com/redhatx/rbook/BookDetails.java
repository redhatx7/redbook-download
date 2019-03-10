package com.redhatx.rbook;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class BookDetails extends AppCompatActivity {

    private TextView bookTitle, bookAuthors, bookPublisher, bookPages, bookPrice, bookPublishYear, bookDesc;
    private ImageView bookImage;
    private Button downloadButton;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        final Book book = (Book) getIntent().getSerializableExtra("book");
        context = this;
        bookTitle = (TextView) findViewById(R.id.tv_details_title);
        bookAuthors = (TextView) findViewById(R.id.tv_details_authors);
        bookPublisher = (TextView) findViewById(R.id.tv_details_publisher);
        bookPages = (TextView) findViewById(R.id.tv_details_pages);
        bookPrice = (TextView) findViewById(R.id.tv_details_price);
        bookPublishYear = (TextView) findViewById(R.id.tv_details_publish_year);
        downloadButton = (Button) findViewById(R.id.btn_details_download);
        bookImage = (ImageView) findViewById(R.id.img_details);
        bookDesc = (TextView) findViewById(R.id.tv_details_desc);
        Picasso.get().load(book.getImage()).into(bookImage);
        bookTitle.setText(book.getTitle());
        bookAuthors.setText(book.getAuthors());
        bookPublisher.setText(book.getPublisher());
        bookPages.setText("" + book.getPages());
        bookPrice.setText(book.getPrice());
        bookPublishYear.setText(book.getYear() + "");
        bookDesc.setText(book.getDesc() + "\n\n\n\n\n");

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "در حال دریافت لینک دانلود...", Toast.LENGTH_LONG).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        LibgenNetwork libgenNetwork = new LibgenNetwork();
                        boolean isAvailable = libgenNetwork.getFileToDownload(book.getIsbn13(), context);
                        if (!isAvailable) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, " متاسفانه این کتاب برای دانلود موجود نمی باشد", Toast.LENGTH_LONG).show();
                                }
                            });
                        }


                    }
                }).start();
            }
        });

    }
}
