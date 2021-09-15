package com.artificialbyte.animaliano;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.artificialbyte.animaliano.dto.user.User;
import com.artificialbyte.animaliano.interfaces.CRUDUser;
import com.artificialbyte.animaliano.services.user.UserService;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

enum ProviderType{
    BASIC,
    GOOGLE,
    FACEBOOK
}

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, CRUDUser {


    private BottomNavigationView bottomNavigationView;

    private String email;
    private String provider;
    SharedPreferences pref;
    private User userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle(R.string.home);
        UserService.setCrudUser(this);

        bottomNavigationView = findViewById(R.id.nvgMain);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.mainItem);

        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("email");
        provider = bundle.getString("provider");
        if (!email.isEmpty()) {
            UserService.getUserInfo(email);
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
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, indexFragment).commit();
                return true;

            case R.id.adopItem:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, adoptionFragment).commit();
                return true;

            case R.id.donateItem:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, donationFragment).commit();
                return true;

            case R.id.profileItem:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
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
    public void isRegister(Boolean e) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showUser(String message) {

    }

    @Override
    public void getUserByEmail(User user) {
        userInfo = user;
        profileFragment.setUser(user);
    }
}