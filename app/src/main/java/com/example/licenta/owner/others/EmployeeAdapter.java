package com.example.licenta.owner.others;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {
    private List<Employee> employeeList;
    private Map<String, Salon> salonMap;

    public EmployeeAdapter(List<Employee> employeeList, List<Salon> salonList) {
        this.employeeList = employeeList;
        this.salonMap = convertSalonListToMap(salonList);
    }

    private Map<String, Salon> convertSalonListToMap(List<Salon> salonList) {
        Map<String, Salon> salonMap = new HashMap<>();
        for (Salon salon : salonList) {
            salonMap.put(salon.getSalonId(), salon);
        }
        return salonMap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Employee employee = employeeList.get(position);
        holder.tvNrEmployee.setText(String.valueOf(position + 1));
        holder.tvEmployeeName.setText(employee.getFirstName() + " " + employee.getLastName());
        holder.tvPhoneNumber.setText(employee.getPhoneNumber());
        holder.tvEmployeeDepartment.setText(employee.getDepartment());

        String salonId = employee.getSalonId();
        Salon salon = salonMap.get(salonId);

        if (salon != null) {
            holder.tvSalonName.setText(salon.getSalonName());
        }
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmployeeName;
        TextView tvPhoneNumber;
        TextView tvEmployeeDepartment;
        TextView tvNrEmployee;
        TextView tvSalonName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvEmployeeName = itemView.findViewById(R.id.tvEmployeeName);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhone);
            tvEmployeeDepartment = itemView.findViewById(R.id.tvDepartment);
            tvNrEmployee = itemView.findViewById(R.id.number);
            tvSalonName = itemView.findViewById(R.id.tvSalonName);

        }
    }
}
