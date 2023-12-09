package com.example.trasstarea.Fragmentos;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;


import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.trasstarea.FragmentsUtilities.CompartirViewModel;
import com.example.trasstarea.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;


public class Fragmento1 extends Fragment {

    private CompartirViewModel compartirViewModel;
    private TextInputEditText etTitulo,etFechaCreacion,etFechaObjetivo;
    private Spinner spProgreso;
    private CheckBox chbPrioritaria;

    public interface ComunicarFragmento1{
        void onBotonIrFragmento2();
    }
    private ComunicarFragmento1 comunicador1;

    public Fragmento1() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        //Sobrescribimos para esto el método onAttach() donde recibimos el contexto (=Actividad)
        super.onAttach(context);
        if (context instanceof ComunicarFragmento1) {  //Si la Actividad implementa la interfaz de comunicación
            comunicador1 = (ComunicarFragmento1) context; //la Actividad se convierte en comunicador
        } else {
            throw new ClassCastException(context + " debe implementar interfaz de comunicación con el 1º fragmento");
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

        View fragmento1 = inflater.inflate(R.layout.fragment_fragmento1, container, false);

        String[] estados = new String[]{
                getString(R.string.estado_no_iniciada),
                getString(R.string.estado_iniciada),
                getString(R.string.estado_avanzada),
                getString(R.string.estado_casi_finalizada),
                getString(R.string.estado_finalizada)};

        Button btnSiguiente = fragmento1.findViewById(R.id.btn_siguiente);
        btnSiguiente.setOnClickListener(view ->{
            if(TextUtils.isEmpty(etTitulo.getText()) || TextUtils.isEmpty(etFechaCreacion.getText()) || TextUtils.isEmpty(etFechaObjetivo.getText())){
                Toast.makeText(requireContext(), getString(R.string.aviso), Toast.LENGTH_LONG).show();
            }else{
                comunicador1.onBotonIrFragmento2();
            }

        });


        etTitulo=fragmento1.findViewById(R.id.et_titulo);
        etFechaCreacion=fragmento1.findViewById(R.id.et_fechaCreacion);
        etFechaObjetivo=fragmento1.findViewById(R.id.et_fechaObjetivo);
        etFechaCreacion.setOnClickListener(view -> showDatePickerDialog(1));
        etFechaObjetivo.setOnClickListener(view -> showDatePickerDialog(0));

        spProgreso=fragmento1.findViewById(R.id.sp_progreso);
        chbPrioritaria=fragmento1.findViewById(R.id.chb_prioritaria);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, estados);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProgreso.setAdapter(adapter);

        etTitulo.setText(compartirViewModel.getTitulo().getValue());
        etFechaCreacion.setText(compartirViewModel.getFechaCreacion().getValue());
        etFechaObjetivo.setText(compartirViewModel.getFechaObjetivo().getValue());
        Boolean prioritariaValue = compartirViewModel.getPrioritarias().getValue();
        chbPrioritaria.setChecked(prioritariaValue != null ? prioritariaValue : false);
        Integer progresoValue = compartirViewModel.getProgreso().getValue();
        spProgreso.setSelection(progresoValue != null ? progresoValue : 0);

        chbPrioritaria.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                // Actualiza el estado del CheckBox en el ViewModel
                compartirViewModel.setPrioritarias(isChecked);
            }
        });

        spProgreso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // Actualiza la posición del Spinner en el ViewModel
                compartirViewModel.setProgreso(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // No es necesario implementar este método para este caso
            }
        });


        etTitulo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No es necesario implementar este método para este caso
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Actualizar el ViewModel con el nuevo texto
                compartirViewModel.setTitulo(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No es necesario implementar este método para este caso
            }
        });

        return fragmento1;

    }

    private void showDatePickerDialog(int opcion) {

        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 porque en enero empieza en 0
                final String selectedDate = day + "/" + (month+1) + "/" + year;
                if(opcion==1){
                    etFechaCreacion.setText(selectedDate);
                    compartirViewModel.setFechaCreacion(selectedDate);

                }else{
                    etFechaObjetivo.setText(selectedDate);
                    compartirViewModel.setFechaObjetivo(selectedDate);

                }

            }
        });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        //Restaurar el texto en el EditText
        if(savedInstanceState!=null) {
            String titulo = savedInstanceState.getString("titulo", "");
            String fechaCre = savedInstanceState.getString("fechaCre", "");
            String fechaObj = savedInstanceState.getString("fechaObj", "");
            boolean prioritaria = savedInstanceState.getBoolean("prioritaria", false);
            int progreso = savedInstanceState.getInt("progreso", 0);

            compartirViewModel.setTitulo(titulo);
            compartirViewModel.setFechaCreacion(fechaCre);
            compartirViewModel.setFechaObjetivo(fechaObj);
            compartirViewModel.setPrioritarias(prioritaria);
            compartirViewModel.setProgreso(progreso);

        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("titulo", Objects.requireNonNull(etTitulo.getText()).toString());
        outState.putString("fechaCre", Objects.requireNonNull(etFechaCreacion.getText()).toString());
        outState.putString("fechaObj", Objects.requireNonNull(etFechaObjetivo.getText()).toString());
        outState.putBoolean("prioritaria", chbPrioritaria.isChecked());
        outState.putInt("progreso", spProgreso.getSelectedItemPosition());

    }
}