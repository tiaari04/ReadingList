package com.example.readinglist;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SearchedBookAdapter extends RecyclerView.Adapter<SearchedBookAdapter.SearchedBookViewHolder> {

    private ArrayList<SearchedBookInfo> searchedBooks = new ArrayList<>();
    private Context context;

    public SearchedBookAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SearchedBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.searched_book_item, parent, false);
        return new SearchedBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchedBookViewHolder holder, int position) {
        SearchedBookInfo bookInfo = searchedBooks.get(position);
        if (bookInfo.getTitle() == null) {
            holder.title.setText("No title found");
        }
        else {
            holder.title.setText(bookInfo.getTitle());
        }

        if (bookInfo.getNumAuthors() == 0) {
            holder.authors.setText("No authors found");
        }
        else {
            int numAuthors = bookInfo.getNumAuthors();
            holder.authors.setText("By ");
            for(int i = 0; i < numAuthors; i++) {
                holder.authors.append(bookInfo.getAuthors().get(i));
                if (i< numAuthors -1) {
                    holder.authors.append(", ");
                }
            }        }

        if (bookInfo.getPublishedDate() == null) {
            holder.title.setText("No publication year found");
        }
        else {
            holder.pubyear.setText("Year of publication: " + bookInfo.getPublishedDate());
        }

        if (bookInfo.getThumbnail() != null) {
            Glide.with(context)
                    .asBitmap()
                    .load(bookInfo.getThumbnail())
                    .into(holder.coverPhoto);
        }
        else {
            holder.coverPhoto.setImageDrawable(context.getApplicationContext().getDrawable(R.drawable.ic_image_not_found));
        }

        holder.searchedBookParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BookDetailsActivity.class);
                intent.putExtra("title", bookInfo.getTitle());
                String authors = bookInfo.getAuthors().toString();
                authors = authors.substring(1, authors.length() - 1);
                intent.putExtra("authors", authors);
                intent.putExtra("publishedDate", bookInfo.getPublishedDate());
                String genres = bookInfo.getGenres().toString();
                genres = genres.substring(1, genres.length() - 1);
                intent.putExtra("genres", genres);
                intent.putExtra("ISBN", bookInfo.getISBN());
                intent.putExtra("description", bookInfo.getDescription());
                intent.putExtra("pageCount", Integer.toString(bookInfo.getPageCount()));
                intent.putExtra("thumbnail", bookInfo.getThumbnail());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchedBooks.size();
    }

    public void setBooks(ArrayList<SearchedBookInfo> books) {
        this.searchedBooks = books;
        notifyDataSetChanged();
    }

    public class SearchedBookViewHolder extends RecyclerView.ViewHolder {
        private TextView title, authors,  pubyear;
        private CardView searchedBookParent;
        private ImageView coverPhoto;

        public SearchedBookViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.bookName_sb);
            authors = itemView.findViewById(R.id.bookAuthor_sb);
            pubyear = itemView.findViewById(R.id.pubYear_sb);
            searchedBookParent = itemView.findViewById(R.id.searched_item_view_parent);
            coverPhoto = itemView.findViewById(R.id.image_sb);
        }
    }
}
