package com.isproject.ey.ui.coanfi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.isproject.ey.R;
import com.isproject.ey.UimainActivity;
import com.isproject.ey.firebase.Coanfitrion;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AddCoanfiActivity extends AppCompatActivity implements View.OnClickListener{
    //
    Button btSave, btCancel;
    EditText etNomb, etCorreo;
    TextView tvMsj, tvCodigo;
    //Fire
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    //
    String idEv, idanf;
    Coanfitrion coanf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coanfi);
        //
        idEv = getIntent().getStringExtra("idEv");
        iniciarElementos();
        iniciarFB();
        idanf = mAuth.getCurrentUser().getUid();


    }
    private void iniciarElementos(){
        btSave = findViewById(R.id.bt_save_coanf);
        btCancel = findViewById(R.id.bts_cancel_coanf);
        btSave.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        //
        etNomb = findViewById(R.id.edit_nom_coanf);
        etCorreo = findViewById(R.id.edit_correo_coanf);
        //
        tvMsj = findViewById(R.id.tv_cdg);
        tvCodigo = findViewById(R.id.tv_codigo_coanf);
    }
    private void iniciarFB()
    {
        try
        {
            FirebaseApp.initializeApp(AddCoanfiActivity.this);
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
            mAuth = FirebaseAuth.getInstance();
        }catch(Exception e)
        {
            Toast.makeText(AddCoanfiActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_save_coanf:
                if(mValidar()){
                    String idCoan = UUID.randomUUID().toString();
                    idCoan = idCoan.substring(1,10);
                    String nomb = etNomb.getText().toString();
                    String corr = etCorreo.getText().toString();
                    try {
                        coanf = new Coanfitrion();
                        coanf.setIdCo(idCoan);
                        coanf.setIdEv(idEv);
                        coanf.setNombre(nomb);
                        coanf.setCorreo(corr);
                        //
                        databaseReference.child("Coanfitrion").child(coanf.getIdCo()).setValue(coanf).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    tvMsj.setText("Codigo de Acceso:");
                                    tvCodigo.setText(coanf.getIdCo().toString());
                                    btSave.setEnabled(false);
                                    Toast.makeText(AddCoanfiActivity.this, "Coanfitrion Registrado", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }catch (Exception e){
                        Toast.makeText(AddCoanfiActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.bts_cancel_coanf:
                Intent intent = new Intent(AddCoanfiActivity.this, UimainActivity.class);
                intent.putExtra("idEv",idEv);
                startActivity(intent);
                finish();
                break;
        }
    }

    private boolean mValidar(){
        if(etNomb.getText().toString().equals("")||etCorreo.getText().toString().equals("")){
            Toast.makeText(AddCoanfiActivity.this, "Campos Vacios", Toast.LENGTH_LONG).show();
            return false;
        }else {
            return true;
        }
    }
}