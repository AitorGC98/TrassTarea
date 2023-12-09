package com.example.trasstarea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);

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
}