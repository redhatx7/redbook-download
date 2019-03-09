package com.redhatx.rbook;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> implements ApiQueryTask.BookAsyncResponse {



    List<Book> books = null;
    private Context context;
    public BookAdapter(Context context){
        books = new ArrayList<>();
        this.context = context;
        new ApiQueryTask(this).new GetNewBookRequest().execute();
    }

    @Override
    public void onNewBookTaskComplete(List<Book> books) {
        this.books = books;
        this.notifyDataSetChanged();
    }

    @Override
    public void onSingleBookTaskComplete(Book book) {

    }

    @Override
    public void onSearchBooksTaskComplete(List<Book> books) {

    }

    interface OnClick {

    }

    class BookViewHolder extends RecyclerView.ViewHolder  {

        ImageView imageView;
        TextView bookTitle;
        TextView bookPrice;
        TextView bookAuthors;
        TextView bookPublishers;
        TextView bookPage;
        Button infoBtn;
        public BookViewHolder(@NonNull final View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.im_book_image);
            bookTitle = (TextView) itemView.findViewById(R.id.tv_book_title);
            bookPrice = (TextView) itemView.findViewById(R.id.tv_books_price);
            bookAuthors = (TextView) itemView.findViewById(R.id.tv_book_authors);
            bookPublishers = (TextView) itemView.findViewById(R.id.tv_book_publishers);
            bookPage = (TextView) itemView.findViewById(R.id.tv_book_pages);
            infoBtn = (Button) itemView.findViewById(R.id.btn_info);
            infoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,BookDetails.class);
                    int index = getAdapterPosition();
                    intent.putExtra("book",books.get(index));
                    context.startActivity(intent);

                }
            });

        }



        void bind(int pos){
            Book book = books.get(pos);
            bookTitle.setText(book.getTitle());
            bookPrice.setText(book.getPrice());
            bookAuthors.setText(book.getAuthors());
            bookPublishers.setText(book.getPublisher());
            bookPage.setText(book.getPages() + "");
            Uri uri = Uri.parse(book.getImage());
            Picasso.get().load(uri).into(imageView);
        }
    }


    @NonNull
    @Override
    public BookAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutId = R.layout.book_item;
        boolean attachImd = false;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId,viewGroup,attachImd);
        BookViewHolder viewHolder = new BookViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.BookViewHolder bookViewHolder, int i) {
        bookViewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
