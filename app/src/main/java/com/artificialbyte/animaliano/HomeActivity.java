package com.artificialbyte.animaliano;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

enum ProviderType{
    BASIC
}

public class HomeActivity extends AppCompatActivity {

    private TextView mail;
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle(R.string.home);

        mail = findViewById(R.id.txtCorreo);
        message = findViewById(R.id.txtNombre);

        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");
        String message = bundle.getString("provider");

        this.mail.setText(email);
        this.message.setText(message);
    }

    public void btnLogOut_click(View view){
        FirebaseAuth.getInstance().signOut();
        onBackPressed();
    }


}