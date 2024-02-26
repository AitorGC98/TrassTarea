package com.example.trasstarea;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trasstarea.Datos.Tarea;

import java.text.SimpleDateFormat;
import java.util.List;

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

            // Verificar y habilitar los botones según las URLs
            if (TextUtils.isEmpty(tareaSeleccionada.getUrlDoc())) {
                btndoc.setEnabled(false);
            } else {
                btndoc.setOnClickListener(view -> lanzarActividadVisualizarArchivo(tareaSeleccionada.getUrlDoc()));
            }

            if (TextUtils.isEmpty(tareaSeleccionada.getUrlImg())) {
                btnImg.setEnabled(false);
            } else {
                btnImg.setOnClickListener(view -> lanzarActividadVisualizarArchivo(tareaSeleccionada.getUrlImg()));
            }

            if (TextUtils.isEmpty(tareaSeleccionada.getUrlAud())) {
                btnAun.setEnabled(false);
            } else {
                btnAun.setOnClickListener(view -> lanzarActividadVisualizarArchivo(tareaSeleccionada.getUrlAud()));
            }

            if (TextUtils.isEmpty(tareaSeleccionada.getUrlVid())) {
                btnVid.setEnabled(false);
            } else {
                btnVid.setOnClickListener(view -> lanzarActividadVisualizarArchivo(tareaSeleccionada.getUrlVid()));
            }
            btndoc.setOnClickListener(view -> lanzarActividadVisualizarArchivo(tareaSeleccionada.getUrlDoc()));
            btnImg.setOnClickListener(view -> {
                String audioUriString = tareaSeleccionada.getUrlImg(); // Obtener la URI del audio como String
                Uri audioUri = Uri.parse(audioUriString); // Convertir la URI del audio de String a Uri
                Intent intent3 = new Intent(this, ImagenActividad.class); // Crear un intento para la actividad AudioActividad
                intent3.putExtra("URI_IMG", audioUri); // Pasar la URI del audio como un extra al intento
                startActivity(intent3); // Iniciar la actividad AudioActividad
            });
            btnAun.setOnClickListener(view -> {
                String audioUriString = tareaSeleccionada.getUrlAud(); // Obtener la URI del audio como String
                Uri audioUri = Uri.parse(audioUriString); // Convertir la URI del audio de String a Uri
                Intent intent2 = new Intent(this, AudioActividad.class); // Crear un intento para la actividad AudioActividad
                intent2.putExtra("URI_AUDIO", audioUri); // Pasar la URI del audio como un extra al intento
                startActivity(intent2); // Iniciar la actividad AudioActividad
            });

            btnVid.setOnClickListener(view -> {
                String audioUriString = tareaSeleccionada.getUrlVid(); // Obtener la URI del audio como String
                Uri audioUri = Uri.parse(audioUriString); // Convertir la URI del audio de String a Uri
                Intent intent4 = new Intent(this, VideoActividad.class); // Crear un intento para la actividad AudioActividad
                intent4.putExtra("URI_VID", audioUri); // Pasar la URI del audio como un extra al intento
                startActivity(intent4); // Iniciar la actividad AudioActividad
            });


        }

    }
    /*   btndoc.setOnClickListener(view -> {
                // Obtener la URL del documento
                String urlDocumento = tareaSeleccionada.getUrlDoc();

                // Crear un Intent para ver el archivo
                Intent intento = new Intent(Intent.ACTION_VIEW);

                // Establecer la URI del archivo a abrir
                Uri uri = Uri.parse(urlDocumento);
                intento.setDataAndType(uri, "application/pdf");
                 // Iniciar la actividad para ver el archivo
                    startActivity(intento);

            });*/
    private void lanzarActividadVisualizarArchivo(String uriString) {
        Intent intent = new Intent(this, MostrarContenidos.class);
        intent.putExtra("uri", uriString);
        startActivity(intent);
    }



    private void mostrarAlertDialog(String tipo, String urlTarea) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Url " + tipo)
                .setMessage(urlTarea)
                .setPositiveButton(getString(R.string.boton_aceptar), (dialog, which) -> dialog.dismiss())
                .show();
    }
}