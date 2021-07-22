package com.artificialbyte.animaliano.services.user;

import android.content.Context;

import androidx.annotation.NonNull;

import com.artificialbyte.animaliano.dto.user.User;
import com.artificialbyte.animaliano.interfaces.CRUDUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserService {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static CRUDUser crudUser;

    public UserService(Context context) {
    }

    public static void setCrudUser(CRUDUser crudUser) {
        UserService.crudUser = crudUser;
    }

    public static void addUser(User user) {
        try {
            db.collection("users")
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
}
