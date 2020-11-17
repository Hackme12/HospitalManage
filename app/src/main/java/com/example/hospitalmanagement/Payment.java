package com.example.hospitalmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;

public class Payment extends AppCompatActivity {

    private EditText patientId,cardNumber,expDate,amount;
    private Button btnPayNow;;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        database = FirebaseDatabase.getInstance();
        patientId = (EditText)findViewById(R.id.edPatientId);
        cardNumber = (EditText)findViewById(R.id.edCardNumber);
        expDate = (EditText)findViewById(R.id.edExpiredate);
        amount = (EditText)findViewById(R.id.edPaymentAmount);

        btnPayNow = (Button)findViewById(R.id.btn_payment);
        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String PateintID = patientId.getText().toString();
                String CardNumber= cardNumber.getText().toString();
                String expdate= expDate.getText().toString();
                String PayAmnt= amount.getText().toString();

                if(TextUtils.isEmpty(PateintID)){
                    Toast.makeText(Payment.this, "Please Enter Patient Id", Toast.LENGTH_SHORT).show();
                }
                else  if(TextUtils.isEmpty(CardNumber)||(CardNumber.length()!=16)){
                    Toast.makeText(Payment.this, "Please Enter Your Card Number ", Toast.LENGTH_SHORT).show();
                }
                else  if(TextUtils.isEmpty(expdate)){
                    Toast.makeText(Payment.this, "Please Enter Expiration date", Toast.LENGTH_SHORT).show();
                }
                else  if(TextUtils.isEmpty(PayAmnt)){
                    Toast.makeText(Payment.this, "Please Enter Payment Amount", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Payment.this,"Authorization of card",Toast.LENGTH_LONG).show();
                    Payment(PateintID,PayAmnt);

                }

            }
        });




    }

    private void Payment(final String patientID, final String PayAmnt) {

        DatabaseReference reference = database.getReference();

        final Calendar c = Calendar.getInstance();
        String cdate = DateFormat.getDateInstance().format(c.getTime());
        final String key = patientID + ":" + cdate;

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("AppointmentList").child(key).exists()){
                    DatabaseReference myref1 = database.getReference();
                    myref1.child("Patient").child(patientID).child("Payment Profile").
                            child("Payment Amount").setValue(PayAmnt);
                    Toast.makeText(Payment.this, "Payment Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Payment.this,StaffActivity.class);
                    startActivity(intent);

                }
                else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}