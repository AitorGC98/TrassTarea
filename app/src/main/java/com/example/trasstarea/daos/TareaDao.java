package com.example.trasstarea.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.trasstarea.Datos.Tarea;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TareaDao {
    //Consulta de todas las tareas de la lista
    @Query("SELECT * FROM tarea")
    LiveData<List<Tarea>> getAll();
    //Consulta tareas prioritarias
    @Query("SELECT * FROM tarea WHERE prioritario = 1")
    LiveData<List<Tarea>> getTareasFavoritas();
    //Comprobar si quedan tareas prioritarias
    @Query("SELECT EXISTS (SELECT 1 FROM tarea WHERE prioritario = 1 LIMIT 1)")
    boolean hayTareasPrioritarias();
    //comprobar si esta vacia la base de datos
    @Query("SELECT COUNT(*) FROM tarea")
    int contarTareas();

    //Actualizar base de datos
    @Update
    void update(Tarea tarea);

    //Anotación que permite realizar la inserción de nuevas tareas
    @Insert
    //Método que realiza la inserción anterior
    void insertAll(Tarea... tarea);

    //Anotación que permite realizar el borrado de una nueva tarea
    @Delete
    //Método que realiza el borrado anterior
    void delete(Tarea tarea);
}
