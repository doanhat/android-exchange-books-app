<<<<<<< HEAD:app/src/main/java/com/example/donpoly/adapter/PropositionAdapter.java
package com.example.donpoly.adapter;
=======
package com.example.donpoly.views.adapters;
>>>>>>> develop:app/src/main/java/com/example/donpoly/adapter/adapters/PropositionAdapter.java

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.PointerIcon;
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

import java.util.List;

import static com.example.donpoly.ui.home.HomeFragment.PROP_MOD;

public class PropositionAdapter extends RecyclerView.Adapter<PropositionAdapter.ViewHolder> {

    private Context context;
    public static final String MODIFICATION = "modification";
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View propView = inflater.inflate(R.layout.item_proposition, parent, false);

        // Return a new holder instance
        return new ViewHolder(propView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
        Proposition proposition = mProps.get(position);

        // Set item views based on your views and data model
        ImageView vProp_image = holder.prop_image;
        TextView vProp_title = holder.prop_title;
        vProp_title.setText(proposition.getTitle());
        TextView vProp_price = holder.prop_price;
        vProp_price.setText(String.valueOf(proposition.getPrice()));
        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,
                        PropositionActivity.class);
                intent.putExtra(HomeFragment.PROP_DATA, JSONModel.serialize(proposition));
                intent.putExtra(MODIFICATION,PROP_MOD);
                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mProps.size();
    }

    public Proposition getItem(int position){
        return mProps.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ImageView prop_image;
        public TextView prop_title;
        public TextView prop_price;

        public View getmView() {
            return mView;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            prop_image = (ImageView) itemView.findViewById(R.id.prop_image);
            prop_title = (TextView) itemView.findViewById(R.id.prop_title);
            prop_price = (TextView) itemView.findViewById(R.id.prop_price);
        }
    }

    private List<Proposition> mProps;

    // Pass in the contact array into the constructor
    public PropositionAdapter(List<Proposition> props) {
        mProps = props;
    }

}
