package com.example.donpoly;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.donpoly.data.model.Proposition;
import com.example.donpoly.data.tools.JSONModel;
import com.example.donpoly.data.tools.Status;
import com.example.donpoly.ui.home.HomeFragment;
import com.example.donpoly.views.PropositionAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

import static com.example.donpoly.ui.home.HomeFragment.PROP_DATA;

public class PropositionActivity extends AppCompatActivity {
    public static final int CRE_OK = 1;
    public static final int CANCEL = -1;
    public static final int MOD_OK = 2;
    private Proposition proposition;
    private int mYear, mMonth, mDay;
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
        }else{
            proposition = new Proposition();
            this.setTitle("Créer une proposition");
            validate_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent();
                    proposition.setTitle(title_zone.getText().toString());
                    proposition.setValidDay(date_zone.getText().toString());
                    proposition.setPostedDay(proposition.getDateTimeFromCalendar(Calendar.getInstance()));
                    // TODO : set Author
                    intent.putExtra(PROP_DATA, JSONModel.serialize(proposition));
                    setResult(CRE_OK,intent);
                    finish();
                }
            });
        }

        title_zone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                proposition.setTitle(title_zone.getText().toString());
                return false;
            }
        });
        des_zone.setText(proposition.getDescription());
//        status_zone.setMax(3);
//        status_zone.incrementProgressBy(1);
        date_zone.setText(proposition.getValidDay());
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
        price_zone.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        price_zone.setText(String.valueOf(proposition.getPrice()));
        cancel_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(CANCEL,intent);
                finish();
            }
        });


    }
}
