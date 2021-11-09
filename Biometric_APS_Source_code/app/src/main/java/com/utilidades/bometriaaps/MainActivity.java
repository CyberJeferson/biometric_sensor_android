package com.utilidades.bometriaaps;


//------------------------------------------
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import java.sql.*;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;


import java.util.concurrent.Executor;



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
  protected  Connection conect;
  private Switch savePass;
  private boolean checkuser = false;
    static final String[] teste = {"SELECT * FROM users WHERE"};
    static  final String[] texto = {"","","", "", ""};
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
      setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


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
              TxtStatus.setText("A Biometria falhou, use a biometria para entrar");
          }

          @Override
          public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
              super.onAuthenticationSucceeded(result);

              //---------------------------------------------------------------------------inicio do banco de dados mysql verificações
              class Task1 extends AsyncTask<Void, Void, Void>{
                  @Override
                  protected Void doInBackground(Void... Void) {

                      conect = conmysql.connect();
                      try {


                          teste[0] = "SELECT * FROM users INNER JOIN job_role\n" +
                                  "ON users.user_role = job_role.job_level  WHERE user_reg ='" + txtName.getText().toString() + "'";
                          Statement stmt = conect.createStatement();
                          ResultSet rs = stmt.executeQuery(teste[0]);
                          while(rs.next()){

                              texto[0] = rs.getString("user_reg");
                              texto[1] = rs.getString("pass_user");
                              texto[2] =  rs.getString("nm_user");
                              texto[3] = rs.getString("job_role.ds");
                              texto[4] = rs.getString("job_role.job_level");


                          }








                        conect.close();

                      }catch (Exception e){
                          //TxtStatus.setText(e.getMessage());
                      }

                      return null;

                  }
                  @Override
                  protected void onPostExecute(Void aVoid){

                      if (texto[0].compareTo(txtName.getText().toString()) == 0 && texto[1].compareTo(txtPassword.getText().toString())==0){

                          Intent nowa = new Intent(MainActivity.this, infos.class);
                          con_user.setName(texto[2]);
                          con_user.setRegister(texto[0]);
                          con_user.setJobDS(texto[3]);
                          con_user.setjobLvl(Integer.valueOf(texto[4]));

                          startActivity(nowa);
                      }else{
                          TxtStatus.setText("Matricula ou senha incorreto");
                      }
                      super.onPostExecute(aVoid);

                  }
              }

              new Task1().execute();





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
//-------------------------------------------------------------------------FIM DAS VERIFICAÇÕES



          }

          @Override
          public void onAuthenticationFailed() {
              super.onAuthenticationFailed();
              TxtStatus.setText("VOCÊ NÃO FOI AUTENTICADO");
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