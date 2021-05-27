package com.example.donpoly.ui.profile;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.donpoly.R;
import com.example.donpoly.data.model.Proposition;
import com.example.donpoly.data.tools.JSONModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import adapter.MyRecyclerViewAdapter;

public class MyPropositionActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private List<Proposition> mList;
    private DatabaseReference mDbPropositions;
    private String databasePath;
    private ImageView mPhoto;
    private TextView mName;
    private ActionBar mActionBar;

    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_proposition);

        initView();
        initData();
    }


    private void initView() {
        mRecyclerView = findViewById(R.id.rv_listProp);
        mName = findViewById(R.id.tv_name_proposition);
        mPhoto = findViewById(R.id.iv_photo_proposition);
        mActionBar = getActionBar();
        //mActionBar.setDisplayHomeAsUpEnabled(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(mList = new ArrayList<>(), new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                Intent intent = new Intent(MyPropositionActivity.this, ShowPropositionActivity.class);
                intent.putExtra(ShowPropositionActivity.SHOW, JSONModel.serialize(mList.get(pos)));
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(myRecyclerViewAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initData() {
        mName.setText(user.getDisplayName());
        Glide.with(MyPropositionActivity.this).load(user.getPhotoUrl()).into(mPhoto);

        databasePath = getString(R.string.database_path);
        Intent intent = getIntent();

        if (intent != null){
            String str = intent.getStringExtra("prop_or_comm");
            if (str.equals("proposition")){
                // show the list of propositions
                this.setTitle("Mes Propositions");

                mDbPropositions = FirebaseDatabase.getInstance(databasePath).getReference().child("propositions");
                mDbPropositions.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mList.clear();

                        for (DataSnapshot postSnapshot:snapshot.getChildren()) {
                            Proposition proposition = postSnapshot.getValue(Proposition.class);
                            // get user's propositions and add them to the list
                            if (proposition.getAuthor().equals(user.getUid())){
                                mList.add(proposition);
                            }
                        }
                        myRecyclerViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }else if (str.equals("command")){
                // show the list of commands
                this.setTitle("Mes Commandes");
            }else{
                String polyId = getIntent().getStringExtra(ShowPropositionActivity.DELETE);
                mDbPropositions.child(polyId).removeValue();
                // delete the item from the proposition list
                IntStream.range(0,mList.size()).filter(i->mList.get(i).getId().equals(polyId))
                        .boxed().findFirst().map(i->mList.remove((int)i));
                myRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }
}