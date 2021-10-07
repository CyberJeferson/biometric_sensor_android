package com.utilidades.bometriaaps;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;


import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {
  protected Button getBio;
  protected TextView TxtStatus;
  public Executor exec;
  private androidx.biometric.BiometricPrompt Bprompt;
  private androidx.biometric.BiometricPrompt.PromptInfo pinfo;
  private user_bio user = new user_bio();

  @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getBio = findViewById(R.id.btn1);
        TxtStatus = findViewById(R.id.txtv1);

//--------------------------------------------------
      exec = ContextCompat.getMainExecutor(this);
      Bprompt = new BiometricPrompt(MainActivity.this, new BiometricPrompt.AuthenticationCallback() {
          @Override
          public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
              super.onAuthenticationError(errorCode, errString);
              TxtStatus.setText("Você não foi autenticado");
          }

          @Override
          public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
              super.onAuthenticationSucceeded(result);
              TxtStatus.setText("Parabéns você pode  entrar");

          }

          @Override
          public void onAuthenticationFailed() {
              super.onAuthenticationFailed();
              TxtStatus.setText("erros");
          }
      });
      pinfo = new BiometricPrompt.PromptInfo.Builder()
              .setTitle("Autenticação  biométrica")
              .setSubtitle("Use sua impressão digital  para logar")
              .setNegativeButtonText("Voltar")
              .build();
    getBio.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bprompt.authenticate(pinfo);
        }
    });
    }
}