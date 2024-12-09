package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CodeVerificationActivity extends AppCompatActivity {

    private EditText editTextCode;
    private Button buttonVerify;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verification);

        editTextCode = findViewById(R.id.editTextCode);
        buttonVerify = findViewById(R.id.buttonVerify);
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);

        buttonVerify.setOnClickListener(v -> {
            String enteredCode = editTextCode.getText().toString();

            if (enteredCode.isEmpty()) {
                Toast.makeText(CodeVerificationActivity.this, "Por favor, ingresa el código", Toast.LENGTH_SHORT).show();
                return;
            }

            // Obtener el código original y el correo desde SharedPreferences
            String originalCode = sharedPreferences.getString("verification_code", null);
            String userEmail = sharedPreferences.getString("user_email", null);

            if (originalCode == null || userEmail == null) {
                Toast.makeText(CodeVerificationActivity.this, "Error interno. Por favor, intenta nuevamente.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verificar el código en el servidor
            verifyCode(userEmail, enteredCode);
        });
    }

    private void verifyCode(String email, String enteredCode) {
        ApiInterface apiService = ApiService.getApiInterface();
        Call<Void> call = apiService.verifyCode(email, enteredCode);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CodeVerificationActivity.this, "Código verificado correctamente", Toast.LENGTH_SHORT).show();

                    // Redirigir al perfil del usuario
                    Intent intent = new Intent(CodeVerificationActivity.this, UserProfileActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(CodeVerificationActivity.this, "Código incorrecto. Inténtalo nuevamente.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(CodeVerificationActivity.this, "Error de conexión. Intenta nuevamente.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
