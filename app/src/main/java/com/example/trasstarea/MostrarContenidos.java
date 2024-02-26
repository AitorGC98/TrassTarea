package com.example.trasstarea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;

import com.example.trasstarea.Fragmentos.FragmentPDF;

public class MostrarContenidos extends AppCompatActivity {
    private static final String ARG_PDF_URI = "pdf_uri";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_contenidos);
        // Configurar la flecha de retroceso en el ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Obtener la URI del archivo del intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("uri")) {
            String uriString = intent.getStringExtra("uri");
            if (uriString != null) {
                Uri archivoUri = Uri.parse(uriString);
                String tipoArchivo = obtenerTipoArchivo(archivoUri);
                cargarFragmento(tipoArchivo, archivoUri);
            }
        }
    }
    private String obtenerTipoArchivo(Uri uri) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
    private void cargarFragmento(String tipoArchivo, Uri archivoUri) {
        Fragment fragmento= FragmentPDF.newInstance(archivoUri.toString());
        switch (tipoArchivo) {
            case "application/pdf":
                fragmento = FragmentPDF.newInstance(archivoUri.toString());
                break;
            case "audio/mpeg":
                //fragmento = MP3Fragment.newInstance(archivoUri);
                break;
            case "video/mp4":
                //fragmento = MP4Fragment.newInstance(archivoUri);
                break;
            case "image/jpeg":
                //fragmento = JPGFragment.newInstance(archivoUri);
                break;
            default:
                // Manejar caso por defecto (puede ser un fragmento para mostrar un mensaje de error)
                fragmento =FragmentPDF.newInstance(archivoUri.toString());
                break;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mostrar_fragmentos, fragmento)
                .commit();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Volver atrás cuando se presiona la flecha de retroceso en el ActionBar
        return true;
    }

}