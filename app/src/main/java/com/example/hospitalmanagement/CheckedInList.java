package com.example.hospitalmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.hospitalmanagement.Users.AppointmentInformation;
import com.example.hospitalmanagement.Users.checkList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CheckedInList extends AppCompatActivity {

    ListView listView;

    FirebaseDatabase database;
    DatabaseReference reference;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    checkList objCheckList;
    private ProgressDialog loadingBar;
    int hr, min, sec;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checked_in_list);
        objCheckList = new checkList();

        listView = (ListView) findViewById(R.id.list2);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("CheckedInList");
        list = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        final String Currentdate = DateFormat.getDateInstance().format(c.getTime());

        DateFormat date = new SimpleDateFormat("HH:mm:ss");
        Date dt = new Date();
        String currentTime = date.format(dt);
        hr = Integer.parseInt(currentTime.substring(0,2));
        min = Integer.parseInt(currentTime.substring(3,5));
        sec = Integer.parseInt(currentTime.substring(6,8));

        adapter = new ArrayAdapter<>(CheckedInList.this, R.layout.appointmentlist,
                R.id.tvAppointmentList, list);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot ds : snapshot.getChildren()) {
                    objCheckList = ds.getValue(checkList.class);
                   if(objCheckList.getDate().equals(Currentdate)&&((hr >= 20)&& hr<=23)){
                        reference.removeValue();
                    }
                        list.add(objCheckList.getPatientId() + "\n"+objCheckList.getDate() +
                                "\n"+ objCheckList.getStatus());

                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}