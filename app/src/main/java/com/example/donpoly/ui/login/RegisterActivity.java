package com.example.donpoly.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.donpoly.R;

import com.example.donpoly.data.model.User;
import com.example.donpoly.data.tools.FirebaseController;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
public class RegisterActivity extends AppCompatActivity {
    EditText id,name,password;
    ImageView profile;
    Button btn;
    Uri imageUri;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        id=findViewById(R.id.id);
        password=findViewById(R.id.password);
        name=findViewById(R.id.name);
        profile=findViewById(R.id.img_profile);
        btn=findViewById(R.id.signup);
        loadingBar=new ProgressDialog(RegisterActivity.this);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                upload();

            }
        });
    }

    private void upload() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, 10);
    }

    private void signup() {
        String email = id.getText().toString();
        String passwordS = password.getText().toString();
        String nameS=name.getText().toString();
        if (email.length() == 0) {
            id.setError("Entrez une adresse email");
            return;
        }

        if (password.length() < 6) {
            password.setError("Le mot de passe doit être au moins de 6 caractères");
            return;
        }
        if (nameS.length() ==0) {
            password.setError("Entrez un nom");
            return;
        }
        loadingBar.setTitle("Créer un nouveau compte");
        loadingBar.setMessage("Veuillez patienter...");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,passwordS).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    final String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();

                    //final StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("users/"+uid+"/"+imageUri.getLastPathSegment());
                    final StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("users").child(uid);
                    UploadTask uploadTask = storageReference.putFile(imageUri);
                    User userModel=new User();
                    userModel.setName(nameS);
                    userModel.setUid(uid);
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
                                userModel.setImageurl(downloadUri.toString());
                            } else {
                                // Handle failures
                                // ...
                            }
                        }
                    });

                    /*uploadTask.addOnFailureListener(new OnFailureListener() {

                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("error", "upload image failed");
                            Toast.makeText(RegisterActivity.this,"upload profil image failed",Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    });

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // this is where we will end up if our image uploads successfully.
                            User userModel=new User();
                            userModel.setName(nameS);
                            userModel.setUid(uid);
                            //userModel.setImageurl(taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Got the download URL for 'users/me/profile.png' in uri
                                    System.out.println(uri.toString());
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
                            /*Task<Uri> downloadUrl = storageReference.getDownloadUrl();
                            downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {

                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageReference = uri.toString();
                                    userModel.setImageurl(imageReference);
                                }
                            });*/
                            FirebaseController firebaseController = new FirebaseController("users");
                            DatabaseReference mDbUsers = firebaseController.getReferences().get("users");
                            mDbUsers.child(userModel.getUid()).setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // after the data addition is successful
                                    // we are displaying a success toast message.
                                    Toast.makeText(RegisterActivity.this, "Utilisateur ajoutée avec succès " + userModel.getUid(), Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // this method is called when the data addition process is failed.
                                    // displaying a toast message when data addition is failed.
                                    Toast.makeText(RegisterActivity.this, "Echec lors de l'ajoute de l'Utilisateur \n" + e, Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                }
                            });
                        //}
                    //});
                }else{
                    Toast.makeText(RegisterActivity.this,"Account creation failed",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {
            profile.setImageURI(data.getData());
            imageUri = data.getData();
        }
    }
}
