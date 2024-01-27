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
    //Obtener el progreso medio de todas las tareas
    @Query("SELECT AVG(progreso) FROM Tarea")
    int obtenerProgresoMedio();
    //Querys para los progresos
    @Query("SELECT COUNT(*) FROM Tarea WHERE progreso = 0")
    int contarTareasProgreso0();
    @Query("SELECT COUNT(*) FROM Tarea WHERE progreso = 25")
    int contarTareasProgreso25();
    @Query("SELECT COUNT(*) FROM Tarea WHERE progreso = 50")
    int contarTareasProgreso50();
    @Query("SELECT COUNT(*) FROM Tarea WHERE progreso = 75")
    int contarTareasProgreso75();
    @Query("SELECT COUNT(*) FROM Tarea WHERE progreso = 100")
    int contarTareasProgreso100();

    //query para la fecha objetivo promedio
    @Query("SELECT AVG(CAST(fechaObjetio AS INTEGER)) FROM Tarea WHERE fechaObjetio IS NOT NULL")
    long obtenerFechaObjetivoPromedio();
    // Consulta para obtener el número de tareas empezadas este mes
    @Query("SELECT COUNT(*) FROM Tarea WHERE strftime('%Y-%m', datetime(fechaCreacion/1000, 'unixepoch', 'localtime')) = strftime('%Y-%m', 'now')")
    int obtenerNumeroTareasEmpezadasEsteMes();

    //Tareas que finalizan este mes
    @Query("SELECT COUNT(*) FROM Tarea WHERE strftime('%Y-%m', datetime(fechaObjetio/1000, 'unixepoch', 'localtime')) = strftime('%Y-%m', 'now')")
    int obtenerNumeroTareasFinalizanEsteMes();

    //Tareas que han terminado su fecha objetivo y no estan al 100%
    @Query("SELECT COUNT(*) FROM Tarea WHERE fechaObjetio < strftime('%s','now') * 1000 AND progreso < 100")
    int obtenerNumeroTareasCaducadas();
    //Numero de tareas prioritarias
    @Query("SELECT COUNT(*) FROM Tarea WHERE prioritario = 1")
    int obtenerNumeroTareasPrioritarias();


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
