package com.westframework.waterbillingapp.data.amnesty;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.westframework.waterbillingapp.R;

import java.util.ArrayList;
import java.util.List;

public class AmnestyAdapter extends RecyclerView.Adapter<AmnestyAdapter.AmnestyHolder>{

    public List<Amnesty> amnesties = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public AmnestyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.amnesty_item, parent, false);
        return new AmnestyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AmnestyHolder holder, int position){
        Amnesty currentAmnesty = amnesties.get(position);
        holder.textViewIncrement.setText(String.valueOf(position + 1));
        holder.textViewTitle.setText(currentAmnesty.getOrdinanceName());
        holder.textViewMin.setText(currentAmnesty.getBillMonth());
        holder.textViewMax.setText(currentAmnesty.getBillYear());
        holder.textViewAmount.setText(currentAmnesty.getDesc());
    }

    @Override
    public int getItemCount() {
        return amnesties.size();
    }

    public void setAmnesties(List<Amnesty> amnesties){
        this.amnesties = amnesties;
        notifyDataSetChanged();
    }

    public Amnesty getAmnesties(int position){
        return amnesties.get(position);
    }

    class AmnestyHolder extends RecyclerView.ViewHolder{
        private TextView textViewTitle;
        private TextView textViewMin;
        private TextView textViewMax;
        private TextView textViewAmount;
        private TextView textViewIncrement;

        public AmnestyHolder(@NonNull View itemView){
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewMin = itemView.findViewById(R.id.textViewMin);
            textViewMax = itemView.findViewById(R.id.textViewMax);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
            textViewIncrement = itemView.findViewById(R.id.textViewIncrement);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION)
                    {
                        listener.onItemClick(amnesties.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Amnesty amnesty);
    }

    public void setOnItemClickListerner(OnItemClickListener listener){
        this.listener = listener;
    }
}
