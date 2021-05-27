package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donpoly.R;
import com.example.donpoly.data.model.Chat;
import com.example.donpoly.ui.messages.MessageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{
    private Context context;
    private List  <Chat> mChats;
    private String imgURL;
    public  static final int MSG_TYPE_LEFT=0;
    public  static final int MSG_TYPE_RIGHT=1;

    //Firebase
    FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

    //constructor
    public MessageAdapter(MessageActivity context, List<Chat> mChats, String imgURL){
        this.context=context;
        this.mChats=mChats;
        this.imgURL=imgURL;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent,int viewType){
        if(viewType==MSG_TYPE_RIGHT){
            View view= LayoutInflater.from(context).inflate(R.layout.chat_item_right,parent,false);
            return new ViewHolder(view);
        }else{
            View view= LayoutInflater.from(context).inflate(R.layout.chat_item_left,parent,false);
            return new ViewHolder(view);
        }

    }
    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder,int position){
        Chat chat=mChats.get(position);
        holder.show_message.setText(chat.getMessage());
        holder.show_time.setText(chat.getTime());
        if(imgURL.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }else{
            //Glide.with(context).load(imgURL).into(holder.profile_image);
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }
    }


    @Override
    public int getItemViewType(int position){
        if(mChats.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message;
        public ImageView profile_image;
        public TextView show_time;
        public ViewHolder(@NotNull View itemView){
            super(itemView);
            show_message=itemView.findViewById(R.id.show_message);
            profile_image=itemView.findViewById(R.id.profile_image);
            show_time=itemView.findViewById(R.id.show_time);

        }
    }

}
