package com.artificialbyte.animaliano;

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

import com.artificialbyte.animaliano.dto.user.User;
import com.artificialbyte.animaliano.utils.Functions;
import com.fevziomurtekin.payview.Payview;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PaymentActivity extends AppCompatActivity {


    Button btnFirstPrice, btnSecondPrice, btnThirdPrice, btnOtherPrice, btnContinue;
    TextView lblFoundationName;
    CircleImageView imgFoundation;
    TextInputLayout txtOtherPrice;
    LinearLayout layoutSetPrice, layoutUserData;
    Payview payview;
    TextInputLayout cardName, cardNumber, cardMonth, cardYear, cardCVC;
    TextView ownerCard;
    Button btnPay;
    AutoCompleteTextView documentType;

    User foundation, user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        foundation = (User) getIntent().getSerializableExtra("Foundation");
        user = (User) getIntent().getSerializableExtra("User");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

        imgFoundation = findViewById(R.id.imgFoundation);

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
                    btnOtherPrice.setText("$" + Functions.decimalFormat(Integer.valueOf(txtOtherPrice.getEditText().getText().toString())));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        payview = findViewById(R.id.payview);

        // on below line we are setting pay on listener for our card.
        payview.setPayOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // after clicking on pay we are displaying toast message as card added.
                Toast.makeText(getApplicationContext(), "Card Added. ", Toast.LENGTH_SHORT).show();
            }
        });

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

        ArrayList<String> documentTypes = new ArrayList<String>(){{add("CC");add("CE");add("NIT");}};
        documentType = findViewById(R.id.documentType);
        documentType.setThreshold(3);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, documentTypes);
        documentType.setAdapter(adapter);
        documentType.setListSelection(0);
        adapter.setNotifyOnChange(true);

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
        layoutSetPrice.setVisibility(View.GONE);
        layoutUserData.setVisibility(View.VISIBLE);
    }

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
                btnFirstPrice.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.common_google_signin_btn_icon_dark));
                btnFirstPrice.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                //txtOtherPrice.getEditText().setText("5000");
                break;
            case 1:
                btnSecondPrice.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.common_google_signin_btn_icon_dark));
                btnSecondPrice.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                //txtOtherPrice.getEditText().setText("15000");
                break;
            case 2:
                btnThirdPrice.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.common_google_signin_btn_icon_dark));
                btnThirdPrice.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                //txtOtherPrice.getEditText().setText("20000");
                break;
            case 3:
                btnOtherPrice.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.common_google_signin_btn_icon_dark));
                btnOtherPrice.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                txtOtherPrice.getEditText().setText("");
                txtOtherPrice.setVisibility(View.VISIBLE);
                break;
        }
    }


}