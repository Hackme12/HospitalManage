package com.example.hospitalmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hospitalmanagement.Users.AppointmentInformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.time.Year;
import java.util.Calendar;

public class CancelAppointment extends AppCompatActivity {


    private EditText edPatientId,edPatientName,edAppointment;
    private Button btn_next;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_appointment);

        edPatientId = (EditText)findViewById(R.id.edPatientId);
        edPatientName = (EditText)findViewById(R.id.edPatientName);
        edAppointment= (EditText)findViewById(R.id.edAppointmentdate);

        btn_next = (Button)findViewById(R.id.btn_next);
        loadingBar = new ProgressDialog(this);





        edAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar c = Calendar.getInstance();
                int YEAR = c.get(Calendar.YEAR);
                int MONTH = c.get(Calendar.MONTH);
                int DAY_0F_MONTH = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(CancelAppointment.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.DAY_OF_MONTH, day);

                        String selected_date = DateFormat.getDateInstance().format(c.getTime());
                        edAppointment.setText(selected_date);


                    }
                }, YEAR, MONTH, DAY_0F_MONTH);
                datePickerDialog.show();
            }

    });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String PatientId = edPatientId.getText().toString();
                String PatientName= edPatientName.getText().toString();
                String AppointmentDate = edAppointment.getText().toString();
                if (TextUtils.isEmpty(PatientId)) {
                    Toast.makeText(CancelAppointment.this, "Please Enter Patient Id", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(PatientName)) {
                    Toast.makeText(CancelAppointment.this, "Please Enter Full Name", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(AppointmentDate)) {
                    Toast.makeText(CancelAppointment.this, "Please Enter Full Name", Toast.LENGTH_SHORT).show();
                }
                else {

                    loadingBar.setTitle("Message");
                    loadingBar.setMessage("Please wait while checking your credentials");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    deletePatientAppointment(PatientId,AppointmentDate);

                }





            }
        });








    }

    private void deletePatientAppointment(final String patientId, final String appointmentDate) {


        final String key = patientId +":"+appointmentDate;

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myref = database.getReference();

        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


              if(snapshot.child("AppointmentList").child(key).exists()){



                  loadingBar.dismiss();
                   //AppointmentInformation API = snapshot.child("AppointmentList").child(key).getValue(AppointmentInformation.class);
                   DatabaseReference drKey = database.getReference("AppointmentList").child(key);
                    drKey.removeValue();
                    Toast.makeText(CancelAppointment.this, "Thank you", Toast.LENGTH_SHORT).show();

               }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }


    public void DialogBox(String Key){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);





    }
}