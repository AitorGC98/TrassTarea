package com.example.trasstarea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import com.example.trasstarea.Datos.Tarea;
import com.example.trasstarea.Fragmentos.Fragmento1;
import com.example.trasstarea.Fragmentos.Fragmento2;
import com.example.trasstarea.FragmentsUtilities.CompartirViewModel;

public class CrearTareaActivity extends AppCompatActivity implements Fragmento1.ComunicarFragmento1, Fragmento2.ComunicarFragmento2 {

    Fragmento1 fragmentoUno=new Fragmento1();
    Fragmento2 fragmentoDos=new Fragmento2();
    private FragmentManager fragmentManager;
    CompartirViewModel compartirViewModel;
    private final int[] porcentajes = {0, 25, 50, 75, 100};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_tarea);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.ly_fragmento, fragmentoUno).commit();

        compartirViewModel = new ViewModelProvider(this).get(CompartirViewModel.class);


        if (savedInstanceState == null) {//si es la primera vez que se crea la actividad mostramos el fragmento 1

            showFragment(fragmentoUno);
        }else{
            // Hay un estado guardado, restaura el fragmento correspondiente
            int currentFragmentId = savedInstanceState.getInt("currentFragment", 0);
            if (currentFragmentId == 1) {
                showFragment(fragmentoUno);
            } else if (currentFragmentId == 2) {
                showFragment(fragmentoDos);
            }
        }

    }

    private int getCurrentFragmentId() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.ly_fragmento);
        if (currentFragment instanceof Fragmento1) {
            return 1;
        } else if (currentFragment instanceof Fragmento2) {
            return 2;
        }
        return 0;
    }

    @Override
    public void onBotonIrFragmento2() {
        //Si el fragmento 2 no está cargado, se inicia una transición de fragmentos
        if(!fragmentoDos.isAdded())
            fragmentManager.beginTransaction().replace(R.id.ly_fragmento, fragmentoDos).commit();
    }

    @Override
    public void onBotonGuardar() {
        volver();
    }

    @Override
    public void onBotonIr1Clicked() {
        //Si el fragmento 1 no está cargado, se inicia una transición de fragmentos
        if(!fragmentoUno.isAdded())
            fragmentManager.beginTransaction().replace(R.id.ly_fragmento, fragmentoUno).commit();
    }


    public  void showFragment(Fragment fragment) {
        if (!fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.ly_fragmento, fragment)
                    .commit();
        }
    }


    public void volver() {
        String titulo = compartirViewModel.getTitulo().getValue();
        String descripcion = compartirViewModel.getDescripcion().getValue();
        String fechaCreacion = compartirViewModel.getFechaCreacion().getValue();
        String fechaObjetivo = compartirViewModel.getFechaObjetivo().getValue();
        boolean prioritaria;
        if(compartirViewModel.getPrioritarias().getValue()==null){
            prioritaria =false;
        }else{
            prioritaria =compartirViewModel.getPrioritarias().getValue();
        }
        int progreso = porcentajes[compartirViewModel.getProgreso().getValue()];

        Tarea tarea=new Tarea(titulo, descripcion, progreso, fechaCreacion, fechaObjetivo, prioritaria);
        Intent intentVolver = new Intent();
        intentVolver.putExtra("Resultado", tarea);
        setResult(RESULT_OK, intentVolver);
        finish();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentFragment", getCurrentFragmentId());
    }


}