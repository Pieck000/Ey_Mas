package com.isproject.ey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
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
import com.isproject.ey.firebase.Evento;
import com.isproject.ey.firebase.Invitado;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class EditeventoActivity extends AppCompatActivity implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    Button btSave, btDele, btCancel;
    EditText etNombre, etFecha, etUbi, etHora;
    //Fecha
    DatePickerDialog dpg;
    Calendar calendar;
    private static int ANIO, MES,DIA;
    //Hora
    TimePickerDialog tpd;
    private static int HORA, MIN;
    //FireB
    Evento evnt;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    //
    String idEv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editevento);
        //
        idEv = getIntent().getStringExtra("idEv");
        iniciarElementos();
        iniciarFB();
        llenarElementos();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.edit_hora_ede:
                calendar = Calendar.getInstance();
                HORA = calendar.get(Calendar.HOUR_OF_DAY);
                MIN = calendar.get(Calendar.MINUTE);
                tpd = new TimePickerDialog(this, this, HORA, MIN, true);
                tpd.show();
                break;
            case R.id.edit_fecha_ede:
                calendar = Calendar.getInstance();
                ANIO = calendar.get(Calendar.YEAR);
                MES = calendar.get(Calendar.MONTH);
                DIA = calendar.get(Calendar.DAY_OF_MONTH);
                dpg = new DatePickerDialog(EditeventoActivity.this, this,ANIO,MES,DIA);
                dpg.show();
                break;
            case R.id.bt_save_ede:
                if(mValidar()){
                    String nom = etNombre.getText().toString();
                    String ubi = etUbi.getText().toString();
                    String fe = etFecha.getText().toString();
                    String h = etHora.getText().toString();
                    try{
                        evnt.setNombre(nom);
                        evnt.setUbicacion(ubi);
                        evnt.setFecha(fe);
                        evnt.setHora(h);
                        AlertDialog.Builder dialog = new AlertDialog.Builder(EditeventoActivity.this);
                        dialog.setTitle("Guardar Cambios?");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseReference.child("Evento").child(evnt.getIdEVento()).setValue(evnt).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        Intent intent = new Intent(EditeventoActivity.this, UimainActivity.class);
                                        intent.putExtra("idEv", idEv);
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(EditeventoActivity.this, "Evento actualizado", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
                        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent2 = new Intent(EditeventoActivity.this, UimainActivity.class);
                                intent2.putExtra("idEv", idEv);
                                startActivity(intent2);
                                finish();
                            }
                        });
                        dialog.show();
                    }catch (Exception e){

                    }
                }
                break;
            case R.id.bt_cancel_ede:
                Intent intent2 = new Intent(EditeventoActivity.this, UimainActivity.class);
                intent2.putExtra("idEv", idEv);
                startActivity(intent2);
                finish();
                break;
            case R.id.bts_del_ede:
                AlertDialog.Builder dialog = new AlertDialog.Builder(EditeventoActivity.this);
                dialog.setTitle("Eliminar Evento?");
                dialog.setMessage("Terminos y condiciones");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference.child("Evento").child(idEv).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {

                                databaseReference.child("Invitado").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        for (DataSnapshot objtSnapshot : snapshot.getChildren()) {
                                            Invitado inv  = objtSnapshot.getValue(Invitado.class);
                                            if (inv.getIdEvnt().equals(idEv)){
                                                databaseReference.child("Invitado").child(inv.getIdInvitado()).removeValue();
                                            }
                                        }
                                        Intent intent = new Intent(EditeventoActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {}
                                });
                            }
                        });
                    }
                });
                dialog.setNegativeButton("Cancelar", null);
                dialog.show();
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
    private void llenarElementos(){
        try{
            databaseReference.child("Evento").child(idEv).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    evnt = snapshot.getValue(Evento.class);
                    //
                    etNombre.setText(evnt.getNombre().toString());
                    etFecha.setText(evnt.getFecha().toString());
                    etUbi.setText(evnt.getUbicacion().toString());
                    etHora.setText(evnt.getHora().toString());
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {}
            });
        }catch (Exception e){
            Toast.makeText(EditeventoActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private boolean mValidar(){
        if(etNombre.getText().toString().equals("")|| etHora.getText().toString().equals("")||
                etFecha.getText().toString().equals("")||etUbi.getText().toString().equals(""))
        {
            Toast.makeText(this, "Campos Vacios", Toast.LENGTH_LONG).show();
            return false;
        }else
        {
            try {
                if(false)//FALTA VALIDAR FECHA DE EDITAR EVENTO
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
        }
    }
    private void iniciarElementos(){
        etNombre = findViewById(R.id.edit_nom_ede);
        etFecha = findViewById(R.id.edit_fecha_ede);
        etUbi = findViewById(R.id.edit_ubica_ede);
        etHora = findViewById(R.id.edit_hora_ede);
        //
        btSave = findViewById(R.id.bt_save_ede);
        btDele = findViewById(R.id.bts_del_ede);
        btCancel = findViewById(R.id.bt_cancel_ede);
        //
        btSave.setOnClickListener(this);
        btDele.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        etFecha.setOnClickListener(this);
        etHora.setOnClickListener(this);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        etFecha.setText(dayOfMonth+"/"+(month+1)+"/"+year);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        etHora.setText(hourOfDay+":"+minute);
    }
}