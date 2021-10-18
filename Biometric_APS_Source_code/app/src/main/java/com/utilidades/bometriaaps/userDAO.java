package com.utilidades.bometriaaps;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;



public class userDAO {
private String comm;
 ConnectBD Conbd;
 private SQLiteDatabase infos;

 public userDAO(Context context){ //Construtor

     Conbd = new ConnectBD(context);
     infos =  Conbd.getWritableDatabase();
 }

 public long insertUser(user_bio user){ //Inserir novos dados de senha e user
     ContentValues val = new ContentValues();
     val.put("name", user.getName());
     val.put("pass", user.getPasswd());

  return infos.insert("user",null,val);

 }

 public void searchUser(user_bio user){ //Procurar usuarios salvos
    Cursor c = infos.query("user",new String[]{"name", "pass"}, null, null, null, null, null);
    while(c.moveToNext()){
        user.setName(c.getString(0));
        user.setPasswd(c.getString(1));
        infos.close();
    }




 }

 //Deletar a tabela
public void deteAlluser(){
infos.execSQL("DELETE FROM  user");//Deleta todas informações salvas de usuarios
}

}
