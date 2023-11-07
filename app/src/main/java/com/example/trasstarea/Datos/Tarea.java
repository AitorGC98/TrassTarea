package com.example.trasstarea.Datos;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Tarea implements Parcelable {
    private String titulo;
    private String descripcion;
    private int progreso;
    private Date fechaCreacion;
    private Date fechaObjetio;
    private boolean prioritario;

    public Tarea(String titulo, String descripcion, int progreso, Date fechaCreacion, Date fechaObjetio,boolean prioritario) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.progreso = progreso;
        this.fechaCreacion = fechaCreacion;
        this.fechaObjetio = fechaObjetio;
        this.prioritario=prioritario;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tarea tareas = (Tarea) o;
        return progreso == tareas.progreso && Objects.equals(titulo, tareas.titulo) && Objects.equals(descripcion, tareas.descripcion) && Objects.equals(fechaCreacion, tareas.fechaCreacion) && Objects.equals(fechaObjetio, tareas.fechaObjetio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo, descripcion, progreso, fechaCreacion, fechaObjetio);
    }

    public boolean isPrioritario() {
        return prioritario;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getProgreso() {
        return progreso;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public Date getFechaObjetio() {
        return fechaObjetio;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setProgreso(int progreso) {
        this.progreso = progreso;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setFechaObjetio(Date fechaObjetio) {
        this.fechaObjetio = fechaObjetio;
    }

    public void setPrioritario(boolean prioritario) {
        this.prioritario = prioritario;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.titulo);
        dest.writeString(this.descripcion);
        dest.writeInt(this.progreso);
        dest.writeLong(this.fechaCreacion != null ? this.fechaCreacion.getTime() : -1);
        dest.writeLong(this.fechaObjetio != null ? this.fechaObjetio.getTime() : -1);
        dest.writeByte(this.prioritario ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.titulo = source.readString();
        this.descripcion = source.readString();
        this.progreso = source.readInt();
        long tmpFechaCreacion = source.readLong();
        this.fechaCreacion = tmpFechaCreacion == -1 ? null : new Date(tmpFechaCreacion);
        long tmpFechaObjetio = source.readLong();
        this.fechaObjetio = tmpFechaObjetio == -1 ? null : new Date(tmpFechaObjetio);
        this.prioritario = source.readByte() != 0;
    }

    protected Tarea(Parcel in) {
        this.titulo = in.readString();
        this.descripcion = in.readString();
        this.progreso = in.readInt();
        long tmpFechaCreacion = in.readLong();
        this.fechaCreacion = tmpFechaCreacion == -1 ? null : new Date(tmpFechaCreacion);
        long tmpFechaObjetio = in.readLong();
        this.fechaObjetio = tmpFechaObjetio == -1 ? null : new Date(tmpFechaObjetio);
        this.prioritario = in.readByte() != 0;
    }

    public static final Creator<Tarea> CREATOR = new Creator<Tarea>() {
        @Override
        public Tarea createFromParcel(Parcel source) {
            return new Tarea(source);
        }

        @Override
        public Tarea[] newArray(int size) {
            return new Tarea[size];
        }
    };
    public int getDiasRestantes() {
        if (fechaObjetio == null) {
            // Si la fecha objetivo es nula, retornamos un valor negativo para indicar que no hay fecha objetivo establecida.
            return -1;
        }

        // Obtén la fecha actual
        Calendar fechaActual = Calendar.getInstance();

        // Convierte la fecha objetivo a un objeto Calendar
        Calendar fechaObjetivo = Calendar.getInstance();
        fechaObjetivo.setTime(fechaObjetio);

        // Calcula la diferencia en milisegundos entre la fecha objetivo y la fecha actual
        long diferenciaMilisegundos = fechaObjetivo.getTimeInMillis() - fechaActual.getTimeInMillis();

        // Convierte la diferencia en milisegundos a días
        int diasRestantes = (int) (diferenciaMilisegundos / (1000 * 60 * 60 * 24));

        return diasRestantes;
    }
}
