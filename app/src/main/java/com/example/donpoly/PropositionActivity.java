package com.example.donpoly;

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
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.donpoly.data.model.Proposition;
import com.example.donpoly.data.tools.JSONModel;
import com.example.donpoly.data.tools.Status;
import com.example.donpoly.ui.home.HomeFragment;
import com.example.donpoly.ui.login.LoginActivity;
import com.example.donpoly.ui.messages.MessageActivity;
import com.example.donpoly.views.adapters.PropositionAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

import pub.devrel.easypermissions.EasyPermissions;
import static com.example.donpoly.ui.home.HomeFragment.PROP_DATA;

public class PropositionActivity extends AppCompatActivity {
    public static final int CRE_OK = 1;
    public static final int CANCEL = -1;
    public static final int MOD_OK = 2;
    private Proposition proposition;
    private int mYear, mMonth, mDay;
    StorageReference storageReference;

    // 1 - Uri of image selected by user
    private Uri uriImageSelected;

    // 1 - STATIC DATA FOR PICTURE
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int RC_IMAGE_PERMS = 100;
    private static final int RC_CHOOSE_PHOTO = 200;
    private LinearLayout linearImageLayout;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        String userid = user.getUid();
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
        Button add_photo_button = findViewById(R.id.add_photo_button);
        linearImageLayout = findViewById(R.id.image_zone);
        if (intent.getIntExtra(PropositionAdapter.MODIFICATION, 0) == HomeFragment.PROP_MOD) {
            proposition = JSONModel.deserialize(intent.getStringExtra(PROP_DATA), Proposition.class);
            this.setTitle("Modifier la proposition");
            //TODO error
                    title_zone.setText(proposition.getTitle());
                    //status_zone.setProgress(0,proposition.getStatus()==Status.ACCEPTABLE);
                    //status_zone.setProgress(1,proposition.getStatus()==Status.GOOD);
                    //status_zone.setProgress(2,proposition.getStatus()==Status.VERY_GOOD);
                    //status_zone.setProgress(3,proposition.getStatus()==Status.NEW);
                    des_zone.setText(proposition.getDescription());
                    date_zone.setText(proposition.getValidDay());
                    price_zone.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    price_zone.setText(String.valueOf(proposition.getPrice()));
            validate_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intentProp = new Intent();
                    proposition.setTitle(title_zone.getText().toString());
                    proposition.setDescription(des_zone.getText().toString());
                    proposition.setPrice(Float.parseFloat(price_zone.getText().toString()));
                    proposition.setPostedDay(proposition.getDateTimeFromCalendar(Calendar.getInstance()));
                    // TODO : set Author
                    intentProp.putExtra(PROP_DATA, JSONModel.serialize(proposition));
                    setResult(MOD_OK, intentProp);
                    finish();
                }
            });
        } else {
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
                    proposition.setAuthor(userid);
                    proposition.setTaker("default");
                    if (uriImageSelected != null) {
                        storageReference= FirebaseStorage.getInstance().getReference().child("Images").child(proposition.getId());
                        //StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(uriImageSelected));
                        UploadTask uploadTask = storageReference.putFile(uriImageSelected);
                        uploadTask.addOnFailureListener(new OnFailureListener() {

                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("error", "upload image failed");
                                // TODO properly handle this error.
                            }
                        });

                        uploadTask.addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {

                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // this is where we will end up if our image uploads successfully.
                                proposition.setImageUrl(taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                            }
                        });
                    }
                    Log.d("champ", proposition.getDescription() + " // " + des_zone.getText().toString());
                    intentProp.putExtra(PROP_DATA, JSONModel.serialize(proposition));
                    setResult(CRE_OK, intentProp);
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
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
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
        add_photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                this.chooseImageFromPhone();
            }

            private void chooseImageFromPhone() {
                if (!EasyPermissions.hasPermissions(getBaseContext(), PERMS)) {
                    EasyPermissions.requestPermissions(PropositionActivity.this, getString(R.string.popup_title_permission_files_access), RC_IMAGE_PERMS, PERMS);
                    return;
                }
                // 3 - Launch an "Selection Image" Activity
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RC_CHOOSE_PHOTO);
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 2 - Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 6 - Calling the appropriate method after activity result
        this.handleResponse(requestCode, resultCode, data);
    }

    private void handleResponse(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) { //SUCCESS
                this.uriImageSelected = data.getData();
                ImageView imageViewPreview = new ImageView(this);
                Glide.with(this) //SHOWING PREVIEW OF IMAGE
                        .load(this.uriImageSelected)
                        .apply(RequestOptions.noTransformation())
                        .into(imageViewPreview);
                imageViewPreview.setMaxWidth(5);
                imageViewPreview.setMaxHeight(5);
                int count = linearImageLayout.getChildCount();
                Log.d("image",String.valueOf(count));
                linearImageLayout.addView(imageViewPreview);
            } else {
                Toast.makeText(this, getString(R.string.toast_title_no_image_chosen), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
}
