package com.example.donpoly.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.donpoly.R;
import com.example.donpoly.data.model.Proposition;
import com.example.donpoly.data.tools.JSONModel;
import com.example.donpoly.ui.login.LoginActivity;
import com.example.donpoly.ui.messages.MessageActivity;
import com.example.donpoly.ui.profile.ShowPropositionActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ShowPolyDetailActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_poly_detail);

        // set the window
        Display display = getWindowManager().getDefaultDisplay();
        Window window = this.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = (int) (display.getWidth() * 0.8);
        layoutParams.height = (int) (display.getHeight() * 0.45);
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);


        final TextView name = findViewById(R.id.tv_name_detail_poly);
        final ImageView picture = findViewById(R.id.iv_photo_detail_poly);
        final TextView status = findViewById(R.id.tv_condition_detail_poly);
        final TextView description = findViewById(R.id.tv_note_detail_poly);
        final TextView price = findViewById(R.id.tv_price_detail_poly);
        final Button chat = findViewById(R.id.btn_chat_detail_poly);
        final Button buy = findViewById(R.id.btn_buy_detail_poly);


        // get the information of the poly
        Intent intent = getIntent();
        Proposition proposition = JSONModel.deserialize(intent.getStringExtra(ShowPropositionActivity.SHOW), Proposition.class);
        name.setText(proposition.getTitle());
        switch (proposition.getStatus()){
            case NEW:
                status.setText(R.string.status_3);
                break;
            case VERY_GOOD:
                status.setText(R.string.status_2);
                break;
            case GOOD:
                status.setText(R.string.status_1);
                break;
            case ACCEPTABLE:
                status.setText(R.string.status_0);
                break;
        }
        description.setText(proposition.getDescription().toUpperCase());
        price.setText(String.valueOf(proposition.getPrice()));
        // get the picture of the poly
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Images").child(proposition.getId());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).into(picture);
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && proposition.getAuthor().equals(user.getUid())){
            chat.setEnabled(false);
            buy.setEnabled(false);
            chat.setTextColor(R.color.gray);
            buy.setBackground(getDrawable(R.color.gray));
        }

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null){
                    Toast.makeText(ShowPolyDetailActivity.this,"Vous devez vous connecter.",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ShowPolyDetailActivity.this, LoginActivity.class));
                }else {
                    Intent intent1 = new Intent(ShowPolyDetailActivity.this, MessageActivity.class);
                    intent1.putExtra("visitUserId",proposition.getAuthor());
                    startActivity(intent1);
                }
                finish();
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null){
                    Toast.makeText(ShowPolyDetailActivity.this,"Vous devez vous connecter.",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ShowPolyDetailActivity.this, LoginActivity.class));
                }else {
                    proposition.setTaker(user.getUid());
                    FirebaseDatabase.getInstance(getString(R.string.database_path)).getReference()
                            .child("propositions").child(proposition.getId())
                            .setValue(proposition).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ShowPolyDetailActivity.this,"Commande ajoutée avec succès ",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ShowPolyDetailActivity.this,"Echec lors de l'achète de cette polycopié \n",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                finish();

            }
        });
    }

}
