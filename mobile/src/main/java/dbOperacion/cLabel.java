package dbOperacion;
/*
//////////////////////////////////////////////////////////////////////
KMF Tecnología - Saint Gobain Sekurit
Desarrolló:  Joaquín Andrés García Gutierrez
Fecha:       08/01/2018
Descripción: Administra las reglas de negocio
//////////////////////////////////////////////////////////////////////
*/

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import dbConexion.DBManager;
public class cLabel {

    private static DBManager oDB;
    private static String sSql  = "";

    public static Boolean setLastLabel(String Etiqueta){
        Boolean iReturn = false;
        try{
            oDB = DBManager.instance();
            sSql = "Update LastLabel set cLabel= '" + Etiqueta + "'";
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            int iID = 0;
            if(oCursor.getCount() > 0 && oCursor != null) {
                iReturn = true;
            }

        }catch (Exception ex){
            iReturn = false;
        }
        return iReturn;
    }

    public static String getLastLabel(){
        String iReturn = "";
        try{
            oDB = DBManager.instance();
            sSql = "Select cLabel From LastLabel";
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            int iID = 0;
            if(oCursor.getCount() > 0 && oCursor != null) {
                oCursor.moveToFirst();
                iReturn = oCursor.getString(0);
            }

        }catch (Exception ex){
            iReturn = "";
        }
        return iReturn;
    }
}
