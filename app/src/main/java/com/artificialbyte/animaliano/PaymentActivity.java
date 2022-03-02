package com.artificialbyte.animaliano;

import static com.artificialbyte.animaliano.utils.Functions.decimalFormat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.artificialbyte.animaliano.dto.donation.Donation;
import com.artificialbyte.animaliano.dto.user.User;
import com.artificialbyte.animaliano.interfaces.donation.AddDonation;
import com.artificialbyte.animaliano.services.donation.DonationService;
import com.artificialbyte.animaliano.services.epayco.EpayService;
import com.artificialbyte.animaliano.services.epayco.Transaction;
import com.artificialbyte.animaliano.utils.Constans;
import com.artificialbyte.animaliano.utils.Functions;
import com.bumptech.glide.Glide;
import com.fevziomurtekin.payview.Payview;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import co.epayco.android.models.Card;
import co.epayco.android.models.Charge;
import co.epayco.android.models.Client;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

public class PaymentActivity extends AppCompatActivity implements AddDonation {


    Button btnFirstPrice, btnSecondPrice, btnThirdPrice, btnOtherPrice, btnContinue;
    TextView lblFoundationName, textInfo;
    CircleImageView imgFoundation;
    TextInputLayout txtOtherPrice;
    LinearLayout layoutSetPrice, layoutUserData, layoutResult;
    Payview payview;
    TextInputLayout cardName, cardNumber, cardMonth, cardYear, cardCVC;
    TextView ownerCard;
    Button btnPay;
    AutoCompleteTextView documentType;
    LottieAnimationView imgResult;

    User foundation, user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        foundation = (User) getIntent().getSerializableExtra("Foundation");
        user = (User) getIntent().getSerializableExtra("User");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        DonationService.setAddDonation(this);
        layoutSetPrice = findViewById(R.id.layoutSetPrice);
        layoutUserData = findViewById(R.id.layoutUserData);

        btnFirstPrice = findViewById(R.id.btnFirstPrice);
        btnFirstPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCurrentButton(0);
            }
        });

        btnSecondPrice = findViewById(R.id.btnSecondPrice);
        btnSecondPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCurrentButton(1);
            }
        });

        btnThirdPrice = findViewById(R.id.btnThirdPrice);
        btnThirdPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCurrentButton(2);
            }
        });

        btnOtherPrice = findViewById(R.id.btnOtherPrice);
        btnOtherPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCurrentButton(3);
            }
        });

        btnContinue = findViewById(R.id.btnContinuePayment);

        lblFoundationName = findViewById(R.id.lblFoundationName);
        lblFoundationName.setText(foundation.getName());

        imgFoundation = findViewById(R.id.imgFoundation);
        if (!foundation.getProfilePhoto().isEmpty()) {
            Glide.with(getApplicationContext()).load(foundation.getProfilePhoto()).into(imgFoundation);
        }

        txtOtherPrice = findViewById(R.id.txtOtherPrice);
        txtOtherPrice.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (txtOtherPrice.getEditText().getText().toString().isEmpty()){
                    btnOtherPrice.setText("OTRA CANTIDAD");
                }else {
                    btnOtherPrice.setText("$" + decimalFormat(Integer.valueOf(txtOtherPrice.getEditText().getText().toString())));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        payview = findViewById(R.id.payview);

        // on below line we are setting pay on listener for our card.
        /*payview.setPayOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDonationWithData();
                // after clicking on pay we are displaying toast message as card added.
                //Toast.makeText(getApplicationContext(), "Card Added. ", Toast.LENGTH_SHORT).show();
            }
        });*/

        layoutResult = findViewById(R.id.layoutResult);
        textInfo = findViewById(R.id.textInfo);

        cardName = findViewById(R.id.til_card_name);
        cardName.setHint("Nombre de la tarjeta");

        cardNumber = findViewById(R.id.til_card_no);
        cardNumber.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        cardNumber.setHint("Número de tarjeta");

        cardMonth = findViewById(R.id.til_card_month);
        cardMonth.setHint("Mes");

        cardYear = findViewById(R.id.til_card_year);
        cardYear.setHint("Año");

        cardCVC = findViewById(R.id.til_card_cv);
        cardCVC.setHint("CVC");

        ownerCard = findViewById(R.id.tv_card_owner);
        ownerCard.setText("Nombre Apellido");

        btnPay = findViewById(R.id.btn_pay);
        btnPay.setText("DONAR");
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDonationWithData();
            }
        });

        ArrayList<String> documentTypes = new ArrayList<String>(){{add("CC");add("CE");add("NIT");}};
        documentType = findViewById(R.id.documentType);
        documentType.setThreshold(3);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, documentTypes);
        documentType.setAdapter(adapter);
        documentType.setListSelection(0);
        adapter.setNotifyOnChange(true);

        imgResult = findViewById(R.id.imgResult);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return false;
    }

    public void changeLayoutPrice(View view){
        layoutUserData.setVisibility(View.GONE);
        payview.setVisibility(View.VISIBLE);
    }

    public void changeLayoutInfo(View view){
        if (selectPrice != -1) {
            if (selectPrice != 3) {
                layoutSetPrice.setVisibility(View.GONE);
                layoutUserData.setVisibility(View.VISIBLE);
            }else{
                if (txtOtherPrice.getEditText().getText().toString().isEmpty()){
                    Toast.makeText(this, "Debería poner un valor en la transacción", Toast.LENGTH_LONG).show();
                }else if(getAmountDonation() < 5000) {
                    Toast.makeText(this, "La donación minima es de $5.000 pesos.", Toast.LENGTH_LONG).show();
                }else {
                    layoutSetPrice.setVisibility(View.GONE);
                    layoutUserData.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void createDonationWithData(){
        payview.setVisibility(View.GONE);
        layoutResult.setVisibility(View.VISIBLE);
        EpayService.createTransaction(createTransaction(), foundation, user);
    }

    public Transaction createTransaction(){
        Transaction transaction = new Transaction();
        transaction.setCard(getCard());
        transaction.setClient(getClient());
        transaction.setCharge(getCharge());
        return Transaction.getNotFoundsCard();
    }

    public Card getCard(){
        Card card = new Card();
        card.setNumber("4575623182290326"); //Numero tarjeta
        card.setMonth("12"); //Mes tarjeta
        card.setYear("2025"); //Año tarjeta
        card.setCvc("123"); //CVC Tarjeta
        return card;
    }

    public Client getClient(){
        Client client = new Client();
        client.setCustomer_id(user.getUid()); //Uid user
        client.setName(user.getName()); //Nombre user
        client.setEmail(user.getEmail()); //Email user
        client.setPhone("305274321"); //Numero user
        client.setDefaultCard(true);
        return client;
    }

    public Charge getCharge(){
        String chargeValue = String.valueOf(getAmountDonation());
        Charge charge = new Charge();

        charge.setDocType("CC");
        charge.setDocNumber(user.getNit()); //Documento usuario
        charge.setName(user.getName()); //Nombre de usuario
        charge.setLastName(user.getLastName());
        charge.setAddress("ADDRESS"); //Dirección de casa

        charge.setEmail(user.getEmail()); //Email user
        charge.setInvoice(user.getUid());// UID
        charge.setDescription("PAGO A FUNDACIÓN LLAMADA: " + foundation.getName()); //Descripción de la donación
        charge.setValue(chargeValue); //Valor base + IVA
        charge.setTax("0"); //Precio IVA
        charge.setTaxBase(chargeValue); //Precio base
        charge.setCurrency("COP"); //Moneda
        charge.setDues("1"); //Numero de cuotas
        charge.setIp(Functions.getLocalIpAddress());
        return charge;
    }

    private int selectPrice = -1;
    public void setCurrentButton(int btn){

        btnFirstPrice.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_border));
        btnFirstPrice.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text));
        btnSecondPrice.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_border));
        btnSecondPrice.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text));
        btnThirdPrice.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_border));
        btnThirdPrice.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text));
        btnOtherPrice.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_border));
        btnOtherPrice.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text));
        txtOtherPrice.setVisibility(View.GONE);
        btnOtherPrice.setText("OTRA CANTIDAD");

        switch (btn){
            case 0:
                selectPrice = 0;
                btnFirstPrice.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.common_google_signin_btn_icon_dark));
                btnFirstPrice.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                //txtOtherPrice.getEditText().setText("5000");
                break;
            case 1:
                selectPrice = 1;
                btnSecondPrice.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.common_google_signin_btn_icon_dark));
                btnSecondPrice.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                //txtOtherPrice.getEditText().setText("15000");
                break;
            case 2:
                btnThirdPrice.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.common_google_signin_btn_icon_dark));
                btnThirdPrice.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                //txtOtherPrice.getEditText().setText("20000");
                selectPrice = 2;
                break;
            case 3:
                btnOtherPrice.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.common_google_signin_btn_icon_dark));
                btnOtherPrice.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                txtOtherPrice.getEditText().setText("");
                txtOtherPrice.setVisibility(View.VISIBLE);
                selectPrice = 3;
                break;
        }
    }

    public double getAmountDonation(){
        double price = 0;
        if (selectPrice == 0){
            price = 5000;
        }else if (selectPrice == 1){
            price = 15000;
        }else if (selectPrice == 2){
            price = 20000;
        }else{
            price = Double.parseDouble(txtOtherPrice.getEditText().getText().toString());
        }
        return price;
    }


    @Override
    public void addDonation(Donation donation) {
        Toast.makeText(this, donation.toString(), Toast.LENGTH_LONG).show();
        textInfo.setText(donation.toString());

        imgResult.setRepeatCount(0);
        if (donation.getStatus().equals(Constans.CARD_ACCEPTED)){
            imgResult.setAnimation(R.raw.check_successfully);
        } else if (donation.getStatus().equals(Constans.CARD_PENDING)){
            imgResult.setAnimation(R.raw.check_pending);
        } else if (donation.getStatus().equals(Constans.CARD_FAILED)){
            imgResult.setAnimation(R.raw.check_error);
        } else if (donation.getStatus().equals(Constans.CARD_NOT_FOUNDS)){
            imgResult.setAnimation(R.raw.check_error);
        }
        imgResult.playAnimation();

    }
}