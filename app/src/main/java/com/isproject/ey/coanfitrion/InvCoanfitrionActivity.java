package com.isproject.ey.coanfitrion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.isproject.ey.firebase.Evento;
import com.isproject.ey.firebase.Invitado;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InvCoanfitrionActivity extends AppCompatActivity implements View.OnClickListener {
    //
    Button btAdd, btBack, btList;
    EditText etNom, etCorreo, etMesa, etPases;
    RadioGroup rgN;
    RadioButton rbYn, rbNn;
    ListView lvInv;
    //Fire
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //
    Evento evn;
    Invitado inv;
    String idEv;
    int lim=0;
    int conteoPases=0;
    private List<Invitado> listaInvitado = new ArrayList<Invitado>();
    ArrayAdapter<Invitado> arrayAdapterItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inv_coanfitrion);
        //
        idEv = getIntent().getStringExtra("idEv");
        iniiciarElementos();
        iniciarFB();
        //
        listarDatos();
        limiteInv();
        llenarLista(listaInvitado);

    }
    private void iniiciarElementos(){
        btAdd = findViewById(R.id.bt_add_inca);
        btAdd.setOnClickListener(this);
        btList = findViewById(R.id.bt_list_inca);
        btList.setOnClickListener(this);
        btBack = findViewById(R.id.bts_back_inca);
        btBack.setOnClickListener(this);
        //
        etNom = findViewById(R.id.edit_nomf_inca);
        etCorreo = findViewById(R.id.edit_correo_inca);
        etMesa = findViewById(R.id.edit_mesa_inca);
        etPases = findViewById(R.id.edit_pases_inca);
        //
        rgN = findViewById(R.id.rg_inca);
        rbNn = findViewById(R.id.rb_no_inca);
        rbYn = findViewById(R.id.rb_si_inca);
        //
        lvInv = findViewById(R.id.lv_inca);
    }
    private void iniciarFB()
    {
        try
        {
            FirebaseApp.initializeApp(InvCoanfitrionActivity.this);
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
        }catch(Exception e)
        {
            Toast.makeText(InvCoanfitrionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void listarDatos()
    {
        try {
            databaseReference.child("Invitado").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listaInvitado.clear();
                    conteoPases =0;
                    for(DataSnapshot objtSnapshot: snapshot.getChildren()){
                        Invitado in = objtSnapshot.getValue(Invitado.class);
                        if(in.getIdEvnt().equals(idEv))
                        {
                            conteoPases = conteoPases+ in.getPases();
                            listaInvitado.add(in);
                        }

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            Toast.makeText(InvCoanfitrionActivity.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public void llenarLista(List<Invitado> lista){
        try {
            arrayAdapterItem = new ArrayAdapter<Invitado>(
                    this,
                    android.R.layout.simple_list_item_1, lista);
            lvInv.setAdapter(arrayAdapterItem);
        }catch (Exception e){
            Toast.makeText(InvCoanfitrionActivity.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_add_inca:
                if(mValidar()){
                    try {
                        String idInv = UUID.randomUUID().toString();
                        idInv = idInv.substring(1,10);
                        String nom = etNom.getText().toString();
                        String corr = etCorreo.getText().toString();
                        int pases = Integer.parseInt(etPases.getText().toString());
                        int mesa = Integer.parseInt(etMesa.getText().toString());
                        //
                        inv = new Invitado();
                        inv.setIdEvnt(idEv);
                        inv.setIdInvitado(idInv);
                        inv.setNombreFamilia(nom);
                        inv.setCorreo(corr);
                        inv.setPases(pases);
                        inv.setMesa(mesa);
                        if(rbYn.isChecked()){
                            inv.setNube(1);
                        }else {
                            inv.setNube(0);
                        }
                        databaseReference.child("Invitado").child(idInv).setValue(inv).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(InvCoanfitrionActivity.this, "Invitado Registrado",
                                            Toast.LENGTH_LONG).show();
                                    llenarLista(listaInvitado);
                                    mLimpiar();
                                }else {
                                    Toast.makeText(InvCoanfitrionActivity.this, "Error en Registro",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }catch (Exception e)
                    {
                        Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.bt_list_inca:
                llenarLista(listaInvitado);
                break;
            case R.id.bts_back_inca:
                Intent intent = new Intent(InvCoanfitrionActivity.this, CoanfitrionActivity.class);
                intent.putExtra("idEv",idEv.toString());
                startActivity(intent);
                break;
        }
    }
    private void limiteInv(){
        databaseReference.child("Evento").child(idEv).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                evn = snapshot.getValue(Evento.class);
                lim = Integer.parseInt(evn.getPaquete().toString());
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {}
        });
    }
    private boolean mValidar(){
        if(etCorreo.getText().toString().equals("")||etNom.getText().toString().equals("")||
                etMesa.getText().toString().equals("")||etPases.getText().toString().equals("")){
            Toast.makeText(this, "Campos Vacios", Toast.LENGTH_LONG).show();

            return false;
        }else{
            if(!rbYn.isChecked() && !rbNn.isChecked()){
                Toast.makeText(this, "Campos Vacios", Toast.LENGTH_LONG).show();
                return false;
            }
            else {
                int p = Integer.parseInt(etPases.getText().toString());
                int m = Integer.parseInt(etMesa.getText().toString());
                if(p==0){
                    Toast.makeText(this, "Otorga por lo menos un pase", Toast.LENGTH_LONG).show();
                    return false;
                }else if (m==0){
                    Toast.makeText(this, "Asigna No. de mesa", Toast.LENGTH_LONG).show();
                    return false;
                }else {
                    int limpases = 0;
                    int pases = Integer.parseInt(etPases.getText().toString());
                    switch (lim){
                        case 1:
                            limpases = 100;
                            break;
                        case 2:
                            limpases = 200;
                            break;
                        case 3:
                            limpases = 500;
                            break;
                        case 4:
                            limpases = 1000;
                            break;
                    }
                    if(limpases-(conteoPases+pases) <= 0){
                        Toast.makeText(this, "Limite de pases Excedido ", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    else {
                        return true;
                    }
                }
            }
        }
    }

    private void mLimpiar(){
        etCorreo.setText(null);
        etNom.setText(null);
        etMesa.setText(null);
        etPases.setText(null);
        rgN.clearCheck();
    }
}
