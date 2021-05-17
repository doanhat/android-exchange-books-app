package com.example.donpoly.ui.profile;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.donpoly.R;
import com.example.donpoly.data.model.Proposition;
import com.example.donpoly.data.tools.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void listen() {
        while (true){
            Intent intent = getIntent();
            String name = intent.getStringExtra("namePoly");
            //remove the item from list
            IntStream.range(0,propositionList.size()).filter(i->propositionList.get(i).getTitle().equals(name)).boxed().findFirst().map(i->propositionList.remove((int)i));
            myRecyclerViewAdapter.notifyDataSetChanged();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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