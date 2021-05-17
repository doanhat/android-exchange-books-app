package com.example.donpoly.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donpoly.R;
import com.example.donpoly.data.model.Proposition;
import com.example.donpoly.adapter.PropositionAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private HomeViewModel homeViewModel;
    ArrayList<Proposition> propositions = new ArrayList<>();
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDbPropositions;

    public HomeFragment(){
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView rvProps = (RecyclerView) root.findViewById(R.id.proposition_rv);
        // Create adapter passing in the sample user data
        PropositionAdapter adapter = new PropositionAdapter(propositions);
        adapter.notifyDataSetChanged();
        // Attach the adapter to the recyclerview to populate items
        rvProps.setAdapter(adapter);
        // Set layout manager to position the items
        rvProps.setLayoutManager(new LinearLayoutManager(getContext()));
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://nf28-donpoly-default-rtdb.europe-west1.firebasedatabase.app/");
        mFirebaseDatabase.setLogLevel(Logger.Level.DEBUG);
        mDbPropositions = mFirebaseDatabase.getReference().child("propositions");
        mDbPropositions.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Proposition prop = postSnapshot.getValue(Proposition.class);
                    assert prop != null;
                    propositions.add(0,prop);
                    adapter.notifyDataSetChanged();
                    Log.e("Get Data", prop.getId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });

        FloatingActionButton button = root.findViewById(R.id.reload_item_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Opération affectée", Toast.LENGTH_LONG).show();
                mDbPropositions = mFirebaseDatabase.getReference().child("propositions");
                /*for (Proposition p : propositions) {
                    mDbPropositions.child(p.getId()).setValue(p);
                }*/
                mDbPropositions.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                        }
                    }
                });
            }
        });
        return root;
    }

}