package com.example.donpoly.views;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donpoly.PropositionActivity;
import com.example.donpoly.R;
import com.example.donpoly.data.model.Proposition;
import com.example.donpoly.data.tools.JSONModel;
import com.example.donpoly.ui.home.HomeFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

import static com.example.donpoly.ui.home.HomeFragment.PROP_MOD;

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class PropositionItemAdapter extends FirebaseRecyclerAdapter<
        Proposition, PropositionItemAdapter.propositionsViewholder> {

    private Context context;
    public static final String MODIFICATION = "modification";
    private List<Proposition> mProps = new ArrayList<>();
    public PropositionItemAdapter(@NonNull FirebaseRecyclerOptions<Proposition> options, List<Proposition> props) {
        super(options);
        mProps = props;
    }

    public PropositionItemAdapter(@NonNull FirebaseRecyclerOptions<Proposition> options) {
        super(options);
    }

    // Function to bind the view in Card view(here
    // "person.xml") iwth data in
    // model class(here "person.class")
    @Override
    protected void onBindViewHolder(@NonNull propositionsViewholder holder,
                     int position, @NonNull Proposition proposition) {
        // Get the data model based on position
        // Set item views based on your views and data model
        ImageView vProp_image = holder.prop_image;
        TextView vProp_title = holder.prop_title;
        vProp_title.setText(proposition.getTitle());
        TextView vProp_price = holder.prop_price;
        vProp_price.setText(String.valueOf(proposition.getPrice()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,
                        PropositionActivity.class);
                intent.putExtra(HomeFragment.PROP_DATA, JSONModel.serialize(proposition));
                intent.putExtra(MODIFICATION,PROP_MOD);
                context.startActivity(intent);
            }
        });
    }

    // Function to tell the class about the Card view (here
    // "person.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public propositionsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View propView = inflater.inflate(R.layout.item_proposition, parent, false);

        // Return a new holder instance
        return new PropositionItemAdapter.propositionsViewholder(propView);
    }

    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    class propositionsViewholder extends RecyclerView.ViewHolder {
        public ImageView prop_image;
        public TextView prop_title;
        public TextView prop_price;

        public propositionsViewholder(@NonNull View itemView) {
            super(itemView);
            prop_image = (ImageView) itemView.findViewById(R.id.prop_image);
            prop_title = (TextView) itemView.findViewById(R.id.prop_title);
            prop_price = (TextView) itemView.findViewById(R.id.prop_price);
        }
    }
    @Override
    public int getItemCount() {
        return mProps.size();
    }
}