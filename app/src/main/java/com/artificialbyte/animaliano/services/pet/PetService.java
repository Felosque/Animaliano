package com.artificialbyte.animaliano.services.pet;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.artificialbyte.animaliano.dto.pet.Pet;
import com.artificialbyte.animaliano.dto.user.User;
import com.artificialbyte.animaliano.interfaces.activity.ShowMessage;
import com.artificialbyte.animaliano.interfaces.pet.AddPet;
import com.artificialbyte.animaliano.interfaces.pet.DeletePet;
import com.artificialbyte.animaliano.interfaces.pet.GetPetBy;
import com.artificialbyte.animaliano.interfaces.pet.GetPetsFromParam;
import com.artificialbyte.animaliano.interfaces.pet.PetImageUpload;
import com.artificialbyte.animaliano.utils.Constans;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class PetService {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static FirebaseStorage storage = FirebaseStorage.getInstance();
    private final static String COLLECTION = "Pets";


    private static PetImageUpload petImageUpload;
    private static GetPetBy getPetBy;
    private static GetPetsFromParam getPetsFromParam;
    private static AddPet addPet;
    private static ShowMessage showMessage;
    private static DeletePet deletePet;

    public static void setPetImageUpload(PetImageUpload petImageUpload) {
        PetService.petImageUpload = petImageUpload;
    }

    public static void setDeletePet(DeletePet deletePet) {
        PetService.deletePet = deletePet;
    }

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

    public static void deletePet(Pet pet){
        db.collection("Pets").document(pet.getUid())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        deletePet.deletePet(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        deletePet.deletePet(false);
                    }
                });
    }

    public static void addPet(Pet pet){
        try {
            String uid = FirebaseFirestore.getInstance().collection("Pets").document().getId();
            pet.setUid(uid);
            db.collection(COLLECTION)
                    .document(uid)
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

    public static void updatePetProfileImage(String uid, Bitmap bitmap){

        StorageReference userImageRef = storage.getReference().child("photoPet/" + uid + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = userImageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                showMessage.showMessage("Error al subir la imagen");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                try {
                    DocumentReference userReference = db.collection("Pets").document(uid);
                    Task<Uri> downloadPhoto = taskSnapshot.getStorage().getDownloadUrl();
                    while(!downloadPhoto.isComplete());
                    Uri url = downloadPhoto.getResult();
                    String refImage = url.toString();
                    ArrayList<String> photos = new ArrayList<>();
                    photos.add(refImage);
                    userReference.update("photos", photos)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    petImageUpload.petImageUpload(true);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    showMessage.showMessage("Error al subir la imagen");
                                }
                            });
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        });
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
                                pet.setUid(document.getId());
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
                                    Pet pet = document.toObject(Pet.class);
                                    pet.setUid(document.getId());
                                    pets.add(pet);
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
