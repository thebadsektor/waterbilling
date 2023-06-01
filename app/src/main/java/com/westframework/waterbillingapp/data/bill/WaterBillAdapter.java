package com.westframework.waterbillingapp.data.bill;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.westframework.waterbillingapp.R;

import java.util.ArrayList;
import java.util.List;

public class WaterBillAdapter extends RecyclerView.Adapter<WaterBillAdapter.WaterBillHolder> {

    public List<WaterBill> waterBills = new ArrayList<>();
    private List<WaterBill> waterBillsFiltered;
    private OnItemClickListener listener;

    @NonNull
    @Override
    public WaterBillHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_item, parent, false);
        return  new WaterBillHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull WaterBillHolder holder, int position){
        WaterBill currentBill = waterBills.get(position);
        String billingMonth = currentBill.getReadingMonth();
        String appNum = currentBill.getApplicationNo();
        String title = currentBill.getOrNum();
        String status = currentBill.getStatus();

        if("paid".equals(status.toLowerCase())){
            int color = ContextCompat.getColor(holder.textStatus.getContext(), R.color.success);
            holder.statusIcon.setImageResource(R.drawable.verified);
            ColorStateList colorStateList = ColorStateList.valueOf(color);
            holder.statusIcon.setImageTintList(colorStateList);
            holder.statusIcon.setImageTintMode(PorterDuff.Mode.SRC_IN);
            holder.textStatus.setTextColor(color);
        }else{
            int color = ContextCompat.getColor(holder.textStatus.getContext(), R.color.warning);
            holder.statusIcon.setImageResource(R.drawable.unverified);
            ColorStateList colorStateList = ColorStateList.valueOf(color);
            holder.statusIcon.setImageTintList(colorStateList);
            holder.statusIcon.setImageTintMode(PorterDuff.Mode.SRC_IN);
            holder.textStatus.setTextColor(color);
        }


        holder.textStatus.setText(toTitleCase(status));
        holder.textViewTitle.setText(title);
        holder.textViewDescription.setText(appNum + " - " + currentBill.meter_read +  " (" + billingMonth + ") " +"\n"
                + currentBill.getFullName().trim() + " \n "
                + currentBill.getBillAddress().trim());
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
        private TextView textStatus;
        private AppCompatImageView icon;
        private ImageView statusIcon;

        public WaterBillHolder(@NonNull View itemView){
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textStatus = itemView.findViewById(R.id.textStatus);
            statusIcon = itemView.findViewById(R.id.statusIcon);

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

    public void setOnItemClickListerner(WaterBillAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

    public Filter getFilter(int mode){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                if (charSequence == null || charSequence.length() == 0) {
                    if(waterBillsFiltered == null){
                        filterResults.count = 0;
                    }else{
                        filterResults.count = waterBillsFiltered.size();
                    }
                    filterResults.values = waterBillsFiltered;
                }else{
                    String searchChr = charSequence.toString().toLowerCase();
                    List<WaterBill> resultData = new ArrayList<>();

                    for (WaterBill waterBill: waterBillsFiltered){
                        String meter_no = String.valueOf(waterBill.getMeterRead());
                        switch (mode){
                            case 1:
                                if(searchChr.equals(meter_no)){
                                    resultData.add(waterBill); }
                                break;
                            case 2:
                                if(waterBill.getApplicationNo().contains(searchChr)){
                                    resultData.add(waterBill); }
                                break;
                            case 3:
                                if (waterBill.getFullName().toLowerCase().contains(searchChr))
                                { resultData.add(waterBill); }
                                break;
                            case 4:
                                if(waterBill.getOrNum().contains(searchChr))
                                { resultData.add(waterBill);}
                                break;
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

    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder(input.length());
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }
}