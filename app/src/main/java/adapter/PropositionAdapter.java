package adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.donpoly.data.model.Proposition;
import com.example.donpoly.data.tools.JSONModel;
import com.example.donpoly.ui.home.ShowPolyDetailActivity;
import com.example.donpoly.ui.profile.ShowPropositionActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static com.example.donpoly.R.id;
import static com.example.donpoly.R.layout;

public class PropositionAdapter extends RecyclerView.Adapter<PropositionAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<Proposition> mProps;
    private List<Proposition> mSourceList;
    private List<Proposition> mFilterList;

    // Pass in the contact array into the constructor
    public PropositionAdapter(List<Proposition> props) {
        mProps = props;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View propView = inflater.inflate(layout.item_proposition, parent, false);

        // Return a new holder instance
        return new ViewHolder(propView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
        Proposition proposition = mFilterList.get(position);

        // Set item views based on your views and data model
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Images").child(proposition.getId());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.prop_image);
            }
        });
        holder.prop_title.setText(proposition.getTitle().toUpperCase());
        holder.prop_price.setText(String.valueOf(proposition.getPrice()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowPolyDetailActivity.class);
                intent.putExtra(ShowPropositionActivity.SHOW, JSONModel.serialize(proposition));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilterList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString().toUpperCase();
                if (charString.isEmpty()){
                    // no content to filter, we use the original data
                    mFilterList = mSourceList;
                }else {
                    List<Proposition> filteredList = new ArrayList<>();
                    for (Proposition pro:mSourceList) {
                        // filter the name of propositions
                        if (pro.getTitle().contains(charString)){
                            filteredList.add(pro);
                        }
                    }
                    mFilterList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilterList = (ArrayList<Proposition>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void appendList(List<Proposition> list){
        mSourceList = list;
        mFilterList = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView prop_image;
        public TextView prop_title;
        public TextView prop_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            prop_image = (ImageView) itemView.findViewById(id.prop_image);
            prop_title = (TextView) itemView.findViewById(id.prop_title);
            prop_price = (TextView) itemView.findViewById(id.prop_price);
        }
    }

}
