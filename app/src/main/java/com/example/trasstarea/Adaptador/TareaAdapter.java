package com.example.trasstarea.Adaptador;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
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
    private boolean mostrarSoloPrioritarias;
    Context contexto;

    public TareaAdapter(Context contexto,ArrayList<Tarea> datos) {
        this.datos = datos;
        this.contexto = contexto;
    }
    public void setMostrarSoloPrioritarias(boolean mostrarSoloPrioritarias) {
        this.mostrarSoloPrioritarias = mostrarSoloPrioritarias;
        notifyDataSetChanged(); // Actualiza la vista cuando cambia el filtro
    }
    public void ocultarVista(RecyclerView.ViewHolder holder) {
        holder.itemView.setVisibility(View.GONE);
        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
    }
    public void mostrarTarea(RecyclerView.ViewHolder holder, Tarea tarea) {
        holder.itemView.setVisibility(View.VISIBLE);
        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        ((TareaViewHolder) holder).bindTarea(tarea);
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
        Tarea tarea = datos.get(position);
        if (mostrarSoloPrioritarias && !tarea.isPrioritario()) {
            // Si se debe mostrar solo tareas prioritarias y la tarea no es prioritaria, oculta el ViewHolder
            ocultarVista(holder);
        } else {
            // Muestra la tarea si no es necesario aplicar el filtro
            mostrarTarea(holder, tarea);
        }
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
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fecha.setText(formatoFecha.format(t.getFechaCreacion()));

            int diasRestantes = t.getDiasRestantes();

            if (diasRestantes < 0) {// Si los días restantes son negativos, cambia el color del texto a rojo
                dias.setTextColor(Color.RED);
            } else {// Si los días restantes son no negativos, establece el color del texto en su valor predeterminado
                dias.setTextColor(Color.BLACK); // O el color que desees
            }

            dias.setText(String.valueOf(diasRestantes));

            if (t.getProgreso() == 100) { // Si el progreso es 100, establece la barra de progreso en verde
                progreso.getProgressDrawable().setColorFilter(
                        Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);

                // Pone el texto tachado en tv_tareas
                tarea.setPaintFlags(tarea.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                // Establece los días restantes en 0
                dias.setText("0");

            } else {
                // En otros casos, establece la barra de progreso en su color predeterminado
                //TODO porque cambia todo si borro estas lineas
                progreso.getProgressDrawable().setColorFilter(
                        Color.GRAY, android.graphics.PorterDuff.Mode.SRC_IN);

                // Restaura el estilo del texto
                tarea.setPaintFlags(0);

            }

            if (t.isPrioritario()) {
                prioritaria.setImageResource(R.drawable.baseline_stars_24);
                tarea.setTypeface(null, Typeface.BOLD);
            } else {
                prioritaria.setImageResource(R.drawable.baseline_stars_black);
            }
            progreso.setProgress(t.getProgreso());

        }
    }

}
