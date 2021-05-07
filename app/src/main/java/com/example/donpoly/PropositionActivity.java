package com.example.donpoly;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.donpoly.data.model.Proposition;
import com.example.donpoly.data.tools.JSONModel;
import com.example.donpoly.data.tools.Status;
import com.example.donpoly.ui.home.HomeFragment;
import com.example.donpoly.views.adapters.PropositionAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

import static com.example.donpoly.ui.home.HomeFragment.PROP_DATA;

public class PropositionActivity extends AppCompatActivity {
    public static final int CRE_OK = 1;
    public static final int CANCEL = -1;
    public static final int MOD_OK = 2;
    private Proposition proposition;
    private int mYear, mMonth, mDay;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposition);
        Intent intent = getIntent();
        //Binding

        EditText title_zone = findViewById(R.id.title_zone);
        SeekBar status_zone = findViewById(R.id.status_bar);
        EditText des_zone = findViewById(R.id.description_zone);
        EditText date_zone = findViewById(R.id.date_zone);
        Button date_picker = findViewById(R.id.btn_date);
        EditText price_zone = findViewById(R.id.price_zone);
        FloatingActionButton cancel_button = findViewById(R.id.cancel_button);
        FloatingActionButton validate_button = findViewById(R.id.validate_button);

        if (intent.getIntExtra(PropositionAdapter.MODIFICATION, 0)==HomeFragment.PROP_MOD){
            proposition = JSONModel.deserialize(intent.getStringExtra(PROP_DATA),Proposition.class);
            this.setTitle("Modifier la proposition");
            validate_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intentProp = new Intent();
                    proposition.setTitle(title_zone.getText().toString());
                    proposition.setDescription(des_zone.getText().toString());
                    proposition.setPrice(Float.parseFloat(price_zone.getText().toString()));
                    proposition.setPostedDay(proposition.getDateTimeFromCalendar(Calendar.getInstance()));
                    // TODO : set Author
                    intentProp.putExtra(PROP_DATA, JSONModel.serialize(proposition));
                    setResult(MOD_OK,intentProp);
                    finish();
                }
            });
        }else{
            proposition = new Proposition();
            this.setTitle("Créer une proposition");
            validate_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intentProp = new Intent();
                    proposition.setTitle(title_zone.getText().toString());
                    proposition.setDescription(des_zone.getText().toString());
                    proposition.setPrice(Float.parseFloat(price_zone.getText().toString()));
                    proposition.setValidDay(date_zone.getText().toString());
                    proposition.setPostedDay(proposition.getDateTimeFromCalendar(Calendar.getInstance()));
                    // TODO : set Author
                    Log.d("champ",proposition.getDescription()+" // "+des_zone.getText().toString());
                    intentProp.putExtra(PROP_DATA, JSONModel.serialize(proposition));
                    setResult(CRE_OK,intentProp);
                    finish();
                }
            });
        }

        title_zone.setText(proposition.getTitle());
        status_zone.setProgress(0,proposition.getStatus()==Status.ACCEPTABLE);
        status_zone.setProgress(1,proposition.getStatus()==Status.GOOD);
        status_zone.setProgress(2,proposition.getStatus()==Status.VERY_GOOD);
        status_zone.setProgress(3,proposition.getStatus()==Status.NEW);
        des_zone.setText(proposition.getDescription());
        date_zone.setText(proposition.getValidDay());
        price_zone.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        price_zone.setText(String.valueOf(proposition.getPrice()));


        title_zone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                proposition.setTitle(title_zone.getText().toString());
                return false;
            }
        });

        status_zone.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    proposition.setStatus(Status.ACCEPTABLE);
                } else if (progress == 1) {
                    proposition.setStatus(Status.GOOD);
                } else if (progress == 2) {
                    proposition.setStatus(Status.VERY_GOOD);
                } else if (progress == 3) {
                    proposition.setStatus(Status.NEW);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        des_zone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                proposition.setDescription(des_zone.getText().toString());
                return false;
            }
        });

        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(PropositionActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint({"SetTextI18n", "DefaultLocale"})
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                date_zone.setText(String.format("%s/%s/%s",
                                        String.format("%02d", dayOfMonth),
                                        String.format("%02d", monthOfYear + 1),
                                        String.format("%02d", year)));
                                proposition.setValidDay(date_zone.getText().toString());
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        price_zone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                proposition.setPrice(Float.parseFloat(price_zone.getText().toString()));
                return false;
            }
        });


        cancel_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(CANCEL,intent);
                finish();
            }
        });
    }
}
