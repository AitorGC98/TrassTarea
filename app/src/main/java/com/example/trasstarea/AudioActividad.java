package com.example.trasstarea;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class AudioActividad extends AppCompatActivity {
    private MediaPlayer mp;
    private boolean bucle = false;
    private int posicion = 0;

    public boolean pausa=true;
    TextView texto;
    SeekBar barraReproduccion;
    Uri audioUri;
    private Button botonSeleccionarMp3;
    private ActivityResultLauncher<String> mGetContent;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_actividad);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        audioUri = getIntent().getParcelableExtra("URI_AUDIO");
        barraReproduccion=findViewById(R.id.seekBar_ac);
        barraReproduccion.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mp != null) {
                    mp.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            reproducirArchivoMp3(result);
                        }
                    }
                });

        ImageButton btnAdelantar = findViewById(R.id.btn_adelantar);
        btnAdelantar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp != null && mp.isPlaying()) {
                    int currentPosition = mp.getCurrentPosition();
                    int newPosition = currentPosition + 5000; // 5000 milisegundos (5 segundos)

                    // Verificar que la nueva posición no exceda la duración total del audio
                    if (newPosition < mp.getDuration()) {
                        mp.seekTo(newPosition);
                    } else {
                        mp.seekTo(mp.getDuration()); // Ir al final del audio
                    }
                }
            }
        });

        ImageButton btnplay = findViewById(R.id.btn_play);

        btnplay.setOnClickListener(view -> {
            if (pausa) {
                btnplay.setImageResource(R.drawable.ic_pause_grande);
                reproducirArchivoMp3(audioUri);
                pausa=false;
            }else{
                btnplay.setImageResource(R.drawable.ic_play_grande);
                Pausar();
                pausa=true;
            }
        });


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Volver atrás cuando se presiona la flecha de retroceso en el ActionBar
        return true;
    }

    private void reproducirArchivoMp3(Uri uri) {
        try {
            // Liberar recursos anteriores si los hubiera
            if (mp != null) {
                mp.stop();
                mp.release();
            }

            // Crear un nuevo MediaPlayer y configurar la fuente de datos
            mp = new MediaPlayer();
            mp.setDataSource(getApplicationContext(), uri);

            // Preparar y comenzar la reproducción
            mp.prepare();
            mp.start();

            // Establecer el máximo de la barra de progreso
            barraReproduccion.setMax(mp.getDuration());

            // Actualizar la barra de progreso mientras se reproduce el archivo
            actualizarBarraReproduccion();

            // Mostrar un mensaje de reproducción iniciada

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al reproducir el archivo", Toast.LENGTH_SHORT).show();
        }
    }
    private void actualizarBarraReproduccion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mp != null && mp.isPlaying()) {
                    try {
                        Thread.sleep(100);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mp != null) {
                                    int mCurrentPosition = mp.getCurrentPosition();
                                    barraReproduccion.setProgress(mCurrentPosition);
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    public void Pausar(){
        if (mp != null && mp.isPlaying()) {            //El método isPlaying() nos devolverá un booleano con el estado de reproducción
            posicion = mp.getCurrentPosition();        //El método getCurrentPosition() nos devolverá un entero con el milisegundo actual de reproducción
            mp.pause();
        }
    }

}