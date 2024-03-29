package com.artificialbyte.animaliano;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.artificialbyte.animaliano.dto.pet.Pet;
import com.artificialbyte.animaliano.dto.pet.PetRequest;
import com.artificialbyte.animaliano.dto.pet.Vaccination;
import com.artificialbyte.animaliano.dto.user.User;
import com.artificialbyte.animaliano.interfaces.activity.ShowMessage;
import com.artificialbyte.animaliano.interfaces.pet.AddPet;
import com.artificialbyte.animaliano.interfaces.user.GetUserBy;
import com.artificialbyte.animaliano.interfaces.user.GetUsersFromParam;
import com.artificialbyte.animaliano.services.pet.PetService;
import com.artificialbyte.animaliano.services.user.UserService;
import com.droidbyme.dialoglib.DroidDialog;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

enum ProviderType{
    BASIC,
    GOOGLE,
    FACEBOOK
}

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, GetUserBy, ShowMessage, AddPet {


    private BottomNavigationView bottomNavigationView;
    private LinearLayout fragLoading;


    private String email;
    private String provider;
    SharedPreferences pref;
    private int optionItem = 0;
    private User userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("-");
        UserService.setGetUserBy(this);
        UserService.setShowMessage(this);

        bottomNavigationView = findViewById(R.id.nvgMain);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setVisibility(View.GONE);

        fragLoading = findViewById(R.id.frag_loading);

        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("email");
        provider = bundle.getString("provider");
        if (!email.isEmpty()) {
            UserService.getUserInfo(email, "email");
        }

        pref = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE);
        pref.edit().putString("email", email).commit();
        pref.edit().putString("provider", provider).commit();
        pref.edit().apply();

    }

    IndexFragment indexFragment = new IndexFragment();
    AdoptionFragment adoptionFragment = new AdoptionFragment();
    DonationFragment donationFragment = new DonationFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    AlertFragment alertFragment = new AlertFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mainItem:
                optionItem = 0;
                getSupportFragmentManager().beginTransaction().replace(R.id.container, indexFragment).commit();
                setTitle(R.string.home);
                return true;

            case R.id.adopItem:
                optionItem = 1;
                getSupportFragmentManager().beginTransaction().replace(R.id.container, adoptionFragment).commit();
                setTitle(R.string.title_adoption);
                return true;

            case R.id.donateItem:
                optionItem = 2;
                getSupportFragmentManager().beginTransaction().replace(R.id.container, donationFragment).commit();
                setTitle(R.string.title_donate);
                return true;

            case R.id.alertItem:
                optionItem = 3;
                getSupportFragmentManager().beginTransaction().replace(R.id.container, alertFragment).commit();
                setTitle(R.string.title_alert);
                return true;

            case R.id.profileItem:
                optionItem = 4;
                getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                setTitle(R.string.title_profile);
                return true;
        }
        return false;
    }

    public void btnLogOut_click(View view){
        pref.edit().clear().commit();
        pref.edit().apply();
        if (provider == ProviderType.FACEBOOK.name()){
            LoginManager.getInstance().logOut();
        }
        FirebaseAuth.getInstance().signOut();
        Intent authActivity = new Intent(this, AuthActivity.class);
        startActivity(authActivity);
        finish();
    }


    @Override
    public void getUserBy(User user) {
        if (user != null) {
            setTitle("Inicio");
            userInfo = user;
            fragLoading.setVisibility(View.GONE);
            bottomNavigationView.setSelectedItemId(R.id.mainItem);
            bottomNavigationView.setVisibility(View.VISIBLE);
            profileFragment.setUser(user);
            donationFragment.setUser(user);
            new DroidDialog.Builder(getWindow().getContext())
                    .icon(R.mipmap.logo)
                    .color(ContextCompat.getColor(getWindow().getContext(), R.color.text),
                            ContextCompat.getColor(getWindow().getContext(), R.color.white),
                            ContextCompat.getColor(getWindow().getContext(), R.color.text))
                    .animation(2)
                    .title("¡Completa tu perfil!")
                    .content("Desbes completar tu perfil para poder usar todas las funcionalidades.")
                    .cancelable(true, false)
                    .positiveButton("Ir al perfil", new DroidDialog.onPositiveListener() {
                        @Override
                        public void onPositive(Dialog dialog) {
                            bottomNavigationView.setSelectedItemId(R.id.profileItem);
                            Toast.makeText(getWindow().getContext(), "hola", Toast.LENGTH_LONG).show();
                            dialog.hide();
                        }
                    })
                    .show();

            ArrayList<String> photos = new ArrayList<>();
            photos.add("https://t2.uc.ltmcdn.com/es/posts/9/5/5/10_cosas_que_los_perros_pueden_predecir_te_sorprenderas_43559_600.jpg");
            ArrayList<Vaccination> vacunas = new ArrayList<>();
            vacunas.add(new Vaccination("Rabia", "10-12-2022"));
            ArrayList<PetRequest> request = new ArrayList<>();
            request.add(new PetRequest("prueba", "prueba", "Perro", "31231323", "Quiero ese perrito"));
            Pet pet = new Pet();
            pet.setPetRequests(request);
            pet.setPhotos(photos);
            pet.setVaccinationSchedule(vacunas);

            pet.setUid("example");
            pet.setUidFoundation("example");
            pet.setDescription("example");
            pet.setOwner("example owner");
            pet.setBirthDate("10-10-2021");
            pet.setName("Lupita");
            pet.setAvailable("AVAILABLE");
            //PetService.setAddPet(this);
            //PetService.addPet(pet);


        }else {
            Toast.makeText(this, "No se pudo recuperar la información del usuario.", Toast.LENGTH_LONG).show();
            btnLogOut_click(getWindow().getDecorView());
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        btnLogOut_click(getWindow().getDecorView());
    }

    public void viewPet(View view){
        indexFragment.viewPet(view);
    }

    public void btnAdminFoundation_click(View view){
        profileFragment.btnAdminFoundation_click(view);
    }

    @Override
    public void addPet(Pet pet) {

    }

    public User getUser(){
        return userInfo;
    }

}