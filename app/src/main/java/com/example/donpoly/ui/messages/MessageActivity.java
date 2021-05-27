package com.example.donpoly.ui.messages;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donpoly.R;
import com.example.donpoly.data.model.Chat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import adapter.MessageAdapter;

public class MessageActivity extends AppCompatActivity {
    TextView username;
    ImageView imageView;

    RecyclerView recyclerView;
    EditText editText;
    Button sendBtn;

    FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference reference;
    Intent intent;
    MessageAdapter messageAdapter;
    List<Chat> mchat;
    String userid;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        this.setTitle("Chat");

        imageView=findViewById(R.id.imageview_profile);
        username=findViewById(R.id.username);
        sendBtn=findViewById(R.id.btn_send);
        editText=findViewById(R.id.text_send);

        //RecyclerView
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        intent=getIntent();
        userid=intent.getStringExtra("visitUserId");
        DatabaseReference mDbUsers = FirebaseDatabase.getInstance(getString(R.string.database_path))
                .getReference().child("users").child(userid);
        mDbUsers.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username.setText(snapshot.child("name").getValue().toString());
                //Glide.with(MessageActivity.this).load(snapshot.child("imageurl").getValue().toString()).into(imageView);
                imageView.setImageResource(R.drawable.profile);

                readMessager(fuser.getUid(),userid,"default");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=editText.getText().toString();
                if(!msg.equals("")){
                    sendMessage(fuser.getUid(),userid,msg);
                }else{
                    Toast.makeText(MessageActivity.this,"Veuillez envoyer un message non vide",Toast.LENGTH_SHORT);
                }
                editText.setText("");
            }
        });
    }
    private void sendMessage(String sender,String receiver,String message){
        DatabaseReference mDbUsers = FirebaseDatabase.getInstance(getString(R.string.database_path))
                .getReference().child("chats");
        String currentDateandTime = new SimpleDateFormat("MM-dd HH:mm").format(new Date());
        HashMap<String,Object> hashMap =new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("time",currentDateandTime);
        mDbUsers.push().setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //  the data addition is successful
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
            }
        });
        //adding user to chat fragment latest chats with contacts
        DatabaseReference mDbChatsList = FirebaseDatabase.getInstance(getString(R.string.database_path))
                .getReference().child("ChatList").child(fuser.getUid()).child(userid);
        mDbChatsList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    mDbChatsList.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference mDbChatsList2 = FirebaseDatabase.getInstance(getString(R.string.database_path))
                .getReference().child("ChatList").child(userid).child(fuser.getUid());
        mDbChatsList2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    mDbChatsList2.child("id").setValue(fuser.getUid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void readMessager(String myid,String userid,String imageurl){
        mchat=new ArrayList<>();
        DatabaseReference mDbChats = FirebaseDatabase.getInstance(getString(R.string.database_path))
                .getReference().child("chats");
        mDbChats.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mchat.clear();

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Chat chat = postSnapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(myid)&&chat.getSender().equals(userid)||chat.getReceiver().equals(userid)&&chat.getSender().equals(myid)){
                        mchat.add(chat);
                    }
                    messageAdapter=new MessageAdapter(MessageActivity.this,mchat,imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
            }
        });
    }
}