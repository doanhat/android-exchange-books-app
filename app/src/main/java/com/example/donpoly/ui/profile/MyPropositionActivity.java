package com.example.donpoly.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.donpoly.R;
import com.example.donpoly.data.model.Proposition;
import com.example.donpoly.data.tools.JSONModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import adapter.MyRecyclerViewAdapter;

public class MyPropositionActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private List<Proposition> mList;
    private String databasePath;
    private ImageView mPhoto;
    private TextView mName;

    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_proposition);

        //initView();
        //initData();
    }


//    String str;
//    private void initView() {
//        mRecyclerView = findViewById(R.id.rv_listProp);
//        mName = findViewById(R.id.tv_name_proposition);
//        mPhoto = findViewById(R.id.iv_photo_proposition);
//        ActionBar mActionBar = getSupportActionBar();
//        mActionBar.setDisplayHomeAsUpEnabled(true);
//
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference("users").child(user.getUid());
//        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Glide.with(getApplicationContext()).load(uri).into(mPhoto);
//            }
//        });
//
//        myRecyclerViewAdapter = new MyRecyclerViewAdapter(mList = new ArrayList<>(), new MyRecyclerViewAdapter.OnItemClickListener() {
//            @Override
//            public void onClick(int pos) {
//                Intent intent = new Intent(MyPropositionActivity.this, ShowPropositionActivity.class);
//                if (str.equals("proposition")){
//                    intent.putExtra("p_c","proposition");
//                }else {
//                    intent.putExtra("p_c","command");
//                }
//                intent.putExtra(ShowPropositionActivity.SHOW, JSONModel.serialize(mList.get(pos)));
//                startActivity(intent);
//            }
//        });
//        mRecyclerView.setAdapter(myRecyclerViewAdapter);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private void initData() {
//        mName.setText(user.getDisplayName());
//        Glide.with(MyPropositionActivity.this).load(user.getPhotoUrl()).into(mPhoto);
//
//        databasePath = getString(R.string.database_path);
//        DatabaseReference mDbPropositions = FirebaseDatabase.getInstance(databasePath).getReference().child("propositions");
//
//        Intent intent = getIntent();
//        str = intent.getStringExtra("prop_or_comm");
//        if (intent != null){
//            if (str.equals("proposition")){
//                // show the list of propositions
//                this.setTitle("Mes Propositions");
//
//                mDbPropositions.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        mList.clear();
//
//                        for (DataSnapshot postSnapshot:snapshot.getChildren()) {
//                            Proposition proposition = postSnapshot.getValue(Proposition.class);
//                            // get user's propositions and add them to the list
//                            if (proposition.getAuthor().equals(user.getUid())){
//                                mList.add(proposition);
//                            }
//                        }
//                        myRecyclerViewAdapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }else if (str.equals("command")){
//                // show the list of commands
//                this.setTitle("Mes Commandes");
//
//                mDbPropositions.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        mList.clear();
//
//                        for (DataSnapshot postSnapshot:snapshot.getChildren()) {
//                            Proposition proposition = postSnapshot.getValue(Proposition.class);
//                            // get user's commands and add them to the list
//                            if (proposition.getTaker() != null && proposition.getTaker().equals(user.getUid())){
//                                mList.add(proposition);
//                            }
//                        }
//                        myRecyclerViewAdapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        }
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                this.finish();
//                return false;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}
