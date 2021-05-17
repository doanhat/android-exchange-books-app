package com.example.donpoly.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.donpoly.R;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private ImageView mImageView;
    private TextView mTextView;
    private Button mBtnProposition;
    private Button mBtnCommande;
    private Button mBtnDeconnect;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        //final TextView textView = root.findViewById(R.id.text_profile);
        /*profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        mBtnProposition = root.findViewById(R.id.btn_prop_profile);
        mBtnCommande = root.findViewById(R.id.btn_commande_profile);
        mImageView = root.findViewById(R.id.iv_photo_profile);
        mTextView = root.findViewById(R.id.tv_name_profile);
        mBtnDeconnect = root.findViewById(R.id.btn_deconnect);

        mBtnProposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PropositionActivity.class);
                intent.putExtra("prop_or_comm","proposition");
                startActivity(intent);
            }
        });

        mBtnCommande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PropositionActivity.class);
                intent.putExtra("prop_or_comm","command");
                startActivity(intent);
            }
        });

        return root;
    }
}