package com.example.trasstarea;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.VideoView;

public class VideoActividad extends AppCompatActivity {

    private Uri uriVideo;
    private VideoView videoView;
    private SeekBar seekBar;
    private ImageButton playPauseButton;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_actividad);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        uriVideo = getIntent().getParcelableExtra("URI_VID");
        videoView = findViewById(R.id.video_view);
        seekBar = findViewById(R.id.bara_video);
        playPauseButton = findViewById(R.id.btn_pausa_video);

        // Configurar el videoView para reproducir el video desde la URI proporcionada
        videoView.setVideoURI(uriVideo);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer = mp;
                seekBar.setMax(mediaPlayer.getDuration());
                updateSeekBar(); // Actualizar el SeekBar periódicamente
            }
        });

        // Configurar el botón para reproducir y pausar el video
        playPauseButton.setOnClickListener(v -> {
            if (isPlaying) {
                videoView.pause();
                playPauseButton.setImageResource(R.drawable.ic_play); // Cambiar la imagen a "Play"
            } else {
                videoView.start();
                playPauseButton.setImageResource(R.drawable.baseline_pause_circle_outline_24); // Cambiar la imagen a "Pause"
            }
            isPlaying = !isPlaying;
        });

        // Controlar el progreso del video usando el SeekBar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    // Método para actualizar el progreso del SeekBar
    private void updateSeekBar() {
        if (mediaPlayer != null) {
            int currentPosition = mediaPlayer.getCurrentPosition();
            seekBar.setProgress(currentPosition);
            handler.postDelayed(this::updateSeekBar, 100); // Actualizar cada 100 milisegundos
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Volver atrás cuando se presiona la flecha de retroceso en el ActionBar
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacksAndMessages(null); // Detener la actualización del SeekBar al salir de la actividad
    }
}