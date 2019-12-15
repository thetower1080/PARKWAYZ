package com.example.parkwayz.ui.logout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.MenuItem;



import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.parkwayz.LoginActivity;
import com.example.parkwayz.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogoutFragment extends Fragment {

//    private LogoutViewModel logoutViewModel;
//
//    private MenuItem item;



//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        logoutViewModel =
//                ViewModelProviders.of(this).get(LogoutViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_logout, container, false);
//        final TextView textView = root.findViewById(R.id.text_send);
//        logoutViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            FirebaseAuth.getInstance().signOut();
            Intent loginActivity = new Intent(getActivity(), LoginActivity.class);
            startActivity(loginActivity);
            getActivity().finish();



//        return root;
    }
}