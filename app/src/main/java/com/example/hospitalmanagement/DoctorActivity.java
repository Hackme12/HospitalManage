package com.example.hospitalmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hospitalmanagement.Users.AppointmentInformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DoctorActivity extends AppCompatActivity {


    ListView list;
    FirebaseDatabase database;
    DatabaseReference reference;
    ArrayList<String> patientList;
    ArrayAdapter<String> adapter;
    AppointmentInformation APPI;
    String PatientId;

    String drId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);


        list = (ListView) findViewById(R.id.list1);
        database = FirebaseDatabase.getInstance();
        APPI = new AppointmentInformation();
        reference = database.getReference().child("AppointmentList");

        patientList = new ArrayList<>();
        adapter = new ArrayAdapter<>(DoctorActivity.this, R.layout.appointmentlist, R.id.tvAppointmentList, patientList);
        Calendar c = Calendar.getInstance();
        final String CurrentDate = DateFormat.getDateInstance().format(c.getTime());

        Intent intent = getIntent();
        drId = intent.getStringExtra("Doctor Name");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {
                    APPI = item.getValue(AppointmentInformation.class);
                   // System.out.println("        *************"+APPI.getDate() +"date" +CurrentDate  + "drId"+drId   +"  "+APPI.getDoctorName());
                    if (APPI.getDate().equals(CurrentDate) && (APPI.getDoctorName().equals(drId)))
                    {
                        patientList.add(" " + APPI.getPatientName());


                    } else {
                        Toast.makeText(DoctorActivity.this, "There is no patient today", Toast.LENGTH_SHORT).show();
                    }


                }
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected_patient = list.getItemAtPosition(i).toString().trim();
                view.setBackgroundColor(Color.BLUE);
                gettingDetailOfPatient(selected_patient);

            }
        });


    }

    private void gettingDetailOfPatient(final String selected_patient) {
        DatabaseReference myref = database.getReference().child("AppointmentList");
        Calendar c = Calendar.getInstance();
        final String CDate = DateFormat.getDateInstance().format(c.getTime());

        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item: snapshot.getChildren()){
                    APPI = item.getValue(AppointmentInformation.class);
                    if(APPI.getDoctorName().equals(drId)&&
                            (APPI.getPatientName().equals(selected_patient)&&APPI.getDate().equals(CDate))){
                        PatientId = APPI.patientId;
                        break;
                    }
                }
                Intent intent = new Intent (DoctorActivity.this,PatientChartWithPrescription.class);
                intent.putExtra("PatientId",PatientId);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case (R.id.Logout):
                Intent intent = new Intent(DoctorActivity.this, MainActivity.class);
                startActivity(intent);

                break;

            default:
                break;


        }
        return true;


    }
}