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
import com.example.hospitalmanagement.Users.DailyReport;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CeoActivity extends AppCompatActivity {

    ListView list;
    FirebaseDatabase database;
    DatabaseReference reference;
    ArrayList<String> DrList;
    ArrayAdapter<String> adapter;
    DailyReport dailyReport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ceo);
        list = (ListView) findViewById(R.id.list1);
        database = FirebaseDatabase.getInstance();
        dailyReport = new DailyReport();
        reference = database.getReference().child("DailyReport");
        DrList = new ArrayList<>();
        list.setAdapter(adapter);
        adapter = new ArrayAdapter<>(CeoActivity.this, R.layout.appointmentlist, R.id.tvAppointmentList, DrList);


        DisplayReport();

    }

// Display the daily report which includes doctor's performance of the day

    public void DisplayReport(){
        Calendar c = Calendar.getInstance();
        int hrs = c.get(Calendar.HOUR);
        int mnts = c.get(Calendar.MINUTE);
        final String Currentdate = DateFormat.getDateInstance().format(c.getTime());
        hrs = 10;
       mnts = 10;
        if((hrs>=9&&(mnts>0))){  // comparing if the current time past 9
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot item : snapshot.getChildren()) {
                        dailyReport = item.getValue(DailyReport.class);
                        if(dailyReport.getDate().equals(Currentdate)){
                        DrList.add(dailyReport.getDrName()+
                                "\nNo of patient:"+dailyReport.getTotalPatientVisitToday()+ "\nAmount Earned:"+dailyReport.getTotal_Amount()) ;
                    }
                    }
                    list.setAdapter(adapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        else{
            Toast.makeText(this, "The system will display the today's report only " +
                    "after 09:00 Pm", Toast.LENGTH_SHORT).show();
        }
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
                Intent intent = new Intent(CeoActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}