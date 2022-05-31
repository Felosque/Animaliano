package com.artificialbyte.animaliano;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.artificialbyte.animaliano.adapters.SliderAdapter;
import com.artificialbyte.animaliano.dto.pet.Pet;
import com.artificialbyte.animaliano.dto.pet.Vaccination;
import com.artificialbyte.animaliano.dto.user.User;
import com.artificialbyte.animaliano.interfaces.activity.ShowMessage;
import com.artificialbyte.animaliano.interfaces.pet.GetPetBy;
import com.artificialbyte.animaliano.services.pet.PetService;
import com.artificialbyte.animaliano.utils.SliderData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class PetViewActivity extends AppCompatActivity implements ShowMessage {

    private SliderAdapter adapter;
    private ArrayList<SliderData> sliderDataArrayList;
    private SliderView sliderView;

    private LinearLayout petInfo, petRequest;

    private EditText txtPetName, txtDescription, txtVaccination, txtDate, txtFoundation;

    private Pet pet;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_view);

        user = (User) getIntent().getSerializableExtra("User");
        pet = (Pet) getIntent().getSerializableExtra("Pet");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtPetName = findViewById(R.id.txtPetName);
        txtDescription = findViewById(R.id.txtDescription);
        txtVaccination = findViewById(R.id.txtVaccination);
        txtDate = findViewById(R.id.txtDate);
        txtFoundation = findViewById(R.id.txtFoundation);

        petInfo = findViewById(R.id.petInfo);
        petRequest = findViewById(R.id.petRequest);

        sliderDataArrayList = new ArrayList<>();
        sliderView = findViewById(R.id.slider);
        if (pet != null) {
            loadImages(pet);
            txtPetName.setText(pet.getName());
            txtDate.setText(pet.getBirthDate());
            txtDescription.setText(pet.getDescription());
            txtFoundation.setText(pet.getOwner());
            StringBuilder stringBuilder = new StringBuilder();
            for (Vaccination vaccination : pet.getVaccinationSchedule()){
                stringBuilder.append(vaccination.getName());
                stringBuilder.append(", ");
            }
            txtVaccination.setText(stringBuilder.toString());
        }
    }

    private void loadImages(Pet pet) {
        if (pet.getPhotos() != null) {
            for (String imageUrl : pet.getPhotos()) {

                SliderData model = new SliderData();
                model.setImgUrl(imageUrl);
                sliderDataArrayList.add(model);

                adapter = new SliderAdapter(PetViewActivity.this, sliderDataArrayList);

                sliderView.setSliderAdapter(adapter);
                sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                sliderView.setScrollTimeInSec(3);
                sliderView.setAutoCycle(true);
                sliderView.startAutoCycle();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (petRequest.getVisibility() == View.VISIBLE){
                    showPetRequest(false);
                }else {
                    Log.i("ActionBar", "Atrás!");
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void sendRequest(View view) {
        if(petInfo.getVisibility() == View.VISIBLE){
            showPetRequest(true);
        }else if(petRequest.getVisibility() == View.VISIBLE){
            Toast.makeText(this, "Se envío la petición", Toast.LENGTH_LONG).show();
        }
    }

    private void showPetRequest(boolean show){
        if (show){
            petInfo.setVisibility(View.GONE);
            petRequest.setVisibility(View.VISIBLE);
        }else{
            petInfo.setVisibility(View.VISIBLE);
            petRequest.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}