package com.example.trasstarea.Adaptador;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trasstarea.Datos.Tarea;
import com.example.trasstarea.EditarTareaActivity;
import com.example.trasstarea.ListadoTareasActivity;
import com.example.trasstarea.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.TareaViewHolder>{
    private ArrayList<Tarea> datos;
    private boolean mostrarSoloPrioritarias;
    private OnDataChangeListener onDataChangeListener;
    Context contexto;
    private TextView tvLista;

    public TareaAdapter(Context contexto,ArrayList<Tarea> datos) {
        this.datos = datos;
        this.contexto = contexto;
    }
    public void setMostrarSoloPrioritarias(boolean mostrarSoloPrioritarias) {
        this.mostrarSoloPrioritarias = mostrarSoloPrioritarias;
        notifyDataSetChanged(); // Actualiza la vista cuando cambia el filtro
    }

    public void ocultarVista(TareaViewHolder holder) {
        holder.itemView.setVisibility(View.GONE);
        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        if (onDataChangeListener != null) {
            onDataChangeListener.onDataChanged(); // Notifica a la actividad que los datos han cambiado
        }
    }

    public void agregarNuevaTarea(Tarea nuevaTarea) {
        datos.add(nuevaTarea);
        notifyItemInserted(datos.size()-1); // Notifica al adaptador que se ha insertado un nuevo ítem
        if (onDataChangeListener != null) {
            onDataChangeListener.onDataChanged(); // Notifica a la actividad que los datos han cambiado
        }
    }
    public void mostrarTarea(TareaViewHolder holder, Tarea tarea) {
        holder.itemView.setVisibility(View.VISIBLE);
        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
         holder.bindTarea(tarea);
        if (onDataChangeListener != null) {
            onDataChangeListener.onDataChanged(); // Notifica a la actividad que los datos han cambiado
        }
    }
    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitemtarea,parent,false);
        TareaViewHolder tarea = new TareaViewHolder(item);
        return tarea;
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        Tarea tarea = datos.get(position);
        if (mostrarSoloPrioritarias && !tarea.isPrioritario()) {
            // Si se debe mostrar solo tareas prioritarias y la tarea no es prioritaria, oculta el ViewHolder
            ocultarVista(holder);
        } else {
            // Muestra la tarea si no es necesario aplicar el filtro
            mostrarTarea(holder, tarea);
        }
    }
    public void setOnDataChangeListener(OnDataChangeListener listener) {
        this.onDataChangeListener = listener;

    }

    @Override
    public int getItemCount() {
        //Devolvemos el tamaño de array de datos de Capitales
        return datos.size();
    }

    public interface OnDataChangeListener {
        void onDataChanged();
        void onTareaEdit(Tarea tarea,int posicion);

    }
    public void editarLista(Tarea tareaEditada, int posicion) {
        if (posicion >= 0 && posicion < datos.size()) {
            datos.set(posicion, tareaEditada);
            notifyItemChanged(posicion); // Notifica al adaptador que el ítem en la posición dada ha cambiado
            if (onDataChangeListener != null) {
                onDataChangeListener.onDataChanged(); // Notifica a la actividad que los datos han cambiado
            }
        }
    }

    public class TareaViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        private TextView tarea,fecha,dias;
        private ImageView prioritaria;
        private ProgressBar progreso;


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

            // Agrega listeners para cada elemento del menú
            menu.findItem(R.id.menu_descripcion).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                    mostrarDescripcionDialog();
                    return true;
                }
            });

            menu.findItem(R.id.menu_Editar).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                    onDataChangeListener.onTareaEdit(datos.get(getAdapterPosition()),getAdapterPosition());
                    return true;
                }
            });

            menu.findItem(R.id.menu_borrar).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                    eliminarTarea(getAdapterPosition());

                    return true;
                }
            });
        }


        public void eliminarTarea(int position) {
            if (position >= 0 && position < datos.size()) {
                datos.remove(position);
                notifyItemRemoved(position);
                showToast("Tarea borrada");
                if (onDataChangeListener != null) {
                    onDataChangeListener.onDataChanged(); // Notifica a la actividad que los datos han cambiado
                }
            }
        }

        // Método para mostrar el cuadro de diálogo de descripción
        private void mostrarDescripcionDialog() {
            // Obtén la tarea actualmente seleccionada (puedes adaptar esto según tu lógica)
            Tarea tarea = datos.get(getAdapterPosition());

            // Crea un cuadro de diálogo
            AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
            builder.setTitle("Descripción de la tarea");
            builder.setMessage(tarea.getDescripcion());
            builder.setPositiveButton("Aceptar", null);  // Puedes agregar botones adicionales según sea necesario
            builder.show();
        }

        private void showToast(String message) {
            Toast.makeText(itemView.getContext(), message, Toast.LENGTH_SHORT).show();
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
