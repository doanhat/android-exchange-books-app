package com.example.donpoly.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.donpoly.MainActivity;
import com.example.donpoly.R;
import com.example.donpoly.data.LoginDataSource;
import com.example.donpoly.data.LoginRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final Button mBtnProposition = root.findViewById(R.id.btn_prop_profile);
        final Button mBtnCommand = root.findViewById(R.id.btn_commande_profile);
        final ImageView mImageView = root.findViewById(R.id.iv_photo_profile);
        final TextView mTextView = root.findViewById(R.id.tv_name_profile);
        mTextView.setText(user.getDisplayName());
        final Button mBtnLogout = root.findViewById(R.id.btn_logout);
        // set the image of user
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("users").child(user.getUid());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext()).load(uri).into(mImageView);
            }
        });

//        mBtnProposition.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), MyPropositionActivity.class);
//                intent.putExtra("prop_or_comm","proposition");
//                startActivity(intent);
//            }
//        });
//
//        mBtnCommand.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), MyPropositionActivity.class);
//                intent.putExtra("prop_or_comm","command");
//                startActivity(intent);
//            }
//        });
//
//        mBtnLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LoginRepository.getInstance(new LoginDataSource()).logout();
//                startActivity(new Intent(getActivity(), MainActivity.class));
//            }
//        });

        return root;
    }
}
