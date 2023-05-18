package com.example.licenta.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.licenta.R;
import com.example.licenta.RecyclerViewAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private ArrayList<Integer> integerArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}