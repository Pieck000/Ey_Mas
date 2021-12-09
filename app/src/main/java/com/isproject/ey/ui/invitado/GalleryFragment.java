package com.isproject.ey.ui.invitado;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
import com.isproject.ey.databinding.FragmentGalleryBinding;
import com.isproject.ey.firebase.Evento;
import com.isproject.ey.firebase.Invitado;
import com.isproject.ey.ui.proveedor.EditarProovedor;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GalleryFragment extends Fragment implements View.OnClickListener{

    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;
    //
    Button btAdd, btRefresh;
    RadioGroup rgN;
    RadioButton rbYN, rbNN;
    EditText etNombre, etPases, etMesa, etCorreo;
    ListView lvInv;
    //Fire
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    Invitado inv;
    Evento evn;
    //
    String idEv;
    int lim=0;
    int conteoPases=0;
    private List<Invitado> listaInvitado = new ArrayList<Invitado>();
    ArrayAdapter<Invitado> arrayAdapterItem;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //
        idEv = getActivity().getIntent().getStringExtra("idEv");
        iniciarFB();
        iniciarElementos(root);
        //
        listarDatos();
        limiteInv();
        llenarLista(listaInvitado);
        //
        try {
            lvInv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        inv = (Invitado) parent.getItemAtPosition(position);
                        String idIn = inv.getIdInvitado().toString();
                        String noI = inv.getNombreFamilia().toString();
                        Toast.makeText(getContext(), ""+idEv,Toast.LENGTH_LONG).show();
                        AlertDialog.Builder dialog =new AlertDialog.Builder(getContext());
                        dialog.setTitle("Invitado: \n"+noI);
                        dialog.setNeutralButton("Ok", null);
                        dialog.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try{
                                    Intent intent = new Intent(getActivity(), EditarInvitado.class);
                                    intent.putExtra("idInv", idIn);
                                    startActivity(intent);
                                }catch (Exception e){
                                    Toast.makeText(getContext(), e.getMessage()+" "+e.getCause(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        dialog.setNegativeButton("Eliminar",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /**/
                                try {
                                    databaseReference.child("Invitado").child(idIn).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            Toast.makeText(getContext(), "Eliminado", Toast.LENGTH_LONG).show();
                                            llenarLista(listaInvitado);
                                        }
                                    });

                                }catch (Exception e)
                                {
                                    Toast.makeText(getContext(), e.getMessage()+" "+e.getCause(), Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                        dialog.show();
                    }catch (Exception e){
                        Toast.makeText(getContext(), e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return root;
    }
    private void iniciarElementos(View root){
        btAdd = root.findViewById(R.id.bt_add_inv);
        btAdd.setOnClickListener(this);
        btRefresh = root.findViewById(R.id.bt_list_inv);
        btRefresh.setOnClickListener(this);
        //
        rgN = root.findViewById(R.id.rg_inv);
        rbYN = root.findViewById(R.id.rb_si_inv);
        rbYN.setOnClickListener(this);
        rbNN = root.findViewById(R.id.rb_no_inv);
        rbNN.setOnClickListener(this);
        //
        etCorreo = root.findViewById(R.id.edit_correo_inv);
        etNombre = root.findViewById(R.id.edit_nomf_inv);
        etPases = root.findViewById(R.id.edit_pases_inv);
        etMesa = root.findViewById(R.id.edit_mesa_inv);
        //
        lvInv= root.findViewById(R.id.lv_eventos_h);
    }
    private void iniciarFB()
    {
        try
        {
            FirebaseApp.initializeApp(getContext());
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
            mAuth = FirebaseAuth.getInstance();
        }catch(Exception e)
        {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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
                            System.out.println(conteoPases);
                            listaInvitado.add(in);
                        }

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public void llenarLista(List<Invitado> lista){
        try {
            arrayAdapterItem = new ArrayAdapter<Invitado>(
                    getActivity(),
                    android.R.layout.simple_list_item_1, lista);
            lvInv.setAdapter(arrayAdapterItem);
        }catch (Exception e){
            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bt_add_inv:
                if(mValidar()){
                    try {
                        String idInv = UUID.randomUUID().toString();
                        idInv = idInv.substring(1,10);
                        String nom = etNombre.getText().toString();
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
                        if(rbYN.isChecked()){
                            inv.setNube(1);
                        }else {
                            inv.setNube(0);
                        }
                        databaseReference.child("Invitado").child(idInv).setValue(inv).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(), "Invitado Registrado", Toast.LENGTH_LONG).show();
                                    llenarLista(listaInvitado);
                                    mLimpiar();
                                }else {
                                    Toast.makeText(getContext(), "Error en Registro", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }catch (Exception e)
                    {
                        Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }else{
                    //Toast.makeText(getContext(), "Campos Vacios", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.bt_list_inv:
                llenarLista(listaInvitado);
                break;
        }
    }
    private boolean mValidar(){
        if(etCorreo.getText().toString().equals("")||etNombre.getText().toString().equals("")||
            etMesa.getText().toString().equals("")||etPases.getText().toString().equals("")){
            Toast.makeText(getContext(), "Campos Vacios", Toast.LENGTH_LONG).show();

            return false;
        }else{
            if(!rbYN.isChecked() && !rbNN.isChecked()){
                Toast.makeText(getContext(), "Campos Vacios", Toast.LENGTH_LONG).show();
                return false;
            }
            else {
                int p = Integer.parseInt(etPases.getText().toString());
                int m = Integer.parseInt(etMesa.getText().toString());
                if(p==0){
                    Toast.makeText(getContext(), "Otorga por lo menos un pase", Toast.LENGTH_LONG).show();
                    return false;
                }else if (m==0){
                    Toast.makeText(getContext(), "Asigna No. de mesa", Toast.LENGTH_LONG).show();
                    return false;
                }else {
                   limiteInv();
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
                    if(limpases-(conteoPases+pases) < 0){
                        Toast.makeText(getContext(), "Limite de pases Excedido ", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    else {
                        return true;
                    }
                }
            }
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
    private void mLimpiar(){
        etCorreo.setText(null);
        etNombre.setText(null);
        etMesa.setText(null);
        etPases.setText(null);
        rgN.clearCheck();
    }
}