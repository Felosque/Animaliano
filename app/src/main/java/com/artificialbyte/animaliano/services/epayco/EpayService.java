package com.artificialbyte.animaliano.services.epayco;

import android.content.Context;
import android.util.Log;

import com.artificialbyte.animaliano.dto.donation.Donation;
import com.artificialbyte.animaliano.interfaces.donation.AddDonation;
import com.artificialbyte.animaliano.services.donation.DonationService;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import co.epayco.android.Epayco;
import co.epayco.android.models.Authentication;
import co.epayco.android.util.EpaycoCallback;

public class EpayService implements AddDonation {

    public EpayService(@NotNull Transaction transaction) {

        Authentication auth = new Authentication();
        auth.setApiKey("7be429ee3b358e7066be8c978cab72f4");
        auth.setPrivateKey("c017cd60e58a395c472d4f1757263fa5");
        auth.setLang("ES");
        auth.setTest(true);

        Epayco epayco  = new Epayco(auth);

        epayco.getReferencePayment("74789714", new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                Log.d("sdadasd", "dataEpayco: " + data);
           }

            @Override
           public void onError(Exception error) {
                System.out.println("dataEpayco: " + error.getMessage());
            }
        });


        epayco.createToken(transaction.getCard(), new EpaycoCallback() {
            @Override
            public void onSuccess(JSONObject object) throws JSONException {
                String idTokenCard = object.optString("id");
                transaction.getClient().setTokenId(idTokenCard);

                epayco.createCustomer(transaction.getClient(), new EpaycoCallback() {
                    @Override
                    public void onSuccess(JSONObject data) throws JSONException {
                        Log.d("d", data.toString());


                        transaction.getCharge().setTokenCard(transaction.getClient().getTokenId());
                        transaction.getCharge().setCustomerId(data.getString("customerId"));

                        epayco.createCharge(transaction.getCharge(), new EpaycoCallback() {

                            @Override
                            public void onSuccess(JSONObject data) throws JSONException {
                                Log.d("d", data.toString());
                                Donation donation = new Donation();
                                donation.setRefPayco(data.getString("ref_payco"));
                                donation.setFacture(data.getString("factura"));
                                donation.setFoundationName("PRUEBAS FUNDACIÓN");
                                donation.setMount(data.getString("valor"));
                                donation.setUserid("unusuarioid");
                                DonationService.addDonation(donation);
                            }

                            @Override
                            public void onError(Exception error) { Log.d("d", error.toString()); }
                        });

                    }
                    @Override
                    public void onError(Exception error) {Log.d("d", error.toString());}

                });

            }

            @Override
            public void onError(Exception error) {
                System.out.println("EERROOOOORRR HPTAAAAA: " + error);
            }
        });

    }

    @Override
    public void addDonation(Donation donation) {
        Log.d("DONACIÓN", donation.toString());
    }
}
