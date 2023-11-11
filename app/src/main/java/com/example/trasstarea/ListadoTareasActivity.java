package com.example.trasstarea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trasstarea.Adaptador.TareaAdapter;
import com.example.trasstarea.Datos.Tarea;

import  java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListadoTareasActivity extends AppCompatActivity {
    private ArrayList<Tarea> datos=new ArrayList<Tarea>();
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
            Toast.makeText(this, "Pantalla añadir", Toast.LENGTH_SHORT).show();
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
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaCreacion = formatoFecha.parse("12/03/2023");
            Date fechaObjetivo = formatoFecha.parse("12/11/2023");
            Date fechaCreacion1 = formatoFecha.parse("20/07/2023");
            Date fechaObjetivo1 = formatoFecha.parse("23/12/2023");
            Date fechaCreacion2 = formatoFecha.parse("30/03/2023");
            Date fechaObjetivo2 = formatoFecha.parse("30/12/2023");
            Date fechaCreacion3 = formatoFecha.parse("02/11/2023");
            Date fechaObjetivo3 = formatoFecha.parse("01/11/2024");
            Date fechaCreacion4 = formatoFecha.parse("19/08/2023");
            Date fechaObjetivo4 = formatoFecha.parse("25/11/2023");
            Date fechaCreacion5 = formatoFecha.parse("23/11/2023");
            Date fechaObjetivo5 = formatoFecha.parse("12/12/2023");
            Date fechaCreacion6 = formatoFecha.parse("11/10/2023");
            Date fechaObjetivo6 = formatoFecha.parse("12/01/2024");
            Date fechaCreacion7 = formatoFecha.parse("12/11/2023");
            Date fechaObjetivo7 = formatoFecha.parse("30/11/2023");
            Date fechaCreacion8 = formatoFecha.parse("12/11/2023");
            Date fechaObjetivo8 = formatoFecha.parse("02/11/2023");
            Date fechaCreacion9 = formatoFecha.parse("12/04/2023");
            Date fechaObjetivo9 = formatoFecha.parse("02/12/2023");

            datos.add(new Tarea("PSP", "realizar una tarea de psp", 23, fechaCreacion, fechaObjetivo, true));
            datos.add(new Tarea("Moviles", "realizar una tarea de para moviles", 50, fechaCreacion1, fechaObjetivo1, true));
            datos.add(new Tarea("Empresas", "realizar una tarea para empresas", 10, fechaCreacion2, fechaObjetivo2, true));
            datos.add(new Tarea("Acceso a datos", "realizar una tarea de acceso a datos", 78, fechaCreacion3, fechaObjetivo3, true));
            datos.add(new Tarea("Desarrollo de interfaces", "realizar una tarea de desarrollo de interfaces", 0, fechaCreacion4, fechaObjetivo4, true));
            datos.add(new Tarea("Sistemas de gestion", "realizar una tarea de sistemas de gestion", 99, fechaCreacion5, fechaObjetivo5, true));
            datos.add(new Tarea("tarea de limpieza", "limpiar el cuarto", 89, fechaCreacion6, fechaObjetivo6, false));
            datos.add(new Tarea("Ir a comprar", "realizar una compra", 50, fechaCreacion7, fechaObjetivo7, false));
            datos.add(new Tarea("Hacer un pedido", "hacer un pedido", 8, fechaCreacion8, fechaObjetivo8, true));
            datos.add(new Tarea("Hacer la comida", "hacer la comida", 100, fechaCreacion9, fechaObjetivo9, false));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}