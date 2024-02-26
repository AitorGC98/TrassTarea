package com.example.trasstarea.Adaptador;





import static android.provider.Settings.System.getString;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.trasstarea.Datos.Tarea;
import com.example.trasstarea.ListadoTareasActivity;
import com.example.trasstarea.MostrarTarea;
import com.example.trasstarea.R;
import com.example.trasstarea.basedatos.BaseDatos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.TareaViewHolder>{
    private List<Tarea> datos;
    Context contexto;
    private int posicion;
    private boolean primeraEjecucion=true;
    private  int pos;


    public TareaAdapter(Context contexto,List<Tarea> datos) {
        this.datos = datos;
        this.contexto = contexto;
    }

    public List<Tarea> getDatos() {
        return datos;
    }

    public void setDatos(List<Tarea> datos) {
        this.datos = datos;
        notifyDataSetChanged();

    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitemtarea,parent,false);
        return new TareaViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        ((TareaViewHolder) holder).bindTarea(datos.get(position));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosicion(holder.getAdapterPosition());
                return false;
            }
        });
        //Animation slideInAnimation = AnimationUtils.loadAnimation(contexto, R.anim.recylcer_anim);
        //holder.itemView.startAnimation(slideInAnimation);
    }

    @Override
    public int getItemCount() {
        //Devolvemos el tamaño de array de datos de Capitales
        return datos.size();
    }

    public class TareaViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        private final TextView tarea;
        private final TextView fecha;
        private final TextView dias;
        private final ImageView prioritaria;
        private final ProgressBar progreso;


        //Método constructor
        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);

            tarea = itemView.findViewById(R.id.tv_tarea);
            fecha = itemView.findViewById(R.id.tv_fecha);
            dias = itemView.findViewById(R.id.tv_dias);
            prioritaria = itemView.findViewById(R.id.iv_prioritaria);
            progreso = itemView.findViewById(R.id.pb_progreso);
            itemView.setOnCreateContextMenuListener(this);
        }
        // Este método se llama cuando se crea el menú contextual
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            // Obtén el inflater del contexto
            MenuInflater inflater = new MenuInflater(contexto);
            inflater.inflate(R.menu.menu_contextual, menu);
        }


        // Método para mostrar el cuadro de diálogo de descripción
        private void mostrarDescripcionDialog() {
            // Obtén la tarea actualmente seleccionada (puedes adaptar esto según tu lógica)
            Tarea tarea = datos.get(getAdapterPosition());

            // Crea un cuadro de diálogo
            AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
            builder.setTitle(contexto.getString(R.string.descripcion_tarea));
            builder.setMessage(tarea.getDescripcion());
            builder.setPositiveButton(contexto.getString(R.string.boton_aceptar), null);

            builder.show();
        }

        public void bindTarea(Tarea t) {

            // Agregar un OnClickListener al itemView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Obtener la tarea actualmente seleccionada
                    Tarea tareaSeleccionada = datos.get(getAdapterPosition());
                     pos= getAdapterPosition();
                    // Crear un Intent para iniciar la nueva actividad
                    Intent intent = new Intent(contexto, MostrarTarea.class);
                    intent.putExtra("tarea", tareaSeleccionada);
                    contexto.startActivity(intent);
                }
            });

            tarea.setText(t.getTitulo());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
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
                progreso.getProgressDrawable().setColorFilter(
                        Color.GRAY, android.graphics.PorterDuff.Mode.SRC_IN);

                // Restaura el estilo del texto
                tarea.setPaintFlags(0);

            }

            if (t.isPrioritario()) {
                prioritaria.setImageResource(R.drawable.baseline_star_24);
                tarea.setTypeface(null, Typeface.BOLD);
            } else {
                prioritaria.setImageResource(R.drawable.baseline_star_outline_24);
            }
            progreso.setProgress(t.getProgreso());

            // Verificar si es la primera ejecución y ejecutar la animación
            if (primeraEjecucion) {
                primeraEjecucion = false;
                Animation slideInAnimation = AnimationUtils.loadAnimation(contexto, R.anim.recylcer_anim);
                itemView.startAnimation(slideInAnimation);
            } else {
                // Verificar si la posición actual es mayor que la anterior
                if (getAdapterPosition() > pos) {
                    // Si es mayor, se ha añadido una nueva tarea, ejecutar la animación
                    Animation slideInAnimation = AnimationUtils.loadAnimation(contexto, R.anim.recylcer_anim);
                    itemView.startAnimation(slideInAnimation);
                    // Actualizar la posición
                    pos = getAdapterPosition();
                }
            }


        }

    }

}
