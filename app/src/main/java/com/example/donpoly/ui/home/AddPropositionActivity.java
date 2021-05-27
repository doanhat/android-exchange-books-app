package com.example.donpoly.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.donpoly.R;
import com.example.donpoly.data.model.Proposition;
import com.example.donpoly.data.tools.JSONModel;
import com.example.donpoly.data.tools.Status;
import com.example.donpoly.ui.profile.ShowPropositionActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

import static com.example.donpoly.ui.profile.ShowPropositionActivity.MODIFICATION;

public class AddPropositionActivity extends AppCompatActivity {
    public static final int CRE_OK = 1;
    public static final int CANCEL = -1;
    public static final int MOD_OK = 2;
    private Proposition proposition;
    private int mYear, mMonth, mDay;
    private ImageView mImageView;
    StorageReference storageReference;

    // 1 - Uri of image selected by user
    private Uri uriImageSelected;

    // 1 - STATIC DATA FOR PICTURE
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int RC_IMAGE_PERMS = 100;
    private static final int RC_CHOOSE_PHOTO = 200;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_proposition);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            //TODO:no user signed
        }
        String userId = user.getUid();

        //Binding
        final EditText title_zone = findViewById(R.id.title_zone);
        final SeekBar status_zone = findViewById(R.id.status_bar);
        final EditText des_zone = findViewById(R.id.description_zone);
        final EditText date_zone = findViewById(R.id.date_zone);
        final Button date_picker = findViewById(R.id.btn_date);
        final EditText price_zone = findViewById(R.id.price_zone);
        final FloatingActionButton cancel_button = findViewById(R.id.cancel_button);
        final FloatingActionButton validate_button = findViewById(R.id.validate_button);
        final Button add_photo_button = findViewById(R.id.add_photo_button);
        mImageView = findViewById(R.id.image_zone);
        storageReference = FirebaseStorage.getInstance().getReference("Images");


        if (getIntent().getStringExtra("add_or_edit").equals(HomeFragment.CREATE)){
            // user add a new proposition
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
                    proposition.setAuthor(userId);

                    if (uriImageSelected == null) {
                        uriImageSelected = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                                + "://"+getResources().getResourcePackageName(R.drawable.book)
                                + "/" + getResources().getResourceTypeName(R.drawable.book)
                                + "/" + getResources().getResourceEntryName(R.drawable.book));
                        proposition.setImageUrl(uriImageSelected.toString());
                    }

                    Log.d("champ", proposition.getDescription() + " // " + des_zone.getText().toString());
                    intentProp.putExtra(HomeFragment.CREATE, JSONModel.serialize(proposition));
                    setResult(CRE_OK, intentProp);
                    finish();
                }
            });
        }else {
            // user change a proposition
            proposition = JSONModel.deserialize(getIntent().getStringExtra(MODIFICATION), Proposition.class);
            this.setTitle("Modifier la proposition");
            title_zone.setText(proposition.getTitle());
            switch (proposition.getStatus()){
                case ACCEPTABLE:
                    status_zone.setProgress(0);
                    break;
                case GOOD:
                    status_zone.setProgress(1);
                    break;
                case VERY_GOOD:
                    status_zone.setProgress(2);
                    break;
                case NEW:
                    status_zone.setProgress(3);
                    break;
            }
            des_zone.setText(proposition.getDescription());
            date_zone.setText(proposition.getValidDay());
            price_zone.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            price_zone.setText(String.valueOf(proposition.getPrice()));

            validate_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intentProp = new Intent(AddPropositionActivity.this, ShowPropositionActivity.class);
                    proposition.setTitle(title_zone.getText().toString());
                    proposition.setDescription(des_zone.getText().toString());
                    proposition.setPrice(Float.parseFloat(price_zone.getText().toString()));
                    proposition.setPostedDay(proposition.getDateTimeFromCalendar(Calendar.getInstance()));

                    intentProp.putExtra(ShowPropositionActivity.MODIFICATION, JSONModel.serialize(proposition));
                    setResult(MOD_OK, intentProp);
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


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddPropositionActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint({"SetTextI18n", "DefaultLocale"})
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date_zone.setText(String.format("%s/%s/%s",
                                String.format("%02d", dayOfMonth),
                                String.format("%02d", monthOfYear + 1),
                                String.format("%02d", year)));
                        proposition.setValidDay(date_zone.getText().toString()); }
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
                setResult(CANCEL, intent);
                finish();
            }
        });

        add_photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 3 - Launch an "Selection Image" Activity
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RC_CHOOSE_PHOTO);
            }
        });
    }

    @Override
    public void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // 6 - Calling the appropriate method after activity result
        if (requestCode == RC_CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) { //SUCCESS
                this.uriImageSelected = data.getData();
                Glide.with(AddPropositionActivity.this).load(uriImageSelected).into(mImageView);
                proposition.setImageUrl(uriImageSelected.toString());

                UploadTask uploadTask = storageReference.child(proposition.getId()).putFile(uriImageSelected);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("error", "upload image failed");
                        // TODO properly handle this error.
                    }
                });

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // this is where we will end up if our image uploads successfully.
                        //proposition.setImageUrl(taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                    }
                });
            } else {
                Toast.makeText(this, getString(R.string.toast_title_no_image_chosen), Toast.LENGTH_SHORT).show();
            }
        }
    }


}