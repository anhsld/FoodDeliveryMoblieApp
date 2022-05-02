package com.example.quanlydoan.ui.home;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydoan.R;
import com.example.quanlydoan.data.model.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private List<Category> categories;
    private Callback callback;
    public int row_index = -1;
    public interface Callback{
        void onItemClick(int position, String categoryId);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView imgType;
        LinearLayout llItemCategory;

        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.txtType);
            imgType = view.findViewById(R.id.imgType);
            llItemCategory = view.findViewById(R.id.llItemCategory);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onItemClick(getAdapterPosition(), categories.get(getAdapterPosition()).getCategoryId());
                }
            });
        }


    }

    public CategoryAdapter(List<Category> categories, Callback callback) {
        this.callback = callback;
        this.categories = categories;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_category, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Category category = categories.get(position);
        holder.name.setText(category.getName());
        holder.imgType.setImageResource(R.drawable.type_pizza);
        Picasso.get().load(category.getImage()).into(holder.imgType);

        holder.llItemCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=position;
                notifyDataSetChanged();
                callback.onItemClick(position, category.getCategoryId());
            }
        });

        if(row_index==position){
            holder.llItemCategory.setBackgroundResource(R.drawable.bg_primary_elip);
//            holder.name.setTextColor(Color.parseColor("#BD9828"));
        }
        else
        {
            holder.llItemCategory.setBackgroundResource(R.drawable.bg_white_elip);
//            holder.name.setTextColor(Color.parseColor("#000000"));
        }

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
