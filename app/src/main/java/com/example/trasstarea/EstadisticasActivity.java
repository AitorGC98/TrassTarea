package com.example.trasstarea;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.example.trasstarea.basedatos.BaseDatos;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EstadisticasActivity extends AppCompatActivity {

    private BaseDatos baseDatos;
    private int progresoMedio,noIniciada,iniciada,avanzada,casiFinalizada,finalizada,tareasEmpezadas,tareasTerminadas,tareasCaducadas,tareasPrioritarias;
    private Handler handler= new Handler();

    String fechaObjetivoAVGString ;
    private TextView tvProgresoAVG,tvNoIniciado,tvIniciada,tvAvanzada,tvCasiFinalizada,tvFinalizada,tvFechaAVG,tvFechaEmpieza,tvFechatermina,tvTareasCaducadas,tvTareasPrioritarias;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        baseDatos=BaseDatos.getInstance(getApplicationContext());

        tvProgresoAVG=findViewById(R.id.tv_progesoAVG);
        tvNoIniciado=findViewById(R.id.tv_no_inicializada);
        tvIniciada=findViewById(R.id.tv_iniciada);
        tvAvanzada=findViewById(R.id.tv_avanzada);
        tvCasiFinalizada=findViewById(R.id.tv_casi_finalizada);
        tvFinalizada=findViewById(R.id.tv_finalizada);
        tvFechaAVG=findViewById(R.id.tv_fecha_objetivoAVG);
        tvFechaEmpieza=findViewById(R.id.tv_tareas_empezadas);
        tvFechatermina=findViewById(R.id.tv_tareas_terminan);
        tvTareasCaducadas=findViewById(R.id.tv_tareas_caducadas);
        tvTareasPrioritarias=findViewById(R.id.tv_tareas_prioritarias);

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new EstadisticasActivity.ObtenerProgresoMedio());
        executor.execute(new EstadisticasActivity.ObtenerNumeroTareasProgreso());
        executor.execute(new EstadisticasActivity.ObtenerFechaObjetivoAVG());
        executor.execute(new EstadisticasActivity.ObtenerFechaTerminaAcaba());
        executor.execute(new EstadisticasActivity.ObtenerPrioritarias());

    }

    class ObtenerProgresoMedio implements Runnable {

        @Override
        public void run() {
            progresoMedio= baseDatos.productoDAO().obtenerProgresoMedio();
            handler.post(() -> {
                tvProgresoAVG.setText(getString(R.string.promedio_progreso) +" "+ progresoMedio+" %");
            });
        }
    }
    class ObtenerPrioritarias implements Runnable {

        @Override
        public void run() {
            tareasPrioritarias= baseDatos.productoDAO().obtenerNumeroTareasPrioritarias();
            handler.post(() -> {
                tvTareasPrioritarias.setText(getString(R.string.tarea_prioritaria) +" : "+ tareasPrioritarias);
            });
        }
    }

    class ObtenerNumeroTareasProgreso implements Runnable {

        @Override
        public void run() {
            noIniciada= baseDatos.productoDAO().contarTareasProgreso0();
            iniciada= baseDatos.productoDAO().contarTareasProgreso25();
            avanzada= baseDatos.productoDAO().contarTareasProgreso50();
            casiFinalizada= baseDatos.productoDAO().contarTareasProgreso75();
            finalizada= baseDatos.productoDAO().contarTareasProgreso100();

            handler.post(() -> {
                tvNoIniciado.setText("-"+getString(R.string.estado_no_iniciada) +" : "+ noIniciada);
                tvIniciada.setText("-"+getString(R.string.estado_iniciada) +" : "+ iniciada);
                tvAvanzada.setText("-"+getString(R.string.estado_avanzada) +" : "+ avanzada);
                tvCasiFinalizada.setText("-"+getString(R.string.estado_casi_finalizada) +" : "+ casiFinalizada);
                tvFinalizada.setText("-"+getString(R.string.estado_finalizada) +" : "+ finalizada);
            });
        }
    }
    class ObtenerFechaObjetivoAVG implements Runnable {

        @Override
        public void run() {
            long fechaObjetivoAVGSeconds = baseDatos.productoDAO().obtenerFechaObjetivoPromedio();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Date fecha = Date.from(Instant.ofEpochMilli(fechaObjetivoAVGSeconds));
                SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                 fechaObjetivoAVGString = formatoFecha.format(fecha);
            }else{
                fechaObjetivoAVGString=getString(R.string.error_fecha);
            }

            handler.post(() -> {
                tvFechaAVG.setText(getString(R.string.fecha_promedio) +" : "+ fechaObjetivoAVGString);

            });
        }
    }
    class ObtenerFechaTerminaAcaba implements Runnable {

        @Override
        public void run() {
            tareasEmpezadas= baseDatos.productoDAO().obtenerNumeroTareasEmpezadasEsteMes();
            tareasTerminadas=baseDatos.productoDAO().obtenerNumeroTareasFinalizanEsteMes();
            tareasCaducadas=baseDatos.productoDAO().obtenerNumeroTareasCaducadas();

            handler.post(() -> {
                tvFechaEmpieza.setText(getString(R.string.fecha_empieza) +" : "+ tareasEmpezadas);
                tvFechatermina.setText(getString(R.string.fecha_termina) +" : "+ tareasTerminadas);
                tvTareasCaducadas.setText(getString(R.string.tarea_caducada) +" : "+ tareasCaducadas);

            });
        }
    }
}