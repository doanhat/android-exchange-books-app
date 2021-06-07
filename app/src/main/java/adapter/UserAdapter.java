package adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.donpoly.R;
import com.example.donpoly.data.model.User;
import com.example.donpoly.data.tools.FirebaseController;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private List<User> mUsers;

    public UserAdapter(Context context, List<User> mUsers) {
        this.context = context;
        this.mUsers = mUsers;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user=mUsers.get(position);
        holder.username.setText(user.getName());
        /*if(user.getImageurl().equals("default")){
            holder.imageView.setImageResource(R.drawable.profile);
        }else{
            Glide.with(context).load(user.getImageurl()).into(holder.imageView);
        }*/

        // set the image of user
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("users").child(user.getUid());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.imageView);
            }
        });

        //unread message
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseController firebaseController2 = new FirebaseController("ChatList");
        DatabaseReference mDbChatsList = firebaseController2.getReferences().get("ChatList");
        assert mDbChatsList != null;
        mDbChatsList.child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String flagUnread=(String) snapshot.child(user.getUid()).child("unread").getValue();
                    if (flagUnread != null){
                        if(flagUnread.equals("true")){
                            holder.imageViewUnread.setVisibility(View.VISIBLE);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView imageView;
        public ImageView imageViewUnread;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.textView30);
            imageView=itemView.findViewById(R.id.imageView30);
            imageViewUnread=itemView.findViewById(R.id.imageViewUnread);
        }
    }

    public User getItem(int position){
        return mUsers.get(position);
    }
}
