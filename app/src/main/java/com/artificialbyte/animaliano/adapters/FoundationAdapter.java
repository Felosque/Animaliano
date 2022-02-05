package com.artificialbyte.animaliano.adapters;

import android.os.Build;
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
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class FoundationAdapter extends RecyclerView.Adapter<FoundationAdapter.ViewHolderFoundation>{

    private ArrayList<User> foundationList;
    private ArrayList<User> originalItems;
    private View view;

    public FoundationAdapter(ArrayList<User> foundationList) {
        this.foundationList = foundationList;
        this.originalItems = new ArrayList<>();
        originalItems.addAll(foundationList);
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

    public void filter(String strSearch){
        if (strSearch.length() == 0){
            foundationList.clear();
            foundationList.addAll(originalItems);
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                List<User> collect = foundationList.stream().filter(i -> i.getName().toLowerCase().contains(strSearch))
                        .collect(Collectors.toList());
                foundationList.clear();
                foundationList.addAll(collect);
            }
            else {
                foundationList.clear();
                for (User user: originalItems) {
                    if (user.getName().toLowerCase().contains(strSearch)){
                        foundationList.add(user);
                    }
                }
            }
        }
        notifyDataSetChanged();
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
