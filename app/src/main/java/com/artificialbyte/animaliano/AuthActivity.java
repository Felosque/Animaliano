package com.artificialbyte.animaliano;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthActivity extends AppCompatActivity {

    private LinearLayout step_0, step_1, step_2, step_3, title_desc;

    private int state_step = 0;
    private TextView txtTitle, txtDescription;
    private TextView email, password;
    private Button btnSignUp;
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        step_0 = findViewById(R.id.step_0);
        step_1 = findViewById(R.id.step_1);
        step_2 = findViewById(R.id.step_2);
        step_3 = findViewById(R.id.step_3);

        txtTitle = findViewById(R.id.txtTitle_fragment);
        txtDescription = findViewById(R.id.txtDescription_fragment);
        title_desc = findViewById(R.id.title_description);
        /*email = findViewById(R.id.txtMail);
        password = findViewById(R.id.txtPassword);
        btnSignIn = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnRegister);*/
    }

    public void nextStep_Click(View view){
        switch (state_step){
            case 0:
                step_0.setVisibility(View.GONE);
                step_1.setVisibility(View.VISIBLE);
                title_desc.setVisibility(View.VISIBLE);
                txtTitle.setText(getResources().getString(R.string.title_step_1));
                txtDescription.setText(getResources().getString(R.string.description_step_1));
                break;
            case 1:
                title_desc.setVisibility(View.GONE);
                step_1.setVisibility(View.GONE);
                step_2.setVisibility(View.VISIBLE);
                break;
            case 2:
                title_desc.setVisibility(View.VISIBLE);
                txtTitle.setText(getResources().getString(R.string.title_step_3));
                txtDescription.setText(getResources().getString(R.string.description_step_3));
                step_2.setVisibility(View.GONE);
                step_3.setVisibility(View.VISIBLE);
                break;
            default:
                step_0.setVisibility(View.VISIBLE);
                title_desc.setVisibility(View.GONE);
                step_1.setVisibility(View.GONE);
                step_2.setVisibility(View.GONE);
                step_3.setVisibility(View.GONE);
                state_step = -1;
                break;
        }
        state_step++;
    }

    public void backStep_Click(View view){
        switch (state_step){
            case 1:
                step_0.setVisibility(View.VISIBLE);
                step_1.setVisibility(View.GONE);
                break;
            case 2:
                step_1.setVisibility(View.VISIBLE);
                step_2.setVisibility(View.GONE);
                break;
            case 3:
                step_2.setVisibility(View.VISIBLE);
                step_3.setVisibility(View.GONE);
                break;
            default:
                step_0.setVisibility(View.VISIBLE);
                step_1.setVisibility(View.GONE);
                step_2.setVisibility(View.GONE);
                step_3.setVisibility(View.GONE);
                break;
        }
        state_step--;
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