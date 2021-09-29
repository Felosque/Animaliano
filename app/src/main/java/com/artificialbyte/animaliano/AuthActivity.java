package com.artificialbyte.animaliano;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.artificialbyte.animaliano.dto.user.User;
import com.artificialbyte.animaliano.interfaces.user.GetUserBy;
import com.artificialbyte.animaliano.interfaces.user.InUserRegister;
import com.artificialbyte.animaliano.services.user.UserService;
import com.artificialbyte.animaliano.utils.Constans;
import com.droidbyme.dialoglib.DroidDialog;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AuthActivity extends AppCompatActivity implements InUserRegister, GetUserBy {

    private LinearLayout step_0, step_1, step_2, step_3, step_4, step_5, step_resume, title_desc, frag_loading;
    private LinearLayout step_login, step_mail_login, step_recovery_password;
    private int state_step = 0;
    private int login_step = 0;
    private TextView txtTitle, txtDescription, txtMailRecover;
    private TextView email, password, password_2; //Step 3
    private TextView name, nit, description; //Step 4
    private CircleImageView img; //step 5
    private TextView name_resume, mail_resume, description_resume; //Resume
    private CircleImageView img_resume; //Resume
    private TextView email_login, password_login;
    private User userToRegister;
    private LinearLayout authLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        FirebaseAuth.getInstance().setLanguageCode("es");

        frag_loading = findViewById(R.id.frag_loading);
        frag_loading.setVisibility(View.VISIBLE);

        UserService.setInUserRegister(this);
        UserService.setGetUserBy(this);
        userToRegister = new User();

        step_0 = findViewById(R.id.step_0);
        step_0.setVisibility(View.GONE);
        step_1 = findViewById(R.id.step_1);
        step_2 = findViewById(R.id.step_2);
        step_3 = findViewById(R.id.step_3);
        step_4 = findViewById(R.id.step_4);
        step_5 = findViewById(R.id.step_5);
        step_resume = findViewById(R.id.step_resume);
        step_login = findViewById(R.id.step_login);
        step_mail_login = findViewById(R.id.step_mail_login);
        step_recovery_password = findViewById(R.id.step_recovery_password);
        txtMailRecover = findViewById(R.id.txtMail_Recover);

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
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        img_resume = findViewById(R.id.img_resume);
        //Step Resume
        mail_resume = findViewById(R.id.txtMailResume);
        description_resume = findViewById(R.id.txtDesc_Resume);
        name_resume = findViewById(R.id.txtName_Resume);
        //Login
        email_login = findViewById(R.id.txtMail_Login);
        password_login = findViewById(R.id.txtPassword_Login);
        authLayout = findViewById(R.id.authLayout);
        step_0.setVisibility(View.VISIBLE);
        frag_loading.setVisibility(View.GONE);

        setup();
    }

    public void setup(){
        /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            showHome(user.getEmail(), ProviderType.BASIC);
        }*/
        SharedPreferences pref = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE);
        String email = pref.getString("email", null);
        String provider = pref.getString("provider", null);

        if (email != null && provider != null){
            authLayout.setVisibility(View.GONE);
            showHome(email, ProviderType.valueOf(provider));
        }
    }

    public void nextStep_Click(View view){
        switch (state_step){
            case 0:
                step_0.setVisibility(View.GONE);
                step_1.setVisibility(View.VISIBLE);
                title_desc.setVisibility(View.VISIBLE);
                txtTitle.setVisibility(View.VISIBLE);
                txtDescription.setVisibility(View.VISIBLE);
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
                txtTitle.setVisibility(View.VISIBLE);
                txtDescription.setVisibility(View.VISIBLE);
                if (userToRegister.getRol().equals(Constans.USER_ROL_FOUNDATION)){
                    txtTitle.setText(getResources().getString(R.string.title_fund));
                }else{
                    txtTitle.setText(getResources().getString(R.string.title_user));
                }
                txtDescription.setText(getResources().getString(R.string.description_step_3));
                step_2.setVisibility(View.GONE);
                step_3.setVisibility(View.VISIBLE);
                break;
            case 3:
                title_desc.setVisibility(View.GONE);
                step_3.setVisibility(View.GONE);
                frag_loading.setVisibility(View.VISIBLE);
                signUp_onClick(getWindow().getDecorView().getRootView());
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(email.getWindowToken(), 0);
                return;
            case 4:
                userToRegister.setName(name.getText().toString());
                userToRegister.setNit(nit.getText().toString());
                userToRegister.setDescription(description.getText().toString());
                title_desc.setVisibility(View.VISIBLE);
                txtTitle.setVisibility(View.VISIBLE);
                txtDescription.setVisibility(View.VISIBLE);
                if (userToRegister.getRol().equals(Constans.USER_ROL_FOUNDATION)){
                    txtTitle.setText(getResources().getString(R.string.title_fund));
                    txtDescription.setText(getResources().getString(R.string.description_step_5_fund));
                }else{
                    txtTitle.setText(getResources().getString(R.string.title_user));
                    txtDescription.setText(getResources().getString(R.string.description_step_5_user));
                }
                step_4.setVisibility(View.GONE);
                step_5.setVisibility(View.VISIBLE);
                break;
            case 5:
                title_desc.setVisibility(View.VISIBLE);
                txtTitle.setText(getResources().getString(R.string.title_step_resume));
                txtTitle.setVisibility(View.VISIBLE);
                txtDescription.setVisibility(View.GONE);
                step_5.setVisibility(View.GONE);
                name_resume.setText(userToRegister.getName());
                description_resume.setText(userToRegister.getDescription());
                mail_resume.setText(userToRegister.getEmail());
                img_resume.setImageURI(imageUri);
                step_resume.setVisibility(View.VISIBLE);
                break;
            case 6:
                title_desc.setVisibility(View.GONE);
                step_resume.setVisibility(View.GONE);
                frag_loading.setVisibility(View.VISIBLE);
                providerRegister = Constans.EMAIL_REGISTER;
                UserService.addUser(userToRegister, Constans.EMAIL_REGISTER);
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
                txtTitle.setVisibility(View.GONE);
                txtDescription.setVisibility(View.GONE);
                break;
            case 2:
                step_2.setVisibility(View.GONE);
                step_1.setVisibility(View.VISIBLE);
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
            frag_loading.setVisibility(View.GONE);
            step_3.setVisibility(View.VISIBLE);
            title_desc.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Las contraseñas deben ser iguales", Toast.LENGTH_LONG).show();
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
                        if (userToRegister.getRol().equals(Constans.USER_ROL_FOUNDATION)){
                            txtTitle.setText(getResources().getString(R.string.title_fund));
                        }else{
                            txtTitle.setText(getResources().getString(R.string.title_user));
                        }
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
            frag_loading.setVisibility(View.GONE);
            step_3.setVisibility(View.VISIBLE);
            title_desc.setVisibility(View.VISIBLE);
        }
    }

    private CallbackManager callbackManager = CallbackManager.Factory.create();
    public void signUpFacebook_onClick(View view){
        List<String> permission = new ArrayList<>();
        permission.add("email");
        LoginManager.getInstance().logInWithReadPermissions(this, permission);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (loginResult != null){
                    AccessToken token = loginResult.getAccessToken();
                    AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        userToRegister.setUid(task.getResult().getUser().getUid());
                                        userToRegister.setName(task.getResult().getUser().getDisplayName());
                                        userToRegister.setPhone(task.getResult().getUser().getPhoneNumber());
                                        userToRegister.setEmail(task.getResult().getUser().getEmail());
                                        userToRegister.setDescription("Registrado con Facebook");
                                        providerRegister = Constans.FACEBOOK_REGISTER;
                                        UserService.getUserInfo(userToRegister.getUid(), "uid");
                                    } else {
                                        showError(task.getException().getMessage());
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                showError("Ha ocurrido un error al intentar iniciar sesión");
            }
        });
    }

    public void signUpGoogle_onClick(View view){
        signInGoogle_onClick(view);
    }

    public void signInGoogle_onClick(View view){
        GoogleSignInOptions googleConf =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail().build();
        GoogleSignInClient googleClient = GoogleSignIn.getClient(this, googleConf);
        googleClient.signOut();
        startActivityForResult(googleClient.getSignInIntent(), Constans.GOOGLE_SIGN_IN);
    }

    public void signIn_onClick(View view) {
        String email = this.email_login.getText().toString();
        String password = this.password_login.getText().toString();
        if (!email.isEmpty() && !password.isEmpty()){
            step_mail_login.setVisibility(View.GONE);
            frag_loading.setVisibility(View.VISIBLE);
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String emailUser = task.getResult().getUser().getEmail();
                                showHome(emailUser, ProviderType.BASIC);
                            } else {
                                frag_loading.setVisibility(View.GONE);
                                step_mail_login.setVisibility(View.VISIBLE);
                                showError(task.getException().getMessage());
                            }
                        }
                    });
        }else {
            Toast.makeText(this, "Debe completar el formulario", Toast.LENGTH_LONG).show();
        }
    }

    public void loginNextStep(View view){
        switch (login_step) {
            case 0:
                step_0.setVisibility(View.GONE);
                step_login.setVisibility(View.VISIBLE);
                break;
            case 1:
                step_login.setVisibility(View.GONE);
                step_mail_login.setVisibility(View.VISIBLE);
                break;
            default:
                step_0.setVisibility(View.VISIBLE);
                step_login.setVisibility(View.GONE);
                step_mail_login.setVisibility(View.GONE);
                login_step = 0;
                break;
        }
        login_step++;
    }

    public void loginBackStep(View view){
        switch (login_step) {
            case 1:
                step_login.setVisibility(View.GONE);
                step_0.setVisibility(View.VISIBLE);
                break;
            case 2:
                step_login.setVisibility(View.VISIBLE);
                step_mail_login.setVisibility(View.GONE);
                break;
            default:
                step_0.setVisibility(View.VISIBLE);
                step_login.setVisibility(View.GONE);
                step_mail_login.setVisibility(View.GONE);
                break;
        }
        login_step--;
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


    Uri imageUri;
    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, Constans.PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constans.PICK_IMAGE) {
            imageUri = data.getData();
            img.setImageURI(imageUri);
        }
        if (requestCode == Constans.GOOGLE_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult();
                if (account != null) {
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        userToRegister.setUid(task.getResult().getUser().getUid());
                                        userToRegister.setName(task.getResult().getUser().getDisplayName());
                                        userToRegister.setPhone(task.getResult().getUser().getPhoneNumber());
                                        userToRegister.setEmail(task.getResult().getUser().getEmail());
                                        userToRegister.setDescription("Registrado con Google");
                                        providerRegister = Constans.GOOGLE_REGISTER;
                                        UserService.getUserInfo(userToRegister.getUid(), "uid");
                                    } else {
                                        showError(task.getException().getMessage());
                                    }
                                }
                            });
                }
            }catch (Exception e){
                showError(e.getMessage());
            }
        }
    }

    int uploadProcess = 0;
    int providerRegister = -1;
    @Override
    public void inUserRegister(Boolean e,  int provider) {
        if(provider == Constans.EMAIL_REGISTER) {
            if (e) {
                uploadProcess++;
                if (uploadProcess == 1) {
                    img_resume.setDrawingCacheEnabled(true);
                    img_resume.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) img_resume.getDrawable()).getBitmap();
                    UserService.updateUserProfileImage(userToRegister.getUid(), bitmap);
                } else if (uploadProcess == 2) {
                    showHome(userToRegister.getEmail(), ProviderType.BASIC);
                }
            } else {
                Toast.makeText(this, "Ocurrió un problema a la hora de actualizar datos", Toast.LENGTH_LONG).show();
                step_resume.setVisibility(View.VISIBLE);
                frag_loading.setVisibility(View.GONE);
                title_desc.setVisibility(View.VISIBLE);
            }
        }
        else if (provider == Constans.GOOGLE_REGISTER) {
            showHome(userToRegister.getEmail(), ProviderType.GOOGLE);
        }
        else if (provider == Constans.FACEBOOK_REGISTER){
            showHome(userToRegister.getEmail(), ProviderType.FACEBOOK);
        }
    }

    public void setUserFundation(View view){
        userToRegister.setRol(Constans.USER_ROL_FOUNDATION);
        nextStep_Click(view);
    }
    public void setUserDefault(View view){
        userToRegister.setRol(Constans.USER_ROL_DEFAULT);
        nextStep_Click(view);
    }

    public void recoveryPassword(View view){

        String emailRecover = txtMailRecover.getText().toString();
        FirebaseAuth.getInstance().setLanguageCode("es");
        FirebaseAuth.getInstance().sendPasswordResetEmail(emailRecover)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            new DroidDialog.Builder(getWindow().getContext())
                                    .icon(R.mipmap.logo)
                                    .color(ContextCompat.getColor(getWindow().getContext(), R.color.text),
                                            ContextCompat.getColor(getWindow().getContext(), R.color.white),
                                            ContextCompat.getColor(getWindow().getContext(), R.color.text))
                                    .animation(2)
                                    .title("¡Enviamos un correo!")
                                    .content("Dirigite al correo " + emailRecover + " para finalizar el proceso de recuperación de contraseña.")
                                    .cancelable(true, false)
                                    .positiveButton("¡Entendido!", new DroidDialog.onPositiveListener() {
                                        @Override
                                        public void onPositive(Dialog dialog) {
                                            backStepRecoveryPassword(getWindow().getDecorView());
                                            txtMailRecover.setText("");
                                            dialog.hide();
                                        }
                                    })
                                    .show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Ups, ocurrió un error al intentar enviar el correo", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void nextStepRecoveryPassword(View view){
        step_login.setVisibility(View.GONE);
        step_recovery_password.setVisibility(View.VISIBLE);
    }
    public void backStepRecoveryPassword(View view){
        step_login.setVisibility(View.VISIBLE);
        step_recovery_password.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        authLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void getUserBy(User user) {
        if(user == null){
            UserService.addUser(userToRegister, providerRegister);
        }else{
            String emailUser = user.getEmail();
            if (providerRegister == Constans.EMAIL_REGISTER) {
                showHome(emailUser, ProviderType.BASIC);
            }
            else if (providerRegister == Constans.GOOGLE_REGISTER) {
                showHome(emailUser, ProviderType.GOOGLE);
            }
            else if (providerRegister == Constans.FACEBOOK_REGISTER) {
                showHome(emailUser, ProviderType.FACEBOOK);
            }
        }

    }
}