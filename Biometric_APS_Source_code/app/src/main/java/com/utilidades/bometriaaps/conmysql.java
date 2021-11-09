package com.utilidades.bometriaaps;

import android.os.StrictMode;
import android.widget.Toast;

import java.sql.*;

public class conmysql {
    static java.sql.Connection conn = null;
    public static Connection connect(){


        try {

            conn = DriverManager.getConnection("jdbc:mysql://ipbancodedados:3306/aps?" +
                    "user=usuario&password=senha");




            return conn;

        } catch (SQLException e) {
            System.out.println( ">>>>"+e.getMessage());
            return conn;
        }

    }



}
