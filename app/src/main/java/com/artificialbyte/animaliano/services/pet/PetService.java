package com.artificialbyte.animaliano.services.pet;

import android.content.Context;

import androidx.annotation.NonNull;

import com.artificialbyte.animaliano.dto.pet.Pet;
import com.artificialbyte.animaliano.dto.user.User;
import com.artificialbyte.animaliano.interfaces.activity.ShowMessage;
import com.artificialbyte.animaliano.interfaces.pet.AddPet;
import com.artificialbyte.animaliano.interfaces.pet.GetPetBy;
import com.artificialbyte.animaliano.interfaces.pet.GetPetsFromParam;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class PetService {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static FirebaseStorage storage = FirebaseStorage.getInstance();
    private final static String COLLECTION = "Pets";


    private static GetPetBy getPetBy;
    private static GetPetsFromParam getPetsFromParam;
    private static AddPet addPet;
    private static ShowMessage showMessage;

    public static void setGetPetBy(GetPetBy getPetBy) {
        PetService.getPetBy = getPetBy;
    }

    public static void setShowMessage(ShowMessage showMessage) {
        PetService.showMessage = showMessage;
    }

    public static void setAddPet(AddPet addPet) {
        PetService.addPet = addPet;
    }

    public static void setGetPetsFromParam(GetPetsFromParam getPetsFromParam) {
        PetService.getPetsFromParam = getPetsFromParam;
    }

    public PetService(Context context) {
    }

    public static void addPet(Pet pet){
        try {
            db.collection(COLLECTION)
                    .document()
                    .set(pet)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                addPet.addPet(pet);
                            } else {
                                showMessage.showMessage("No se pudo agregar la mascota.");
                            }
                        }
                    });
        }catch (Exception e){

        }
    }

    public static void findPetBy(String name, String param){

        db.collection(COLLECTION)
                .whereEqualTo(param, name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Pet pet = null;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                pet = document.toObject(Pet.class);
                            }
                            getPetBy.getPetBy(pet);
                        } else {
                            showMessage.showMessage("¡Ups! No se ha encontrado la mascota");
                        }
                    }
                });
    }

    public static void getPetsBy(String name, String param){
        try {
            db.collection("Pets")
                    .whereEqualTo(param, name)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<Pet> pets = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    pets.add(document.toObject(Pet.class));
                                }
                                getPetsFromParam.getPetsFromParam(pets);
                            } else {
                                showMessage.showMessage("¡Ups! No se ha encontrado ninguna fundación");
                            }
                        }
                    });
        }catch (Exception e){

        }
    }

}
