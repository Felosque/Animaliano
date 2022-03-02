package com.artificialbyte.animaliano.services.donation;

import android.content.Context;

import androidx.annotation.NonNull;

import com.artificialbyte.animaliano.dto.donation.Donation;
import com.artificialbyte.animaliano.interfaces.activity.ShowMessage;
import com.artificialbyte.animaliano.interfaces.donation.AddDonation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import co.epayco.android.util.EpaycoCallback;

public class DonationService {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static ShowMessage showMessage;
    private static AddDonation addDonation;

    public DonationService(Context context){
    }

    public static void setShowMessage(ShowMessage showMessage) {
        DonationService.showMessage = showMessage;
    }

    public static void setAddDonation(AddDonation addDonation) {
        DonationService.addDonation = addDonation;
    }

    public static void addDonation(Donation donation){
        db.collection("donations")
                .document(donation.getRefPayco())
                .set(donation)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            addDonation.addDonation(donation);
                        } else {
                            showMessage.showMessage("No se pudo guardar la información");
                        }
                    }
                });
    }
}
