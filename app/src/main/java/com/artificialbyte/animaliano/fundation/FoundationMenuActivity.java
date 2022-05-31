package com.artificialbyte.animaliano.fundation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.artificialbyte.animaliano.AddPet;
import com.artificialbyte.animaliano.R;
import com.artificialbyte.animaliano.dto.user.User;
import com.bumptech.glide.Glide;
import de.hdodenhof.circleimageview.CircleImageView;

public class FoundationMenuActivity extends AppCompatActivity {

    TextView name;

    CircleImageView image;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fundation_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        name = findViewById(R.id.txtNombre);
        image = findViewById(R.id.img_profile);

        Bundle params = getIntent().getExtras();
        if (params != null) {
            user = (User) params.getSerializable("USER");
            name.setText("Balance: "+ user.getBalance());
            if (!user.getProfilePhoto().isEmpty()) {
                Glide.with(this.getApplicationContext()).load(user.getProfilePhoto()).into(image);
            }else {
                image.setImageResource(R.mipmap.logo);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i("ActionBar", "Atrás!");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addPet(View view){
        Intent paymentActivity = new Intent(getApplicationContext(), AddPet.class);
        paymentActivity.putExtra("Foundation", user);
        startActivity(paymentActivity);
    }

    public void deletePet(View view){
        Intent deleteActivity = new Intent(getApplicationContext(), DeletePetActivity.class);
        deleteActivity.putExtra("user", user);
        startActivity(deleteActivity);
    }

}