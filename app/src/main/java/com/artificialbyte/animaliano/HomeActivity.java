package com.artificialbyte.animaliano;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.artificialbyte.animaliano.dto.user.User;
import com.artificialbyte.animaliano.interfaces.activity.ShowMessage;
import com.artificialbyte.animaliano.interfaces.user.GetUserBy;
import com.artificialbyte.animaliano.services.user.UserService;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

enum ProviderType{
    BASIC,
    GOOGLE,
    FACEBOOK
}

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, GetUserBy, ShowMessage {


    private BottomNavigationView bottomNavigationView;
    private LinearLayout fragLoading;


    private String email;
    private String provider;
    SharedPreferences pref;
    private int optionItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("-");
        UserService.setGetUserBy(this);
        UserService.setShowMessage(this);

        fragLoading = findViewById(R.id.frag_loading);

        bottomNavigationView = findViewById(R.id.nvgMain);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setVisibility(View.GONE);

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

            case R.id.profileItem:
                optionItem = 3;
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
            fragLoading.setVisibility(View.GONE);
            bottomNavigationView.setSelectedItemId(R.id.mainItem);
            bottomNavigationView.setVisibility(View.VISIBLE);
            profileFragment.setUser(user);
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
}