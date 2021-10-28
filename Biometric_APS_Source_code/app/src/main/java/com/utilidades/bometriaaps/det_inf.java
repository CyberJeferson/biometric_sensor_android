package com.utilidades.bometriaaps;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



public class det_inf extends AppCompatActivity {
            String sql;
            Bitmap bitm;
            private Connection conect;

            class taxations{
                String Title;
               double Valprod, IRPF, CSLL, PIS, COFINS, ICMS, IPI, INSS, TOTALTRIBUTOS;
            }

            class taxes{
                String Title;
                double PRESUN, IRPJ, ADDIRPJ, CSLL, PIS, COFINS, TOTALTAXES;
            }
            class incet{
                String  TITLE;
                boolean PROIBIDO;

            }
            incet agrotox;
            taxations trib;
            taxes presun;
            List<taxations> tributacao = new ArrayList<>();
            List<taxes> imp = new ArrayList<>();
            List<incet> agrotoxicos = new ArrayList<>();
            TextView vprodname, vprodann, vprodnmachine, vprodnaddres, vprodinfprod, vprodinfdest, vprodinflvl, vnumberemplo;
            String  sprodname, sprodann, sprodnmachine, sprodnaddres, sprodinfprod, sprodinfdest, sprodinflvl, snumberemplo;
            LinearLayout ListTrib;
            private int IDEMPRESA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_det_inf);
        //setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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
        ListTrib = findViewById(R.id.det_inlist1);


        class loadTaskdb extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

        try{
            conect = conmysql.connect();

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
                IDEMPRESA = rs.getInt("inf_id");



            }
          conect.close();

            }catch(Exception ex){

            }
        //CARREGAR INFORMAÇÕES PRIVADAS PARA CADA CARGO
                if(con_user.getjobLvl() == 2){
                try{
                    conect = conmysql.connect();
                    sql = "SELECT * FROM taxation WHERE taxation_infID =" + IDEMPRESA;
                    Statement stmt = conect.createStatement();
                    ResultSet rs = stmt.executeQuery(sql);
                    while(rs.next()){
                        trib = new taxations();
                        trib.Title = rs.getString("taxation_name");
                        trib.Valprod = rs.getDouble("taxation_valueprod");
                        trib.IRPF = rs.getDouble("taxation_IRPF");
                        trib.CSLL = rs.getDouble("taxation_CSLL");
                        trib.PIS = rs.getDouble("taxation_PIS");
                        trib.COFINS = rs.getDouble("taxation_COFINS");
                        trib.ICMS = rs.getDouble("taxation_ICMS");
                        trib.IPI = rs.getDouble("taxation_IPI");
                        trib.INSS = rs.getDouble("taxation_INSS");
                        trib.TOTALTRIBUTOS = rs.getDouble("taxation_TOTAL");
                        tributacao.add(trib);
                    }
                    conect.close();

                }catch (Exception conex){

                }

                //IMPOSTOS SEMESTRAIS
                    try{
                        conect = conmysql.connect();
                        sql = "SELECT * FROM inf_taxes WHERE taxe_job_id =" + IDEMPRESA;
                        Statement stmt = conect.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);
                        while(rs.next()){
                            presun =  new taxes();
                            presun.Title = rs.getString("taxe_name");
                            presun.PRESUN = rs.getDouble("taxe_value");
                            presun.IRPJ = rs.getDouble("taxe_IR");
                            presun.ADDIRPJ  = rs.getDouble("taxe_adicionalIR");
                            presun.CSLL  =  rs.getDouble("taxe_CSLL");
                            presun.PIS  = rs.getDouble("taxe_PIS");
                            presun.COFINS = rs.getDouble("taxe_COFINS");
                            presun.TOTALTAXES = rs.getDouble("taxe_totalTaxes");
                            imp.add(presun);
                        }
                        conect.close();
                    }catch (Exception conex){

                    }

                }else{
                    //INFORMAÇÕES DO MINISTRO DO MEIO AMBIENTE

                    try{
                        conect = conmysql.connect();
                           sql = "SELECT * FROM biohazard_pesticide WHERE bio_jobid =" + IDEMPRESA;
                        Statement stmt = conect.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);
                        while(rs.next()){
                            agrotox =  new incet();
                            agrotox.TITLE = rs.getString("bio_name");
                            agrotox.PROIBIDO = rs.getBoolean("bio_prohibited");
                            agrotoxicos.add(agrotox);
                        }
                       conect.close();
                    }catch (Exception e){

                    }







                }


                return null;
            }
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @SuppressLint("WrongConstant")
            @Override
            protected void onPostExecute(Void aVoid) {
                Locale ptBr = new Locale("pt", "BR");
                String moneybr;
                imagep.setImageBitmap(bitm);
                vprodname.setText("PRODUTORA: " + sprodname);
                vprodann.setText("PRODUÇÃO ANUAL: " + sprodann);
                vprodnmachine.setText("NÚMERO DE MÁQUINAS: " + sprodnmachine);
                vprodnaddres.setText("ENDEREÇO: " + sprodnaddres);
                vprodinfprod.setText("PRODUTOS PRODUZIDOS: " + sprodinfprod);
                vprodinfdest.setText("DESTINO DA PRODUÇÃO: " + sprodinfdest);
                vprodinflvl.setText("NÍVEL DE AUTOMAÇÃO: " + sprodinflvl);
                vnumberemplo.setText("TOTAL DE FUNCIONÁRIOS: " + snumberemplo);

                //INFORMAÇÕES SEPARADAS PARA CADA CARGO DE UM FUNCIONÁRIO PÚBLICO
                GradientDrawable gradientDrawable=new GradientDrawable();
                gradientDrawable.setStroke(4,getResources().getColor(R.color.black));
                int tamanho = 600;
                int altura = 80;
                NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
                nf.setMaximumFractionDigits(2);
                nf.setMaximumFractionDigits(2);

                if (con_user.getjobLvl() == 2){
                    GridLayout grido = new GridLayout(det_inf.this);


                    grido.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    grido.setRowCount(tributacao.size() + 1);
                    grido.setColumnCount(10);
                    TextView getInfo = new TextView(det_inf.this);
                    getInfo.setText("DESCRIÇÃO");
                    getInfo.setTextColor(Color.RED);
                    getInfo.setHeight(altura);
                    getInfo.setWidth(tamanho);
                    getInfo.setBackground(gradientDrawable);
                    grido.addView(getInfo);
                    getInfo = new TextView(det_inf.this);
                    getInfo.setText("VALOR PRODUTO:");
                    getInfo.setTextColor(Color.RED);
                    getInfo.setHeight(altura);
                    getInfo.setWidth(tamanho);
                    getInfo.setBackground(gradientDrawable);
                    grido.addView(getInfo);
                    getInfo = new TextView(det_inf.this);
                    getInfo.setText("IRPF:");
                    getInfo.setTextColor(Color.RED);
                    getInfo.setHeight(altura);
                    getInfo.setWidth(tamanho);
                    getInfo.setBackground(gradientDrawable);
                    grido.addView(getInfo);
                    getInfo = new TextView(det_inf.this);
                    getInfo.setText("CSLL:");
                    getInfo.setTextColor(Color.RED);
                    getInfo.setHeight(altura);
                    getInfo.setWidth(tamanho);
                    getInfo.setBackground(gradientDrawable);
                    grido.addView(getInfo);
                    getInfo = new TextView(det_inf.this);
                    getInfo.setText("PIS:");
                    getInfo.setTextColor(Color.RED);
                    getInfo.setHeight(altura);
                    getInfo.setWidth(tamanho);
                    getInfo.setBackground(gradientDrawable);
                    grido.addView(getInfo);
                    getInfo = new TextView(det_inf.this);
                    getInfo.setText("COFINS:");
                    getInfo.setTextColor(Color.RED);
                    getInfo.setHeight(altura);
                    getInfo.setWidth(tamanho);
                    getInfo.setBackground(gradientDrawable);
                    grido.addView(getInfo);
                    getInfo = new TextView(det_inf.this);
                    getInfo.setText("ICMS:");
                    getInfo.setTextColor(Color.RED);
                    getInfo.setHeight(altura);
                    getInfo.setWidth(tamanho);
                    getInfo.setBackground(gradientDrawable);
                    grido.addView(getInfo);
                    getInfo = new TextView(det_inf.this);
                    getInfo.setText("IPI:");
                    getInfo.setTextColor(Color.RED);
                    getInfo.setHeight(altura);
                    getInfo.setWidth(tamanho);
                    getInfo.setBackground(gradientDrawable);
                    grido.addView(getInfo);
                    getInfo = new TextView(det_inf.this);
                    getInfo.setText("INSS:");
                    getInfo.setTextColor(Color.RED);
                    getInfo.setHeight(altura);
                    getInfo.setWidth(tamanho);
                    getInfo.setBackground(gradientDrawable);
                    grido.addView(getInfo);
                    getInfo = new TextView(det_inf.this);
                    getInfo.setText("TAXA TOTAL:");
                    getInfo.setTextColor(Color.RED);
                    getInfo.setHeight(altura);
                    getInfo.setWidth(tamanho);
                    getInfo.setBackground(gradientDrawable);

                    grido.addView(getInfo);
                    for (int i = 0;i< tributacao.size();i++){
                        getInfo = new TextView(det_inf.this);
                        getInfo.setText(tributacao.get(i).Title);
                        getInfo.setTextColor(Color.BLACK);
                        getInfo.setHeight(altura);
                        getInfo.setWidth(tamanho);
                        getInfo.setBackground(gradientDrawable);
                        grido.addView(getInfo);

                        getInfo = new TextView(det_inf.this);
                        if (tributacao.get(i).Valprod != 0){
                            moneybr = NumberFormat.getCurrencyInstance(ptBr).format(tributacao.get(i).Valprod);
                            getInfo.setText(moneybr);
                        }else{
                            getInfo.setText("ISENTO");
                        }
                        getInfo.setTextColor(Color.BLACK);
                        getInfo.setHeight(altura);
                        getInfo.setWidth(tamanho);
                        getInfo.setBackground(gradientDrawable);
                        grido.addView(getInfo);

                        getInfo = new TextView(det_inf.this);
                        if (tributacao.get(i).IRPF != 0){
                            moneybr = NumberFormat.getCurrencyInstance(ptBr).format(tributacao.get(i).IRPF);
                            getInfo.setText(moneybr);
                        }else{
                            getInfo.setText("ISENTO");

                        }
                        getInfo.setTextColor(Color.BLACK);
                        getInfo.setHeight(altura);
                        getInfo.setWidth(tamanho);
                        getInfo.setBackground(gradientDrawable);
                        grido.addView(getInfo);

                        getInfo = new TextView(det_inf.this);
                        if (tributacao.get(i).CSLL != 0){
                            moneybr = NumberFormat.getCurrencyInstance(ptBr).format(tributacao.get(i).CSLL);
                            getInfo.setText(moneybr);
                        }else{
                            getInfo.setText("ISENTO");

                        }
                        getInfo.setTextColor(Color.BLACK);
                        getInfo.setHeight(altura);
                        getInfo.setWidth(tamanho);
                        getInfo.setBackground(gradientDrawable);
                        grido.addView(getInfo);

                        getInfo = new TextView(det_inf.this);
                        if (tributacao.get(i).PIS != 0){
                            moneybr = NumberFormat.getCurrencyInstance(ptBr).format(tributacao.get(i).PIS);
                            getInfo.setText(moneybr);
                        }else{
                            getInfo.setText("ISENTO");

                        }
                        getInfo.setTextColor(Color.BLACK);
                        getInfo.setHeight(altura);
                        getInfo.setWidth(tamanho);
                        getInfo.setBackground(gradientDrawable);
                        grido.addView(getInfo);

                        getInfo = new TextView(det_inf.this);
                        if (tributacao.get(i).COFINS != 0){
                            moneybr = NumberFormat.getCurrencyInstance(ptBr).format(tributacao.get(i).COFINS);
                            getInfo.setText(moneybr);
                        }else{
                            getInfo.setText("ISENTO");

                        }
                        getInfo.setTextColor(Color.BLACK);
                        getInfo.setHeight(altura);
                        getInfo.setWidth(tamanho);
                        getInfo.setBackground(gradientDrawable);
                        grido.addView(getInfo);

                        getInfo = new TextView(det_inf.this);
                        if (tributacao.get(i).ICMS != 0){
                            moneybr = NumberFormat.getCurrencyInstance(ptBr).format(tributacao.get(i).ICMS);
                            getInfo.setText(moneybr);
                        }else{
                            getInfo.setText("ISENTO");

                        }
                        getInfo.setTextColor(Color.BLACK);
                        getInfo.setHeight(altura);
                        getInfo.setWidth(tamanho);
                        getInfo.setBackground(gradientDrawable);
                        grido.addView(getInfo);

                        getInfo = new TextView(det_inf.this);
                        if (tributacao.get(i).IPI != 0){
                            moneybr = NumberFormat.getCurrencyInstance(ptBr).format(tributacao.get(i).IPI);
                            getInfo.setText(moneybr);
                        }else{
                            getInfo.setText("ISENTO");

                        }
                        getInfo.setTextColor(Color.BLACK);
                        getInfo.setHeight(altura);
                        getInfo.setWidth(tamanho);
                        getInfo.setBackground(gradientDrawable);
                        grido.addView(getInfo);

                        getInfo = new TextView(det_inf.this);
                        if (tributacao.get(i).INSS != 0){
                            moneybr = NumberFormat.getCurrencyInstance(ptBr).format(tributacao.get(i).INSS);
                            getInfo.setText(moneybr);
                        }else{
                            getInfo.setText("ISENTO");

                        }
                        getInfo.setTextColor(Color.BLACK);
                        getInfo.setHeight(altura);
                        getInfo.setWidth(tamanho);
                        getInfo.setBackground(gradientDrawable);
                        grido.addView(getInfo);

                        getInfo = new TextView(det_inf.this);
                        if (tributacao.get(i).TOTALTRIBUTOS != 0){
                            moneybr = NumberFormat.getCurrencyInstance(ptBr).format(tributacao.get(i).TOTALTRIBUTOS);
                            getInfo.setText(moneybr);
                        }else{
                            getInfo.setText("ISENTO");

                        }
                        getInfo.setTextColor(Color.BLACK);
                        getInfo.setHeight(altura);
                        getInfo.setWidth(tamanho);
                        getInfo.setBackground(gradientDrawable);
                        grido.addView(getInfo);



                    }

               // grido.setOrientation(LinearLayout.VERTICAL);

                    HorizontalScrollView horizontal;
                    horizontal = new HorizontalScrollView(det_inf.this);
                    grido.setBackgroundColor(Color.rgb(161, 193, 139));
                    horizontal.addView(grido);
                    TextView titulo1 = new TextView(det_inf.this);
                    titulo1.setText("TRIBUTAÇÕES A PAGAR:");
                    titulo1.setTextColor(Color.WHITE);
                    titulo1.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    titulo1.setTextScaleX(3f);
                    ListTrib.addView(titulo1);
                    ListTrib.addView(horizontal);

                    //INFORMAÇÕES DE IMPOSTOS TRIMESTRAIS
                    grido = new GridLayout(det_inf.this);
                    grido.setColumnCount(8);
                    grido.setRowCount(imp.size()+ 1);
//----------------------------------TITULOS.
                    getInfo = new TextView(det_inf.this);
                    getInfo.setText("DESCRIÇÃO DO IMPOSTO:");
                    getInfo.setTextColor(Color.RED);
                    getInfo.setHeight(altura);
                    getInfo.setWidth(tamanho);
                    getInfo.setBackground(gradientDrawable);
                    grido.addView(getInfo);

                    getInfo = new TextView(det_inf.this);
                    getInfo.setText("VALOR PRESUMIDO:");
                    getInfo.setTextColor(Color.RED);
                    getInfo.setHeight(altura);
                    getInfo.setWidth(tamanho);
                    getInfo.setBackground(gradientDrawable);
                    grido.addView(getInfo);

                    getInfo = new TextView(det_inf.this);
                    getInfo.setText("IRPJ:");
                    getInfo.setTextColor(Color.RED);
                    getInfo.setHeight(altura);
                    getInfo.setWidth(tamanho);
                    getInfo.setBackground(gradientDrawable);
                    grido.addView(getInfo);

                    getInfo = new TextView(det_inf.this);
                    getInfo.setText("ADD IRPJ:");
                    getInfo.setTextColor(Color.RED);
                    getInfo.setHeight(altura);
                    getInfo.setWidth(tamanho);
                    getInfo.setBackground(gradientDrawable);
                    grido.addView(getInfo);

                    getInfo = new TextView(det_inf.this);
                    getInfo.setText("CSLL:");
                    getInfo.setTextColor(Color.RED);
                    getInfo.setHeight(altura);
                    getInfo.setWidth(tamanho);
                    getInfo.setBackground(gradientDrawable);
                    grido.addView(getInfo);

                    getInfo = new TextView(det_inf.this);
                    getInfo.setText("PIS:");
                    getInfo.setTextColor(Color.RED);
                    getInfo.setHeight(altura);
                    getInfo.setWidth(tamanho);
                    getInfo.setBackground(gradientDrawable);
                    grido.addView(getInfo);

                    getInfo = new TextView(det_inf.this);
                    getInfo.setText("COFINS:");
                    getInfo.setTextColor(Color.RED);
                    getInfo.setHeight(altura);
                    getInfo.setWidth(tamanho);
                    getInfo.setBackground(gradientDrawable);
                    grido.addView(getInfo);

                    getInfo = new TextView(det_inf.this);
                    getInfo.setText("TAXA TOTAL:");
                    getInfo.setTextColor(Color.RED);
                    getInfo.setHeight(altura);
                    getInfo.setWidth(tamanho);
                    getInfo.setBackground(gradientDrawable);
                    grido.addView(getInfo);

//---------------------------------------------------------------------------INFORMAÇÕES DO BANCO DE DADOS.

                    for (int i = 0; i<imp.size();i++){
                        getInfo = new TextView(det_inf.this);
                        getInfo.setText(imp.get(i).Title);
                        getInfo.setTextColor(Color.BLACK);
                        getInfo.setHeight(altura);
                        getInfo.setWidth(tamanho);
                        getInfo.setBackground(gradientDrawable);
                        grido.addView(getInfo);

                        getInfo = new TextView(det_inf.this);
                        if (imp.get(i).PRESUN != 0){
                            moneybr = NumberFormat.getCurrencyInstance(ptBr).format(imp.get(i).PRESUN);
                            getInfo.setText(moneybr);
                        }else{
                            getInfo.setText("ISENTO");

                        }
                        getInfo.setTextColor(Color.BLACK);
                        getInfo.setHeight(altura);
                        getInfo.setWidth(tamanho);
                        getInfo.setBackground(gradientDrawable);
                        grido.addView(getInfo);

                        getInfo = new TextView(det_inf.this);
                        if (imp.get(i).IRPJ != 0){
                            moneybr = NumberFormat.getCurrencyInstance(ptBr).format(imp.get(i).IRPJ);
                            getInfo.setText(moneybr);
                        }else{
                            getInfo.setText("ISENTO");

                        }
                        getInfo.setTextColor(Color.BLACK);
                        getInfo.setHeight(altura);
                        getInfo.setWidth(tamanho);
                        getInfo.setBackground(gradientDrawable);
                        grido.addView(getInfo);

                        getInfo = new TextView(det_inf.this);
                        if (imp.get(i).ADDIRPJ != 0){
                            moneybr = NumberFormat.getCurrencyInstance(ptBr).format(imp.get(i).ADDIRPJ);
                            getInfo.setText(moneybr);
                        }else{
                            getInfo.setText("ISENTO");

                        }
                        getInfo.setTextColor(Color.BLACK);
                        getInfo.setHeight(altura);
                        getInfo.setWidth(tamanho);
                        getInfo.setBackground(gradientDrawable);
                        grido.addView(getInfo);

                        getInfo = new TextView(det_inf.this);
                        if (imp.get(i).CSLL != 0){
                            moneybr = NumberFormat.getCurrencyInstance(ptBr).format(imp.get(i).CSLL);
                            getInfo.setText(moneybr);
                        }else{
                            getInfo.setText("ISENTO");

                        }
                        getInfo.setTextColor(Color.BLACK);
                        getInfo.setHeight(altura);
                        getInfo.setWidth(tamanho);
                        getInfo.setBackground(gradientDrawable);
                        grido.addView(getInfo);

                        getInfo = new TextView(det_inf.this);
                        if (imp.get(i).PIS != 0){
                            moneybr = NumberFormat.getCurrencyInstance(ptBr).format(imp.get(i).PIS);
                            getInfo.setText(moneybr);
                        }else{
                            getInfo.setText("ISENTO");

                        }
                        getInfo.setTextColor(Color.BLACK);
                        getInfo.setHeight(altura);
                        getInfo.setWidth(tamanho);
                        getInfo.setBackground(gradientDrawable);
                        grido.addView(getInfo);


                        getInfo = new TextView(det_inf.this);
                        if (imp.get(i).COFINS != 0){
                            moneybr = NumberFormat.getCurrencyInstance(ptBr).format(imp.get(i).COFINS);
                            getInfo.setText(moneybr);
                        }else{
                            getInfo.setText("ISENTO");

                        }
                        getInfo.setTextColor(Color.BLACK);
                        getInfo.setHeight(altura);
                        getInfo.setWidth(tamanho);
                        getInfo.setBackground(gradientDrawable);
                        grido.addView(getInfo);

                        getInfo = new TextView(det_inf.this);
                        if (imp.get(i).TOTALTAXES != 0){
                            moneybr = NumberFormat.getCurrencyInstance(ptBr).format(imp.get(i).TOTALTAXES);
                            getInfo.setText(moneybr);
                        }else{
                            getInfo.setText("ISENTO");

                        }
                        getInfo.setTextColor(Color.BLACK);
                        getInfo.setHeight(altura);
                        getInfo.setWidth(tamanho);
                        getInfo.setBackground(gradientDrawable);
                        grido.addView(getInfo);
                    }




//----------------------------------------------------------------------------------
                    grido.setBackgroundColor(Color.rgb(161, 193, 139));
                    titulo1 = new TextView(det_inf.this);
                    titulo1.setText("IMPOSTOS TRIMESTRAIS PAGOS:");
                    titulo1.setTextColor(Color.WHITE);
                    titulo1.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    titulo1.setTextScaleX(3f);
                    ListTrib.addView(titulo1);
                    horizontal = new HorizontalScrollView(det_inf.this);
                    horizontal.addView(grido);
                    ListTrib.addView(horizontal);
                    imp = null;





                }else{
//----------------------------AQUI VAI AS INFORMAÇÕES DE AGROTÓXICOS.
                    ImageView biohazard;
                    GridLayout agro = new GridLayout(det_inf.this);
                    HorizontalScrollView horizontal;
                    agro.setBackgroundColor(Color.rgb(161, 193, 139));

                    biohazard = new ImageView(det_inf.this);
                    biohazard.setImageResource(R.drawable.bio);
                    biohazard.setLayoutParams(new LinearLayout.LayoutParams(100,100));

                    agro.setRowCount(agrotoxicos.size() + 1);
                    agro.setColumnCount(3);
                    TextView getInfo = new TextView(det_inf.this);
                    getInfo.setText("!:");
                    getInfo.setTextColor(Color.RED);
                    getInfo.setHeight(100);
                    getInfo.setWidth(100);
                    getInfo.setBackground(gradientDrawable);
                    agro.addView(getInfo);

                    getInfo = new TextView(det_inf.this);
                    getInfo.setText("NOME DO AGROTÓXICO:");
                    getInfo.setTextColor(Color.RED);
                    getInfo.setHeight(100);
                    getInfo.setWidth(tamanho);
                    getInfo.setBackground(gradientDrawable);
                    agro.addView(getInfo);

                    getInfo = new TextView(det_inf.this);
                    getInfo.setText("PROIBIDO:");
                    getInfo.setTextColor(Color.RED);
                    getInfo.setHeight(100);
                    getInfo.setWidth(tamanho);
                    getInfo.setBackground(gradientDrawable);
                    agro.addView(getInfo);
                    for (int i = 0; i< agrotoxicos.size(); i ++){

                        if(agrotoxicos.get(i).PROIBIDO){
                            biohazard = new ImageView(det_inf.this);
                            biohazard.setImageResource(R.drawable.bio);
                            biohazard.setLayoutParams(new LinearLayout.LayoutParams(100,100));
                            biohazard.setBackground(gradientDrawable);
                            agro.addView(biohazard);

                            getInfo = new TextView(det_inf.this);
                            getInfo.setText(agrotoxicos.get(i).TITLE);
                            getInfo.setTextColor(Color.RED);
                            getInfo.setHeight(100);
                            getInfo.setWidth(tamanho);
                            getInfo.setBackground(gradientDrawable);
                            agro.addView(getInfo);

                            getInfo = new TextView(det_inf.this);
                            getInfo.setText("SIM");
                            getInfo.setTextColor(Color.RED);
                            getInfo.setHeight(100);
                            getInfo.setWidth(tamanho);
                            getInfo.setBackground(gradientDrawable);
                            agro.addView(getInfo);

                        }else{
                            biohazard = new ImageView(det_inf.this);
                            biohazard.setLayoutParams(new LinearLayout.LayoutParams(100,100));
                            biohazard.setBackground(gradientDrawable);
                            agro.addView(biohazard);

                            getInfo = new TextView(det_inf.this);
                            getInfo.setText(agrotoxicos.get(i).TITLE);
                            getInfo.setTextColor(Color.BLACK);
                            getInfo.setHeight(100);
                            getInfo.setWidth(tamanho);
                            getInfo.setBackground(gradientDrawable);
                            agro.addView(getInfo);

                            getInfo = new TextView(det_inf.this);
                            getInfo.setText("NÃO");
                            getInfo.setTextColor(Color.BLACK);
                            getInfo.setHeight(100);
                            getInfo.setWidth(tamanho);
                            getInfo.setBackground(gradientDrawable);
                            agro.addView(getInfo);

                        }

                    }

                    //------------------------------------------------------------
                    TextView titulo1;
                    titulo1 = new TextView(det_inf.this);
                    titulo1.setText("INFORMAÇÕES P/ MINISTRO DO MEIO AMBIENTE:");
                    titulo1.setTextColor(Color.WHITE);
                    titulo1.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    titulo1.setTextScaleX(3f);
                    ListTrib.addView(titulo1);

                    horizontal = new HorizontalScrollView(det_inf.this);
                    horizontal.addView(agro);
                    ListTrib.addView(horizontal);
                    agrotoxicos = null;


                }








                super.onPostExecute(aVoid);
            }
        }
        new loadTaskdb().execute();











    }


}