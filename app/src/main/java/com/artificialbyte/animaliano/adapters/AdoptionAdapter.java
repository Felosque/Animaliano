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
import com.artificialbyte.animaliano.dto.pet.Pet;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdoptionAdapter extends RecyclerView.Adapter<AdoptionAdapter.ViewHolderAdoption> {

    private ArrayList<Pet> foundationList;
    private ArrayList<Pet> originalItems;
    private View view;

    public AdoptionAdapter(ArrayList<Pet> foundationList) {
        this.foundationList = foundationList;
        this.originalItems = new ArrayList<>();
        originalItems.addAll(foundationList);
    }

    @NonNull
    @Override
    public ViewHolderAdoption onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_adoption, null, false);
        return new AdoptionAdapter.ViewHolderAdoption(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAdoption holder, int position) {
        Pet pet = foundationList.get(position);
        holder.name.setText(pet.getName());
        holder.description.setText(pet.getDescription());
        holder.date.setText(pet.getBirthDate());
        if (!pet.getPhotos().isEmpty()) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("photoPet").child(pet.getUid()+".jpg");
            System.out.println("" +storageReference.getPath());
            Glide.with(view.getContext()).load(storageReference).into(holder.photo);
        }else{
            holder.photo.setImageResource(R.mipmap.logo);
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
                List<Pet> collect = foundationList.stream().filter(i -> i.getName().toLowerCase().contains(strSearch.toLowerCase()))
                        .collect(Collectors.toList());
                foundationList.clear();
                foundationList.addAll(collect);
            }
            else {
                foundationList.clear();
                for (Pet pets: originalItems) {
                    if (pets.getOwner().toLowerCase().contains(strSearch.toLowerCase())){
                        foundationList.add(pets);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolderAdoption extends RecyclerView.ViewHolder {

        TextView name, date, description;
        ImageView photo;

        public ViewHolderAdoption(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.idName);
            date = itemView.findViewById(R.id.idTime);
            description = itemView.findViewById(R.id.adopDescription);
            photo = itemView.findViewById(R.id.adopImage);
        }
    }

}
