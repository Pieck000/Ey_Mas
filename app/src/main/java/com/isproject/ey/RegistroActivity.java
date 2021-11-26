package com.isproject.ey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.isproject.ey.firebase.Usuario;

import java.util.Calendar;

public class RegistroActivity extends AppCompatActivity
        implements View.OnClickListener,DatePickerDialog.OnDateSetListener {
    //Components
    Button btNext, btBack;
    EditText etNom, etAp, etFecha, etTel, etCorreo, etPassword;
    CheckBox chTyC;
    //Fecha
    DatePickerDialog dpg;
    Calendar calendar;
    private static int ANIO, MES,DIA;
    int monthPicked, anioPicked, dayPicked;
    //FireB
    Usuario usu;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        iniciarFB();
        iniciarElementos();
        btNext = findViewById(R.id.bt_next_r);
        btNext.setOnClickListener(this);
        btBack = findViewById(R.id.bt_back_r);
        btBack.setOnClickListener(this);

    }
    private void iniciarElementos()
    {
        etNom = findViewById(R.id.edit_nom_r);
        etAp = findViewById(R.id.edit_apellido_r);
        etFecha = findViewById(R.id.edit_fechan_r);
        etCorreo = findViewById(R.id.edit_correo_r);
        etTel = findViewById(R.id.edit_tel_r);
        etPassword = findViewById(R.id.edit_password_r);
        chTyC = findViewById(R.id.chb_tyc_r);

        etFecha.setOnClickListener(this);
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        etFecha.setText(dayOfMonth+"/"+(month+1)+"/"+year);
        monthPicked = month;
        anioPicked = year;
        dayPicked = dayOfMonth;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.edit_fechan_r:
                calendar = Calendar.getInstance();
                ANIO = calendar.get(Calendar.YEAR);
                MES = calendar.get(Calendar.MONTH);
                DIA = calendar.get(Calendar.DAY_OF_MONTH+1);
                dpg = new DatePickerDialog(this, this,ANIO,MES,DIA);
                dpg.show();
                break;
            case R.id.bt_next_r:
                if(mValidar()){
                    String nom = etNom.getText().toString();
                    String ape = etAp.getText().toString();
                    String fech = etFecha.getText().toString();
                    String tel = etTel.getText().toString();
                    String corr = etCorreo.getText().toString();
                    String pass = etPassword.getText().toString();
                    try{
                        mAuth.createUserWithEmailAndPassword(corr,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    usu = new Usuario();
                                    usu.setIdUsu(mAuth.getCurrentUser().getUid());
                                    usu.setNombre(nom);
                                    usu.setApellidos(ape);
                                    usu.setFecha(fech);
                                    usu.setTel(tel);
                                    usu.setCorreo(corr);
                                    usu.setPassword(pass);

                                    databaseReference.child("Usuario").child(usu.getIdUsu()).setValue(usu).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task2) {
                                            if(task2.isSuccessful()){
                                                Intent intent = new Intent(RegistroActivity.this, PaquetesActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }else{
                                                Toast.makeText(RegistroActivity.this, "Error en el registro!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }else {
                                    Toast.makeText(RegistroActivity.this, "Error en el registro.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }catch (Exception e){
                        Toast.makeText(this, " "+e.getCause(), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.bt_back_r:
                Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
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
    private boolean mValidar(){
        if(etNom.getText().toString().equals("")|| etAp.getText().toString().equals("")||
                etFecha.getText().toString().equals("")||etTel.getText().toString().equals("")
                ||etCorreo.getText().toString().equals("")||etPassword.getText().toString().equals(""))
        {
            Toast.makeText(this, "Campos Vacios", Toast.LENGTH_LONG).show();
            return false;
        }else
        {
            if(chTyC.isChecked())
            {
                return true;
            }else
            {
                Toast.makeText(this, "Aceptar terminos y condiciones es necesario", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }
}