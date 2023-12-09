package com.example.trasstarea;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trasstarea.Adaptador.TareaAdapter;
import com.example.trasstarea.Datos.Tarea;

import java.util.ArrayList;
import java.util.Objects;

public class ListadoTareasActivity extends AppCompatActivity implements TareaAdapter.OnDataChangeListener {
    private ArrayList<Tarea> datos=new ArrayList<>();
    private RecyclerView rv;
    private TextView tvLista;
    private boolean mostrarSoloPrioritarias=false,estadodialogo=false;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_tareas);

        if (savedInstanceState != null) {//si no es la primera vez que se ejecuta
            datos = savedInstanceState.getParcelableArrayList("datos");
            mostrarSoloPrioritarias = savedInstanceState.getBoolean("mostrarSoloPrioritarias");
            estadodialogo=savedInstanceState.getBoolean("estadoDialogo");
        } else {
            init(); // Solo inicializa datos si no hay datos guardados en el Bundle
        }
        if(estadodialogo){
            mostrarAcercaDe();
        }

        // Configurar el RecyclerView y su adaptador
        TareaAdapter adaptador = new TareaAdapter(this, datos);
        adaptador.setMostrarSoloPrioritarias(mostrarSoloPrioritarias); // mostramos aquellas que sean favoritas
        adaptador.setOnDataChangeListener(this); // Establece la actividad como escucha de cambios

        rv = findViewById(R.id.rv_lista);
        rv.setAdapter(adaptador);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        tvLista = findViewById(R.id.tv_lista);
        actualizarEstadoTextView();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // GUARDAR DATOS Y ESTADO DEL ADAPTADOR
        outState.putParcelableArrayList("datos", datos);
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
                        TareaAdapter adaptador = (TareaAdapter) rv.getAdapter();
                        if(adaptador!=null){
                            adaptador.agregarNuevaTarea(tarea);
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
                        TareaAdapter adaptador = (TareaAdapter) rv.getAdapter();
                        if(adaptador!=null){
                            adaptador.editarLista(tarea,pos);
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
            adaptador.setMostrarSoloPrioritarias(mostrarSoloPrioritarias);
            actualizarEstadoTextView();

            if (mostrarSoloPrioritarias) {
                item.setIcon(android.R.drawable.btn_star_big_on);
            } else {
                item.setIcon(android.R.drawable.btn_star_big_off);
            }

        //opcion de menu para acerca de
        } else if (id == R.id.it_acerca) {
            mostrarAcercaDe();

        //opcion de menu al salir
        } else if (id == R.id.it_salir) {
            Toast.makeText(getApplicationContext(), getString(R.string.salir), Toast.LENGTH_LONG).show();
            finishAffinity();
        }

        return super.onOptionsItemSelected(item);
    }

    //metodo que recibe el objeto y la posicion del mismo en la lista
    @Override
    public void onTareaEdit(Tarea tarea, int posicion) {
        irEditarTarea(tarea);
        pos=posicion;
    }

    //Metodo que llama al lanzador de editartarea
    public void irEditarTarea(Tarea tarea) {
        Intent intentIda = new Intent(this, EditarTareaActivity.class);
        intentIda.putExtra("objeto", tarea);
        lanzadorEditar.launch(intentIda);
    }


    //metodo usado cada vez que un dato cambia, llama al metodo actualizar estado
    @Override
    public void onDataChanged() {
        actualizarEstadoTextView();
    }

    //metodo que actualiza el estado de las tareas
    private void actualizarEstadoTextView() {
        if (mostrarSoloPrioritarias) {
            if (hayTareasPrioritarias()) {
                tvLista.setText(""); // Si hay tareas prioritarias, no mostrar mensaje
            } else {
                tvLista.setText(getString(R.string.estado_tareas));
            }
        } else {
            if (datos.isEmpty()) {
                tvLista.setText(getString(R.string.estado_tareas));
            } else {
                tvLista.setText(""); // Limpiar el texto si hay tareas
            }
        }
    }
//MEtodo que nos indica si hay tareas prioritarias
    private boolean hayTareasPrioritarias() {
        for (Tarea tarea : datos) {
            if (tarea.isPrioritario()) {
                return true;
            }
        }
        return false;
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
    //Inciador de la lista
    private void init(){

            datos.add(new Tarea("PSP", "realizar una tarea de psp", 23, "12/03/2023", "12/11/2023", true));
            datos.add(new Tarea("Moviles", "realizar una tarea de para moviles", 50, "20/07/2023", "23/12/2023", true));
            datos.add(new Tarea("Empresas", "realizar una tarea para empresas", 10, "30/03/2023", "30/12/2023", true));
            datos.add(new Tarea("Acceso a datos", "realizar una tarea de acceso a datos", 78, "02/11/2023", "01/11/2024", true));
            datos.add(new Tarea("Desarrollo de interfaces", "realizar una tarea de desarrollo de interfaces", 0, "19/08/2023", "25/11/2023", true));
            datos.add(new Tarea("Sistemas de gestion", "realizar una tarea de sistemas de gestion", 100, "23/11/2023", "12/12/2023", true));
            datos.add(new Tarea("tarea de limpieza", "limpiar el cuarto", 89, "11/10/2023", "12/01/2024", false));
            datos.add(new Tarea("Ir a comprar", "realizar una compra", 50, "12/11/2023", "30/11/2023", false));
            datos.add(new Tarea("Hacer un pedido", "hacer un pedido", 8, "12/11/2023", "02/11/2023", true));
            datos.add(new Tarea("Hacer la comida", "hacer la comida", 100, "12/04/2023", "02/12/2023", false));
            datos.add(new Tarea("Completar trabajo", "Completar trabajo final", 45, "23/09/2023", "02/02/2024", false));
            datos.add(new Tarea("Desarrollo Web", "Crear un sitio web responsive", 25, "02/04/2023", "20/04/2023", false));
            datos.add(new Tarea("Redes de Computadoras", "Configurar una red local", 30, "10/05/2023", "30/05/2023", true));
            datos.add(new Tarea("Inteligencia Artificial", "Implementar un algoritmo de aprendizaje supervisado", 40, "01/06/2023", "15/07/2023", false));
            datos.add(new Tarea("Sistemas Operativos", "Estudiar el manejo de procesos", 20, "08/04/2023", "01/05/2023", false));
            datos.add(new Tarea("Seguridad Informática", "Realizar un análisis de vulnerabilidades", 15, "18/06/2023", "25/06/2023", true));

    }
}