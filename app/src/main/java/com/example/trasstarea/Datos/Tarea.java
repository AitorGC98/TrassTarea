package com.example.trasstarea.Datos;

import android.os.Parcel;
import android.os.Parcelable;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Tarea implements Parcelable {
    private String titulo;
    private String descripcion;
    private int progreso,id;
    private Date fechaCreacion;
    private Date fechaObjetio;
    private boolean prioritario;
    private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

    private static int contador;
    public Tarea(String titulo, String descripcion, int progreso, String fechaCreacion, String fechaObjetio,boolean prioritario) {
        this.id=contador++;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.progreso = progreso;
        try {
            this.fechaCreacion = formatoFecha.parse(fechaCreacion);
            this.fechaObjetio = formatoFecha.parse(fechaObjetio);
        }catch (ParseException e){
            e.printStackTrace();
        }
        this.prioritario=prioritario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tarea)) return false;
        Tarea tarea = (Tarea) o;
        return id == tarea.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getId() {return id; }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.titulo);
        dest.writeString(this.descripcion);
        dest.writeInt(this.progreso);
        dest.writeInt(this.id);
        dest.writeLong(this.fechaCreacion != null ? this.fechaCreacion.getTime() : -1);
        dest.writeLong(this.fechaObjetio != null ? this.fechaObjetio.getTime() : -1);
        dest.writeByte(this.prioritario ? (byte) 1 : (byte) 0);

    }

    public void readFromParcel(Parcel dest, int flags) {
        dest.writeString(this.titulo);
        dest.writeString(this.descripcion);
        dest.writeInt(this.progreso);
        dest.writeInt(this.id);
        dest.writeLong(this.fechaCreacion != null ? this.fechaCreacion.getTime() : -1);
        dest.writeLong(this.fechaObjetio != null ? this.fechaObjetio.getTime() : -1);
        dest.writeByte(this.prioritario ? (byte) 1 : (byte) 0);
    }

    protected Tarea(Parcel in) {
        this.titulo = in.readString();
        this.descripcion = in.readString();
        this.progreso = in.readInt();
        this.id = in.readInt();
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
}
