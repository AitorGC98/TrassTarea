package com.example.trasstarea.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trasstarea.Datos.Tarea;
import com.example.trasstarea.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TareaAdapter extends RecyclerView.Adapter{
    private ArrayList<Tarea> datos;
    Context contexto;

    public TareaAdapter(Context contexto,ArrayList<Tarea> datos) {
        this.datos = datos;
        this.contexto = contexto;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitemtarea,parent,false);
        TareaViewHolder tarea = new TareaViewHolder(item);
        return tarea;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //Asignamos el dato del array correspondiente a la posición actual al
        //objeto ViewHolder, de forma que se represente en el RecyclerView.
        ((TareaViewHolder) holder).bindTarea(datos.get(position));
    }

    @Override
    public int getItemCount() {
        //Devolvemos el tamaño de array de datos de Capitales
        return datos.size();
    }

    public class TareaViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener */{

        private TextView tarea,fecha,dias;
        private ImageView prioritaria;
        private ProgressBar progreso;

        //Método constructor
        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);
            //itemView.setOnClickListener(this);
            tarea = itemView.findViewById(R.id.tv_tarea);
            fecha = itemView.findViewById(R.id.tv_fecha);
            dias = itemView.findViewById(R.id.tv_dias);
            prioritaria = itemView.findViewById(R.id.iv_prioritaria);
            progreso = itemView.findViewById(R.id.pb_progreso);
        }
        public void bindTarea(Tarea t) {
            tarea.setText(t.getTitulo());
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");//convierte a string

            fecha.setText(formatoFecha.format(t.getFechaCreacion()));

            dias.setText(String.valueOf(t.getDiasRestantes()));
            if(t.isPrioritario()){
                prioritaria.setImageResource(R.drawable.baseline_stars_24);
            }else{
                prioritaria.setImageResource(R.drawable.baseline_stars_black);
            }
            progreso.setProgress(t.getProgreso());

        }
    }

}
