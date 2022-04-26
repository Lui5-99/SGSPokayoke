package dbOperacion;
/*
//////////////////////////////////////////////////////////////////////
KMF Tecnología - Saint Gobain Sekurit
Desarrolló:  Joaquín Andrés García Gutierrez
Fecha:       08/01/2018
Descripción: Administra los supervisores
//////////////////////////////////////////////////////////////////////
*/
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import dbConexion.DBManager;

public class cSupervisor {
    private static DBManager oDB;
    private static String sSql  = "";

    //Valida Password Supervisor
    public static String ValidaPass(String iSupervisor){
        String sReturn = "";
        try{
            oDB = DBManager.instance();
            sSql = "select IdSupervisor from catSupervisores where IdSupervisor = '" + iSupervisor + "'  and bActivo = 1";
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            if(oCursor != null  && oCursor.getCount() > 0) {
                oCursor.moveToFirst();
                sReturn = oCursor.getString(0);
            }
        }catch (Exception ex){
            sReturn = "";
        }
        return sReturn;
    }

    public static Cursor Supervisores(){
        Cursor iReturn = null;
        try{
            oDB = DBManager.instance();
            sSql = "Select IdSupervisor, cNombre from catSupervisores where bActivo = 1";
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            if(oCursor.getCount() > 0) {
                iReturn = oCursor;
            }
        }catch (Exception ex){
            iReturn = null;
        }
        return iReturn;
    }

    public static Cursor Editar(String Nombre, String ISupervisor){
        Cursor iReturn = null;
        try{
            oDB = DBManager.instance();
            sSql = "Update catSupervisores Set IdSupervisor=" + ISupervisor +
                    " Where cNombre ='" + Nombre + "'";
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            if(oCursor.getCount() > 0) {
                iReturn = oCursor;
            }
        }catch (Exception ex){
            iReturn = null;
        }
        return iReturn;
    }

    public static int Nuevo(String Nombre, String Password){
        int iReturn = 0;
        try{
            oDB = DBManager.instance();
            ContentValues oCampos = new ContentValues();
            oCampos.put("IdSupervisor",Password);
            oCampos.put("cNombre",Nombre);
            boolean bResult = false;
            SQLiteDatabase oSQLDB = oDB.getoDataBase();
            bResult = oSQLDB.insert("catSupervisores",null,oCampos) > 0;

            iReturn = 1;
        }catch (Exception ex){
            iReturn = -1;
        }
        return iReturn;
    }
}
