package com.utilidades.bometriaaps;

import android.os.StrictMode;
import android.widget.Toast;

import java.sql.*;

public class conmysql {
    static java.sql.Connection conn = null;
    public static Connection connect(){


        try {

            conn = DriverManager.getConnection("jdbc:mysql://trabalhosremotos.serveblog.net:3306/aps?" +
                    "user=Mike&password=");
            System.out.println( ">>>>sucesso>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            return conn;

        } catch (SQLException e) {
            System.out.println( ">>>>"+e.getMessage());
            return conn;
        }

    }



}
