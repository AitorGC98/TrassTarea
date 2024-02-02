package com.example.trasstarea.FragmentsUtilities;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CompartirViewModel extends ViewModel {
    private final MutableLiveData<String> titulo = new MutableLiveData<>();
    private final MutableLiveData<String> descripcion = new MutableLiveData<>();
    private final MutableLiveData<Integer> progreso = new MutableLiveData<>();
    private final MutableLiveData<String> fechaCreacion = new MutableLiveData<>();
    private final MutableLiveData<String> fechaObjetivo = new MutableLiveData<>();
    private final MutableLiveData<Boolean> prioritarias = new MutableLiveData<>();
    private final MutableLiveData<String> urlDoc = new MutableLiveData<>();
    private final MutableLiveData<String> urlImg = new MutableLiveData<>();
    private final MutableLiveData<String> urlAud = new MutableLiveData<>();
    private final MutableLiveData<String> urlVid = new MutableLiveData<>();


    public void setTitulo(String nomb) {
       titulo.setValue(nomb);
    }

    public MutableLiveData<String> getTitulo() {
        return titulo;
    }

    public MutableLiveData<String> getUrlDoc() {

        return urlDoc;
    }
    public MutableLiveData<String> getUrlImg() {

        return urlImg;
    }
    public MutableLiveData<String> getUrlAud() {

        return urlAud;
    }
    public MutableLiveData<String> getUrlVid() {

        return urlVid;
    }
    public void setUrlDoc(String des) {
        urlDoc.setValue(des);
    }
    public void setUrlImg(String des) {
        urlImg.setValue(des);
    }
    public void setUrlAud(String des) {
        urlAud.setValue(des);
    }
    public void setUrlVid(String des) {
        urlVid.setValue(des);
    }


    public void setDescripcion(String des) {
        descripcion.setValue(des);
    }

    public MutableLiveData<String> getDescripcion() {
        return descripcion;
    }

    public void setProgreso(int pro) {
        progreso.setValue(pro);
    }

    public MutableLiveData<Integer> getProgreso() {
        Integer valorActual = progreso.getValue();
        return valorActual != null ? progreso : new MutableLiveData<>(0);//si no hacemos click en el spinner esta se establece como 0
    }

    public void setFechaCreacion(String fech) {
        fechaCreacion.setValue(fech);
    }

    public MutableLiveData<String> getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaObjetivo(String fech) {
        fechaObjetivo.setValue(fech);
    }

    public MutableLiveData<String> getFechaObjetivo() {
        return fechaObjetivo;
    }

    public void setPrioritarias(Boolean pri) {
        prioritarias.setValue(pri);
    }

    public MutableLiveData<Boolean> getPrioritarias() {
        Boolean valorActual = prioritarias.getValue();
        return valorActual != null ? prioritarias : new MutableLiveData<>(false);//si no hacemos check en el checkbox esta se establece como false
    }
}


