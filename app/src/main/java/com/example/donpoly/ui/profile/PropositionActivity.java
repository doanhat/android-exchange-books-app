package com.example.donpoly.ui.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.donpoly.R;
import com.example.donpoly.data.model.Proposition;
import com.example.donpoly.data.tools.Status;

import java.util.ArrayList;
import java.util.List;

public class PropositionActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private List<Proposition> propositionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposition);
        
        initData();
        initView();
    }


    private void initView() {
        mRecyclerView = findViewById(R.id.rv_listProp);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(propositionList);
        mRecyclerView.setAdapter(myRecyclerViewAdapter);
    }

    private void initData() {
        Intent intent = getIntent();
        propositionList = new ArrayList<>();

        if (intent != null){
            if (intent.getStringExtra("prop_or_comm").equals("proposition")){
                propositionList.add(new Proposition("NF28", Status.ACCEPTABLE, "un poly de nf28", "2021-5-3", "2021-5-5", 3.2f, false, null, null));
                propositionList.add(new Proposition("NF18", Status.ACCEPTABLE, "un poly de nf18", "2021-5-13", "2021-5-15", 3.8f, false, null, null));
            }else {
                propositionList.add(new Proposition("SY02",Status.ACCEPTABLE, "un poly de sy02", "2021-5-23", "2021-5-25", 4.2f, false, null, null));
            }
        }
    }

    public void showPoly(View view){
        Intent intent = new Intent(PropositionActivity.this, PolyActivity.class);
        startActivity(intent);
    }
}