package com.example.hospitalmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PaymentType extends AppCompatActivity {

    private Button btnCash;
    private Button btnCreditDebit;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_type);

        btnCash = (Button)findViewById(R.id.btnCash);
        btnCreditDebit =(Button) findViewById(R.id.btnCreditDebit);
        getName();

       btnCash.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               System.out.println("*************************"+name);
               Intent intent = new Intent(PaymentType.this, CashPayment.class);
               intent.putExtra("Name",name);
               startActivity(intent);
           }
       });
       btnCreditDebit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(PaymentType.this, Payment.class);
               intent.putExtra("patName",name);
               startActivity(intent);
           }
       });


    }
    public void getName(){
        Intent intent = getIntent();
        name = intent.getStringExtra("tempName");
        System.out.println("*************************"+name);
    }
}