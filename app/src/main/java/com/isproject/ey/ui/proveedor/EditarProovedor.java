package com.isproject.ey.ui.proveedor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.isproject.ey.firebase.Proovedor;

import org.jetbrains.annotations.NotNull;

public class EditarProovedor extends AppCompatActivity implements View.OnClickListener{
    Button btSave, btCancel;
    EditText etNomb, etCorreo, etTel, etPersonal;
    //Fire
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //
    String idPr;
    Proovedor prov;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_proovedor);
        //
        idPr = getIntent().getStringExtra("idPr");
        iniciarFB();
        iniciarElementos();
        //
        llenarDatos();

    }
    private void iniciarElementos(){
        btSave = findViewById(R.id.bt_save_proov);
        btCancel = findViewById(R.id.bts_cancel_proov);
        //
        btSave.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        //
        etNomb = findViewById(R.id.edit_nom_edpr);
        etCorreo = findViewById(R.id.edit_correo_edpr);
        etTel = findViewById(R.id.edit_tel_edpr);
        etPersonal = findViewById(R.id.edit_pers_edpr);
    }
    private void iniciarFB()
    {
        try
        {
            FirebaseApp.initializeApp(EditarProovedor.this);
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
        }catch(Exception e)
        {
            Toast.makeText(EditarProovedor.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void llenarDatos(){
        try{
            databaseReference.child("Proovedor").child(idPr).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    prov = snapshot.getValue(Proovedor.class);
                    //
                    etNomb.setText(prov.getNombre().toString());
                    etCorreo.setText(prov.getCorreo().toString());
                    etTel.setText(prov.getTelefono().toString());
                    etPersonal.setText(""+prov.getPersonal());
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {}
            });
        }catch (Exception e){
            Toast.makeText(EditarProovedor.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_save_proov:
                try {
                    String nom = etNomb.getText().toString();
                    String corr = etCorreo.getText().toString();
                    String tel = etTel.getText().toString();
                    int pers = Integer.parseInt(etPersonal.getText().toString());
                    //
                    prov.setNombre(nom);
                    prov.setCorreo(corr);
                    prov.setTelefono(tel);
                    prov.setPersonal(pers);
                    //
                    databaseReference.child("Proovedor").child(prov.getIdProv()).setValue(prov).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            Toast.makeText(EditarProovedor.this, "Proveedor actualizado.", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });

                }catch (Exception e){

                }
                break;
            case R.id.bts_cancel_proov:
                finish();
                break;
        }
    }
}