package com.isproject.ey.ui.perfil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.isproject.ey.firebase.Usuario;
import com.isproject.ey.ui.invitado.EditarInvitado;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class EditarPerfil extends AppCompatActivity
        implements View.OnClickListener ,DatePickerDialog.OnDateSetListener{
    //
    Button btSave, btCancel;
    EditText etNomb, etAp, etTel, etFecha;
    //Fire
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    //Fecha
    DatePickerDialog dpg;
    Calendar calendar;
    private static int ANIO, MES,DIA;
    //
    Usuario usu;
    String idUs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        //
        iniciarFB();
        iniciarElementos();
        idUs = mAuth.getCurrentUser().getUid();
        llenarDatos();
    }
    private void iniciarElementos(){
        btSave = findViewById(R.id.bt_save_edper);
        btCancel = findViewById(R.id.bts_cancel_edper);
        btSave.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        //
        etNomb = findViewById(R.id.edit_nom_edper);
        etAp = findViewById(R.id.edit_apellido_edper);
        etTel = findViewById(R.id.edit_tel_edper);
        etFecha = findViewById(R.id.edit_fechan_edper);
        etFecha.setOnClickListener(this);
    }
    private void iniciarFB()
    {
        try
        {
            FirebaseApp.initializeApp(EditarPerfil.this);
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
            mAuth = FirebaseAuth.getInstance();
        }catch(Exception e)
        {
            Toast.makeText(EditarPerfil.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_fechan_edper:
                calendar = Calendar.getInstance();
                ANIO = calendar.get(Calendar.YEAR);
                MES = calendar.get(Calendar.MONTH);
                DIA = calendar.get(Calendar.DAY_OF_MONTH+1);
                dpg = new DatePickerDialog(this, this,ANIO,MES,DIA);
                dpg.show();
                break;
            case R.id.bt_save_edper:
                if(mValidar()){
                    try {
                        String nom = etNomb.getText().toString();
                        String ap = etAp.getText().toString();
                        String fech = etFecha.getText().toString();
                        String tel = etTel.getText().toString();
                        //
                        usu.setNombre(nom);
                        usu.setApellidos(ap);
                        usu.setTel(tel);
                        usu.setFecha(fech);
                        //
                        databaseReference.child("Usuario").child(usu.getIdUsu()).setValue(usu).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                Toast.makeText(EditarPerfil.this, "Información actualizada.", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
                        }catch (Exception e){

                    }
                }
                break;
            case R.id.bts_cancel_edper:
                finish();
                break;
        }
    }
    private boolean mValidar(){
        if(etNomb.getText().toString().equals("")||etAp.getText().toString().equals("")||
            etTel.getText().toString().equals("")||etFecha.getText().toString().equals("")){
            Toast.makeText(EditarPerfil.this, "Campos vacios", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return  true;
        }
    }
    private void llenarDatos(){
        try {
            databaseReference.child("Usuario").child(idUs).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    usu = snapshot.getValue(Usuario.class);
                    //
                    etNomb.setText(usu.getNombre().toString());
                    etAp.setText(usu.getApellidos().toString());
                    etFecha.setText(usu.getFecha().toString());
                    etTel.setText(usu.getTel().toString());
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {}
            });
        }catch (Exception e){
            Toast.makeText(EditarPerfil.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        etFecha.setText(dayOfMonth+"/"+(month+1)+"/"+year);
    }
}