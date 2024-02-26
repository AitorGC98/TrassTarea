package com.example.trasstarea.Fragmentos;


import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trasstarea.FragmentsUtilities.CompartirViewModel;

import com.example.trasstarea.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Fragmento2 extends Fragment {

    private EditText etmDescripcion;
    private TextView tvImg,tvDoc,tvAud,tvVid;
    private CompartirViewModel compartirViewModel;

    private static final String DIRECTORIO_ARCHIVOS = "archivos";
    private SharedPreferences sharedPreferences;
    private boolean tarjetaSD =false;
    private ActivityResultLauncher<String> mGetContentLauncher;
    private ActivityResultLauncher<Intent> mCameraLauncher;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    ActivityResultLauncher<Intent> mVideoLauncher;
    private ActivityResultLauncher<Intent> mAudioLauncher;

    public interface ComunicarFragmento2{
        void onBotonGuardar();
        void onBotonIr1Clicked();
    }
    private ComunicarFragmento2 comunicador2;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ComunicarFragmento2) {
            comunicador2 = (ComunicarFragmento2) context;
        } else {
            throw new ClassCastException(context + " debe implementar interfaz de comunicación con el 2º fragmento");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGetContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                // Handle the result here
                handleResult(uri);
                Log.d("MiAplicacion", "LO QUE LE DOY AL HANDLE: "+uri );
            }
        });
        compartirViewModel = new ViewModelProvider(requireActivity()).get(CompartirViewModel.class);

        mCameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null && extras.containsKey("data")) {
                        // La imagen se capturó desde la cámara
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        Uri imageUri = getImageUri(requireContext(), imageBitmap);
                        handleResult(imageUri);
                    } else {
                        // La imagen se seleccionó desde la galería
                        Uri imageUri = data.getData();
                        Log.d("MiAplicacion", "LO QUE LE DOY AL HANDLE abajo: "+imageUri );
                        handleResult(imageUri);
                    }
                }
            }
        });

         mAudioLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                // Aquí puedes manejar el resultado de la actividad, como obtener la URI del audio y manejarla
                Intent data = result.getData();
                if (data != null) {
                    Uri audioUri = data.getData();
                    handleResult(audioUri);
                }
            }
        });

         mVideoLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                // Aquí puedes manejar el resultado de la actividad, como obtener la URI del video y manejarla
                Intent data = result.getData();
                if (data != null) {
                    Uri videoUri = data.getData();
                    handleResult(videoUri);
                }
            }
        });

        compartirViewModel = new ViewModelProvider(requireActivity()).get(CompartirViewModel.class);

    }

    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(requireContext());

        tarjetaSD = sharedPreferences.getBoolean("sd", false);
        View fragmento2 =inflater.inflate(R.layout.fragment_fragmento2, container, false);

        Button btnVolver = fragmento2.findViewById(R.id.btn_volver);
        AppCompatImageButton btndoc = fragmento2.findViewById(R.id.btn_documento);
        AppCompatImageButton btnVid = fragmento2.findViewById(R.id.btn_video);
        AppCompatImageButton btnAud = fragmento2.findViewById(R.id.btn_audio);
        AppCompatImageButton btnImg = fragmento2.findViewById(R.id.btn_imagen);
        btnVolver.setOnClickListener(view -> comunicador2.onBotonIr1Clicked());
        Button btnGuardar = fragmento2.findViewById(R.id.btn_guardar);
        btnGuardar.setOnClickListener((view ->{
            if(TextUtils.isEmpty(etmDescripcion.getText())){
                Toast.makeText(requireContext(), "Por favor, complete la descripción", Toast.LENGTH_LONG).show();
            }else{
                comunicador2.onBotonGuardar();
            }

        }));
        btndoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContentLauncher.launch("application/pdf");
            }
        });

        btnVid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un intent para seleccionar un video de los archivos del dispositivo
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

                // Crear un intent para grabar un video utilizando la cámara del dispositivo
                Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                // Crear un intent chooser para elegir entre la galería y la cámara
                Intent chooserIntent = Intent.createChooser(galleryIntent, "Seleccionar video");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});

                // Lanzar el intent utilizando el lanzador mVideoLauncher
                mVideoLauncher.launch(chooserIntent);
            }
        });
        btnImg.setOnClickListener(v -> {
            // Crear un intent para seleccionar una imagen de la galería
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            // Crear un intent para capturar una imagen desde la cámara
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // Crear un intent chooser para elegir entre la cámara y la galería
            Intent chooserIntent = Intent.createChooser(galleryIntent, "Seleccionar imagen");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});

            // Lanzar el lanzador de actividad mCameraLauncher con el intent chooser
            mCameraLauncher.launch(chooserIntent);
        });
        btnAud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un diálogo de selección de opciones
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Seleccionar opción");
                builder.setItems(new CharSequence[]{"Seleccionar archivo", "Grabar audio"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // El usuario elige seleccionar un archivo de audio
                                mGetContentLauncher.launch("audio/*");
                                break;
                            case 1:
                                // El usuario elige grabar audio
                                try{
                                    Intent recordIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                                    mAudioLauncher.launch(recordIntent);
                                    break;
                                }catch (Exception e){
                                    Toast.makeText(requireContext(), "No hay aplicaciones disponibles para grabar audio", Toast.LENGTH_SHORT).show();
                                    break;
                                }

                        }
                    }
                });
                builder.create().show();
            }
        });


        etmDescripcion = fragmento2.findViewById(R.id.etm_descripcion);
        tvDoc = fragmento2.findViewById(R.id.tv_docuemento);
        tvImg = fragmento2.findViewById(R.id.tv_imagen);
        tvAud = fragmento2.findViewById(R.id.tv_audio);
        tvVid = fragmento2.findViewById(R.id.tv_video);
        if (compartirViewModel.getUrlDoc().getValue() != null) {
            tvDoc.setText(compartirViewModel.getUrlDoc().getValue().toString());
        }

        if (compartirViewModel.getUrlAud().getValue() != null) {
            tvAud.setText(compartirViewModel.getUrlAud().getValue().toString());
        }

        if (compartirViewModel.getUrlVid().getValue() != null) {
            tvVid.setText(compartirViewModel.getUrlVid().getValue().toString());
        }

        if (compartirViewModel.getUrlImg().getValue() != null) {
            tvImg.setText(compartirViewModel.getUrlImg().getValue().toString());
        }
        if (compartirViewModel.getDescripcion().getValue() != null) {
            etmDescripcion.setText(compartirViewModel.getDescripcion().getValue().toString());
        }

        // Agregar TextWatcher al EditText para escuchar cambios en tiempo real
        etmDescripcion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No es necesario implementar este método para este caso
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Actualizar el ViewModel con el nuevo texto
                compartirViewModel.setDescripcion(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No es necesario implementar este método para este caso
            }
        });


        return fragmento2;

    }
    private void handleResult(Uri uri) {
        Log.d("MiAplicacion", "URI de la imagen: " + uri);
        // Aquí puedes manejar el resultado según el tipo de archivo seleccionado
        if (uri != null) {
            // Implementa la lógica necesaria para cada tipo de archivo

            String tipoArchivo = requireContext().getContentResolver().getType(uri);
            if ("application/pdf".equals(tipoArchivo)) {
                LiveData<String> urlDocLiveData = compartirViewModel.getUrlDoc();
                String urlDocumento="";
                if (urlDocLiveData != null && urlDocLiveData.getValue() != null) {
                    // El LiveData y su valor no son nulos, así que puedes acceder a toString()
                    urlDocumento = urlDocLiveData.getValue().toString();
                }

                if(tarjetaSD){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                        try {
                            // Obtén el InputStream del contenido del archivo PDF
                            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);

                            // Crea el directorio si no existe
                            File directorioArchivos = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), DIRECTORIO_ARCHIVOS);
                            if (!directorioArchivos.exists()) {
                                directorioArchivos.mkdirs(); // Use mkdirs() to create the entire directory path if it doesn't exist
                            }

                            // Crea el archivo en el directorio "archivos"
                            String nombreArchivo = System.currentTimeMillis() + ".pdf";
                            File archivoPDF = new File(directorioArchivos, nombreArchivo);
                            if(urlDocumento.isEmpty() || urlDocumento==null){

                            }else{
                                borrarArchivo(urlDocumento);
                            }
                            // Copia el contenido del InputStream al OutputStream del archivo
                            OutputStream outputStream = new FileOutputStream(archivoPDF);
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = inputStream.read(buffer)) > 0) {
                                outputStream.write(buffer, 0, length);
                            }

                            // Cierra los flujos
                            inputStream.close();
                            outputStream.close();


                            // Puedes mostrar la URL en un TextView
                            tvDoc.setText(archivoPDF.getAbsolutePath());
                            compartirViewModel.setUrlDoc(archivoPDF.getAbsolutePath());

                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "Error al guardar el archivo PDF", Toast.LENGTH_SHORT).show();
                        }
                    }else{


                        try {
                            // Obtén el InputStream del contenido del archivo PDF
                            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);

                            // Obtiene la ruta del directorio de la tarjeta SD
                            File sdCardDirectory = new File("/mnt/sdcard"); // Ajusta la ruta según la estructura de tu dispositivo

                            // Crea el directorio si no existe
                            File directorioArchivos = new File(sdCardDirectory, DIRECTORIO_ARCHIVOS);
                            if (!directorioArchivos.exists()) {
                                directorioArchivos.mkdirs(); // Use mkdirs() to create the entire directory path if it doesn't exist
                            }

                            // Crea el archivo en el directorio "archivos"
                            String nombreArchivo = System.currentTimeMillis() + ".pdf";
                            File archivoPDF = new File(directorioArchivos, nombreArchivo);

                            if(urlDocumento.isEmpty() || urlDocumento==null){

                            }else{
                                borrarArchivo(urlDocumento);
                            }
                            // Copia el contenido del InputStream al OutputStream del archivo
                            OutputStream outputStream = new FileOutputStream(archivoPDF);
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = inputStream.read(buffer)) > 0) {
                                outputStream.write(buffer, 0, length);
                            }

                            // Cierra los flujos
                            inputStream.close();
                            outputStream.close();



                            // Puedes mostrar la URL en un TextView
                            tvDoc.setText(archivoPDF.getAbsolutePath());
                            compartirViewModel.setUrlDoc(archivoPDF.getAbsolutePath());


                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "Error al guardar el archivo PDF", Toast.LENGTH_SHORT).show();
                        }
                    }

                }else {


                    try {
                        // Obtén el InputStream del contenido del archivo PDF
                        InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);

                        // Crea el directorio si no existe
                        File directorioArchivos = new File(requireContext().getFilesDir(), DIRECTORIO_ARCHIVOS);
                        if (!directorioArchivos.exists()) {
                            directorioArchivos.mkdir();
                        }

                        // Crea el archivo en el directorio "archivos"
                        String nombreArchivo = System.currentTimeMillis() + ".pdf";
                        File archivoPDF = new File(directorioArchivos, nombreArchivo);

                        if(urlDocumento.isEmpty() || urlDocumento==null){

                        }else{
                            borrarArchivo(urlDocumento);
                        }
                        // Copia el contenido del InputStream al OutputStream del archivo
                        OutputStream outputStream = new FileOutputStream(archivoPDF);
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }

                        // Cierra los flujos
                        inputStream.close();
                        outputStream.close();

                        tvDoc.setText(archivoPDF.getAbsolutePath());
                        compartirViewModel.setUrlDoc(archivoPDF.getAbsolutePath());


                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error al guardar el archivo PDF", Toast.LENGTH_SHORT).show();
                    }
            }
            } else if (tipoArchivo.startsWith("video/")) {
                LiveData<String> urlVidLiveData = compartirViewModel.getUrlVid();
                String urlVideo="";
                if (urlVideo != null && urlVidLiveData.getValue() != null) {

                    urlVideo = urlVidLiveData.getValue().toString();
                }

                if(tarjetaSD){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){

                        try {
                            // Obtén el InputStream del contenido del archivo PDF
                            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);

                            // Crea el directorio si no existe
                            File directorioArchivos = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), DIRECTORIO_ARCHIVOS);
                            if (!directorioArchivos.exists()) {
                                directorioArchivos.mkdirs(); // Use mkdirs() to create the entire directory path if it doesn't exist
                            }

                            // Crea el archivo en el directorio "archivos"
                            String nombreArchivo = System.currentTimeMillis() + ".mp4"; // Ajusta la extensión según el formato de video
                            File archivoVideo = new File(directorioArchivos, nombreArchivo);

                            if(urlVideo.isEmpty() || urlVideo==null){

                            }else{
                                borrarArchivo(urlVideo);
                            }
                            // Copia el contenido del InputStream al OutputStream del archivo
                            OutputStream outputStream = new FileOutputStream(archivoVideo);
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = inputStream.read(buffer)) > 0) {
                                outputStream.write(buffer, 0, length);
                            }

                            // Cierra los flujos
                            inputStream.close();
                            outputStream.close();

                            tvVid.setText(archivoVideo.getAbsolutePath());
                            compartirViewModel.setUrlVid(archivoVideo.getAbsolutePath());

                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "Error al guardar el archivo de video", Toast.LENGTH_SHORT).show();
                        }
                    }else{

                        try {
                            // Obtén el InputStream del contenido del archivo PDF
                            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);

                            // Obtiene la ruta del directorio de la tarjeta SD
                            File sdCardDirectory = new File("/mnt/sdcard"); // Ajusta la ruta según la estructura de tu dispositivo

                            // Crea el directorio si no existe
                            File directorioArchivos = new File(sdCardDirectory, DIRECTORIO_ARCHIVOS);
                            if (!directorioArchivos.exists()) {
                                directorioArchivos.mkdirs(); // Use mkdirs() to create the entire directory path if it doesn't exist
                            }

                            // Crea el archivo en el directorio "archivos"
                            String nombreArchivo = System.currentTimeMillis() + ".mp4";
                            File archivoVideo = new File(directorioArchivos, nombreArchivo);

                            if(urlVideo.isEmpty() || urlVideo==null){

                            }else{
                                borrarArchivo(urlVideo);
                            }

                            OutputStream outputStream = new FileOutputStream(archivoVideo);
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = inputStream.read(buffer)) > 0) {
                                outputStream.write(buffer, 0, length);
                            }

                            // Cierra los flujos
                            inputStream.close();
                            outputStream.close();

                            tvVid.setText(archivoVideo.getAbsolutePath());
                            compartirViewModel.setUrlVid(archivoVideo.getAbsolutePath());


                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "Error al guardar el archivo de video", Toast.LENGTH_SHORT).show();
                        }
                    }

                }else {

                    try {
                        // Obtén el InputStream del contenido del archivo de video
                        InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);

                        // Crea el directorio si no existe
                        File directorioArchivos = new File(requireContext().getFilesDir(), DIRECTORIO_ARCHIVOS);
                        if (!directorioArchivos.exists()) {
                            directorioArchivos.mkdir();
                        }

                        // Crea el archivo en el directorio "archivos"
                        String nombreArchivo = System.currentTimeMillis() + ".mp4"; // Ajusta la extensión según el formato de video
                        File archivoVideo = new File(directorioArchivos, nombreArchivo);

                        if(urlVideo.isEmpty() || urlVideo==null){

                        }else{
                            borrarArchivo(urlVideo);
                        }

                        // Copia el contenido del InputStream al OutputStream del archivo
                        OutputStream outputStream = new FileOutputStream(archivoVideo);
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }

                        // Cierra los flujos
                        inputStream.close();
                        outputStream.close();

                        tvVid.setText(archivoVideo.getAbsolutePath());
                        compartirViewModel.setUrlVid(archivoVideo.getAbsolutePath());

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error al guardar el video", Toast.LENGTH_SHORT).show();
                    }
                }

            } else if (tipoArchivo.startsWith("image/")) {
                LiveData<String> urlImgtLiveData = compartirViewModel.getUrlImg();
                String urlImagen="";
                if (urlImagen != null && urlImgtLiveData.getValue() != null) {

                    urlImagen = urlImgtLiveData.getValue().toString();
                }


                if(tarjetaSD) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                        try {
                            // Obtén el InputStream del contenido del archivo PDF
                            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);

                            // Crea el directorio si no existe
                            File directorioArchivos = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), DIRECTORIO_ARCHIVOS);
                            if (!directorioArchivos.exists()) {
                                directorioArchivos.mkdirs(); // Use mkdirs() to create the entire directory path if it doesn't exist
                            }

                            // Crea el archivo en el directorio "archivos"
                            String nombreArchivo = System.currentTimeMillis() + ".jpg"; // Ajusta la extensión según el formato de imagen
                            File archivoImagen = new File(directorioArchivos, nombreArchivo);

                            if(urlImagen.isEmpty() || urlImagen==null){

                            }else{
                                borrarArchivo(urlImagen);
                            }

                            // Copia el contenido del InputStream al OutputStream del archivo
                            OutputStream outputStream = new FileOutputStream(archivoImagen);
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = inputStream.read(buffer)) > 0) {
                                outputStream.write(buffer, 0, length);
                            }

                            // Cierra los flujos
                            inputStream.close();
                            outputStream.close();

                            tvImg.setText(archivoImagen.getAbsolutePath());
                            compartirViewModel.setUrlVid(archivoImagen.getAbsolutePath());

                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "Error al guardar el archivo imagen", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        try {
                            // Obtén el InputStream del contenido del archivo de video
                            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);

                            // Crea el directorio si no existe
                            File directorioArchivos = new File(requireContext().getFilesDir(), DIRECTORIO_ARCHIVOS);
                            if (!directorioArchivos.exists()) {
                                directorioArchivos.mkdir();
                            }

                            // Crea el archivo en el directorio "archivos"
                            String nombreArchivo = System.currentTimeMillis() + ".jpg"; // Ajusta la extensión según el formato de imagen
                            File archivoImagen = new File(directorioArchivos, nombreArchivo);

                            if(urlImagen.isEmpty() || urlImagen==null){

                            }else{
                                borrarArchivo(urlImagen);
                            }

                            OutputStream outputStream = new FileOutputStream(archivoImagen);
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = inputStream.read(buffer)) > 0) {
                                outputStream.write(buffer, 0, length);
                            }

                            // Cierra los flujos
                            inputStream.close();
                            outputStream.close();

                            tvImg.setText(archivoImagen.getAbsolutePath());
                            compartirViewModel.setUrlVid(archivoImagen.getAbsolutePath());

                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "Error al guardar el imagen", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {

                    try {
                        // Obtén el InputStream del contenido del archivo de imagen
                        InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);

                        // Crea el directorio si no existe
                        File directorioArchivos = new File(requireContext().getFilesDir(), DIRECTORIO_ARCHIVOS);
                        if (!directorioArchivos.exists()) {
                            directorioArchivos.mkdir();
                        }

                        // Crea el archivo en el directorio "archivos"
                        String nombreArchivo = System.currentTimeMillis() + ".jpg"; // Ajusta la extensión según el formato de imagen
                        File archivoImagen = new File(directorioArchivos, nombreArchivo);

                        if(urlImagen.isEmpty() || urlImagen==null){

                        }else{
                            borrarArchivo(urlImagen);
                        }

                        // Copia el contenido del InputStream al OutputStream del archivo
                        OutputStream outputStream = new FileOutputStream(archivoImagen);
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }

                        // Cierra los flujos
                        inputStream.close();
                        outputStream.close();

                        // Puedes mostrar la URL en un TextView o almacenarla según tus necesidades
                        tvImg.setText(archivoImagen.getAbsolutePath());
                        compartirViewModel.setUrlImg(archivoImagen.getAbsolutePath());

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error al guardar la imagen", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (tipoArchivo.startsWith("audio/")) {
                LiveData<String> urlAudtLiveData = compartirViewModel.getUrlImg();
                String urlAudio = "";
                if (urlAudio != null && urlAudtLiveData.getValue() != null) {

                    urlAudio = urlAudtLiveData.getValue().toString();
                }


                if (tarjetaSD) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                        try {
                            // Obtén el InputStream del contenido del archivo PDF
                            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);

                            // Crea el directorio si no existe
                            File directorioArchivos = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), DIRECTORIO_ARCHIVOS);
                            if (!directorioArchivos.exists()) {
                                directorioArchivos.mkdirs(); // Use mkdirs() to create the entire directory path if it doesn't exist
                            }

                            // Crea el archivo en el directorio "archivos"
                            String nombreArchivo = System.currentTimeMillis() + ".mp3"; // Ajusta la extensión según el formato de audio
                            File archivoAudio = new File(directorioArchivos, nombreArchivo);

                            if (urlAudio.isEmpty() || urlAudio == null) {

                            } else {
                                borrarArchivo(urlAudio);
                            }

                            // Copia el contenido del InputStream al OutputStream del archivo
                            OutputStream outputStream = new FileOutputStream(archivoAudio);
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = inputStream.read(buffer)) > 0) {
                                outputStream.write(buffer, 0, length);
                            }

                            // Cierra los flujos
                            inputStream.close();
                            outputStream.close();

                            // Puedes mostrar la URL en un TextView o almacenarla según tus necesidades
                            tvAud.setText(archivoAudio.getAbsolutePath());
                            compartirViewModel.setUrlAud(archivoAudio.getAbsolutePath());

                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "Error al guardar el audio", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            // Obtén el InputStream del contenido del archivo de video
                            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);

                            // Crea el directorio si no existe
                            File directorioArchivos = new File(requireContext().getFilesDir(), DIRECTORIO_ARCHIVOS);
                            if (!directorioArchivos.exists()) {
                                directorioArchivos.mkdir();
                            }

                            // Crea el archivo en el directorio "archivos"
                            String nombreArchivo = System.currentTimeMillis() + ".mp3"; // Ajusta la extensión según el formato de audio
                            File archivoAudio = new File(directorioArchivos, nombreArchivo);

                            if (urlAudio.isEmpty() || urlAudio == null) {

                            } else {
                                borrarArchivo(urlAudio);
                            }

                            OutputStream outputStream = new FileOutputStream(archivoAudio);
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = inputStream.read(buffer)) > 0) {
                                outputStream.write(buffer, 0, length);
                            }

                            // Cierra los flujos
                            inputStream.close();
                            outputStream.close();


                            // Puedes mostrar la URL en un TextView o almacenarla según tus necesidades
                            tvAud.setText(archivoAudio.getAbsolutePath());
                            compartirViewModel.setUrlAud(archivoAudio.getAbsolutePath());

                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "Error al guardar el audio", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {

                    try {
                        // Obtén el InputStream del contenido del archivo de audio
                        InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);

                        // Crea el directorio si no existe
                        File directorioArchivos = new File(requireContext().getFilesDir(), DIRECTORIO_ARCHIVOS);
                        if (!directorioArchivos.exists()) {
                            directorioArchivos.mkdir();
                        }

                        // Crea el archivo en el directorio "archivos"
                        String nombreArchivo = System.currentTimeMillis() + ".mp3"; // Ajusta la extensión según el formato de audio
                        File archivoAudio = new File(directorioArchivos, nombreArchivo);

                        if (urlAudio.isEmpty() || urlAudio == null) {

                        } else {
                            borrarArchivo(urlAudio);
                        }

                        // Copia el contenido del InputStream al OutputStream del archivo
                        OutputStream outputStream = new FileOutputStream(archivoAudio);
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }

                        // Cierra los flujos
                        inputStream.close();
                        outputStream.close();


                        // Puedes mostrar la URL en un TextView o almacenarla según tus necesidades
                        tvAud.setText(archivoAudio.getAbsolutePath());
                        compartirViewModel.setUrlAud(archivoAudio.getAbsolutePath());

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error al guardar el audio", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    public static boolean borrarArchivo(String urlArchivo) {
        try {
            // Crear un objeto File con la URL proporcionada
            File archivo = new File(urlArchivo);

            // Verificar si el archivo existe antes de intentar borrarlo
            if (archivo.exists()) {
                // Intentar borrar el archivo
                if (archivo.delete()) {

                    return true;
                } else {

                    return false;
                }
            } else {

                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        //Restaurar el texto en el EditText
        if(savedInstanceState!=null) {
            String descripcion = savedInstanceState.getString("descripcion", "");
            compartirViewModel.setDescripcion(descripcion);


        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("descripcion", etmDescripcion.getText().toString());


    }
}