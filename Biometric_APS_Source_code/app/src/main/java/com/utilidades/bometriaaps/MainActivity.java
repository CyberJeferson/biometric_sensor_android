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
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {
  protected Button getBio;
  protected TextView TxtStatus;
  public Executor exec;
  private androidx.biometric.BiometricPrompt Bprompt;
  private androidx.biometric.BiometricPrompt.PromptInfo pinfo;
  private user_bio user = user = new user_bio();;
  private userDAO userdao;
  private EditText txtName;
  private TextView txtPassword;
  private long iduser;
  private Switch savePass;
  @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getBio = findViewById(R.id.btn1);
        TxtStatus = findViewById(R.id.txtv1);
        txtName = findViewById(R.id.ptxtname);
        txtPassword = findViewById(R.id.txtPass);
        savePass = findViewById(R.id.switch1);

 //-------------------------------------Carregar informações de senhas salvas se existir
      try {
          userdao  = new userDAO(MainActivity.this);
          userdao.searchUser(user);
          if (user.getName().length() > 0){
              txtName.setText(user.getName());
              txtPassword.setText(user.getPasswd());
              savePass.setChecked(true);
          }

      }catch (Exception ex){

      }



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

              if (savePass.isChecked()){

                  userdao  = new userDAO(MainActivity.this);
                  userdao.deteAlluser();
                  user.setName(txtName.getText().toString());
                  user.setPasswd(txtPassword.getText().toString());
                  iduser =  userdao.insertUser(user);
              }else{
                  userdao  = new userDAO(MainActivity.this);
                  userdao.deteAlluser();
              }



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