package com.example.parkwayz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ProfileActivity extends AppCompatActivity {

    //add user info from database to this array for displaying data in a list, after the payment screen is done
    String[] userPaymentHistory = new String[]{

            "Payment 1:  $20  11/04/19",
            "Payment 2:  $50  11/25/19"


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ListView paymentHistory = (ListView)findViewById(R.id.paymentHistoryList);

        ArrayAdapter adapter = new ArrayAdapter(ProfileActivity.this, android.R.layout.simple_list_item_1,userPaymentHistory);
        paymentHistory.setAdapter(adapter);
    }
}
