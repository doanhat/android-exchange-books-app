package com.example.donpoly.ui.messages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donpoly.R;
import com.example.donpoly.data.model.Chatlist;
import com.example.donpoly.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapter.UserAdapter;

public class MessagesFragment extends Fragment {
    private UserAdapter userAdapter;
    private List<User> mUsers;

    FirebaseUser fuser;
    private List<Chatlist> usersList;
    RecyclerView recyclerView;

    private MessagesViewModel messagesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        messagesViewModel =
                new ViewModelProvider(this).get(MessagesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_messages, container, false);

        recyclerView = root.findViewById(R.id.recycler_view2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        usersList = new ArrayList<>();
        DatabaseReference mDbChatsList = FirebaseDatabase.getInstance(getString(R.string.database_path))
                .getReference().child("ChatList").child(fuser.getUid());
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                User user = (User) userAdapter.getItem(position);
                Intent intent = new Intent(getContext(), MessageActivity.class);
                intent.putExtra("visitUserId",user.getUid());
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        mDbChatsList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                usersList.clear();
                //loop for all users
                for (DataSnapshot snapshotItem : snapshot.getChildren()) {
                    Chatlist chatlist = snapshotItem.getValue(Chatlist.class);
                    usersList.add(chatlist);
                }
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return root;
    }

    private void chatList(){
        //getting all recent chats;
        mUsers=new ArrayList<>();
        DatabaseReference mDbUsers = FirebaseDatabase.getInstance(getString(R.string.database_path))
                .getReference().child("users");
        mDbUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for(DataSnapshot snapshotItem:snapshot.getChildren()){
                    User user=snapshotItem.getValue(User.class);
                    for(Chatlist chatlist:usersList){
                        if(user.getUid().equals(chatlist.getId())){
                            mUsers.add(user);
                        }
                    }
                }
                userAdapter=new UserAdapter(getContext(),mUsers);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}