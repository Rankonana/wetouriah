package com.example.wetouriah;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;

public class AddDriversLicense extends AppCompatActivity {

    EditText license_owner, fullname  , identity_number , date_of_birth , license_number , expiry_date , country_of_issue , code , restrictions , gender ,
            date_of_issue,
            uploadLicense;
     CheckBox is_approved ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drivers_license);

        license_owner = findViewById(R.id.license_owner);
        fullname   = findViewById(R.id.fullname);
        identity_number  = findViewById(R.id.identity_number);
        date_of_birth  = findViewById(R.id.date_of_birth);
        license_number  = findViewById(R.id.license_number);
        expiry_date  = findViewById(R.id.expiry_date);
        country_of_issue  = findViewById(R.id.country_of_issue);
        code  = findViewById(R.id.code);
        restrictions  = findViewById(R.id.restrictions);
        gender  = findViewById(R.id.gender);
        date_of_issue = findViewById(R.id.date_of_issue);
        uploadLicense = findViewById(R.id.uploadLicense);
        is_approved = findViewById(R.id.is_approved);
    }
}