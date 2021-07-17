package com.artificialbyte.animaliano;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthActivity extends AppCompatActivity {

    private TextView email;
    private TextView password;
    private Button btnSignUp;
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        email = findViewById(R.id.txtMail);
        password = findViewById(R.id.txtPassword);
        btnSignIn = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnRegister);
    }

    public void signUp_onClick(View view) {
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();
        if (!email.isEmpty() && !password.isEmpty()){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String emailUser = task.getResult().getUser().getEmail();
                        showHome(emailUser, ProviderType.BASIC);
                    } else {
                        showError(task.getException().getMessage());
                    }
                }
            });
        }else {
            Toast.makeText(this, "Debe completar el formulario", Toast.LENGTH_LONG).show();
        }
    }

    public void signIn_onClick(View view) {
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();
        if (!email.isEmpty() && !password.isEmpty()){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String emailUser = task.getResult().getUser().getEmail();
                                showHome(emailUser, ProviderType.BASIC);
                            } else {
                                showError(task.getException().getMessage());
                            }
                        }
                    });
        }else {
            Toast.makeText(this, "Debe completar el formulario", Toast.LENGTH_LONG).show();
        }
    }

    public void showError(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void showHome(String email, ProviderType providerType){
        Intent homeIntent = new Intent(this, HomeActivity.class);
        homeIntent.putExtra("email", email);
        homeIntent.putExtra("provider", providerType.name());
        startActivity(homeIntent);
    }
}