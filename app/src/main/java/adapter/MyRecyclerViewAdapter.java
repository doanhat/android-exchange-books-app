package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donpoly.R;
import com.example.donpoly.data.model.Proposition;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Proposition proposition = propositionList.get(position);

        // if the valid day is already past, the background color turns to gray
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = simpleDateFormat.parse(proposition.getValidDay());
            if (date.before(Calendar.getInstance().getTime())){
                holder.linearLayout.setBackgroundColor(R.color.gray);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.poly.setText(proposition.getTitle());
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
        public LinearLayout linearLayout;
        public TextView poly;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.ll_prop_item);
            poly = itemView.findViewById(R.id.tv_poly_item);
        }
    }

    public interface OnItemClickListener{
        void onClick(int pos);
    }
}
