package com.example.hospitalmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitalmanagement.Users.AppointmentInformation;
import com.example.hospitalmanagement.Users.PatientInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AppointmentList extends AppCompatActivity {


    ListView listView;

    FirebaseDatabase database;
    DatabaseReference reference;
    private EditText PId, PName;
    private String pid,pname;
    ArrayList<String>list;
    ArrayAdapter<String> adapter;
    ArrayList<String>secondList;
   private ProgressDialog loadingBar;
    AppointmentInformation AppInform;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_list);
        AppInform = new AppointmentInformation();

        listView = (ListView) findViewById(R.id.list1);
        database= FirebaseDatabase.getInstance();
        reference = database.getReference().child("AppointmentList");
        loadingBar = new ProgressDialog(this);
        list = new ArrayList<>();
        secondList  = new ArrayList<>();

        adapter = new ArrayAdapter<>(AppointmentList.this,R.layout.appointmentlist,
                R.id.tvAppointmentList,list);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds: snapshot.getChildren())
                {
                    AppInform = ds.getValue(AppointmentInformation.class);
                    list.add("  " +AppInform.getPatientName() +"               " +
                            AppInform.getDate()+"\n                                           " + AppInform.getTime());

                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DialogCheckIn();


            }
        });



    }


        private void DialogCheckIn() {

            AlertDialog.Builder builder = new AlertDialog.Builder(AppointmentList.this);
            View view = LayoutInflater.from(AppointmentList.this).inflate(R.layout.dialogupdatepatientinfo, null);
            builder.setView(view);

            PId = view.findViewById(R.id.id_patient);
            PName = view.findViewById(R.id.id_fullName);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    pid = PId.getText().toString().trim();
                    pname = PName.getText().toString().trim();


                    if (TextUtils.isEmpty(pid)) {
                        Toast.makeText(AppointmentList.this, "Please Enter Patient Id", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(pname)) {
                        Toast.makeText(AppointmentList.this, "Please Enter Full Name", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        loadingBar.setTitle("Message");
                        loadingBar.setMessage("Please wait while checking your credentials");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();
                        ValidatePatientAppointment(pid, pname);

                    }
                }
            });

            builder.setNegativeButton("Cancel                                             ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();


        }

        private void ValidatePatientAppointment(final String pId,final String pName) {


            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();
            Date date = new Date();
            Calendar c = Calendar.getInstance();


            String selected_date = DateFormat.getDateInstance().format(c.getTime());
            final String key = pId+":"+selected_date;

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                    if (datasnapshot.child("Patient").child(pId).exists()&&(datasnapshot.child("AppointmentList").child(key).exists())) {
                        PatientInfo cPatient = datasnapshot.child("Patient").child(pId).child("Personal Information").getValue(PatientInfo.class);

                        if ((pId.equals(cPatient.getPatientID())) && (pName.equals(cPatient.getName()))
                        ) {

                            loadingBar.dismiss();
                            Intent intent = new Intent(AppointmentList.this, CheckInActivity.class);
                            intent.putExtra("id",cPatient.getPatientID());
                            intent.putExtra("pname",cPatient.getName());
                            intent.putExtra("paddress",cPatient.getAddress());
                            intent.putExtra("pphone",cPatient.getPhoneNumber());
                            intent.putExtra("pdateofbirth",cPatient.getDateOfBirth());
                            intent.putExtra("pemail",cPatient.getEmailId());
                            startActivity(intent);
                        }


                    } else
                    {
                        loadingBar.dismiss();
                        Toast.makeText(AppointmentList.this, "Error!!! Please Check provided credentials again. \n Or Patient may " +
                                "have an appointment for another day", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




        }






}