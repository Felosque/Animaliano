package com.artificialbyte.animaliano;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.artificialbyte.animaliano.adapters.FoundationAdapter;
import com.artificialbyte.animaliano.dto.user.User;
import com.artificialbyte.animaliano.services.epayco.EpayService;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DonationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DonationFragment extends Fragment {

    private EditText searchFoundation;
    private ArrayList<User> foundationList;
    private RecyclerView recyclerFoundations;

    public DonationFragment() {

    }


    public static DonationFragment newInstance(String param1, String param2) {
        DonationFragment fragment = new DonationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    final GestureDetector mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donation, container, false);
        foundationList = new ArrayList<>();
        refreshList();
        recyclerFoundations = view.findViewById(R.id.idRecyclerView);
        recyclerFoundations.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerFoundations.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                try {
                    View child = rv.findChildViewUnder(e.getX(), e.getY());

                    if (child != null && mGestureDetector.onTouchEvent(e)) {

                        int position = rv.getChildAdapterPosition(child);

                        String nam = foundationList.get(position).toString();
                        Toast.makeText(getContext(),""+ nam ,Toast.LENGTH_SHORT).show();

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


        searchFoundation = view.findViewById(R.id.searchFoundation);

        FoundationAdapter adapter = new FoundationAdapter(foundationList);
        recyclerFoundations.setAdapter(adapter);
        return view;
    }

    private void refreshList(){
        for (int i = 0; i < 5000; i++){
            User u = new User();
            u.setName("Nombre " + i);
            u.setDescription("Esto es una des " + i);
            foundationList.add(u);
            System.out.println(u.toString());
        }
    }


}