package com.utilidades.bometriaaps;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ActionMode;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.Statement;

import static com.utilidades.bometriaaps.infos.conect;

public class det_inf extends AppCompatActivity {
            String sql;
            Bitmap bitm;
    TextView vprodname, vprodann, vprodnmachine, vprodnaddres, vprodinfprod, vprodinfdest, vprodinflvl, vnumberemplo;
    String  sprodname, sprodann, sprodnmachine, sprodnaddres, sprodinfprod, sprodinfdest, sprodinflvl, snumberemplo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_det_inf);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ImageView imagep;
        imagep = findViewById(R.id.imgProd);


        vprodname = findViewById(R.id.name_up); //NOME DA UNIDADE PRODUTORA
        vprodann = findViewById(R.id.txtAnn);
        vprodnmachine = findViewById(R.id.txtmachinenumber);
        vprodnaddres = findViewById(R.id.end_up);
        vprodinfprod = findViewById(R.id.txtproducts); //PRODUTOS PRODUZIDOS
        vprodinfdest = findViewById(R.id.txtLcation);
        vprodinflvl = findViewById(R.id.txtlvlaut);
        vnumberemplo = findViewById(R.id.txtnemp);

        class loadTaskdb extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
        try{

            sql = "SELECT * FROM private_infos WHERE inf_name LIKE '" + det_list.getGname() + "'";
            Statement stmt = conect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()){
                //=========================================================================
                try {
                    URL aURL = new URL(rs.getString("inf_file"));
                    URLConnection conn = aURL.openConnection();
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    bitm = BitmapFactory.decodeStream(bis);

                    bis.close();
                    is.close();
                }catch (Exception exv){

                }
                //--------------------------------------------------------------------------

                sprodname = rs.getString("inf_name");
                sprodann = rs.getString("inf_ann_production");
                sprodnmachine = rs.getString("inf_qtd_machines");
                sprodinfprod = rs.getString("inf_products");
                sprodnaddres = rs.getString("inf_ address");
                sprodinfdest = rs.getString("inf_dest_product");
                sprodinflvl = rs.getString("inf_aut_lvl");
                snumberemplo = rs.getString("inf_numb_employees");


            }

            }catch(Exception ex){

            }

                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                imagep.setImageBitmap(bitm);
                vprodname.setText("PRODUTORA: " + sprodname);
                vprodann.setText("PRODUÇÃO ANUAL: " + sprodann);
                vprodnmachine.setText("NÚMERO DE MÁQUINAS: " + sprodnmachine);
                vprodnaddres.setText("ENDEREÇO: " + sprodnaddres);
                vprodinfprod.setText("PRODUTOS PRODUZIDOS: " + sprodinfprod);
                vprodinfdest.setText("DESTINO DA PRODUÇÃO: " + sprodinfdest);
                vprodinflvl.setText("NÍVEL DE AUTOMAÇÃO: " + sprodinflvl);
                vnumberemplo.setText("TOTAL DE FUNCIONÁRIOS: " + snumberemplo);


                super.onPostExecute(aVoid);
            }
        }
        new loadTaskdb().execute();











    }


}