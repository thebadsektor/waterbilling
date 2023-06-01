package com.westframework.waterbillingapp.data.classification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.westframework.waterbillingapp.R;

import java.util.ArrayList;
import java.util.List;

public class ClassificationAdapter extends RecyclerView.Adapter<ClassificationAdapter.ClassificationHolder>{

    public List<Classification> classifications = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ClassificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.classification_item, parent, false);
        return new ClassificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassificationHolder holder, int position){
        Classification currentClassification = classifications.get(position);

        holder.tvNo.setText(String.valueOf(position + 1));
        holder.tvCategory.setText(currentClassification.getClassCat());
        holder.tvMin.setText(String.valueOf(currentClassification.cuMin()));
        holder.tvMax.setText(String.valueOf(currentClassification.cuMax()));
        holder.tvAmount.setText(String.valueOf(currentClassification.amount()));
        holder.tvIncrement.setText(String.valueOf(currentClassification.getIncrementBy()));
    }

    @Override
    public int getItemCount() {
        return classifications.size();
    }

    public void setClassifications(List<Classification> classifications){
        this.classifications = classifications;
        notifyDataSetChanged();
    }

    public Classification getClassifications(int position){
        return classifications.get(position);
    }

    class ClassificationHolder extends RecyclerView.ViewHolder{
        private TextView tvNo, tvCategory, tvMin, tvMax, tvAmount, tvIncrement;

        public ClassificationHolder(@NonNull View itemView){
            super(itemView);
            tvNo = itemView.findViewById(R.id.tvNo);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvMin = itemView.findViewById(R.id.tvMin);
            tvMax = itemView.findViewById(R.id.tvMax);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvIncrement = itemView.findViewById(R.id.tvIncrement);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION)
                    {
                        listener.onItemClick(classifications.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Classification classification);
    }

    public void setOnItemClickListerner(ClassificationAdapter.OnItemClickListener listener){
        this.listener = listener;
    }
}
