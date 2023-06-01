package com.westframework.waterbillingapp.data.application;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.westframework.waterbillingapp.R;
import com.westframework.waterbillingapp.data.bill.WaterBill;

import java.util.ArrayList;
import java.util.List;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ApplicationHolder> {

    public List<Application> applications = new ArrayList<>();
    private List<Application> applicationsFiltered;
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ApplicationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.application_item, parent, false);
        return  new ApplicationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationHolder holder, int position){
        Application currentApplication = applications.get(position);

        String name = currentApplication.getFirstName() + " " + currentApplication.getMiddleInitial() + " " + currentApplication.getLastName();
        String title = currentApplication.getApplicationNo();
        String address = currentApplication.getHouseNo() + " | (" + currentApplication.getMeterNo() + ") | " + currentApplication.getBuildingNo() + " " +
                currentApplication.getStreet() + " " + currentApplication.getBrgy();

        holder.textViewTitle.setText(title);
        holder.textViewDescription.setText(name + " - " + address);
    }

    @Override
    public int getItemCount() {
        if (applications == null){
            return 0;
        }
        return applications.size();
    }

    public void setApplications(List<Application> applications){
        this.applications = applications;
        this.applicationsFiltered = applications;
        notifyDataSetChanged();
    }

    public Application getApplications(int position){
        return applications.get(position);
    }

    class ApplicationHolder extends RecyclerView.ViewHolder{
        private TextView textViewTitle;
        private TextView textViewDescription;

        public ApplicationHolder(@NonNull View itemView){
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION)
                    {
                        listener.onItemClick(applications.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Application application);
    }

    public void setOnItemClickListerner(OnItemClickListener listener){
        this.listener = listener;
    }

    public Filter getFilter(int mode){
      return new Filter() {
          @Override
          protected FilterResults performFiltering(CharSequence charSequence) {
              FilterResults filterResults = new FilterResults();

              if (charSequence == null || charSequence.length() == 0) {
                  if(applicationsFiltered == null){
                      filterResults.count = 0;
                  }else{
                      filterResults.count = applicationsFiltered.size();
                  }
                  filterResults.values = applicationsFiltered;
              }else{
                  String searchChr = charSequence.toString().toLowerCase();
                  List<Application> resultData = new ArrayList<>();

                  for (Application application: applicationsFiltered){

                      String fullName1 = application.getFirstName().toLowerCase() + " " + application.getLastName().toLowerCase();
                      String fullName2 = application.getFirstName().toLowerCase() + " " + application.getMiddleInitial().toLowerCase() + " " + application.getLastName().toLowerCase();

                      switch (mode){
                          case 1:
                              if(searchChr.equals(application.getMeterNo())){
                                  resultData.add(application); }
                              break;
                          case 2:
                              if(application.getApplicationNo().contains(searchChr)){
                                  resultData.add(application); }
                              break;
                          case 3:
                              if (application.getFirstName().toLowerCase().contains(searchChr) ||
                                  application.getLastName().toLowerCase().contains(searchChr) ||
                                  application.getMiddleInitial().toLowerCase().contains(searchChr) ||
                                  fullName1.contains(searchChr) || fullName2.contains(searchChr))
                              { resultData.add(application); }
                      }
                  }
                  filterResults.count = resultData.size();
                  filterResults.values = resultData;
              }
              return filterResults;
          }

          @Override
          protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            applications = (List<Application>) filterResults.values;
            notifyDataSetChanged();
          }
      };
    }
}