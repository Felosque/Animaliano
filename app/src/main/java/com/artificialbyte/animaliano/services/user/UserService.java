package com.artificialbyte.animaliano.services.user;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.artificialbyte.animaliano.dto.user.User;
import com.artificialbyte.animaliano.interfaces.activity.ShowMessage;
import com.artificialbyte.animaliano.interfaces.user.GetUserBy;
import com.artificialbyte.animaliano.interfaces.user.InUserRegister;
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

public class UserService {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static FirebaseStorage storage = FirebaseStorage.getInstance();

    private static InUserRegister inUserRegister;
    private static ShowMessage showMessage;
    private static GetUserBy getUserBy;

    public UserService(Context context) {
    }

    public static void setInUserRegister(InUserRegister inUserRegister) {
        UserService.inUserRegister = inUserRegister;
    }

    public static void setShowMessage(ShowMessage showMessage) {
        UserService.showMessage = showMessage;
    }

    public static void setGetUserBy(GetUserBy getUserBy) {
        UserService.getUserBy = getUserBy;
    }

    public static void addUser(User user, int provider) {
        try {
            db.collection("userProfile")
                    .document(String.valueOf(user.getUid()))
                    .set(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                inUserRegister.inUserRegister(true, provider);
                            } else {
                                inUserRegister.inUserRegister(false, provider);
                            }
                        }
                    });
        }catch (Exception e){
            e.getMessage();
        }
    }

    public static void getUserInfo(String name, String param){
        try {
            db.collection("userProfile")
                    .whereEqualTo(param, name)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                User userEmail = null;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    userEmail = document.toObject(User.class);
                                }
                                getUserBy.getUserBy(userEmail);
                            } else {
                                showMessage.showMessage("¡Ups! No se ha encontrado el usuario");
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
                showMessage.showMessage("Error al subir la imagen");
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
                                    inUserRegister.inUserRegister(true, Constans.EMAIL_REGISTER);
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
}
