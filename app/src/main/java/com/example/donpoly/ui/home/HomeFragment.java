package com.example.donpoly.ui.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donpoly.R;
import com.example.donpoly.data.LoginDataSource;
import com.example.donpoly.data.LoginRepository;
import com.example.donpoly.data.model.Proposition;
import com.example.donpoly.data.tools.JSONModel;
import com.example.donpoly.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import adapter.PropositionAdapter;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private static final int PROP_CRE = 1;
    public static final String CREATE = "create";
    private HomeViewModel homeViewModel;
    private List<Proposition> propositionList;
    private Proposition proposition;
    private DatabaseReference mDbPropositions;

    public HomeFragment(){
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final RecyclerView mRecyclerView = root.findViewById(R.id.proposition_rv);
        final SearchView mSearchView = root.findViewById(R.id.search_sv);
        final FloatingActionButton mBtnAdd = root.findViewById(R.id.add_item_button);
        final FloatingActionButton mBtnReload = root.findViewById(R.id.reload_item_button);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        final PropositionAdapter propositionAdapter = new PropositionAdapter(propositionList = new ArrayList<>());
        mRecyclerView.setAdapter(propositionAdapter);

        // get propositions from database
        final String database_path = getString(R.string.database_path);
        mDbPropositions = FirebaseDatabase.getInstance(database_path).getReference().child("propositions");
        mDbPropositions.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                propositionList.clear();

                for (DataSnapshot postSnapshot:snapshot.getChildren()) {
                    Proposition proposition = postSnapshot.getValue(Proposition.class);
                    assert proposition != null;

                    boolean flag = true;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        Date date = simpleDateFormat.parse(proposition.getValidDay());
                        if (date.before(Calendar.getInstance().getTime())){
                            // already past the valid day
                            flag = false;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (proposition.getTaker() == null && flag){
                        propositionList.add(0,proposition);
                        Log.e("Get Date", proposition.getId());
                    }

                }
                propositionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });

        mDbPropositions.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                /*Proposition prop = snapshot.getValue(Proposition.class);
                propositions.add(0,prop);*/
                Collections.sort(propositionList);
                propositionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Collections.sort(propositionList);
                propositionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Collections.sort(propositionList);
                propositionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Collections.sort(propositionList);
                propositionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // search poly with the name given
        propositionAdapter.appendList(propositionList);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                    propositionAdapter.getFilter().filter(newText);
                return false;
            }
        });


        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!LoginRepository.getInstance(new LoginDataSource()).isLoggedIn()){
                    Toast.makeText(getContext(),"Vous devez se connecter.",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    return;
                }
                Intent intent = new Intent(getContext(), AddPropositionActivity.class);
                intent.putExtra("add_or_edit", CREATE);
                startActivityForResult(intent,PROP_CRE);
            }
        });


        mBtnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Opération affectée", Toast.LENGTH_SHORT).show();
                mDbPropositions = FirebaseDatabase.getInstance(database_path).getReference().child("propositions");
                assert mDbPropositions != null;
                mDbPropositions.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                            Log.d("propsition",propositionList.toString());
                        }
                    }
                });

            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PROP_CRE) {
            if (resultCode == AddPropositionActivity.CRE_OK){
                proposition = JSONModel.deserialize(data.getStringExtra(CREATE),Proposition.class);
                if (proposition != null){
                    Log.d("postedDay",proposition.getPostedDay());
                    mDbPropositions.child(proposition.getId()).setValue(proposition).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // after the data addition is successful
                            // we are displaying a success toast message.
                            Toast.makeText(getContext(), "Proposition ajoutée avec succès " + proposition.getTitle(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // this method is called when the data addition process is failed.
                            // displaying a toast message when data addition is failed.
                            Toast.makeText(getContext(), "Echec lors de l'ajoute de la proposition \n" + e, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
            if (resultCode == AddPropositionActivity.CANCEL){
                Toast.makeText(getContext(),"Opération annulée", Toast.LENGTH_LONG).show();
            }
        }

    }
}