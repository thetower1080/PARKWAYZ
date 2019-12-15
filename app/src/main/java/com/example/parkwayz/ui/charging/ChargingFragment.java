package com.example.parkwayz.ui.charging;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.parkwayz.R;

public class ChargingFragment extends Fragment {

    private ChargingViewModel chargingViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        chargingViewModel =
                ViewModelProviders.of(this).get(ChargingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_charging, container, false);
        final TextView textView = root.findViewById(R.id.text_charging);
        chargingViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}