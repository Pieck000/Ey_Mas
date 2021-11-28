package com.isproject.ey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class RecuperarActivity extends AppCompatActivity {
    //
    Button btSend, btCancel;
    EditText etCorreo;
    //Fire
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    //
    ProgressDialog progressDialog;
    String correo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar);
        //
        iniciarFB();
        progressDialog = new ProgressDialog(this);
        //
        etCorreo = findViewById(R.id.edit_correo_recu);
        btSend = findViewById( R.id.bt_send_re);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correo  = etCorreo.getText().toString();
                if(!correo.equals("")){
                    progressDialog.setMessage("Espere un momento...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    resetPassword();
                }else {
                    Toast.makeText(RecuperarActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btCancel = findViewById(R.id.bts_cancel_re);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecuperarActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

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
    private void resetPassword(){
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(correo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(RecuperarActivity.this);
                    dialog.setTitle("Recuperar Contraseña");
                    dialog.setMessage("E-mail de recuperación enviado, porfavor verifica tu buzon");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(RecuperarActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    dialog.show();
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(RecuperarActivity.this, "Fallo al recuperar contraseña. Verifica tu e-mail ", Toast.LENGTH_LONG).show();
                    etCorreo.setText("");
                }
            }
        });
    }
}