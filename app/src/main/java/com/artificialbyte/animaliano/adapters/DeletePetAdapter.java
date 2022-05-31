package com.artificialbyte.animaliano.adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.artificialbyte.animaliano.R;
import com.artificialbyte.animaliano.dto.pet.Pet;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;

public class DeletePetAdapter extends RecyclerView.Adapter<DeletePetAdapter.ViewHolderAdoption>{

    private ArrayList<Pet> petsList;
    private ArrayList<Pet> originalItems;
    private View view;

    public DeletePetAdapter(ArrayList<Pet> foundationList) {
        this.petsList = foundationList;
        this.originalItems = new ArrayList<>();
        originalItems.addAll(foundationList);
    }

    @NonNull
    @Override
    public DeletePetAdapter.ViewHolderAdoption onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_delete_pet, null, false);
        return new DeletePetAdapter.ViewHolderAdoption(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeletePetAdapter.ViewHolderAdoption holder, int position) {
        Pet pet = petsList.get(position);
        holder.name.setText(pet.getName());
        holder.description.setText(pet.getDescription());
        if (!pet.getPhotos().isEmpty()) {
            Glide.with(view.getContext()).load(pet.getPhotos().get(0)).into(holder.photo);
        }else{
            holder.photo.setImageResource(R.mipmap.logo);
        }
    }

    @Override
    public int getItemCount() {
        return petsList.size();
    }

    public void filter(String strSearch){
        if (strSearch.length() == 0){
            petsList.clear();
            petsList.addAll(originalItems);
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                List<Pet> collect = petsList.stream().filter(i -> i.getName().toLowerCase().contains(strSearch.toLowerCase()))
                        .collect(Collectors.toList());
                petsList.clear();
                petsList.addAll(collect);
            }
            else {
                petsList.clear();
                for (Pet pets: originalItems) {
                    if (pets.getOwner().toLowerCase().contains(strSearch.toLowerCase())){
                        petsList.add(pets);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolderAdoption extends RecyclerView.ViewHolder {

        TextView name, description;
        CircleImageView photo;

        public ViewHolderAdoption(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.idName);
            description = itemView.findViewById(R.id.idDescription);
            photo = itemView.findViewById(R.id.idImagen);
        }
    }

}
