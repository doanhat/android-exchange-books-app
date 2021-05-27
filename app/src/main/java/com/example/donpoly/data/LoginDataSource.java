package com.example.donpoly.data;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.donpoly.data.model.LoggedInUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private String id;
    private String name;
    private String email;
    private Uri imageUri;

    public LoginDataSource(){
    }

    public Result<LoggedInUser> login(String username, String password) {
        try {
            // TODO: handle loggedInUser authentication
            FirebaseAuth.getInstance().signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        // user login successfully
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        id = user.getUid();
                        name = user.getDisplayName();
                        email = user.getEmail();
                        imageUri = user.getPhotoUrl();
                    }else {
                        // user login failed
                        return;
                    }
                }
        });
            return new Result.Success<>(new LoggedInUser(id,name,email,imageUri,new ArrayList<>(),new ArrayList<>()));
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));

        }
    }

    public void logout() {
        // TODO: revoke authentication
        FirebaseAuth.getInstance().signOut();
    }
}