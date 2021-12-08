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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.isproject.ey.R;
import com.isproject.ey.UimainActivity;
import com.isproject.ey.firebase.Coanfitrion;

import org.jetbrains.annotations.NotNull;

public class EditCoanfiActivity extends AppCompatActivity implements View.OnClickListener{
    //
    Button btSave, btBack, btDelete;
    EditText etNomb, etCorreo;
    TextView tvCdg;
    //Fire
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    //
    Coanfitrion coanfitrion;
    String idEv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_coanfi);
        //
        idEv = getIntent().getStringExtra("idEv");
        iniciarElementos();
        iniciarFB();
        llenarDatos();

    }
    private void iniciarElementos(){
        btSave = findViewById(R.id.bt_edit_edco);
        btSave.setOnClickListener(this);
        btDelete = findViewById(R.id.bts_delete_edco);
        btDelete.setOnClickListener(this);
        btBack = findViewById(R.id.bts_cancel_edco);
        btBack.setOnClickListener(this);
        //
        etNomb = findViewById(R.id.edit_nom_edco);
        etCorreo = findViewById(R.id.edit_correo_edco);
        tvCdg = findViewById(R.id.tv_codigo_edco);
    }
    private void iniciarFB()
    {
        try
        {
            FirebaseApp.initializeApp(EditCoanfiActivity.this);
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
            mAuth = FirebaseAuth.getInstance();
        }catch(Exception e)
        {
            Toast.makeText(EditCoanfiActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void llenarDatos(){
        try {
            databaseReference.child("Coanfitrion").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    for(DataSnapshot objtSnapshot: snapshot.getChildren()){
                        Coanfitrion coan = objtSnapshot.getValue(Coanfitrion.class);
                        if(coan.getIdEv().equals(idEv)){
                            coanfitrion = coan;
                            etNomb.setText(coanfitrion.getNombre().toString());
                            etCorreo.setText(coanfitrion.getCorreo().toString());
                            tvCdg.setText(coanfitrion.getIdCo());
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {}
            });
        }catch (Exception e){
            Toast.makeText(EditCoanfiActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bts_cancel_edco:
                Intent intent = new Intent(EditCoanfiActivity.this, UimainActivity.class);
                intent.putExtra("idEv",idEv);
                startActivity(intent);
                finish();
                break;
            case R.id.bt_edit_edco:
                if(mValidar()){
                    String nom = etNomb.getText().toString();
                    String corr = etCorreo.getText().toString();
                    try{
                        coanfitrion.setNombre(nom);
                        coanfitrion.setCorreo(corr);
                        //
                        databaseReference.child("Coanfitrion").child(coanfitrion.getIdCo()).setValue(coanfitrion).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                Toast.makeText(EditCoanfiActivity.this, "Coanfitrion Actualizado", Toast.LENGTH_LONG).show();
                                Intent intent1 = new Intent(EditCoanfiActivity.this, UimainActivity.class);
                                intent1.putExtra("idEv",idEv);
                                startActivity(intent1);
                                finish();
                            }
                        });
                    }catch (Exception e){

                    }
                }
                break;
            case R.id.bts_delete_edco:
                try {
                    databaseReference.child("Coanfitrion").child(coanfitrion.getIdCo()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            Toast.makeText(EditCoanfiActivity.this, "Coanfitrion Eliminado", Toast.LENGTH_LONG).show();
                            Intent intent1 = new Intent(EditCoanfiActivity.this, UimainActivity.class);
                            intent1.putExtra("idEv",idEv);
                            startActivity(intent1);
                            finish();
                        }
                    });
                }catch (Exception e){
                    Toast.makeText(EditCoanfiActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    private boolean mValidar(){
        if(etNomb.getText().toString().equals("")||etCorreo.getText().toString().equals("")){
            Toast.makeText(EditCoanfiActivity.this, "Campos Vacios", Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }
    }
}