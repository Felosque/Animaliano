package com.artificialbyte.animaliano.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.artificialbyte.animaliano.R;
import com.artificialbyte.animaliano.dto.user.User;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FoundationAdapter extends RecyclerView.Adapter<FoundationAdapter.ViewHolderFoundation>{

    private ArrayList<User> foundationList;
    private View view;

    public FoundationAdapter(ArrayList<User> foundationList) {
        this.foundationList = foundationList;
    }

    @NonNull
    @Override
    public ViewHolderFoundation onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_foundation, null, false);
        return new ViewHolderFoundation(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderFoundation holder, int position) {
        holder.name.setText(foundationList.get(position).getName());
        holder.description.setText(foundationList.get(position).getDescription());
        if (foundationList.get(position).getProfilePhoto().isEmpty()) {
            Glide.with(view.getContext()).load("https://image.shutterstock.com/image-vector/conversation-talking-black-icon-50x50-260nw-1037215345.jpg").into(holder.photo);
        }
    }

    @Override
    public int getItemCount() {
        return foundationList.size();
    }

    public class ViewHolderFoundation extends RecyclerView.ViewHolder {

        TextView name, description;
        ImageView photo;

        public ViewHolderFoundation(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.idName);
            description = itemView.findViewById(R.id.idDescription);
            photo = itemView.findViewById(R.id.idImagen);
        }
    }


}
