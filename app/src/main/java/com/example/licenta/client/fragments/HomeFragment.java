package com.example.licenta.client.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.licenta.client.others.OnFragmentChangedListener;
import com.example.licenta.client.others.RecyclerViewAdapter;
import com.example.licenta.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private OnFragmentChangedListener fragmentChangedListener;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private ArrayList<Integer> integerArrayList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragmentChangedListener = (OnFragmentChangedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnFragmentChangedListener");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageSlider imageSlider = view.findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.logo, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.logo2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.logo3, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        recyclerView = view.findViewById(R.id.recyclerView);

        integerArrayList = new ArrayList<>();
        integerArrayList.add(R.drawable.unghii);
        integerArrayList.add(R.drawable.make_up);
        integerArrayList.add(R.drawable.barbat_tunsoare);
        integerArrayList.add(R.drawable.coafura);
        integerArrayList.add(R.drawable.unghii2);
        integerArrayList.add(R.drawable.barba);
        integerArrayList.add(R.drawable.make_up2);

        recyclerViewAdapter = new RecyclerViewAdapter(getContext(), integerArrayList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(recyclerViewAdapter);

//        Button buttonBooking = view.findViewById(R.id.buttonBooking);
//        buttonBooking.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                BookingFragment bookingFragment = new BookingFragment();
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.navHostFragment, bookingFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//                fragmentChangedListener.onFragmentChanged("Booking Fragment");
//            }
//        });

        Button buttonChooseService = view.findViewById(R.id.buttonBooking);
        buttonChooseService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseServiceFragment chooseServiceFragment = new ChooseServiceFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.navHostFragment, chooseServiceFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                fragmentChangedListener.onFragmentChanged("Choose Service Fragment");
            }
        });

        Button buttonHistory = view.findViewById(R.id.buttonHistory);
        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryFragment historyFragment = new HistoryFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.navHostFragment, historyFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                fragmentChangedListener.onFragmentChanged("History Fragment");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}