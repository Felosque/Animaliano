package com.artificialbyte.animaliano;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.artificialbyte.animaliano.dto.pet.Pet;
import com.artificialbyte.animaliano.dto.pet.PetRequest;
import com.artificialbyte.animaliano.dto.pet.Vaccination;
import com.artificialbyte.animaliano.dto.user.User;
import com.artificialbyte.animaliano.interfaces.activity.ShowMessage;
import com.artificialbyte.animaliano.interfaces.pet.PetImageUpload;
import com.artificialbyte.animaliano.services.pet.PetService;
import com.artificialbyte.animaliano.utils.Constans;
import com.artificialbyte.animaliano.utils.Functions;

import java.util.ArrayList;

public class AddPet extends AppCompatActivity implements ShowMessage, com.artificialbyte.animaliano.interfaces.pet.AddPet, PetImageUpload {

    EditText vaccinationName, txtDescription, txtPetName;
    TextView textVaccination;
    ImageView imgPet;
    ArrayList<Vaccination> vaccinationSchedule;
    Pet petToUpload;
    User foundation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        foundation = (User) getIntent().getSerializableExtra("Foundation");
        imgPet = findViewById(R.id.imgPet);
        vaccinationSchedule = new ArrayList<>();
        vaccinationName = findViewById(R.id.txtVaccination);
        textVaccination = findViewById(R.id.textVaccination);
        txtDescription = findViewById(R.id.txtDescription);
        txtPetName = findViewById(R.id.txtPetName);
    }

    public void addVaccination(View view){
        String vaccination = vaccinationName.getText().toString().trim();
        if (!vaccination.isEmpty()){
            vaccinationSchedule.add(new Vaccination(vaccination, Functions.getDateNow()));
            StringBuilder listOfVaccination = new StringBuilder();
            for (Vaccination vacc : vaccinationSchedule){
                listOfVaccination.append("** " +vacc.getName());
                listOfVaccination.append("\n");
            }
            textVaccination.setText(listOfVaccination.toString());
            vaccinationName.setText("");
        }
    }

    Uri imageUri;
    public void openGallery(View view){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, Constans.PICK_IMAGE);
    }

    boolean changePhoto = false;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constans.PICK_IMAGE) {
            imageUri = data.getData();
            imgPet.setImageURI(imageUri);
            changePhoto = true;
        }
    }

    private void uploadImage(String uid){
        imgPet.setDrawingCacheEnabled(true);
        imgPet.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imgPet.getDrawable()).getBitmap();
        PetService.updatePetProfileImage(uid, bitmap);
    }

    public void createPet(View view){
        petToUpload = new Pet();
        petToUpload.setVaccinationSchedule(vaccinationSchedule);
        petToUpload.setAvailable(Constans.PETS_AVAILABLE);
        petToUpload.setBirthDate(Functions.getDateNow());
        petToUpload.setDescription(txtDescription.getText().toString());
        petToUpload.setName(txtPetName.getText().toString());
        petToUpload.setOwner(foundation.getName());
        petToUpload.setPetRequests(new ArrayList<PetRequest>());
        petToUpload.setPhotos(new ArrayList<String>());
        petToUpload.setUidFoundation(foundation.getUid());

        PetService.setAddPet(this);
        PetService.setPetImageUpload(this);
        PetService.setShowMessage(this);
        PetService.addPet(petToUpload);
    }

    @Override
    public void addPet(Pet pet) {
        if (pet != null){
            if (changePhoto) {
                uploadImage(pet.getUid());
            }else{
                Toast.makeText(this, "Se agregó el Pet correctamente", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Ocurrió un error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void petImageUpload(boolean photoUpload) {
        if (photoUpload) {
            Toast.makeText(this, "Se agregó el Pet correctamente", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Ocurrió un error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}