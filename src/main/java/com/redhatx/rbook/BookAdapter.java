package com.redhatx.rbook;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private int pageNumber = -1;
    private int currentPage = 1;
    private String searchText;

    public BookAdapter(Context context, NetworkHandler.REQUEST_TYPE type, String... params) {
        books = new ArrayList<>();
        this.context = context;
        switch (type) {
            case NEW_RELEASES:
                new ApiQueryTask(this).new GetNewBookRequest().execute();
                break;
            case SEARCH:
                this.searchText = params[0];
                new ApiQueryTask(this).new SearchBookRequest().execute(params[0], "1");
                break;
            default:
                new ApiQueryTask(this).new GetNewBookRequest().execute();
        }
    }

    public void onChangeQueryMode(NetworkHandler.REQUEST_TYPE type, String... params) {
        this.books.clear();
        this.notifyDataSetChanged();
        switch (type) {
            case NEW_RELEASES:
                new ApiQueryTask(this).new GetNewBookRequest().execute();
                break;
            case SEARCH:
                this.searchText = params[0];
                new ApiQueryTask(this).new SearchBookRequest().execute(params[0], "1");
                break;
            default:
                new ApiQueryTask(this).new GetNewBookRequest().execute();
        }

    }


    @Override
    public void onNewBookTaskComplete(List<Book> books) {
        this.books.addAll(books);
        this.notifyDataSetChanged();
    }

    @Override
    public void onSearchStart(int pageNumber) {
        if (this.pageNumber == -1) {
            this.pageNumber = pageNumber;
        }
    }

    @Override
    public void onSearchBooksTaskComplete(List<Book> books) {
        for (int i = 0; i < books.size(); i++) {
            this.books.add(books.get(i));
        }
        this.notifyItemInserted(this.books.size() - 1);
    }


    class BookViewHolder extends RecyclerView.ViewHolder {

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
                    Intent intent = new Intent(context, BookDetails.class);
                    int index = getAdapterPosition();
                    intent.putExtra("book", books.get(index));
                    context.startActivity(intent);

                }
            });

        }


        void bind(int pos) {
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
        View view = inflater.inflate(layoutId, viewGroup, attachImd);
        BookViewHolder viewHolder = new BookViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.BookViewHolder bookViewHolder, int i) {
        bookViewHolder.bind(i);
        if (i == this.books.size() - 1 && currentPage < pageNumber && searchText != null) {
            Toast.makeText(context, "در حال دریافت اطلاعات جدید... لطفا صبر کنید", Toast.LENGTH_LONG).show();
            ++currentPage;
            new ApiQueryTask(this).new SearchBookRequest().execute(searchText, String.valueOf(currentPage));
        }


    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
