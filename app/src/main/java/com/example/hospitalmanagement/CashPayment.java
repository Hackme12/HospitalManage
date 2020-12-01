package com.example.hospitalmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLOutput;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CashPayment extends AppCompatActivity {

    private EditText patientId;
    private EditText cashAmount;
    private Button btnPayCash;
    FirebaseDatabase database;
    DatabaseReference reference;
    Payment objPayment;
    CancelAppointment  objCancelAppointment;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_payment);

        patientId = (EditText)findViewById(R.id.edCashPatientId);
        cashAmount =(EditText)findViewById(R.id.edCashAmount);
        btnPayCash = (Button) findViewById(R.id.btn_payment);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        objPayment = new Payment();
        objCancelAppointment = new CancelAppointment();
        Intent intent = getIntent();
        name = intent.getStringExtra("Name");


        btnPayCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pID = patientId.getText().toString();
                String cAmount = cashAmount.getText().toString();
                if(TextUtils.isEmpty(pID))
                {
                    Toast.makeText(CashPayment.this,"Please Enter PatientID!",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(cAmount)){
                    Toast.makeText(CashPayment.this,"Please enter an amount to charge",Toast.LENGTH_SHORT).show();
                }
                else {
                    cashPayment(pID,cAmount);
                }


            }
        });



    }

    private void cashPayment(final String pID, final String cAmount) {
        final Calendar c = Calendar.getInstance();
        final String cdate = DateFormat.getDateInstance().format(c.getTime());


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!snapshot.child("Payment").child(pID).child(cdate).exists()){
                    System.out.println("*******************************************************************");
                    //objCancelAppointment.deletePatientAppointment(pID,cdate);
                    objPayment.addToCheckedInList(pID,name,cAmount,cdate);
                    System.out.println("*******************************************************************");
                    HashMap<String, Object> payment_Profile = new HashMap<>();
                    payment_Profile.put("PatientId",pID);
                    payment_Profile.put("Name",name);
                    payment_Profile.put("Payment_Amount",cAmount );
                    payment_Profile.put("Payment_Date",cdate);
                    payment_Profile.put("Payment Type","CASH");

                    String key = pID+":"+ cdate;
                    DatabaseReference removeKey = database.getReference("AppointmentList").child(key);
                    removeKey.removeValue();

                    reference.child("Payment").child(pID).child(cdate).updateChildren(payment_Profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                System.out.println("Succesfully update");
                                Intent intent = new Intent(CashPayment.this,StaffActivity.class);
                                startActivity(intent);
                            }
                            else{
                                System.out.println("failed to update payment");
                            }
                        }
                    });

                }
                else{
                    String key = pID+":"+ cdate;
                    DatabaseReference removeKey = database.getReference("AppointmentList").child(key);
                    removeKey.removeValue();
                    objPayment.addToCheckedInList(pID,name,cAmount,cdate);

                    reference.child("Payment").child(pID).
                            child(cdate).child("Other Payment").setValue(cAmount);
                    Intent intent = new Intent(CashPayment.this,StaffActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




    }
