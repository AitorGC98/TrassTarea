package com.example.trasstarea;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.trasstarea.Datos.Tarea;

import java.text.SimpleDateFormat;

public class MostrarTarea extends AppCompatActivity {

    private SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_tarea);

        // Recuperar la tarea del Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("tarea")) {
            Tarea tareaSeleccionada = intent.getParcelableExtra("tarea");

            // Ahora puedes trabajar con la tareaSeleccionada como lo desees
            // Por ejemplo, puedes mostrar el título de la tarea en un TextView:
            TextView tvTitulo = findViewById(R.id.etMos_titulo);
            tvTitulo.setText(tareaSeleccionada.getTitulo());
            ImageView img = findViewById(R.id.imageView3);
            if(tareaSeleccionada.isPrioritario()){
                img.setImageResource(R.drawable.baseline_star_24);
            }

            TextView tvfecha = findViewById(R.id.etMos_fechaCreacion);
            tvfecha.setText(formato.format(tareaSeleccionada.getFechaCreacion()));

            TextView tvfechaObj = findViewById(R.id.etMos_fechaObjetivo);
            tvfechaObj.setText(formato.format(tareaSeleccionada.getFechaObjetio()));

            ProgressBar progreso = findViewById(R.id.progressBar2);
            progreso.setProgress(tareaSeleccionada.getProgreso());
            TextView tvprogreso=findViewById(R.id.tvMos_progreso);
            tvprogreso.setText(tareaSeleccionada.getProgreso()+" %");
            if(tareaSeleccionada.getProgreso()==100){
                progreso.getProgressDrawable().setColorFilter(
                        Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
            }
            EditText etmDescripcion=findViewById(R.id.etmMos_descripcion);
            etmDescripcion.setText(tareaSeleccionada.getDescripcion());
            ImageButton btndoc=findViewById(R.id.btnMos_documento);
            ImageButton btnImg=findViewById(R.id.btnMos_imagen);
            ImageButton btnAun=findViewById(R.id.btnMos_audio);
            ImageButton btnVid=findViewById(R.id.btnMos_video);
            btndoc.setOnClickListener(view -> mostrarAlertDialog(getString(R.string.documento), tareaSeleccionada.getUrlDoc()));
            btnImg.setOnClickListener(view -> mostrarAlertDialog(getString(R.string.imagen), tareaSeleccionada.getUrlImg()));
            btnAun.setOnClickListener(view -> mostrarAlertDialog(getString(R.string.audio), tareaSeleccionada.getUrlAud()));
            btnVid.setOnClickListener(view -> mostrarAlertDialog(getString(R.string.video), tareaSeleccionada.getUrlVid()));


        }

    }
    private void mostrarAlertDialog(String tipo, String urlTarea) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Url " + tipo)
                .setMessage(urlTarea)
                .setPositiveButton(getString(R.string.boton_aceptar), (dialog, which) -> dialog.dismiss())
                .show();
    }
}