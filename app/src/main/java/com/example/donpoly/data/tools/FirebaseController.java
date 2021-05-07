package com.example.donpoly.data.tools;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import java.util.HashMap;

public class FirebaseController {
    private FirebaseDatabase mFirebaseDatabase;
    private HashMap<String,DatabaseReference> references = new HashMap<>();
    private String path = "https://nf28-donpoly-default-rtdb.europe-west1.firebasedatabase.app";
    public FirebaseController(String childName, String path) {
        mFirebaseDatabase = FirebaseDatabase.getInstance(path);
        references.put(childName,mFirebaseDatabase.getReference().child(childName));
    }

    public FirebaseController() {
        mFirebaseDatabase = FirebaseDatabase.getInstance(path);
    }

    public FirebaseController(String childName) {
        mFirebaseDatabase = FirebaseDatabase.getInstance(path);
        // TODO décommenter si vouloir debuger firebase
        //setLogLevel(Logger.Level.DEBUG);
        references.put(childName,mFirebaseDatabase.getReference().child(childName));
    }

    public FirebaseDatabase getmFirebaseDatabase() {
        return mFirebaseDatabase;
    }

    public void setmFirebaseDatabase(FirebaseDatabase mFirebaseDatabase) {
        this.mFirebaseDatabase = mFirebaseDatabase;
    }

    public HashMap<String, DatabaseReference> getReferences() {
        return references;
    }

    public void setReferences(HashMap<String, DatabaseReference> references) {
        this.references = references;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setLogLevel(Logger.Level level){
        mFirebaseDatabase.setLogLevel(level);
    }
}
