package com.artificialbyte.animaliano.services.pet;

import android.content.Context;

import androidx.annotation.NonNull;

import com.artificialbyte.animaliano.dto.pet.Pet;
import com.artificialbyte.animaliano.interfaces.activity.ShowMessage;
import com.artificialbyte.animaliano.interfaces.pet.AddPet;
import com.artificialbyte.animaliano.interfaces.pet.GetPetBy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class PetService {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static FirebaseStorage storage = FirebaseStorage.getInstance();

    private static GetPetBy getPetBy;
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

    public PetService(Context context) {
    }

    public static void addPet(Pet pet){
        try {
            db.collection("Pets")
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

    }

}
