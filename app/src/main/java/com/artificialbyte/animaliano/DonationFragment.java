package com.artificialbyte.animaliano;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.Formatter;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.artificialbyte.animaliano.adapters.FoundationAdapter;
import com.artificialbyte.animaliano.dto.user.User;
import com.artificialbyte.animaliano.interfaces.user.GetUsersFromParam;
import com.artificialbyte.animaliano.services.epayco.EpayService;
import com.artificialbyte.animaliano.services.user.UserService;
import com.artificialbyte.animaliano.utils.Constans;
import com.artificialbyte.animaliano.utils.Functions;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DonationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DonationFragment extends Fragment implements SearchView.OnQueryTextListener, GetUsersFromParam {

    private SearchView searchFoundation;
    private ArrayList<User> foundationList;
    private RecyclerView recyclerFoundations;
    private FoundationAdapter adapter;
    private User user;

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
        UserService.setGetUsersFromParam(this);
        UserService.getUsersBy(Constans.USER_ROL_FOUNDATION, "rol");
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


                        Intent paymentActivity = new Intent(getActivity(), PaymentActivity.class);
                        paymentActivity.putExtra("Foundation", foundationList.get(position));
                        paymentActivity.putExtra("User", user);
                        startActivity(paymentActivity);

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
        searchFoundation.setOnQueryTextListener(this);
        return view;
    }

    private void refreshList(){
        adapter = new FoundationAdapter(foundationList);
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
    public void getUsersFromParam(ArrayList<User> users) {
        foundationList = users;
        refreshList();
    }

    public void setUser(User pUser){
        this.user = pUser;
    }
}