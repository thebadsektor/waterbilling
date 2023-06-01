package com.westframework.waterbillingapp.data.holiday;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.westframework.waterbillingapp.R;

import java.util.ArrayList;
import java.util.List;

public class HolidayAdapter extends RecyclerView.Adapter<HolidayAdapter.HolidayHolder>{

    public List<Holiday> holidays = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public HolidayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holiday_item, parent, false);
        return new HolidayHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolidayHolder holder, int position){
        Holiday currentHoliday = holidays.get(position);
        holder.textViewTitle.setText(currentHoliday.getHolidayName() + " " + currentHoliday.getHolidayYear());
        holder.textViewMin.setText(currentHoliday.getHolidayMonth().trim());
        holder.textViewMax.setText(currentHoliday.getHolidayDate());
        holder.textViewAmount.setText(currentHoliday.created_at);
        holder.textViewIncrement.setText(currentHoliday.updated_at);
    }

    @Override
    public int getItemCount() {
        return holidays.size();
    }

    public void setHolidays(List<Holiday> holidays){
        this.holidays = holidays;
        notifyDataSetChanged();
    }

    public Holiday getHolidays(int position){
        return holidays.get(position);
    }

    class HolidayHolder extends RecyclerView.ViewHolder{
        private TextView textViewTitle;
        private TextView textViewMin;
        private TextView textViewMax;
        private TextView textViewAmount;
        private TextView textViewIncrement;

        public HolidayHolder(@NonNull View itemView){
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
                        listener.onItemClick(holidays.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Holiday holiday);
    }

    public void setOnItemClickListerner(OnItemClickListener listener){
        this.listener = listener;
    }
}
