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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.lang.Math;
public class Payment extends AppCompatActivity {

    private EditText patientId,cardNumber,expDate,amount;
    private Button btnPayNow;;
    private FirebaseDatabase database;
    private long theRandomNum;
    private Boolean checkReferenceNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        database = FirebaseDatabase.getInstance();

        patientId = (EditText)findViewById(R.id.edPatientId);
        cardNumber = (EditText)findViewById(R.id.edCardNumber);
        expDate = (EditText)findViewById(R.id.edExpiredate);
        amount = (EditText)findViewById(R.id.edPaymentAmount);
        checkReferenceNumber = false;
        theRandomNum = 0;

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
                    Bank(CardNumber, expdate, PayAmnt);
                    if(checkReferenceNumber){
                        Payment(PateintID,PayAmnt);
                    }
                    else{
                        Toast.makeText(Payment.this, "CARD AUTHORIZXATION FAILED \n PLEASE TRY ANOTEHR CARD",Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });




    }

    private void Payment(final String patientID, final String PayAmnt) {

        DatabaseReference reference = database.getReference();

        final Calendar c = Calendar.getInstance();
        final String cdate = DateFormat.getDateInstance().format(c.getTime());


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DatabaseReference myref1 = database.getReference();
                if(!snapshot.child("Payment").child(patientID).child(cdate).exists()){
                    HashMap<String, Object> payment_Profile = new HashMap<>();
                    payment_Profile.put("PatientId",patientID);
                    payment_Profile.put("Amount",PayAmnt );
                    payment_Profile.put("Date",cdate);
                    payment_Profile.put("Payment Type","CARD");
                    payment_Profile.put("Reference Number",theRandomNum);


                   myref1.child("Payment").child(patientID).child(cdate).updateChildren(payment_Profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                               System.out.println("Succesfully update");
                               Intent intent = new Intent(Payment.this,StaffActivity.class);
                               startActivity(intent);
                           }
                           else{
                               System.out.println("failed to update payment");
                           }
                       }
                   });

                }
                else{
                    myref1.child("Payment").child(patientID).
                            child(cdate).child("Other Payment").setValue(PayAmnt).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                System.out.println("Succesfully update");
                                Intent intent = new Intent(Payment.this,StaffActivity.class);
                                startActivity(intent);
                            }
                            else{
                                System.out.println("failed to update payment");
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void Bank(String CardNumber, String expDate, String PayAmnt){

         if(TextUtils.isEmpty(CardNumber)||(CardNumber.length()!=16)){
            Toast.makeText(Payment.this, "Please Enter Your Card Number ", Toast.LENGTH_SHORT).show();

        }
        else  if(TextUtils.isEmpty(expDate)){
            Toast.makeText(Payment.this, "Please Enter Expiration date", Toast.LENGTH_SHORT).show();
        }
        else  if(TextUtils.isEmpty(PayAmnt)){
            Toast.makeText(Payment.this, "Please Enter Payment Amount", Toast.LENGTH_SHORT).show();
        }
        else{

            theRandomNum = (long) (Math.random() * Math.pow(10, 10));
            checkReferenceNumber = true;
        }

    }
}