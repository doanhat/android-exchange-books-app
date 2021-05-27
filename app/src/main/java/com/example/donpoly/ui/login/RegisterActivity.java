package com.example.donpoly.ui.login;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.donpoly.R;
import com.example.donpoly.data.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "register";
    private ImageView mImage;
    private EditText mEmail, mPwd, mName;
    private ProgressDialog mLoadingBar;

    private boolean result = false;
    private static Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // init view
        mEmail = findViewById(R.id.email);
        mPwd = findViewById(R.id.password);
        mImage = findViewById(R.id.img_profile);
        mName = findViewById(R.id.name);
        Button mBtn = findViewById(R.id.signup);
        mLoadingBar = new ProgressDialog(RegisterActivity.this);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isUserEmailValid(mEmail.getText().toString())){
                    mEmail.setError(getString(R.string.invalid_username));
                }else if (!isPasswordValid(mPwd.getText().toString())){
                    mPwd.setError(getString(R.string.invalid_password));
                }else {
                    result = true;
                }
            }
        };

        mEmail.addTextChangedListener(textWatcher);
        mPwd.addTextChangedListener(textWatcher);

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (result){
                    mLoadingBar.setTitle("Créer un nouveau compte");
                    mLoadingBar.setMessage("Veuillez patienter...");
                    mLoadingBar.setCanceledOnTouchOutside(true);
                    mLoadingBar.show();

                    signUp(mEmail.getText().toString(),mPwd.getText().toString());
                }
            }
        });

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // choose a image
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, 10);
            }
        });


    }

    private boolean isUserEmailValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }


    private void signUp(String userEmail,String userPwd) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(userEmail, userPwd).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    // set the displayName of the current user
                    UserProfileChangeRequest profileUpdates;
                    if (mName.getText().toString() != null){
                        // set the name of the user
                        profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(mName.getText().toString())
                                .build();
                    }else {
                        // user didn't write a name, we give him a name with his id
                        profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName("User_"+ user.getUid())
                                .build();
                    }
                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Log.d(TAG, "User profile updated");
                            }else {
                                Log.d(TAG,"failed to add a name for user");
                            }
                        }
                    });


                    // set the image uri of the current user
                    if (imageUri != null){
                        Toast.makeText(RegisterActivity.this,imageUri.toString(),Toast.LENGTH_SHORT).show();
                        profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(imageUri).build();
                    }else {
                        profileUpdates = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                                                + "://"+getResources().getResourcePackageName(R.drawable.profile)
                                                + "/" + getResources().getResourceTypeName(R.drawable.profile)
                                                + "/" + getResources().getResourceEntryName(R.drawable.profile)))
                                        .build();
                    }

                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Log.d(TAG, "User profile updated");
                            }else {
                                Log.d(TAG,"failed to add a photo for user");
                            }
                        }
                    });


                    // create a user model
                    User user1 = new User(user.getUid(),userEmail,imageUri);
                    final StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("users").child(user.getUid());
                    UploadTask uploadTask = storageReference.putFile(imageUri);
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return storageReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                user1.setImageUri(downloadUri);
                            } else {
                                // Handle failures
                                // ...
                            }
                        }
                    });






                    SharedPreferences.Editor editor = getSharedPreferences("login", Context.MODE_PRIVATE).edit();
                    editor.putString("username",mEmail.getText().toString());
                    editor.putString("password",mPwd.getText().toString());
                    editor.commit();
                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                }else{
                    Toast.makeText(RegisterActivity.this,"Account creation failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {
            imageUri = data.getData();
            mImage.setImageURI(data.getData());
        }
    }

}