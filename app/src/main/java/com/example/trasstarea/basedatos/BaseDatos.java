package com.example.trasstarea.basedatos;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.trasstarea.Datos.Tarea;
import com.example.trasstarea.convert.Converters;
import com.example.trasstarea.daos.TareaDao;

@Database(entities = {Tarea.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class BaseDatos extends RoomDatabase {

    //Usando el patrón SINGLETON, nos aseguramos que solo haya una instancia de la
    //base de datos creada en nuestra aplicación.
    private static BaseDatos INSTANCIA;

    public static BaseDatos getInstance(Context context) {
        if (INSTANCIA == null) {
            INSTANCIA = Room.databaseBuilder(
                            context.getApplicationContext(),
                            BaseDatos.class,
                            "dbTarea")
                    .build();
        }
        return INSTANCIA;
    }

    public static void destroyInstance() {
        INSTANCIA = null;
    }

    //Método para construir el objeto ProductoDAO con el que accederemos
    //a la base de datos.
    public abstract TareaDao productoDAO();
}

