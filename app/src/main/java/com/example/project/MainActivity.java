package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTelefono;  // Campo para el teléfono
    private EditText editTextDNI;       // Campo para el DNI
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTelefono = findViewById(R.id.editTextTelefono); // Asegúrate de que este ID coincida con tu XML
        editTextDNI = findViewById(R.id.editTextDNI);           // Asegúrate de que este ID coincida con tu XML
        buttonLogin = findViewById(R.id.buttonLogin);           // Asegúrate de que este ID coincida con tu XML

        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        buttonLogin.setOnClickListener(v -> {
            String telefono = editTextTelefono.getText().toString(); // Obtener teléfono
            String dni = editTextDNI.getText().toString();           // Obtener DNI

            if (telefono.isEmpty() || dni.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            login(telefono, dni, editor); // Llamar a login con teléfono y DNI
        });
    }

    private void sendVerificationCode(String email, String code) {
        ApiInterface apiService = ApiService.getApiInterface();
        Call<Void> call = apiService.verifyCode(email, code);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Código enviado al correo", Toast.LENGTH_SHORT).show();
                    // Redirigir a la pantalla de verificación
                    Intent intent = new Intent(MainActivity.this, CodeVerificationActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Error al enviar el código", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login(String telefono, String dni, SharedPreferences.Editor editor) {
        ApiInterface apiService = ApiService.getApiInterface();
        Call<LoginResponse> call = apiService.login(telefono, dni);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    if (loginResponse.getUser() != null) {
                        String email = loginResponse.getUser().getEmail();
                        String verificationCode = String.format("%06d", new Random().nextInt(999999));

                        // Guardar el código temporalmente
                        editor.putString("verification_code", verificationCode);
                        editor.putString("user_email", email);
                        editor.apply();

                        // Enviar el código al backend
                        sendVerificationCode(email, verificationCode);
                    } else {
                        Toast.makeText(MainActivity.this, "Login fallido. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Error de conexión. Intenta de nuevo.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}