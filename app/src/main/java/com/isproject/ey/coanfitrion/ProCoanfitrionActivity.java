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
import com.isproject.ey.firebase.Coanfitrion;
import com.isproject.ey.firebase.Proovedor;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProCoanfitrionActivity extends AppCompatActivity implements View.OnClickListener{
    //
    Button btAdd, btList, btBack;
    EditText etNombre, etCorreo, etTel, etPers;
    ListView lvPro;
    //Fire
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //
    String idEv;
    Proovedor prov;
    //
    private List<Proovedor> listaProov = new ArrayList<Proovedor>();
    ArrayAdapter<Proovedor> arrayAdapterItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_coanfitrion);
        //
        idEv = getIntent().getStringExtra("idEv");
        iniciarFB();
        iniciarElementos();
        //
        listarDatos();
    }
    private void iniciarElementos(){
        btAdd = findViewById(R.id.bt_add_proca);
        btAdd.setOnClickListener(this);
        btList = findViewById(R.id.bt_list_proca);
        btList.setOnClickListener(this);
        btBack = findViewById(R.id.bts_back_proca);
        btBack.setOnClickListener(this);
        //
        etNombre = findViewById(R.id.edit_nom_proca);
        etCorreo = findViewById(R.id.edit_correo_proca);
        etPers = findViewById(R.id.edit_pers_proca);
        etTel = findViewById(R.id.edit_tel_proca);
        //
        lvPro = findViewById(R.id.lv_proca);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bt_add_proca:
                if(mValidar()){
                    try {
                        String idProv = UUID.randomUUID().toString();
                        idProv = idProv.substring(1,10);
                        String nom = etNombre.getText().toString();
                        String corr = etCorreo.getText().toString();
                        int personal = Integer.parseInt(etPers.getText().toString());
                        String tel = etTel.getText().toString();
                        //
                        prov = new Proovedor();
                        prov.setIdProv(idProv);
                        prov.setIdEv(idEv);
                        prov.setNombre(nom);
                        prov.setCorreo(corr);
                        prov.setPersonal(personal);
                        prov.setTelefono(tel);
                        databaseReference.child("Proovedor").child(idProv).setValue(prov).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ProCoanfitrionActivity.this,
                                            "Proovedor Registrado", Toast.LENGTH_LONG).show();
                                    llenarLista(listaProov);
                                    mLimpiar();
                                }else {
                                    Toast.makeText(ProCoanfitrionActivity.this, "Error en Registro", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }catch (Exception e)
                    {
                        Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.bt_list_proca:
                llenarLista(listaProov);
                break;
            case R.id.bts_back_proca:
                Intent intent = new Intent(ProCoanfitrionActivity.this, CoanfitrionActivity.class);
                intent.putExtra("idEv",idEv.toString());
                startActivity(intent);
                finish();
                break;
        }
    }
    private boolean mValidar(){
        if(etNombre.getText().toString().equals("")||etCorreo.getText().toString().equals("")||
                etTel.getText().toString().equals("")||etPers.getText().toString().equals("")){
            Toast.makeText(this, "Campos Vacios", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }
    private void mLimpiar(){
        etNombre.setText(null);
        etCorreo.setText(null);
        etTel.setText(null);
        etPers.setText(null);
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
    private void listarDatos()
    {
        try {
            databaseReference.child("Proovedor").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listaProov.clear();
                    for(DataSnapshot objtSnapshot: snapshot.getChildren()){
                        Proovedor in = objtSnapshot.getValue(Proovedor.class);
                        if(in.getIdEv().equals(idEv))
                        {
                            listaProov.add(in);
                        }

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public void llenarLista(List<Proovedor> lista){
        try {
            arrayAdapterItem = new ArrayAdapter<Proovedor>(
                    this,
                    android.R.layout.simple_list_item_1, lista);
            lvPro.setAdapter(arrayAdapterItem);
        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
}