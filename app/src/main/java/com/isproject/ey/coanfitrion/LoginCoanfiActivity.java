package com.isproject.ey.coanfitrion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.isproject.ey.MainActivity;
import com.isproject.ey.R;
import com.isproject.ey.firebase.Coanfitrion;

import org.jetbrains.annotations.NotNull;

public class LoginCoanfiActivity extends AppCompatActivity implements View.OnClickListener{
    //
    Button btLog, btBack;
    EditText etCorreo, etCodg;
    //FireB
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //
    Coanfitrion co;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_coanfi);
        //
        iniciarElementos();
        iniciarFB();
        //
    }
    private void iniciarElementos(){
        btLog = findViewById(R.id.bt_login_lgca);
        btLog.setOnClickListener(this);
        btBack = findViewById(R.id.bt_back_lgca);
        btBack.setOnClickListener(this);
        //
        etCorreo = findViewById(R.id.edit_correo_lgca);
        etCodg = findViewById(R.id.edit_password_lgca);
    }
    private void iniciarFB()
    {
        try
        {
            FirebaseApp.initializeApp(this);
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
        }catch(Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login_lgca:
                if(mValidar()){
                    String corr = etCorreo.getText().toString();
                    String cdg = etCodg.getText().toString();
                    //
                    try{
                        databaseReference.child("Coanfitrion").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                for(DataSnapshot objtSnapshot: snapshot.getChildren()){
                                    co = objtSnapshot.getValue(Coanfitrion.class);
                                    if(co.getCorreo().toString().equals(corr) && co.getIdCo().toString().equals(cdg)){
                                        Intent intent = new Intent(LoginCoanfiActivity.this, CoanfitrionActivity.class);
                                        intent.putExtra("idEv",co.getIdEv().toString());
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Toast.makeText(LoginCoanfiActivity.this, "Credenciales Invalidas", Toast.LENGTH_LONG).show();
                                    }
                                }

                            }
                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {}
                        });
                    }catch (Exception e){
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.bt_back_lgca:
                Intent intent = new Intent(LoginCoanfiActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
    private boolean mValidar(){
        if(etCorreo.getText().toString().equals("")|| etCodg.getText().toString().equals("")){
            Toast.makeText(this, "Campos Vacios", Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }
    }
}