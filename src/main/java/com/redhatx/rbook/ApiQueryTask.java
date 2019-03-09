package com.redhatx.rbook;

import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;
import java.util.List;



public class ApiQueryTask{

    private final String TAG = getClass().getSimpleName();


    interface BookAsyncResponse {
        void onNewBookTaskComplete(List<Book> books);
        void onSingleBookTaskComplete(Book book);
        void onSearchBooksTaskComplete(List<Book> books);
    }

    private BookAsyncResponse bookAsyncResponse = null;

    public ApiQueryTask(BookAsyncResponse delegate){
        this.bookAsyncResponse = delegate;
    }

    public class GetNewBookRequest extends AsyncTask<Void,Void,List<Book>> {

        @Override
        protected List<Book> doInBackground(Void... voids) {
            List<Book> newReleaseBooks = BookJsonHandler.getNewReleaseBooks();
            return newReleaseBooks;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG,"Getting new releases books");
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            bookAsyncResponse.onNewBookTaskComplete(books);
        }
    }

    public class SearchBookRequest extends AsyncTask<String,Void,List<Book>>{

        @Override
        protected List<Book> doInBackground(String... strings) {
            String query = strings[0];
            List<Book> searchedBook = BookJsonHandler.searchBook(query);
            return searchedBook;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG,"Searching Books...");
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            bookAsyncResponse.onSearchBooksTaskComplete(books);
        }
    }


    public class  GetSingleBookRequest extends AsyncTask<String,Void,Book>{
        @Override
        protected Book doInBackground(String... strings) {
            String isbn13 = strings[0];
            Book book = BookJsonHandler.getBookByIsbn(isbn13);
            return book;
        }

        @Override
        protected void onPostExecute(Book book) {
            bookAsyncResponse.onSingleBookTaskComplete(book);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG,"Getting book by isbn...");
        }
    }




}
