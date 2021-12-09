package com.isproject.ey.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.isproject.ey.EditeventoActivity;
import com.isproject.ey.HomeActivity;
import com.isproject.ey.R;
import com.isproject.ey.databinding.FragmentHomeBinding;
import com.isproject.ey.firebase.Coanfitrion;
import com.isproject.ey.firebase.Evento;
import com.isproject.ey.firebase.Invitado;
import com.isproject.ey.ui.coanfi.AddCoanfiActivity;
import com.isproject.ey.ui.coanfi.EditCoanfiActivity;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    //
    Button btEdit, btEvts, btCoanf;
    TextView tvNom, tvFech, tvUbi, tvHo, tvCB;
    ViewFlipper vfImgs;
    //Fire
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    Evento evento;
    //
    String idEv;
    boolean COAN = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        idEv = getActivity().getIntent().getStringExtra("idEv");
        iniciarFB();
        iniciarCom(root);
        //
        int imgs [] = {R.raw.imgey1, R.raw.imgey2, R.raw.imgey3};
        for(int img: imgs){
            llenarFlipper(img);
        }
        //Datos evento
        try {
                databaseReference.child("Evento").child(idEv).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    evento = snapshot.getValue(Evento.class);

                    tvNom.setText(evento.getNombre().toString());
                    tvUbi.setText("Dirección: "+evento.getUbicacion().toString());
                    tvFech.setText("Fecha: "+evento.getFecha().toString());
                    tvHo.setText("Hora: "+evento.getHora().toString()+"\n Paquete "+evento.getPaquete());
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {}
            });
        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        //Coanfi
        verificarCoanfitrion();
        return root;
    }
    private void iniciarCom(View root){
        btEdit = root.findViewById(R.id.bt_edit_fh);
        btEdit.setOnClickListener(this);
        btEvts = root.findViewById(R.id.bts_evts_fh);
        btEvts.setOnClickListener(this);
        btCoanf = root.findViewById(R.id.bts_coanf_fh);
        btCoanf.setOnClickListener(this);
        //
        tvNom = root.findViewById(R.id.tv_frhome);
        tvFech = root.findViewById(R.id.tv_frhome_fech);
        tvUbi = root.findViewById(R.id.tv_frhome_ubi);
        tvHo = root.findViewById(R.id.tv_frhome_time);
        tvCB = root.findViewById(R.id.tv_frhome_cr);
        //
        vfImgs = root.findViewById(R.id.vf_imgs);
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
    private void verificarCoanfitrion(){
        try {
            databaseReference.child("Coanfitrion").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    for(DataSnapshot objtSnapshot: snapshot.getChildren()){
                        Coanfitrion coan = objtSnapshot.getValue(Coanfitrion.class);
                        if(coan.getIdEv().equals(idEv)){
                            COAN = true;
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {}
            });
        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void llenarFlipper(int img){
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(img);
        //
        vfImgs.addView(imageView);
        vfImgs.setFlipInterval(3000);
        vfImgs.setAutoStart(true);
        //
        vfImgs.setInAnimation(getContext(), android.R.anim.slide_in_left);
        vfImgs.setOutAnimation(getContext(), android.R.anim.slide_out_right);
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
            case R.id.bt_edit_fh:
                Intent intent1 = new Intent(getActivity(), EditeventoActivity.class);
                intent1.putExtra("idEv",idEv);
                startActivity(intent1);
                getActivity().finish();
                break;
            case R.id.bts_evts_fh:
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.bts_coanf_fh:
                if(!COAN){
                    Intent intent2 = new Intent(getActivity(), AddCoanfiActivity.class);
                    intent2.putExtra("idEv",idEv);
                    startActivity(intent2);
                    getActivity().finish();
                }else{
                    Intent intent2 = new Intent(getActivity(), EditCoanfiActivity.class);
                    intent2.putExtra("idEv",idEv);
                    startActivity(intent2);
                    getActivity().finish();
                }
                break;
        }
    }
}