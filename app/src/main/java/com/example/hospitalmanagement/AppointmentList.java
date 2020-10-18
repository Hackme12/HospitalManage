package com.example.hospitalmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.hospitalmanagement.Users.AppointmentInformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AppointmentList extends AppCompatActivity {


    ListView listView;
    FirebaseDatabase database;
    DatabaseReference reference;
    ArrayList<String>list;
    ArrayAdapter<String> adapter;
    AppointmentInformation AppInform;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_list);
        AppInform = new AppointmentInformation();



        listView = (ListView) findViewById(R.id.ListView);
        database= FirebaseDatabase.getInstance();
        reference = database.getReference().child("AppointmentList");
        list = new ArrayList<>();
        adapter = new ArrayAdapter<>(AppointmentList.this,R.layout.appointmentlist,
                R.id.tvAppointmentList,list);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds: snapshot.getChildren())
                {
                    AppInform = ds.getValue(AppointmentInformation.class);
                    list.add(AppInform.getPatientName() +"     "   +AppInform.time );

                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}