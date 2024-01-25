package com.example.trasstarea.Datos;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;


import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.trasstarea.convert.Converters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Entity
@TypeConverters({Converters.class})
public class Tarea implements Parcelable {
    private String titulo;
    private String descripcion;
    private int progreso;
    @PrimaryKey(autoGenerate = true)
    private  int id;
    private Date fechaCreacion;
    private Date fechaObjetio;
    private boolean prioritario;
    private String urlDoc;
    private String urlImg;
    private String urlAud;
    private String urlVid;


    public Tarea(String titulo, String descripcion, int progreso, String fechaCreacion, String fechaObjetio,boolean prioritario) {

        this.titulo = titulo;
        this.descripcion = descripcion;
        this.progreso = progreso;
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            this.fechaCreacion = formatoFecha.parse(fechaCreacion);
            this.fechaObjetio = formatoFecha.parse(fechaObjetio);
        }catch (ParseException e){
            e.printStackTrace();
        }
        this.prioritario=prioritario;
    }
    public Tarea(){

    }


    public void setId(int id) {
        this.id = id;
    }
    public String getUrlAud() {
        return urlAud;
    }

    public String getUrlDoc() {
        return urlDoc;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public String getUrlVid() {
        return urlVid;
    }

    public void setUrlAud(String urlAud) {
        this.urlAud = urlAud;
    }

    public void setUrlDoc(String urlDoc) {
        this.urlDoc = urlDoc;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public void setUrlVid(String urlVid) {
        this.urlVid = urlVid;
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

        return (int) (diferenciaMilisegundos / (1000 * 60 * 60 * 24));
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

        // Nuevos atributos URL
        dest.writeString(this.urlDoc);
        dest.writeString(this.urlImg);
        dest.writeString(this.urlAud);
        dest.writeString(this.urlVid);

    }

    public void readFromParcel(Parcel dest, int flags) {
        dest.writeString(this.titulo);
        dest.writeString(this.descripcion);
        dest.writeInt(this.progreso);
        dest.writeInt(this.id);
        dest.writeLong(this.fechaCreacion != null ? this.fechaCreacion.getTime() : -1);
        dest.writeLong(this.fechaObjetio != null ? this.fechaObjetio.getTime() : -1);
        dest.writeByte(this.prioritario ? (byte) 1 : (byte) 0);
        // Nuevos atributos URL
        dest.writeString(this.urlDoc);
        dest.writeString(this.urlAud);
        dest.writeString(this.urlVid);
        dest.writeString(this.urlImg);
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
        this.urlAud = in.readString();
        this.urlDoc = in.readString();
        this.urlImg = in.readString();
        this.urlVid = in.readString();
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
