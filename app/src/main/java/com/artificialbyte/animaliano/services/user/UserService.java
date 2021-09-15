package com.artificialbyte.animaliano.services.user;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.artificialbyte.animaliano.dto.user.User;
import com.artificialbyte.animaliano.interfaces.CRUDUser;
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

public class UserService {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static CRUDUser crudUser;
    private static FirebaseStorage storage = FirebaseStorage.getInstance();

    public UserService(Context context) {
    }

    public static void setCrudUser(CRUDUser crudUser) {
        UserService.crudUser = crudUser;
    }

    public static void addUser(User user) {
        try {
            db.collection("userProfile")
                    .document(String.valueOf(user.getUid()))
                    .set(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                crudUser.isRegister(true);
                            } else {
                                crudUser.isRegister(false);
                            }
                        }
                    });
        }catch (Exception e){
            e.getMessage();
        }
    }

    public static void getUserInfo(String email){
        try {
            db.collection("userProfile")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                User userEmail = null;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    userEmail = document.toObject(User.class);
                                }
                                crudUser.getUserByEmail(userEmail);
                            } else {
                                crudUser.showMessage("¡Ups! No se ha encontrado el usuario");
                            }
                        }
                    });
        }catch (Exception e){

        }
    }

    public static void updateUserProfileImage(String uid, Bitmap bitmap){

        StorageReference userImageRef = storage.getReference().child("photoProfile/" + uid + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = userImageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                crudUser.showMessage("Error al subir la imagen");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                try {
                    DocumentReference userReference = db.collection("userProfile").document(uid);
                    Task<Uri> downloadPhoto = taskSnapshot.getStorage().getDownloadUrl();
                    while(!downloadPhoto.isComplete());
                    Uri url = downloadPhoto.getResult();
                    String refImage = url.toString();
                    userReference.update("profilePhoto", refImage)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                   crudUser.isRegister(true);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                   crudUser.showMessage("Error al subir la imagen");
                                }
                            });
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        });
    }
}
