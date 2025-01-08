package com.example.readinglist;

import android.content.Context;
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

public class SubBooksRecViewAdapter extends RecyclerView.Adapter<SubBooksRecViewAdapter.ViewHolder> {

    private ArrayList<Book> books;
    private Context context;

    public SubBooksRecViewAdapter(ArrayList<Book> books, Context context) {
        this.books = books;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_view_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bookName.setText(books.get(position).getBookName());

        if (books.get(position).getImgURL() == null) {
            holder.img.setImageDrawable(context.getApplicationContext().getDrawable(R.drawable.ic_image_not_found));
        }
        else {
            Glide.with(context)
                    .asBitmap()
                    .load(books.get(position).getImgURL())
                    .error(R.drawable.ic_image_not_found)
                    .into(holder.img);
        }

        holder.itemViewParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, books.get(position).getBookName() + " selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView bookName;
        private CardView itemViewParent;
        private ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.bookName);
            itemViewParent = itemView.findViewById(R.id.item_view_parent);
            img = itemView.findViewById(androidx.appcompat.R.id.image);
        }
    }
}
