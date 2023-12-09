package com.example.trasstarea.Fragmentos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment{
    // Listener para manejar la fecha seleccionada
    private DatePickerDialog.OnDateSetListener listener;

    public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener) {
        //nueva instancia del fragmento
        DatePickerFragment fragment = new DatePickerFragment();
        // Establece el listener proporcionado
        fragment.setListener(listener);
        // Devuelve la instancia del fragmento
        return fragment;
    }
    // Método para establecer el listener
    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }
    // Método llamado para crear el diálogo de selección de fecha
    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Obtiene la fecha actual
        final Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        // Crea y devuelve un nuevo DatePickerDialog
        return new DatePickerDialog(getActivity(), listener, year, month, day);

    }
}
