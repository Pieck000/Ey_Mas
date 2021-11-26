package com.isproject.ey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PayActivity extends AppCompatActivity implements View.OnClickListener {
    Button btNext, btBack;
    EditText etCorr, etPass;
    int pqt;
    boolean usuR;
    //Fire
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

       pqt = getIntent().getIntExtra("pqt",9);
       usuR = getIntent().getBooleanExtra("usuR", false);
       iniciarFB();

        btNext = findViewById(R.id.bt_next_pay);
        btNext.setOnClickListener(this);
        btBack = findViewById(R.id.bt_back_pay);
        btBack.setOnClickListener(this);

        etCorr = findViewById(R.id.edit_correo_pay);
        etPass = findViewById(R.id.edit_password_pay);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_next_pay:
                if(mValidar()){
                    Intent intent = new Intent(PayActivity.this, EventoActivity.class);
                    intent.putExtra("pqt",pqt);
                    startActivity(intent);
                    finish();
                    Toast.makeText(this, ""+pqt, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.bt_back_pay:
                if(usuR){
                    Intent intent = new Intent(PayActivity.this, HomeActivity.class);
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
                                        Toast.makeText(PayActivity.this, "Registro Cancelado", Toast.LENGTH_SHORT ).show();
                                        Intent intent = new Intent(PayActivity.this, HomeActivity.class);
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
    private boolean mValidar(){
        if(etPass.getText().toString().equals("")||etCorr.getText().toString().equals("")){
            Toast.makeText(this, "Campos vacios", Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
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