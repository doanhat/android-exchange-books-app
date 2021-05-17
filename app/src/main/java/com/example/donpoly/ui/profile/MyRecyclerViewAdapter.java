package com.example.donpoly.ui.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donpoly.R;
import com.example.donpoly.data.model.Proposition;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private List<Proposition> propositionList;

    public MyRecyclerViewAdapter(List<Proposition> propositionList){
        this.propositionList = propositionList;
    }

    @NonNull
    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.prop_comm_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.getPoly().setText(propositionList.get(position).getTitle());
        holder.getDate().setText(propositionList.get(position).getPostedDay());
    }

    @Override
    public int getItemCount() {
        return propositionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView poly;
        private final TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            poly = itemView.findViewById(R.id.tv_poly_item);
            date = itemView.findViewById(R.id.tv_date_item);
        }

        public TextView getPoly(){
            return poly;
        }

        public TextView getDate(){
            return date;
        }
    }
}
