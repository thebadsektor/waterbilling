package com.westframework.waterbillingapp.data.discount;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.westframework.waterbillingapp.R;

import java.util.ArrayList;
import java.util.List;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.DiscountHolder> {
    public List<Discount> discounts = new ArrayList<>();
    private DiscountAdapter.OnItemClickListener listener;

    @NonNull
    @Override
    public DiscountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discount_item, parent, false);
        return  new DiscountHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountAdapter.DiscountHolder holder, int position){
        Discount currentDiscount = discounts.get(position);

        String title = String.valueOf("ID: " + currentDiscount.getId()) + " - " + currentDiscount.getCategory();

        holder.textViewTitle.setText(title);
        holder.textViewDescription.setText(currentDiscount.getDescription());
        holder.textViewType.setText(currentDiscount.getType());
        holder.textViewAmount.setText(String.valueOf(currentDiscount.getAmount()));
    }

    @Override
    public int getItemCount() {
        return discounts.size();
    }

    public void setDiscounts(List<Discount> discounts){
        this.discounts = discounts;
        notifyDataSetChanged();
    }

    public Discount getDiscounts(int position){
        return discounts.get(position);
    }

    class DiscountHolder extends RecyclerView.ViewHolder{
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewType;
        private TextView textViewAmount;

        public DiscountHolder(@NonNull View itemView){
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewType = itemView.findViewById(R.id.textViewType);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION)
                    {
                        listener.onItemClick(discounts.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Discount application);
    }

    public void setOnItemClickListerner(DiscountAdapter.OnItemClickListener listener){
        this.listener = listener;
    }
}
