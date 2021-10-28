package com.utilidades.bometriaaps;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.autofill.AutofillValue;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;


public class infos extends AppCompatActivity {

           public class info {


                public String desc;

            }


            private static Bitmap bm = null;
             protected  Connection conect;
            ImageView imageInfo;
            private TextView txtName;
            private TextView txtRegister,JobF;
            private GridLayout grid;
            static int vl;



            URL url;
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.infos);
                txtName = findViewById(R.id.info_txtName);
                txtRegister = findViewById(R.id.info_txtMatricula);
                txtRegister.setText("Matrícula: " + con_user.getRegister());
                txtName.setText("Nome: " + con_user.getName());
                grid =  findViewById(R.id.grid1);
                imageInfo = findViewById(R.id.imageView2);
                JobF = findViewById(R.id.jobf1);
                JobF.setText(con_user.getjobDS());
                setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



                class TaskList extends AsyncTask<Void, Void, Void> {

                List<String> getInfo = new ArrayList<>();
                List<Bitmap> urls = new ArrayList<>();


                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    protected Void doInBackground(Void... Void) {

                        conect = conmysql.connect();
                        info newinfos = new info();

                        String sql;
                        try {


                            sql = "SELECT * FROM private_infos";
                            Statement stmt = conect.createStatement();
                            ResultSet rs = stmt.executeQuery(sql);
                            while (rs.next()) {

                                newinfos.desc = rs.getString("inf_name");

                                try {
                                    URL aURL = new URL(rs.getString("inf_file"));
                                    URLConnection conn = aURL.openConnection();
                                    conn.connect();
                                    InputStream is = conn.getInputStream();
                                    BufferedInputStream bis = new BufferedInputStream(is);
                                    bm = BitmapFactory.decodeStream(bis);

                                    bis.close();
                                    is.close();
                                }catch (Exception exv){

                                }

                                    urls.add(bm);
                                    getInfo.add(newinfos.desc);
                                    bm = null;






                            }


                           conect.close();

                        } catch (Exception e) {

                        }

                        return null;

                    }

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @SuppressLint("MissingSuperCall")
                    @Override
                    protected void onPostExecute(Void aVoid) {

                        grid.setRowCount(getInfo.size());
                        grid.setColumnCount(2);
                                for(int i =0; i< getInfo.size(); i++){


                                    ImageView dinamicIn = new ImageView(infos.this);
                                    TextView textD = new TextView(infos.this);
                                    dinamicIn.setImageBitmap(urls.get(i));
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150, 200);
                                    dinamicIn.setLayoutParams(params);
                                    //dinamicIn.requestLayout();
                                    textD.setText(getInfo.get(i));
                                    textD.setWidth(10000);
                                    textD.setHeight(200);
                                    dinamicIn.setBackgroundColor(Color.rgb(131, 187, 95));
                                    textD.setBackgroundColor(Color.rgb(161, 193, 139));








                                    grid.addView(dinamicIn);
                                    grid.addView(textD);


                                    textD.setOnClickListener(new View.OnClickListener() {
                                        String texto = textD.getText().toString();
                                        @Override
                                        public void onClick(View v) {
                                            det_list.setGname(texto);

                                            Intent detProd = new Intent(infos.this, det_inf.class);

                                            startActivity(detProd);




                                        }
                                    });
                                    dinamicIn.setOnClickListener(new View.OnClickListener() {
                                        String texto = textD.getText().toString();
                                        @Override
                                        public void onClick(View v) {
                                            det_list.setGname(texto);

                                            Intent detProd = new Intent(infos.this, det_inf.class);

                                            startActivity(detProd);

                                        }
                                    });









                                }

                        try{
                            getInfo =null;
                            urls = null;
                        }catch (Exception ep){

                        }







                        super.onPostExecute(aVoid);




                    }

                }
                new TaskList().execute();


            }
        }

