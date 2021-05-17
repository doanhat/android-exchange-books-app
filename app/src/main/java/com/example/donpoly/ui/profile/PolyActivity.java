package com.example.donpoly.ui.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.donpoly.R;

public class PolyActivity extends AppCompatActivity {
    private Button btnEdit;
    private Button btnDelete;
    private TextView tvNamePoly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poly);

        initData();

        btnEdit = findViewById(R.id.btn_edit_poly);
        btnDelete = findViewById(R.id.btn_delete_poly);
        tvNamePoly = findViewById(R.id.tv_name_poly);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText condition = findViewById(R.id.et_condition_poly);
                EditText note = findViewById(R.id.et_note_poly);
                EditText price = findViewById(R.id.et_price_poly);

                if (btnEdit.getText().equals("Edit")){
                    condition.setEnabled(true);
                    note.setEnabled(true);
                    price.setEnabled(true);
                    btnEdit.setText("OK");
                }else {
                    btnEdit.setText("Edit");
                    condition.setEnabled(false);
                    note.setEnabled(false);
                    price.setEnabled(false);
                }

            }
        });
    }

    private void initData() {
    }


    public void delete(View view) {
        Intent intent = new Intent(PolyActivity.this, PropositionActivity.class);
        intent.putExtra("namePoly",tvNamePoly.toString());
        startActivity(intent);
    }
}