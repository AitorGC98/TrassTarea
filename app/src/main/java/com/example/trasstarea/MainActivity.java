package com.example.trasstarea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.trasstarea.Datos.Tarea;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
       sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
       //Bindeo del boton
        Button btnEmpezar = findViewById(R.id.btn_empezar);
        //acción establecida para el botón cuando este es pulsado
        btnEmpezar.setOnClickListener(this::enviar);


    }
    //Función que lanza la actividad ListadoTareasActivity
    public void enviar(View view) {
        Intent intent = new Intent(this, ListadoTareasActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();

        if(sharedPreferences.getBoolean("tema",true)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        try{
        String limpieza=sharedPreferences.getString("limpieza", "0");
        int dia= Integer.parseInt(limpieza);
        if(dia!=0) {
            long antiguedad = System.currentTimeMillis() - (dia * 24 * 60 * 60 * 1000);
            File dir = new File("/data/user/0/com.example.trasstarea/files/archivos");
            File direx = new File("/storage/emulated/0/Documents/archivos");
            File[] files = dir.listFiles();
            files = direx.listFiles();
            for (File file : files) {
                if (file.lastModified() < antiguedad) {
                    file.delete();
                        }
                    }
                }

            }catch(Exception e){
            Toast.makeText(getApplicationContext(),"Error en el borrado de archivos antiguos, consulte las preferencias", Toast.LENGTH_LONG).show();
        }
    }


}
