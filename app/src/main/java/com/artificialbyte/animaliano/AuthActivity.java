package com.artificialbyte.animaliano;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.artificialbyte.animaliano.dto.user.User;
import com.artificialbyte.animaliano.interfaces.CRUDUser;
import com.artificialbyte.animaliano.services.user.UserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthActivity extends AppCompatActivity implements CRUDUser {

    private LinearLayout step_0, step_1, step_2, step_3, step_4, step_5, step_resume, title_desc, frag_loading;

    private int state_step = 0;
    private TextView txtTitle, txtDescription;
    private TextView email, password, password_2; //Step 3
    private TextView name, nit, description; //Step 4
    private ImageView img; //step 5
    private User userToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        UserService.setCrudUser(this);

        userToRegister = new User();
        step_0 = findViewById(R.id.step_0);
        step_1 = findViewById(R.id.step_1);
        step_2 = findViewById(R.id.step_2);
        step_3 = findViewById(R.id.step_3);
        step_4 = findViewById(R.id.step_4);
        step_5 = findViewById(R.id.step_5);
        step_resume = findViewById(R.id.step_resume);
        frag_loading = findViewById(R.id.frag_loading);

        txtTitle = findViewById(R.id.txtTitle_fragment);
        txtDescription = findViewById(R.id.txtDescription_fragment);
        title_desc = findViewById(R.id.title_description);
        //Step 3
        email = findViewById(R.id.txtMail_Register);
        password = findViewById(R.id.txtPassword_Register);
        password_2 = findViewById(R.id.txtPassword_Register_2);
        //Step 4
        name = findViewById(R.id.txtName_Register);
        nit = findViewById(R.id.txtNit_Register);
        description = findViewById(R.id.txtDesc_Register);
        //Step 5
        img = findViewById(R.id.imageView);
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
            case 3:
                signUp_onClick(getWindow().getDecorView().getRootView());
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(email.getWindowToken(), 0);
                title_desc.setVisibility(View.GONE);
                step_3.setVisibility(View.GONE);
                frag_loading.setVisibility(View.VISIBLE);
                return;
            case 4:
                userToRegister.setName(name.getText().toString());
                userToRegister.setNit(nit.getText().toString());
                userToRegister.setDescription(description.getText().toString());
                title_desc.setVisibility(View.VISIBLE);
                txtTitle.setText(getResources().getString(R.string.title_step_5));
                txtDescription.setText(getResources().getString(R.string.description_step_5));
                step_4.setVisibility(View.GONE);
                step_5.setVisibility(View.VISIBLE);
                break;
            case 5:
                title_desc.setVisibility(View.VISIBLE);
                txtTitle.setText(getResources().getString(R.string.title_step_resume));
                txtDescription.setVisibility(View.GONE);
                step_5.setVisibility(View.GONE);
                step_resume.setVisibility(View.VISIBLE);
                break;
            case 6:
                //Falta registro en colección
                UserService.addUser(userToRegister);
                break;
            default:
                step_0.setVisibility(View.VISIBLE);
                title_desc.setVisibility(View.GONE);
                txtDescription.setVisibility(View.VISIBLE);
                step_1.setVisibility(View.GONE);
                step_2.setVisibility(View.GONE);
                step_3.setVisibility(View.GONE);
                step_4.setVisibility(View.GONE);
                step_5.setVisibility(View.GONE);
                step_resume.setVisibility(View.GONE);
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

                break;
            case 3:
                state_step = -2;
                nextStep_Click(getWindow().getDecorView().getRootView());
                break;
            case 4:
                step_3.setVisibility(View.VISIBLE);
                step_4.setVisibility(View.GONE);
                break;
            case 5:
                step_4.setVisibility(View.VISIBLE);
                step_5.setVisibility(View.GONE);
                break;
            case 6:
                step_5.setVisibility(View.VISIBLE);
                step_resume.setVisibility(View.GONE);
                break;
            default:
                step_0.setVisibility(View.VISIBLE);
                title_desc.setVisibility(View.GONE);
                txtDescription.setVisibility(View.VISIBLE);
                step_1.setVisibility(View.GONE);
                step_2.setVisibility(View.GONE);
                step_3.setVisibility(View.GONE);
                step_4.setVisibility(View.GONE);
                step_5.setVisibility(View.GONE);
                step_resume.setVisibility(View.GONE);
                state_step = 1;
                break;
        }
        state_step--;
    }

    public void signUp_onClick(View view) {
        String emailF = this.email.getText().toString();
        String passwordF = this.password.getText().toString();
        if (!password.getText().toString().equals(password_2.getText().toString())){
            Toast.makeText(this, "Las contraseñas deben ser iguales", Toast.LENGTH_LONG).show();
            frag_loading.setVisibility(View.GONE);
            step_3.setVisibility(View.VISIBLE);
            title_desc.setVisibility(View.VISIBLE);
            return;
        }
        if (!emailF.isEmpty() && !passwordF.isEmpty()){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailF, passwordF)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //String emailUser = task.getResult().getUser().getEmail();
                        //showHome(emailUser, ProviderType.BASIC);
                        userToRegister.setEmail(emailF);
                        userToRegister.setUid(task.getResult().getUser().getUid());
                        title_desc.setVisibility(View.VISIBLE);
                        txtTitle.setText(getResources().getString(R.string.title_step_4));
                        txtDescription.setText(getResources().getString(R.string.description_step_4));
                        step_3.setVisibility(View.GONE);
                        step_4.setVisibility(View.VISIBLE);
                        frag_loading.setVisibility(View.GONE);
                        state_step = 4;
                    } else {
                        showError(task.getException().getMessage());
                        frag_loading.setVisibility(View.GONE);
                        step_3.setVisibility(View.VISIBLE);
                        title_desc.setVisibility(View.VISIBLE);
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
        finish();
    }

    @Override
    public void isRegister(Boolean e) {
        if(e){
            showHome("prueba@gmail.com", ProviderType.BASIC);
        }else {
            Toast.makeText(this, "Ocurrió un problema a la hora de actualizar datos", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showUser(String message) {

    }
}