package com.example.donpoly.ui.messages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donpoly.R;
import com.example.donpoly.data.model.Chatlist;
import com.example.donpoly.data.model.User;
import com.example.donpoly.data.tools.FirebaseController;
import com.example.donpoly.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import adapter.UserAdapter;

public class MessagesFragment extends Fragment {
    private UserAdapter userAdapter;
    private List<User> mUsers;

    FirebaseUser fuser;
    private List<Chatlist> usersList;
    RecyclerView recyclerView;

    public MessagesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        recyclerView = view.findViewById(R.id.recycler_view2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userAdapter=new UserAdapter(getContext(),mUsers=new ArrayList<>());
        recyclerView.setAdapter(userAdapter);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (fuser == null){
            startActivity(new Intent(getContext(), LoginActivity.class));
        }

        usersList = new ArrayList<>();
        FirebaseController firebaseController2 = new FirebaseController("ChatList");
        DatabaseReference mDbChatsList = firebaseController2.getReferences().get("ChatList");
        assert mDbChatsList != null;
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
        if (fuser != null){
            Query orderRef=mDbChatsList.child(fuser.getUid()).orderByChild("recentTime");
//            Query orderRef=mDbChatsList.child(LoginRepository.getInstance(new LoginDataSource()).getLoggedInUser().getUserId()).orderByChild("recentTime");

            orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    usersList.clear();
                    //loop for all users
                    for (DataSnapshot snapshotItem : snapshot.getChildren()) {
                        Chatlist chatlist = snapshotItem.getValue(Chatlist.class);
                        Log.d("TAG", chatlist.getId());
                        usersList.add(chatlist);
                    }
                    Collections.reverse(usersList);

                    //getting all recent chats;
                    FirebaseController firebaseController3 = new FirebaseController("users");
                    DatabaseReference mDbUsers = firebaseController3.getReferences().get("users");
                    mDbUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mUsers.clear();
                            for(Chatlist chatlist:usersList){
                                for(DataSnapshot snapshotItem:snapshot.getChildren()) {
                                    User user = snapshotItem.getValue(User.class);
                                    if (user.getUid().equals(chatlist.getId())) {
                                        mUsers.add(user);
                                        break;
                                    }
                                }
                            }
                            userAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        return view;
    }


}
