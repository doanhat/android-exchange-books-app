package com.example.donpoly.ui.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donpoly.MainActivity;
import com.example.donpoly.PropositionActivity;
import com.example.donpoly.R;
import com.example.donpoly.data.model.Proposition;
import com.example.donpoly.data.tools.FirebaseController;
import com.example.donpoly.data.tools.JSONModel;
import com.example.donpoly.views.PropositionAdapter;
import com.example.donpoly.views.PropositionItemAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
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
import com.google.firebase.database.Logger;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.annotation.Documented;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private static final int PROP_CRE = 1;
    public static final String PROP_DATA = "data";
    public static final int PROP_MOD = 2;
    private static final String PROP_ID = "id";
    private HomeViewModel homeViewModel;
    ArrayList<Proposition> propositions = new ArrayList<>();
    private FirebaseController firebaseController;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDbPropositions;
    private PropositionAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        firebaseController = new FirebaseController("propositions");
        mDbPropositions = firebaseController.getReferences().get("propositions");
        assert mDbPropositions != null;
        // Get DB instance
        RecyclerView rvProps = (RecyclerView) root.findViewById(R.id.proposition_rv);
        // Create adapter passing in the sample user data
        adapter = new PropositionAdapter(propositions);
        //adapter.notifyDataSetChanged();
        // Attach the adapter to the recyclerview to populate items
        rvProps.setAdapter(adapter);
        // Set layout manager to position the items
        rvProps.setLayoutManager(new LinearLayoutManager(getContext()));


        mDbPropositions.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                propositions.clear();

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Proposition prop = postSnapshot.getValue(Proposition.class);
                    assert prop != null;
                    propositions.add(0,prop);
                    Log.e("Get Data", prop.getId());
                }
                Collections.sort(propositions);
                adapter.notifyDataSetChanged();
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
                Collections.sort(propositions);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Collections.sort(propositions);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Collections.sort(propositions);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        FloatingActionButton reloadButton = root.findViewById(R.id.reload_item_button);
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Opération affectée", Toast.LENGTH_LONG).show();
                mDbPropositions = firebaseController.getReferences().get("propositions");
                assert mDbPropositions != null;
                mDbPropositions.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                            Log.d("propsition",propositions.toString());
                        }
                    }
                });
            }
        });

        FloatingActionButton addButton = root.findViewById(R.id.add_item_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),
                        PropositionActivity.class);
                //intent.putExtra("number_activities",activities.size());

                startActivityForResult(intent, PROP_CRE);
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PROP_CRE) {
            if (resultCode == PropositionActivity.CRE_OK){
                Proposition proposition = JSONModel.deserialize(data.getStringExtra(PROP_DATA),Proposition.class);
                if (proposition != null){
                    /*propositions.add(proposition);
                    adapter.notifyDataSetChanged();*/
                    Log.d("postedDay",proposition.getPostedDay());
                    mDbPropositions.child(proposition.getId()).setValue(proposition).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // after the data addition is successful
                            // we are displaying a success toast message.
                            Toast.makeText(getContext(), "Proposition ajoutée avec succès " + proposition.getId(), Toast.LENGTH_SHORT).show();
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
            if (resultCode == PropositionActivity.CANCEL){
                Toast.makeText(getContext(),"Opération annulée", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == PROP_MOD) {
            if (resultCode == PropositionActivity.MOD_OK){
                Proposition proposition = JSONModel.deserialize(data.getStringExtra(PROP_DATA),Proposition.class);
                if (proposition != null){
                    String idProp = data.getStringExtra(PROP_ID);
                    mDbPropositions.child(idProp).setValue(proposition);
                    adapter.notifyDataSetChanged();
                }
            }
            if (resultCode == PropositionActivity.CANCEL){
                Toast.makeText(getContext(),"Opération annulée", Toast.LENGTH_LONG).show();
            }
        }
    }

}