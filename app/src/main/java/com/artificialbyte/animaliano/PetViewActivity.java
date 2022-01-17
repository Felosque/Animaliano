package com.artificialbyte.animaliano;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.artificialbyte.animaliano.adapters.SliderAdapter;
import com.artificialbyte.animaliano.dto.pet.Pet;
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

public class PetViewActivity extends AppCompatActivity implements GetPetBy, ShowMessage {

    private SliderAdapter adapter;
    private ArrayList<SliderData> sliderDataArrayList;
    private SliderView sliderView;

    private Pet pet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_view);

        PetService.setGetPetBy(this);

        Bundle bundle = getIntent().getExtras();
        String petid = bundle.getString("petid");
        if (petid != null){
            PetService.findPetBy(petid, "uid");
        }

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        sliderDataArrayList = new ArrayList<>();
        sliderView = findViewById(R.id.slider);
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
                Log.i("ActionBar", "Atrás!");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void getPetBy(Pet pet) {
        this.pet = pet;
        if (pet != null) {
            loadImages(pet);
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}