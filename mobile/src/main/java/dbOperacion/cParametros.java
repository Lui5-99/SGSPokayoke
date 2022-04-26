package dbOperacion;
/*
//////////////////////////////////////////////////////////////////////
KMF Tecnología - Saint Gobain Sekurit
        Desarrolló:  Joaquín Andrés García Gutierrez
        Fecha:       08/01/2018
        Descripción: Administra catálogo de parametros generales
//////////////////////////////////////////////////////////////////////
        */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.ContextCompat;

import dbConexion.DBManager;


public class cParametros {

    private static DBManager oDB;
    private static String sSql  = "";

    //Prefijo del SAPEnc para etiqueta final de la tarima
    public static String PrefijoSAPEnc(String INumeroParte){
        String sReturn = "";
        try{
            oDB = DBManager.instance();
            sSql = "select cPrefijoSAPEnc from catParametros";
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            if(oCursor.getCount() > 0) {
                oCursor.moveToFirst();
                sReturn = oCursor.getString(0);
            }
        }catch (Exception ex){
            sReturn = "";
        }
        return sReturn;
    }

    //Ultimo archivo exportado
    public static String UtimoArchivo(){
        String sReturn = "";
        try{
            oDB = DBManager.instance();
            sSql = "select cUltimoArchivo from catParametros";
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            if(oCursor.getCount() > 0 && oCursor != null) {
                oCursor.moveToFirst();
                sReturn = oCursor.getString(0);
            }
        }catch (Exception ex){
            sReturn = "";
        }
        return sReturn;
    }

    public static void ActualizarArchivo(String Archivo){
            String sReturn = "";
        try{
            oDB = DBManager.instance();
            sSql = "Update catParametros set cUltimoArchivo = '" + Archivo + "'";
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            if(oCursor.getCount() > 0 && oCursor != null) {
                oCursor.moveToFirst();
                sReturn = oCursor.getString(0);
            }
        }catch (Exception ex){
            sReturn = "";
        }
    }

    //Marca si se escanea al final las charonas de la tarima
    public static boolean bEscaneoCharolas(){
        boolean bReturn = false;
        try{
            oDB = DBManager.instance();
            sSql = "select cPrefijoSAPEnc from catParametros";
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            if(oCursor.getCount() > 0) {
                oCursor.moveToFirst();
                bReturn = oCursor.getInt(0) > 0;
            }
        }catch (Exception ex){
            bReturn = false;
        }
        return bReturn;
    }


}
