package com.example.readinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ParentBookRecViewAdapter extends RecyclerView.Adapter<ParentBookRecViewAdapter.ParentViewHolder>{

    private ArrayList<BookRecView> recViews;
    private Context context;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    public ParentBookRecViewAdapter(ArrayList<BookRecView> recViews, Context context) {
        this.recViews = recViews;
        this.context = context;
    }

    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_rec_view_item, parent, false);
        ParentViewHolder holder = new ParentViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ParentViewHolder holder, int position) {
        BookRecView recView = recViews.get(position);
        holder.textView.setText(recView.getTitle());

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.subRecyclerView.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(recView.getBooks().size());

        SubBooksRecViewAdapter SubBooksAdapter = new SubBooksRecViewAdapter(recView.getBooks(), this.context);
        holder.subRecyclerView.setLayoutManager(layoutManager);
        holder.subRecyclerView.setAdapter(SubBooksAdapter);
        holder.subRecyclerView.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return recViews.size();
    }

    public static class ParentViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RecyclerView subRecyclerView;

        public ParentViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.parent_item_title);
            subRecyclerView = itemView.findViewById(R.id.child_recycler_view);
        }
    }
}
