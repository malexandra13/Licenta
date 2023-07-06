package com.example.licenta.owner.others;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.R;

import java.util.ArrayList;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
    private List<Service> serviceList;

    public ServiceAdapter() {
        serviceList = new ArrayList<>();
    }

    public void addService(Service service) {
        serviceList.add(service);
    }

    public void clear() {
        serviceList.clear();
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_services, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = serviceList.get(position);
        holder.bind(service);
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    static class ServiceViewHolder extends RecyclerView.ViewHolder {
        private TextView serviceNameTextView;
        private TextView serviceDepartmentTextView;
        private TextView serviceDescriptionTextView;
        private TextView priceTextView;

        ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceNameTextView = itemView.findViewById(R.id.tvServiceName);
            serviceDepartmentTextView = itemView.findViewById(R.id.tvServiceDepartment);
            serviceDescriptionTextView = itemView.findViewById(R.id.tvServiceDescription);
            priceTextView = itemView.findViewById(R.id.tvPrice);
        }

        void bind(Service service) {
            serviceNameTextView.setText(service.getServiceName());
            priceTextView.setText(String.valueOf(service.getServicePrice()));
            serviceDepartmentTextView.setText(service.getServiceDepartment());
            serviceDescriptionTextView.setText(service.getServiceDescription());
        }
    }
}