package com.artificialbyte.animaliano.services.epayco;

import android.content.Context;
import android.util.Log;

import com.artificialbyte.animaliano.dto.donation.Donation;
import com.artificialbyte.animaliano.dto.user.User;
import com.artificialbyte.animaliano.interfaces.donation.AddDonation;
import com.artificialbyte.animaliano.interfaces.donation.TransactionError;
import com.artificialbyte.animaliano.services.donation.DonationService;
import com.artificialbyte.animaliano.utils.Functions;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import co.epayco.android.Epayco;
import co.epayco.android.models.Authentication;
import co.epayco.android.util.EpaycoCallback;

public class EpayService implements AddDonation {

    private static TransactionError transactionError;

    public static void setTransactionError(TransactionError transactionError) {
        EpayService.transactionError = transactionError;
    }

    public static Epayco epayco = getEpayAuthentication();

    public static void createTransaction(@NotNull Transaction transaction, User foundation, User user) {

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
                                Log.d("Entra al Metodo", data.toString());
                                if (data.toString().contains("\"status\":\"error\"")){
                                    transactionError.triggerError();
                                    return;
                                }

                                Donation donation = new Donation();
                                donation.setRefPayco(data.getString("ref_payco"));
                                donation.setFacture(data.getString("factura"));
                                donation.setFoundationName(foundation.getName());
                                donation.setIdFoundation(foundation.getUid());
                                donation.setMount(data.getString("valor"));
                                donation.setStatus(data.getString("estado"));
                                donation.setUserid(user.getUid());
                                DonationService.addDonation(donation);
                            }

                            @Override
                            public void onError(Exception error) {
                                transactionError.triggerError();
                            }
                        });

                    }
                    @Override
                    public void onError(Exception error) {
                        transactionError.triggerError();
                    }
                });

            }
            @Override
            public void onError(Exception error) {
                transactionError.triggerError();
            }
        });
    }

    public static void getTransactionReference(){
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
    }

    public static Epayco getEpayAuthentication(){
        if(epayco == null) {
            Authentication auth = new Authentication();
            auth.setApiKey("7be429ee3b358e7066be8c978cab72f4");
            auth.setPrivateKey("c017cd60e58a395c472d4f1757263fa5");
            auth.setLang("ES");
            auth.setTest(true);
            epayco = new Epayco(auth);
        }
        return epayco;
    }

    @Override
    public void addDonation(Donation donation) {
        Log.d("DONACIÓN", donation.toString());
    }
}
