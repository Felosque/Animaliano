package com.artificialbyte.animaliano;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.artificialbyte.animaliano.dto.user.User;
import com.artificialbyte.animaliano.fundation.FoundationMenuActivity;
import com.artificialbyte.animaliano.services.epayco.EpayService;
import com.artificialbyte.animaliano.services.epayco.Transaction;
import com.artificialbyte.animaliano.utils.Constans;
import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import co.epayco.android.Epayco;
import co.epayco.android.models.Authentication;
import co.epayco.android.models.Card;
import co.epayco.android.models.Charge;
import co.epayco.android.models.Client;
import co.epayco.android.util.EpaycoCallback;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView mail;
    private TextView message;

    private LinearLayout fragProfile;

    Button btnAdminFoundation;

    private EditText txtRol;
    private EditText txtNit;
    private EditText txtPhone;
    private EditText txtDesc;

    private String email;
    private String provider;
    private User user;
    private CircleImageView userImage;
    SharedPreferences pref;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    String idToken = "";
    @Override
    public void onResume() {
        super.onResume();
        //Toast.makeText(getActivity(), "Me resumí", Toast.LENGTH_SHORT).show();
        //EpayService epa = new EpayService(Transaction.getNotFoundsCard());
        EpayService epa2 = new EpayService(Transaction.getPendingCard());
        //EpayService epa3 = new EpayService(Transaction.getAcceptedCard());
        //EpayService epa4 = new EpayService(Transaction.getFailedCard());
        updateUserInformation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Bundle bundle = getActivity().getIntent().getExtras();
        email = bundle.getString("email");
        provider = bundle.getString("provider");

        btnAdminFoundation = view.findViewById(R.id.btnAdmin);
        userImage = view.findViewById(R.id.img_profile);
        mail = view.findViewById(R.id.txtCorreo);
        message = view.findViewById(R.id.txtNombre);
        txtNit = view.findViewById(R.id.txtNit_Profile);
        txtPhone = view.findViewById(R.id.txtPhone_Profile);
        txtRol = view.findViewById(R.id.txtRol_Profile);
        txtDesc = view.findViewById(R.id.txtDesc_Profile);
        fragProfile = view.findViewById(R.id.frag_content_profile);
        updateUserInformation();
        return view;
    }

    public void setUser(User pUser){
        this.user = pUser;
    }

    public void updateUserInformation(){

        if (user != null) {
            this.mail.setText(user.getEmail());
            this.message.setText(user.getName());
            this.txtRol.setText(user.getRol());
            this.txtPhone.setText(user.getPhone());
            this.txtNit.setText(user.getNit());
            this.txtDesc.setText(user.getDescription());

            if (user.getRol() != null && user.getRol().equals(Constans.USER_ROL_FOUNDATION)){
                btnAdminFoundation.setVisibility(View.VISIBLE);
            }else {
                btnAdminFoundation.setVisibility(View.GONE);
            }

            if (!user.getProfilePhoto().isEmpty()) {
                Glide.with(getActivity().getApplicationContext()).load(user.getProfilePhoto()).into(userImage);
            }else {
                userImage.setImageResource(R.mipmap.logo);
            }
        }
    }

    public void btnLogOut_click(View view){
        pref.edit().clear().commit();
        pref.edit().apply();
        if (provider == ProviderType.FACEBOOK.name()){
            LoginManager.getInstance().logOut();
        }
        FirebaseAuth.getInstance().signOut();
        Intent authActivity = new Intent(getActivity(), AuthActivity.class);
        startActivity(authActivity);
        getActivity().finish();
    }

    public void btnAdminFoundation_click(View view){
        Intent intent = new Intent(getActivity(), FoundationMenuActivity.class);
        intent.putExtra("USER", user);
        startActivity(intent);
    }
}