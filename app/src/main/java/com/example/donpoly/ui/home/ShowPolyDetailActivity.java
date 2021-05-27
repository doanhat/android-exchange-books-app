package com.example.donpoly.ui.home;

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

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.donpoly.R;
import com.example.donpoly.data.model.Proposition;
import com.example.donpoly.data.tools.JSONModel;
import com.example.donpoly.ui.messages.MessagesFragment;
import com.example.donpoly.ui.profile.ShowPropositionActivity;

public class ShowPolyDetailActivity extends AppCompatActivity {

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
        description.setText(proposition.getDescription());
        price.setText(String.valueOf(proposition.getPrice()));

        // get the picture of the poly
        Glide.with(getApplicationContext()).load(proposition.getImageUrl()).into(picture);

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ShowPolyDetailActivity.this, MessagesFragment.class);
                intent1.putExtra("visitUserId",proposition.getAuthor());
                startActivity(intent1);
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}