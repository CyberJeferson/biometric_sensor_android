package com.utilidades.bometriaaps;

import java.util.List;

public class con_user {

    private static String register;
    private static String name;
    private static String jobDS;
    private static int jobLvl;
    public static void setJobDS(String descrption){
        jobDS = descrption;

    }
    public static String getjobDS(){
        return jobDS;
    }
    public static void setjobLvl(int lvl){
        jobLvl = lvl;
    }
    public static int getjobLvl(){
        return jobLvl;
    }

    public static void setRegister(String reg){
        register = reg;
    }

    public static void setName(String nam){
        name = nam;
    }
    public static String getName(){
        return name;
    }
    public static String getRegister(){
        return register;
    }

}
