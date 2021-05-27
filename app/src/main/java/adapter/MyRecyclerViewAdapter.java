package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donpoly.R;
import com.example.donpoly.data.model.Proposition;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private OnItemClickListener itemClickListener;
    private List<Proposition> propositionList;

    public MyRecyclerViewAdapter(List<Proposition> propositionList, OnItemClickListener listener){
        this.propositionList = propositionList;
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.prop_comm_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Proposition proposition = propositionList.get(position);
        holder.poly.setText(proposition.getTitle());
        holder.date.setText(proposition.getPostedDay());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return propositionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView poly;
        public TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            poly = itemView.findViewById(R.id.tv_poly_item);
            date = itemView.findViewById(R.id.tv_date_item);
        }
    }

    public interface OnItemClickListener{
        void onClick(int pos);
    }
}
