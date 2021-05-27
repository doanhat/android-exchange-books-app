package com.example.donpoly.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.donpoly.R;
import com.example.donpoly.data.model.Proposition;
import com.example.donpoly.data.tools.JSONModel;
import com.example.donpoly.ui.home.AddPropositionActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

public class ShowPropositionActivity extends AppCompatActivity {
    private Button btnEdit;
    private Button btnDelete;
    private ImageView ivPhoto;
    private TextView tvNamePoly;
    private TextView tvStatus;
    private TextView tvDescription;
    private TextView tvPrice;
    private Proposition proposition;
    public static final String SHOW = "show";
    public static final String MODIFICATION = "modification";
    public static final String DELETE = "delete";
    public static final int PROP_MOD = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_proposition);

        // set the window
        Display display = getWindowManager().getDefaultDisplay();
        Window window = this.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = (int) (display.getWidth() * 0.8);
        layoutParams.height = (int) (display.getHeight() * 0.45);
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);

        initView();
        initData();
    }


    private void initData() {
        Intent intent = getIntent();
        proposition = JSONModel.deserialize(intent.getStringExtra(SHOW), Proposition.class);
        updateData();
    }

    public void updateData(){
        //        ivPhoto.setImageURI(Uri.parse(proposition.getImageUrl()));
        tvNamePoly.setText(proposition.getTitle());
        switch (proposition.getStatus()){
            case ACCEPTABLE:
                tvStatus.setText(R.string.status_0);
                break;
            case GOOD:
                tvStatus.setText(R.string.status_1);
                break;
            case VERY_GOOD:
                tvStatus.setText(R.string.status_2);
                break;
            case NEW:
                tvStatus.setText(R.string.status_3);
                break;
        }
        tvDescription.setText(proposition.getDescription());
        tvPrice.setText(String.valueOf(proposition.getPrice()));
    }


    private void initView() {
        btnEdit = findViewById(R.id.btn_edit_poly);
        btnDelete = findViewById(R.id.btn_delete_poly);
        ivPhoto = findViewById(R.id.iv_photo_poly);
        tvNamePoly = findViewById(R.id.tv_name_poly);
        tvStatus = findViewById(R.id.tv_condition_poly);
        tvDescription = findViewById(R.id.tv_note_poly);
        tvPrice = findViewById(R.id.tv_price_poly);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowPropositionActivity.this, AddPropositionActivity.class);
                intent.putExtra("add_or_edit",MODIFICATION);
                intent.putExtra(MODIFICATION, JSONModel.serialize(proposition));
                startActivityForResult(intent,PROP_MOD);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowPropositionActivity.this, MyPropositionActivity.class);
                intent.putExtra(DELETE,proposition.getId());
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PROP_MOD) {
            if (resultCode == AddPropositionActivity.MOD_OK){
                proposition = JSONModel.deserialize(data.getStringExtra(MODIFICATION),Proposition.class);
                if (proposition != null){
                    FirebaseDatabase.getInstance(getString(R.string.database_path)).getReference()
                            .child("propositions").child(proposition.getId())
                            .setValue(proposition).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // after the data addition is successful
                            // we are displaying a success toast message.
                            updateData();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // this method is called when the data addition process is failed.
                            // displaying a toast message when data addition is failed.
                            Toast.makeText(ShowPropositionActivity.this, "Echec lors de la modofication de la proposition \n" + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
            if (resultCode == AddPropositionActivity.CANCEL){
                Toast.makeText(ShowPropositionActivity.this,"Opération annulée", Toast.LENGTH_SHORT).show();
            }
        }

    }

}