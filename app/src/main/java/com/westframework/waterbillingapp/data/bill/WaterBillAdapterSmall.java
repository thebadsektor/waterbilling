package com.westframework.waterbillingapp.data.bill;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.westframework.waterbillingapp.R;

import java.util.ArrayList;
import java.util.List;

public class WaterBillAdapterSmall extends RecyclerView.Adapter<WaterBillAdapterSmall.WaterBillHolder> {

    public List<WaterBill> waterBills = new ArrayList<>();
    private List<WaterBill> waterBillsFiltered;
    private OnItemClickListener listener;

    @NonNull
    @Override
    public WaterBillHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.arrears_item, parent, false);
        return  new WaterBillHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WaterBillHolder holder, int position){
        WaterBill currentBill = waterBills.get(position);
        String formattedId = String.format("%06d", currentBill.getId());
        holder.textViewTitle.setText(formattedId + " : " + currentBill.getStatus());
    }

    @Override
    public int getItemCount() {
        return waterBills.size();
    }

    public void setWaterBills(List<WaterBill> waterBills){
        this.waterBills = waterBills;
        this.waterBillsFiltered = waterBills;
        notifyDataSetChanged();
    }

    public WaterBill getWaterBills(int position){
        return waterBills.get(position);
    }

    class WaterBillHolder extends RecyclerView.ViewHolder{
        private TextView textViewTitle;
        private TextView textViewDescription;

        public WaterBillHolder(@NonNull View itemView){
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION)
                    {
                        listener.onItemClick(waterBills.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(WaterBill waterBill);
    }

    public void setOnItemClickListerner(WaterBillAdapterSmall.OnItemClickListener listener){
        this.listener = listener;
    }

    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                if (charSequence == null || charSequence.length() == 0) {
                    filterResults.count = waterBillsFiltered.size();
                    filterResults.values = waterBillsFiltered;
                }else{
                    String searchChr = charSequence.toString().toLowerCase();
                    List<WaterBill> resultData = new ArrayList<>();

                    for (WaterBill waterBill: waterBillsFiltered){
                        if (waterBill.getFullName().toLowerCase().contains(searchChr)){
                            resultData.add(waterBill);
                        }
                    }
                    filterResults.count = resultData.size();
                    filterResults.values = resultData;
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                waterBills = (List<WaterBill>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}