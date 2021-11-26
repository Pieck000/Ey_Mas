package com.isproject.ey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.EventLog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.isproject.ey.firebase.Evento;
import java.util.UUID;

import java.util.Calendar;

public class EventoActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener{
    //Elementos
    EditText etNom, etFecha, etUbica, etHora;
    CheckBox chTyc;
    Button btNext;
    //Fecha
    DatePickerDialog dpg;
    Calendar calendar;
    private static int ANIO, MES,DIA;
    int monthPicked, anioPicked, dayPicked;
    //FireB
    Evento evnt;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        iniciarFB();
        iniciarElementos();

        btNext = findViewById(R.id.bt_next_rev);
        btNext.setOnClickListener(this);
    }
    private void iniciarElementos(){
        etNom = findViewById(R.id.edit_nom_rev);
        etFecha = findViewById(R.id.edit_fecha_rev);
        etUbica = findViewById(R.id.edit_ubica_rev);
        etHora = findViewById(R.id.edit_hora_rev);
        chTyc =  findViewById(R.id.chb_tyc_rev);

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
        switch (v.getId()) {
            case R.id.edit_fecha_rev:
                calendar = Calendar.getInstance();
                ANIO = calendar.get(Calendar.YEAR);
                MES = calendar.get(Calendar.MONTH );
                DIA = calendar.get(Calendar.DAY_OF_MONTH);
                dpg = new DatePickerDialog(this,this,ANIO,MES,DIA);
                dpg.show();
                break;
            case R.id.bt_next_rev:
                if(mValidar())
                {
                    String idEv = UUID.randomUUID().toString();
                    idEv = idEv.substring(1,10);
                    String idAnfi = mAuth.getCurrentUser().getUid();
                    String nom = etNom.getText().toString();
                    String fecha = etFecha.getText().toString();
                    String hora = etHora.getText().toString();
                    String ubi = etUbica.getText().toString();
                    String pqt = ""+getIntent().getIntExtra("pqt",9);

                    try {
                        evnt = new Evento();
                        evnt.setIdEVento(idEv);
                        evnt.setNombre(nom);
                        evnt.setIdAnfitrion(idAnfi);
                        evnt.setFecha(fecha);
                        evnt.setHora(hora);
                        evnt.setUbicacion(ubi);
                        evnt.setPaquete(pqt);
                        Toast.makeText(this, "pqt: "+pqt+" idE: "+idEv+" idanf: "+idAnfi, Toast.LENGTH_LONG).show();
                        databaseReference.child("Evento").child(idEv).setValue(evnt).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(EventoActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }catch (Exception e){
                        Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
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
    private boolean mValidar(){
        if(etNom.getText().toString().equals("")|| etHora.getText().toString().equals("")||
                etFecha.getText().toString().equals("")||etUbica.getText().toString().equals(""))
        {
            Toast.makeText(this, "Campos Vacios", Toast.LENGTH_LONG).show();
            return false;
        }else
        {
            if(chTyc.isChecked())
            {
                try {
                    if(ANIO >= anioPicked && MES >= monthPicked)
                    {
                        Toast.makeText(this, "Minimo un mes previo al Evento", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                }catch (Exception e){
                    Toast.makeText(this, "Fecha vacia", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else
            {
                Toast.makeText(this, "Aceptar terminos y condiciones es necesario", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }
}