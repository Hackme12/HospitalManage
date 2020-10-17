package com.example.hospitalmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class StaffActivity extends AppCompatActivity {

    private ImageView CheckIn, Appointment, AddPatient, UpdatePatientInfo, PayNow, Appointment_List;
    private TextView  textCheckIn, textAppointment, textAddPatient;
    private TextView  textUpdate, textPay, textAppointmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        CheckIn = (ImageView) findViewById(R.id.staff_checkIn);
        Appointment =(ImageView) findViewById(R.id.image_Appointment);
        AddPatient = (ImageView) findViewById(R.id.image_addPatient);
        UpdatePatientInfo = (ImageView) findViewById(R.id.image_updatePatientInfo);
        PayNow = (ImageView) findViewById(R.id.image_payment);
        Appointment_List = (ImageView) findViewById(R.id.image_AppointmentList);


        CheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StaffActivity.this, Appointment.class);
                startActivity(intent);

            }
        });
        AddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StaffActivity.this, AddPatient.class);
                startActivity(intent);
            }
        });







    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case (R.id.Logout):
                Intent intent = new Intent(StaffActivity.this,MainActivity.class);
                startActivity(intent);

                break;

            default:
                break;

        }
        return true;





    }
}