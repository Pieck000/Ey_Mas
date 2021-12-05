package com.isproject.ey.ui.invitado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.isproject.ey.R;
import com.isproject.ey.firebase.Evento;
import com.isproject.ey.firebase.Invitado;
import com.isproject.ey.ui.proveedor.EditarProovedor;

import org.jetbrains.annotations.NotNull;

public class EditarInvitado extends AppCompatActivity implements View.OnClickListener{
    //
    Button btSave, btCancel;
    EditText etNombre, etCorreo, etMesa, etPases;
    RadioButton rbNy, rbNn;
    //Fire
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //
    String idInv;
    Invitado inv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_invitado);
        //
        idInv = getIntent().getStringExtra("idInv");
        iniciarFB();
        iniciarElementos();
        //
        llenarDatos();
    }
    private void iniciarElementos(){
        btSave = findViewById(R.id.bt_save_inv);
        btCancel = findViewById(R.id.bts_cancel_inv);
        btSave.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        //
        etNombre = findViewById(R.id.edit_nomf_edinv);
        etCorreo = findViewById(R.id.edit_correo_edinv);
        etMesa = findViewById(R.id.edit_mesa_edinv);
        etPases = findViewById(R.id.edit_pases_edinv);
        //
        rbNy = findViewById(R.id.rb_si_edinv);
        rbNn = findViewById(R.id.rb_no_edinv);
    }
    private void iniciarFB()
    {
        try
        {
            FirebaseApp.initializeApp(EditarInvitado.this);
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
        }catch(Exception e)
        {
            Toast.makeText(EditarInvitado.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void llenarDatos(){
        try {
            databaseReference.child("Invitado").child(idInv).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    inv = snapshot.getValue(Invitado.class);
                    //
                    etNombre.setText(inv.getNombreFamilia().toString());
                    etCorreo.setText(inv.getCorreo().toString());
                    etMesa.setText(""+inv.getMesa());
                    etPases.setText(""+inv.getPases());
                    if(inv.getNube() == 1){
                        rbNy.setChecked(true);
                    }else{
                        rbNn.setChecked(true);
                    }
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {}
            });
        }catch (Exception e){
            Toast.makeText(EditarInvitado.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_save_inv:
                if(mValidar()){
                    try {
                        String nom = etNombre.getText().toString();
                        String corr = etCorreo.getText().toString();
                        int mesa = Integer.parseInt(etMesa.getText().toString());
                        int pases = Integer.parseInt(etPases.getText().toString());
                        //
                        inv.setNombreFamilia(nom);
                        inv.setCorreo(corr);
                        inv.setMesa(mesa);
                        inv.setPases(pases);
                        if(rbNy.isChecked()){
                            inv.setNube(1);
                        }else {
                            inv.setNube(0);
                        }
                        //
                        databaseReference.child("Invitado").child(idInv).setValue(inv).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                Toast.makeText(EditarInvitado.this, "Invitado actualizado.", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
                    }catch (Exception e){
                        Toast.makeText(EditarInvitado.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.bts_cancel_inv:
                finish();
                break;
        }
    }
    private boolean mValidar(){
        if(etCorreo.getText().toString().equals("")||etNombre.getText().toString().equals("")||
                etMesa.getText().toString().equals("")||etPases.getText().toString().equals("")){
            Toast.makeText(EditarInvitado.this, "Campos Vacios", Toast.LENGTH_LONG).show();

            return false;
        }else{
            if(!rbNy.isChecked() && !rbNn.isChecked()){
                Toast.makeText(EditarInvitado.this, "Campos Vacios", Toast.LENGTH_LONG).show();
                return false;
            }
            else {
                int p = Integer.parseInt(etPases.getText().toString());
                int m = Integer.parseInt(etMesa.getText().toString());
                if(p==0){
                    Toast.makeText(EditarInvitado.this, "Otorga por lo menos un pase", Toast.LENGTH_LONG).show();
                    return false;
                }else if (m==0){
                    Toast.makeText(EditarInvitado.this, "Asigna No. de mesa", Toast.LENGTH_LONG).show();
                    return false;
                }else {
                    return true;
                }
            }
        }
    }
}