package com.isproject.ey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.EventLog;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PaquetesActivity extends AppCompatActivity implements View.OnClickListener{
    Button btNext, btCancel;
    RadioGroup rgPqt;
    RadioButton rbP1, rbP2, rbP3, rbP4;
    //Fire
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    int pqt;
    Boolean usuR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paquetes);
        pqt = getIntent().getIntExtra("pqt",9);
        usuR = getIntent().getBooleanExtra("usuR", false);
        iniciarFB();
        iniciarEl();
        btNext = findViewById(R.id.bt_next_p);
        btNext.setOnClickListener(this);
        btCancel = findViewById(R.id.bt_back_p);
        btCancel.setOnClickListener(this);
    }
    private void iniciarEl(){
        rgPqt = findViewById(R.id.rdg_pqt_p);
        rbP1 = findViewById(R.id.rb_pqt1_p);
        rbP2 = findViewById(R.id.rb_pqt2_p);
        rbP3 = findViewById(R.id.rb_pqt3_p);
        rbP4 = findViewById(R.id.rb_pqt4_p);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_next_p:
                try {
                    int rbt=0;
                    if(rbP1.isChecked()){
                        rbt = 1;
                    }else if (rbP2.isChecked()){
                        rbt = 2;
                    }else if (rbP3.isChecked()){
                        rbt = 3;
                    }else if (rbP4.isChecked()){
                        rbt = 4;
                    }

                    Intent intent = new Intent(PaquetesActivity.this, PayActivity.class);
                    intent.putExtra("pqt",rbt);
                    intent.putExtra("usuR", usuR);
                    startActivity(intent);
                    finish();
                }catch (Exception e){
                    Toast.makeText(this, "Elige un paquete", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.bt_back_p:
                if(usuR){
                    Intent intent = new Intent(PaquetesActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(this, "Adición Cancelada", Toast.LENGTH_SHORT ).show();
                }else{
                    try {
                        String id = mAuth.getCurrentUser().getUid().toString();
                        mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                databaseReference.child("Usuario").child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(PaquetesActivity.this, "Registro Cancelado", Toast.LENGTH_SHORT ).show();
                                        Intent intent = new Intent(PaquetesActivity.this, HomeActivity.class);
                                        finish();
                                    }
                                });
                            }
                        });
                    }catch (Exception e){
                        Toast.makeText(this, "E: "+e.getMessage(), Toast.LENGTH_SHORT ).show();
                    }
                }

                break;
        }
    }
    private void iniciarFB()
    {
        try
        {
            FirebaseApp.initializeApp(this);
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
            mAuth = FirebaseAuth.getInstance();
        }catch(Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}