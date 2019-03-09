package com.redhatx.rbook;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class BookDetails extends AppCompatActivity {

    private TextView bookTitle,bookAuthors,bookPublisher,bookPages,bookPrice;
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
        bookPublisher =(TextView) findViewById(R.id.tv_details_publisher);
        bookPages = (TextView) findViewById(R.id.tv_details_pages);
        bookPrice = (TextView) findViewById(R.id.tv_details_price);
        bookImage = (ImageView) findViewById(R.id.tv_details_img);
        downloadButton = (Button) findViewById(R.id.btn_download);
        Picasso.get().load(book.getImage()).into(bookImage);
        bookTitle.setText(book.getTitle());
        bookAuthors.setText(book.getAuthors());
        bookPublisher.setText(book.getPublisher());
        bookPages.setText("" + book.getPages());
        bookPrice.setText(book.getPrice());

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               LibgenNetwork.getFileToDownload(book.getIsbn13(),context);
            }
        });

    }
}
