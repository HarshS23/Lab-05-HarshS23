package com.example.lab5_starter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class CityDialogFragment extends DialogFragment {
    interface CityDialogListener {
        void updateCity(City city, String title, String year);
        void addCity(City city);
        void deleteCity(City city);
    }
    private CityDialogListener listener;

    public static CityDialogFragment newInstance(City city){
        Bundle args = new Bundle();
        args.putSerializable("City", city);

        CityDialogFragment fragment = new CityDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CityDialogListener){
            listener = (CityDialogListener) context;
        }
        else {
            throw new RuntimeException("Implement listener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_city_details, null);
        EditText editMovieName = view.findViewById(R.id.edit_city_name);
        EditText editMovieYear = view.findViewById(R.id.edit_province);

        String tag = getTag();
        Bundle bundle = getArguments();
        City city = null;

        if(bundle != null){
            city = (City) bundle.getSerializable("City");
            if(city != null){
                editMovieName.setText(city.getName());
                editMovieYear.setText(city.getProvince());
            }
        }

        final City finalCity = city;



        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder
                .setView(view)
                .setTitle("City Details")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Continue", (dialog, which) -> {
                    String title = editMovieName.getText().toString();
                    String year = editMovieYear.getText().toString();

                    if (Objects.equals(tag, "City Details")) {
                        listener.updateCity(finalCity, title, year);
                    }else {
                        listener.addCity(new City(title, year));
                    }
                });

        if(Objects.equals(tag,"City Details") && (city != null)){
            builder.setNeutralButton("Delete", ((dialogInterface, i) -> {
                listener.deleteCity(finalCity);
            }));
        }



        return builder.create();
    }
}
