package com.artificialbyte.animaliano;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

enum ProviderType{
    BASIC,
    GOOGLE,
    FACEBOOK
}

public class HomeActivity extends AppCompatActivity {

    private TextView mail;
    private TextView message;
    SharedPreferences pref;

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

        pref = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE);
        pref.edit().putString("email", email).commit();
        pref.edit().putString("provider", message).commit();
        pref.edit().apply();

        this.mail.setText(email);
        this.message.setText(message);
    }

    public void btnLogOut_click(View view){
        pref.edit().clear().commit();
        pref.edit().apply();
        FirebaseAuth.getInstance().signOut();
        Intent authActivity = new Intent(this, AuthActivity.class);
        startActivity(authActivity);
        finish();
    }


}