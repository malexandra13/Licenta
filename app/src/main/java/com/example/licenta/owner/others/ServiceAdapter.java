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
    private OnItemClickListener listener;

    public ServiceAdapter() {
        serviceList = new ArrayList<>();
    }

    public void addService(Service service) {
        serviceList.add(service);
        notifyDataSetChanged();
    }

    public void clear() {
        serviceList.clear();
        notifyDataSetChanged();
    }

    public Service getService(int position) {
        return serviceList.get(position);
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

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class ServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

            itemView.setOnClickListener(this);
        }

        void bind(Service service) {
            serviceNameTextView.setText(service.getServiceName());
            priceTextView.setText(String.valueOf(service.getServicePrice()));
            serviceDepartmentTextView.setText(service.getServiceDepartment());
            serviceDescriptionTextView.setText(service.getServiceDescription());
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position);
                }
            }
        }
    }
}
