package com.example.trasstarea;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class ImagenActividad extends AppCompatActivity {
    private Uri uriImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen_actividad);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        uriImagen = getIntent().getParcelableExtra("URI_IMG");
        ImageView imagen=findViewById(R.id.img_mostrar);
        // Verificar si la URI de la imagen no es nula
        if (uriImagen != null) {
            // Establecer la URI como imagen en el ImageView
            imagen.setImageURI(uriImagen);
        } else {

        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Volver atrás cuando se presiona la flecha de retroceso en el ActionBar
        return true;
    }
}