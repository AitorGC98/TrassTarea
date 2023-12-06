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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trasstarea.Adaptador.TareaAdapter;
import com.example.trasstarea.Datos.Tarea;

import  java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class ListadoTareasActivity extends AppCompatActivity {
    private ArrayList<Tarea> datos=new ArrayList<>();
    private RecyclerView rv;
    private TextView tvLista;
    private boolean mostrarSoloPrioritarias=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_tareas);
        init();

        TareaAdapter adaptador = new TareaAdapter(this,datos);
        rv=findViewById(R.id.rv_lista);
        rv.setAdapter(adaptador);
        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));


        tvLista=findViewById(R.id.tv_lista);
        if(datos.isEmpty()){
            tvLista.setText("No hay tareas disponibles");
        }
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
                        Toast.makeText(getApplicationContext(), "Se ha guaradado una nueva tarea", Toast.LENGTH_LONG).show();
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

        if (id == R.id.it_añadir) {

            Intent intentIda = new Intent(this, CrearTareaActivity.class);

            // Pasa la lista datos como extra al Intent
            //intentIda.putExtra("lista", datos);
            lanzadorActividades.launch(intentIda);

        } else if (id == R.id.it_favoritos) {
            TareaAdapter adaptador = (TareaAdapter) rv.getAdapter();

            // Cambiar entre tareas prioritarias y todas las tareas
            mostrarSoloPrioritarias = !mostrarSoloPrioritarias;
            adaptador.setMostrarSoloPrioritarias(mostrarSoloPrioritarias);
        } else if (id == R.id.it_acerca) {
            mostrarAcercaDe();
        } else if (id == R.id.it_salir) {
            finishAffinity();
        }

        return super.onOptionsItemSelected(item);
    }

    private void mostrarAcercaDe() {
        // Crear un cuadro de diálogo para Acerca de
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Acerca de");

        // Construir el contenido del cuadro de diálogo
        StringBuilder acercaDeTexto = new StringBuilder();
        acercaDeTexto.append("TrassTarea\n");
        acercaDeTexto.append("Centro: IES Trassierra\n");
        acercaDeTexto.append("Autor/a: Aitor García Curado\n");
        acercaDeTexto.append("@ 2023 ");

        builder.setMessage(acercaDeTexto.toString());

        // Agregar un botón "Aceptar" al cuadro de diálogo
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Cerrar el cuadro de diálogo al hacer clic en "Aceptar"
                dialog.dismiss();
            }
        });

        // Mostrar el cuadro de diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void init(){

            datos.add(new Tarea("PSP", "realizar una tarea de psp", 23, "12/03/2023", "12/11/2023", true));
            datos.add(new Tarea("Moviles", "realizar una tarea de para moviles", 50, "20/07/2023", "23/12/2023", true));
            datos.add(new Tarea("Empresas", "realizar una tarea para empresas", 10, "30/03/2023", "30/12/2023", true));
            datos.add(new Tarea("Acceso a datos", "realizar una tarea de acceso a datos", 78, "02/11/2023", "01/11/2024", true));
            datos.add(new Tarea("Desarrollo de interfaces", "realizar una tarea de desarrollo de interfaces", 0, "19/08/2023", "25/11/2023", true));
            datos.add(new Tarea("Sistemas de gestion", "realizar una tarea de sistemas de gestion", 100, "23/11/2023", "12/12/2023", true));
            datos.add(new Tarea("tarea de limpieza", "limpiar el cuarto", 89, "11/10/2023", "12/01/2024", false));
            /*datos.add(new Tarea("Ir a comprar", "realizar una compra", 50, "12/11/2023", "30/11/2023", false));
            datos.add(new Tarea("Hacer un pedido", "hacer un pedido", 8, "12/11/2023", "02/11/2023", true));
            datos.add(new Tarea("Hacer la comida", "hacer la comida", 100, "12/04/2023", "02/12/2023", false));
            datos.add(new Tarea("Completar trabajo", "Completar trabajo final", 45, "23/09/2023", "02/02/2024", false));
            datos.add(new Tarea("Desarrollo Web", "Crear un sitio web responsive", 25, "02/04/2023", "20/04/2023", false));
            datos.add(new Tarea("Redes de Computadoras", "Configurar una red local", 30, "10/05/2023", "30/05/2023", true));
            datos.add(new Tarea("Inteligencia Artificial", "Implementar un algoritmo de aprendizaje supervisado", 40, "01/06/2023", "15/07/2023", false));
            datos.add(new Tarea("Sistemas Operativos", "Estudiar el manejo de procesos", 20, "08/04/2023", "01/05/2023", false));
            datos.add(new Tarea("Seguridad Informática", "Realizar un análisis de vulnerabilidades", 15, "18/06/2023", "25/06/2023", true));

            datos.add(new Tarea("Diseño Gráfico", "Crear un logotipo para un cliente", 10, "22/04/2023", "01/05/2023", false));
            datos.add(new Tarea("Machine Learning", "Entrenar un modelo de machine learning", 35, "05/05/2023", "25/05/2023", false));
            datos.add(new Tarea("Inglés Avanzado", "Estudiar vocabulario y gramática", 15, "10/06/2023", "30/06/2023", true));
            datos.add(new Tarea("Gestión de Proyectos", "Elaborar un plan de proyecto", 25, "15/07/2023", "01/08/2023", false));
            datos.add(new Tarea("Estructuras de Datos", "Implementar una estructura de datos avanzada", 30, "05/04/2023", "20/04/2023", true));
            datos.add(new Tarea("Programación en Java", "Implementar un programa de Java", 15, "05/04/2023", "20/04/2023", false));
            datos.add(new Tarea("Base de Datos", "Diseñar una base de datos relacional", 30, "10/05/2023", "30/05/2023", false));
            datos.add(new Tarea("Proyecto Final", "Desarrollar un proyecto final integrador", 40, "01/06/2023", "15/07/2023", true));
            datos.add(new Tarea("Investigación", "Realizar una investigación sobre inteligencia artificial", 25, "08/04/2023", "01/05/2023", false));
            datos.add(new Tarea("Examen Final", "Preparar y presentar el examen final", 20, "18/06/2023", "25/06/2023", false));

*/

    }
}