package com.artificialbyte.animaliano.fundation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.artificialbyte.animaliano.PetViewActivity;
import com.artificialbyte.animaliano.R;
import com.artificialbyte.animaliano.adapters.AdoptionAdapter;
import com.artificialbyte.animaliano.adapters.DeletePetAdapter;
import com.artificialbyte.animaliano.dto.pet.Pet;
import com.artificialbyte.animaliano.dto.user.User;
import com.artificialbyte.animaliano.interfaces.pet.DeletePet;
import com.artificialbyte.animaliano.interfaces.pet.GetPetsFromParam;
import com.artificialbyte.animaliano.services.pet.PetService;
import com.artificialbyte.animaliano.utils.Constans;
import com.droidbyme.dialoglib.DroidDialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class DeletePetActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, GetPetsFromParam, DeletePet {

    private SearchView searchFoundation;
    private ArrayList<Pet> foundationList;
    private RecyclerView recyclerFoundations;
    private DeletePetAdapter adapter;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_pet);

        searchFoundation = findViewById(R.id.searchPet);
        recyclerFoundations = findViewById(R.id.recyclerPets);

        final GestureDetector mGestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        user = (User) getIntent().getSerializableExtra("user");
        foundationList = new ArrayList<>();
        PetService.setGetPetsFromParam(this);
        PetService.setDeletePet(this);
        PetService.getPetsBy(user.getUid(), "uidFoundation");
        recyclerFoundations.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));

        recyclerFoundations.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                try {
                    View child = rv.findChildViewUnder(e.getX(), e.getY());

                    if (child != null && mGestureDetector.onTouchEvent(e)) {

                        int position = rv.getChildAdapterPosition(child);

                        positionDelete = position;
                        Pet pet = foundationList.get(position);
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Esta es la información de la mascota: " + "\n\n");
                        stringBuilder.append("ID:" + pet.getUid() + "\n");
                        stringBuilder.append("NOMBRE:" + pet.getName() + "\n");
                        stringBuilder.append("DESCRIPCIÓN: " + pet.getDescription() + "\n");
                        stringBuilder.append("FECHA: " + pet.getBirthDate() + "\n");
                        stringBuilder.append("PETICIONES: " + pet.getPetRequests().size() + "\n");
                        stringBuilder.append("\n¿Seguro que quieres eliminarla?\n\n");

                        String nam = foundationList.get(position).toString();
                        Toast.makeText(getApplicationContext(),""+ nam ,Toast.LENGTH_SHORT).show();
                        new DroidDialog.Builder(getWindow().getContext())
                                .icon(R.mipmap.logo)
                                .color(ContextCompat.getColor(getWindow().getContext(), R.color.red),
                                        ContextCompat.getColor(getWindow().getContext(), R.color.white),
                                        ContextCompat.getColor(getWindow().getContext(), R.color.text))
                                .animation(2)
                                .title("ELIMINAR MASCOTA")
                                .content(stringBuilder.toString())
                                .cancelable(true, false)
                                .positiveButton("ELIMINAR", new DroidDialog.onPositiveListener() {
                                    @Override
                                    public void onPositive(Dialog dialogPositive) {
                                        PetService.deletePet(pet);
                                        dialog = dialogPositive;
                                    }
                                })
                                .negativeButton("CONSERVAR", new DroidDialog.onNegativeListener() {
                                    @Override
                                    public void onNegative(Dialog droidDialog) {
                                        Toast.makeText(getApplicationContext(), "NO", Toast.LENGTH_SHORT).show();
                                        droidDialog.hide();
                                    }
                                })
                                .show();


                        return true;
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        searchFoundation.setOnQueryTextListener(this);
    }

    private void refreshList(){
        adapter = new DeletePetAdapter(foundationList);
        recyclerFoundations.setAdapter(adapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return false;
    }

    @Override
    public void getPetsFromParam(ArrayList<Pet> pets) {
        foundationList = pets;
        refreshList();
    }

    int positionDelete = -1;
    Dialog dialog;
    @Override
    public void deletePet(boolean response) {
        //En una función para eliminar desde base de datos
        if (positionDelete != -1) {
            foundationList.remove(positionDelete);
            refreshList();
            dialog.hide();
        }
    }
}