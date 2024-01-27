package com.example.trasstarea;

import static android.app.PendingIntent.getActivity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trasstarea.Adaptador.TareaAdapter;
import com.example.trasstarea.Datos.Tarea;
import com.example.trasstarea.basedatos.BaseDatos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ListadoTareasActivity extends AppCompatActivity {
    private List<Tarea> datos=new ArrayList<>();
    private RecyclerView rv;
    private TextView tvLista;
    private boolean mostrarSoloPrioritarias=false,estadodialogo=false,hayprioritarias;
    private int pos,numTareas;
    private BaseDatos baseDatos;
    private TareaAdapter adaptador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_tareas);

        baseDatos=BaseDatos.getInstance(getApplicationContext());
        if (savedInstanceState != null) {//si no es la primera vez que se ejecuta

            mostrarSoloPrioritarias = savedInstanceState.getBoolean("mostrarSoloPrioritarias");
            estadodialogo=savedInstanceState.getBoolean("estadoDialogo");
        }
        if(estadodialogo){
            mostrarAcercaDe();
        }

        adaptador = new TareaAdapter(this, datos);


        rv = findViewById(R.id.rv_lista);
        rv.setAdapter(adaptador);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        tvLista = findViewById(R.id.tv_lista);

        if(mostrarSoloPrioritarias){
            mostrarPrioritarias();
            actualizarEstadoTextView();
        }else{
            mostrarTodas();
            actualizarEstadoTextView();
        }

        actualizarEstadoTextView();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // GUARDAR DATOS Y ESTADO DEL ADAPTADOR
        //outState.putParcelableArrayList("datos", datos);
        outState.putBoolean("estadoDialogo", estadodialogo);
        outState.putBoolean("mostrarSoloPrioritarias", mostrarSoloPrioritarias);
    }

    ActivityResultLauncher<Intent> lanzadorActividades = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                //Método para gestionar el resultado
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        //No hay códigos de actividad
                        Intent intentDevuelto = result.getData();
                        assert intentDevuelto != null;
                        Tarea tarea = (Tarea) Objects.requireNonNull(intentDevuelto.getExtras()).get("Resultado");
                        adaptador = (TareaAdapter) rv.getAdapter();
                        if(adaptador!=null){

                            Executor executor = Executors.newSingleThreadExecutor();
                            executor.execute(new InsertarTarea(tarea));

                            actualizarEstadoTextView();
                        }
                        Toast.makeText(getApplicationContext(), getString(R.string.tarea_guardada), Toast.LENGTH_LONG).show();
                    }
                }
            });

    ActivityResultLauncher<Intent> lanzadorEditar = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                //Método para gestionar el resultado
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        //No hay códigos de actividad
                        Intent intentDevuelto = result.getData();
                        assert intentDevuelto != null;
                        Tarea tarea = (Tarea) Objects.requireNonNull(intentDevuelto.getExtras()).get("Resultado");
                        adaptador = (TareaAdapter) rv.getAdapter();
                        if(adaptador!=null){
                            Log.d("ActualizarTarea", "Actualizando tarea: " + tarea.getTitulo());
                            Log.d("Arriba ID", "Actualizando tarea: " + tarea.getId()); // Agrega un log
                            Executor executor = Executors.newSingleThreadExecutor();
                            executor.execute(new ActualizarTarea(tarea));
                            adaptador.notifyDataSetChanged();

                        }
                        Toast.makeText(getApplicationContext(), getString(R.string.tarea_editada), Toast.LENGTH_LONG).show();
                    }
                }
            });


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        menu.setGroupVisible(R.id.it_group_añadir,true);
        menu.setGroupVisible(R.id.it_group_favoritos,true);
        menu.setGroupVisible(R.id.it_group_menufinal,true);
        MenuItem favoritosItem = menu.findItem(R.id.it_favoritos);

        // Cambiar el ícono del elemento del menú según la variable mostrarSoloPrioritarias
        if (mostrarSoloPrioritarias) {
            favoritosItem.setIcon(android.R.drawable.btn_star_big_on);
        } else {
            favoritosItem.setIcon(android.R.drawable.btn_star_big_off);
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();



        //opcion de menu añadir tarea
        if (id == R.id.it_añadir) {

            Intent intentIda = new Intent(this, CrearTareaActivity.class);

            lanzadorActividades.launch(intentIda);

        //opcion de menu tareas prioritarias
        } else if (id == R.id.it_favoritos) {

            TareaAdapter adaptador = (TareaAdapter) rv.getAdapter();

            // Cambiar entre tareas prioritarias y todas las tareas
            mostrarSoloPrioritarias = !mostrarSoloPrioritarias;
            actualizarEstadoTextView();

            if (mostrarSoloPrioritarias) {
                item.setIcon(android.R.drawable.btn_star_big_on);
                mostrarPrioritarias();
            } else {
                item.setIcon(android.R.drawable.btn_star_big_off);
                mostrarTodas();
            }

        //opcion de menu para acerca de
        } else if (id == R.id.it_acerca) {
            mostrarAcercaDe();

        //opcion de menu al salir
        } else if (id == R.id.it_salir) {
            Toast.makeText(getApplicationContext(), getString(R.string.salir), Toast.LENGTH_LONG).show();
            finishAffinity();
        }else if(id==R.id.it_estadisticas){
            Intent intent = new Intent(ListadoTareasActivity.this, EstadisticasActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    //Metodo que llama al lanzador de editartarea
    public void irEditarTarea(Tarea tarea) {
        Intent intentIda = new Intent(this, EditarTareaActivity.class);
        intentIda.putExtra("objeto", tarea);
        lanzadorEditar.launch(intentIda);
    }

    public void mostrarPrioritarias(){
        baseDatos.productoDAO().getTareasFavoritas().observe(this, new Observer<List<Tarea>>() {
            @Override
            public void onChanged(List<Tarea> tareas) {
                datos=tareas;
                adaptador.setDatos(datos);
                adaptador.notifyDataSetChanged();
                actualizarEstadoTextView();
            }
        });
    }

    public void mostrarTodas(){
        baseDatos.productoDAO().getAll().observe(this, new Observer<List<Tarea>>() {
            @Override
            public void onChanged(List<Tarea> tareas) {
                if(!mostrarSoloPrioritarias) {
                    datos = tareas;
                    adaptador.setDatos(datos);
                    adaptador.notifyDataSetChanged();
                    actualizarEstadoTextView();
                }
            }
        });
    }


    //metodo que actualiza el estado de las tareas
    private void actualizarEstadoTextView() {

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                HayPrioritarias hayPrioritarias = new HayPrioritarias();
                HayTareas hayTareas = new HayTareas();

                hayPrioritarias.run();
                hayTareas.run();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mostrarSoloPrioritarias) {
                            if (hayprioritarias) {
                                tvLista.setText(""); // Si hay tareas prioritarias, no mostrar mensaje
                            } else {
                                tvLista.setText(getString(R.string.estado_tareas));
                            }
                        } else {
                            if (numTareas == 0) {
                                tvLista.setText(getString(R.string.estado_tareas));
                            } else {
                                tvLista.setText(""); // Limpiar el texto si hay tareas
                            }
                        }
                    }
                });
            }
        });
    }


    //por el usuario.
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int posicion = -1;
        try {
            posicion = adaptador.getPosicion();
        } catch (Exception e) {
            return super.onContextItemSelected(item);
        }
        if(item.getItemId() == R.id.menu_borrar){
            //toamamos el objeto que estamos pulsando
            Tarea tarea = adaptador.getDatos().get(posicion);
            //Se recupera el objeto a borrar desde la lista del adaptador.
            
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.borrar_tarea)+tarea.getTitulo()+"?")
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Acción a realizar si la respuesta es Sí
                            Executor executor = Executors.newSingleThreadExecutor();
                            executor.execute(new BorrarTarea(tarea));
                            if(mostrarSoloPrioritarias){
                                mostrarPrioritarias();
                            }
                            // Espera un breve momento para que el RecyclerView se actualice
                            actualizarEstadoTextView();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Acción a realizar si la respuesta es No
                        }
                    });

            // Mostrar el cuadro de diálogo
            builder.create().show();


        }else if(item.getItemId()==R.id.menu_Editar){
            Tarea tarea=adaptador.getDatos().get(posicion);

            irEditarTarea(tarea);
        }else if(item.getItemId()==R.id.menu_descripcion){
            Tarea tarea=adaptador.getDatos().get(posicion);
            mostrarDescripcion(tarea);
        }
        return super.onContextItemSelected(item);
    }

    private void mostrarAcercaDe() {
        // Crear un cuadro de diálogo para Acerca de
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.acercade));

        // Construir el contenido del cuadro de diálogo
        String acercaDeTexto = "TrassTarea\n" +
                getString(R.string.centro) + ": IES Trassierra\n" +
                getString(R.string.autor) + ": Aitor García Curado\n" +
                "@ 2023 ";

        builder.setMessage(acercaDeTexto);
        estadodialogo=true;

        // Agregar un botón "Aceptar" al cuadro de diálogo
        builder.setPositiveButton(getString(R.string.boton_aceptar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Cerrar el cuadro de diálogo al hacer clic en "Aceptar"
                estadodialogo=false;
                dialog.dismiss();
            }
        });

        // Mostrar el cuadro de diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void mostrarDescripcion(Tarea tarea) {
        // Crear un cuadro de diálogo para Acerca de
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.menu_descripcion));

        // Construir el contenido del cuadro de diálogo
        String descripcionTexto = tarea.getDescripcion();

        builder.setMessage(descripcionTexto);
        estadodialogo=true;

        // Agregar un botón "Aceptar" al cuadro de diálogo
        builder.setPositiveButton(getString(R.string.boton_aceptar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Cerrar el cuadro de diálogo al hacer clic en "Aceptar"
                estadodialogo=false;
                dialog.dismiss();
            }
        });

        // Mostrar el cuadro de diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    class InsertarTarea implements Runnable {

        private Tarea tarea;

        public InsertarTarea(Tarea tarea) {
            this.tarea = tarea;
        }

        @Override
        public void run() {
            baseDatos.productoDAO().insertAll(tarea);
        }
    }
    class ActualizarTarea implements Runnable {

        private Tarea tarea;

        public ActualizarTarea(Tarea tarea) {
            this.tarea = tarea;
        }

        @Override
        public void run() {

            baseDatos.productoDAO().update(tarea);
        }
    }

    class BorrarTarea implements Runnable {

        private Tarea tarea;

        public BorrarTarea(Tarea tarea) {
            this.tarea = tarea;
        }

        @Override
        public void run() {
            baseDatos.productoDAO().delete(tarea);
        }
    }

    class HayPrioritarias implements Runnable {

        @Override
        public void run() {
            hayprioritarias= baseDatos.productoDAO().hayTareasPrioritarias();

        }
    }
    class HayTareas implements Runnable {

        @Override
        public void run() {
            numTareas= baseDatos.productoDAO().contarTareas();

        }
    }

}