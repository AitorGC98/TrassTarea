package com.example.trasstarea.Fragmentos;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trasstarea.FragmentsUtilities.CompartirViewModel;
import com.example.trasstarea.CrearTareaActivity;
import com.example.trasstarea.R;

public class Fragmento2 extends Fragment {

    Fragmento1 fragmentoUno=new Fragmento1();
    private EditText etmDescripcion;
    private CompartirViewModel compartirViewModel;
    private Button btnVolver,btnGuardar;
    private String descripcion;

    public interface ComunicarFragmento2{
        void onBotonGuardar();
        void onBotonIr1Clicked();
    }
    private ComunicarFragmento2 comunicador2;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ComunicarFragmento2) {
            comunicador2 = (ComunicarFragmento2) context;
        } else {
            throw new ClassCastException(context + " debe implementar interfaz de comunicación con el 2º fragmento");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compartirViewModel = new ViewModelProvider(requireActivity()).get(CompartirViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmento2 =inflater.inflate(R.layout.fragment_fragmento2, container, false);

        btnVolver=fragmento2.findViewById(R.id.btn_volver);
        btnVolver.setOnClickListener(view ->{
            comunicador2.onBotonIr1Clicked();
        });
        btnGuardar=fragmento2.findViewById(R.id.btn_guardar);
        btnGuardar.setOnClickListener((view ->{
            if(TextUtils.isEmpty(etmDescripcion.getText())){
                Toast.makeText(requireContext(), "Por favor, complete la descripción", Toast.LENGTH_LONG).show();
            }else{
                comunicador2.onBotonGuardar();
            }

        }));
        etmDescripcion = fragmento2.findViewById(R.id.etm_descripcion);
        etmDescripcion.setText((compartirViewModel.getDescripcion().getValue()));

        // Agregar TextWatcher al EditText para escuchar cambios en tiempo real
        etmDescripcion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No es necesario implementar este método para este caso
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Actualizar el ViewModel con el nuevo texto
                compartirViewModel.setDescripcion(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No es necesario implementar este método para este caso
            }
        });


        return fragmento2;

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        //Restaurar el texto en el EditText
        if(savedInstanceState!=null) {
            descripcion = savedInstanceState.getString("descripcion", "");
            compartirViewModel.setDescripcion(descripcion);


        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("descripcion", etmDescripcion.getText().toString());


    }
}