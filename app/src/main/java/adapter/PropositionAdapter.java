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
import com.example.donpoly.data.model.Proposition;

import java.util.List;

public class PropositionAdapter extends RecyclerView.Adapter<PropositionAdapter.ViewHolder> {
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
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
    }

    @Override
    public int getItemCount() {
        return mProps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView prop_image;
        public TextView prop_title;
        public TextView prop_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
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
