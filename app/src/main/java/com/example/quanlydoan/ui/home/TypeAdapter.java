package com.example.quanlydoan.ui.home;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydoan.R;

import java.util.List;

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.MyViewHolder> {
    private List<Type> types;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView imgType;

        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.txtType);
            imgType = view.findViewById(R.id.imgType);
        }
    }

    public TypeAdapter(List<Type> types) {
        this.types = types;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_type, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Type type = types.get(position);
        holder.name.setText(type.getName());
        switch (position) {
            case 0:
                holder.imgType.setImageResource(R.drawable.type_pizza);
                break;
            case 1:
                holder.imgType.setImageResource(R.drawable.type_hambur);
                break;
            case 2:
                holder.imgType.setImageResource(R.drawable.type_spa);
                break;
            case 3:
                holder.imgType.setImageResource(R.drawable.type_hotdog);
                break;
            case 4:
                holder.imgType.setImageResource(R.drawable.type_drink);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return types.size();
    }
}
