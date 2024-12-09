package com.example.project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.VideoView;
import android.net.Uri;
import android.widget.MediaController;

import androidx.appcompat.app.AppCompatActivity;

public class UserProfileActivity extends AppCompatActivity {

    private TextView textViewName, textViewEmail, textViewTelefono;
    private SharedPreferences sharedPreferences;
    private VideoView videoViewIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Inicializando los TextViews
        textViewName = findViewById(R.id.textViewName);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewTelefono = findViewById(R.id.textViewTelefono);

        // Inicializando el VideoView
        videoViewIntro = findViewById(R.id.videoViewIntro);

        // Cargar datos del usuario desde SharedPreferences
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        String userName = sharedPreferences.getString("user_name", "Nombre no disponible");
        String userEmail = sharedPreferences.getString("user_email", "Email no disponible");
        String userTelefono = sharedPreferences.getString("user_telefono", "Teléfono no disponible");

        // Asignar los datos a los TextViews
        textViewName.setText(userName);
        textViewEmail.setText(userEmail);
        textViewTelefono.setText(userTelefono);

        // Configurar el VideoView
        String videoPath = "android.resource://" + getPackageName() + "/raw/video_intro";
        Uri uri = Uri.parse(videoPath);
        videoViewIntro.setVideoURI(uri);

        // Agregar un MediaController para controlar la reproducción
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoViewIntro);
        videoViewIntro.setMediaController(mediaController);

        // Configurar OnPreparedListener para asegurar que el video esté listo
        videoViewIntro.setOnPreparedListener(mediaPlayer -> {
            // Asegurarse de que el video se esté reproduciendo correctamente
            videoViewIntro.start();
        });

        // Establecer el ZOrderOnTop para asegurarse de que el video se muestra correctamente
        videoViewIntro.setZOrderOnTop(true);
    }
}
