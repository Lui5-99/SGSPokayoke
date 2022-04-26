package dbOperacion;
/*
//////////////////////////////////////////////////////////////////////
KMF Tecnología - Saint Gobain Sekurit
Desarrolló:  Joaquín Andrés García Gutierrez
Fecha:       08/01/2018
Descripción: Administra catálogo de Numero de Partes
//////////////////////////////////////////////////////////////////////
*/

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import dbConexion.DBManager;

public class ctNumerosParte {
    private static DBManager oDB;
    private static String sSql  = "";

    //Valida si el Numero de Parte existe en el catálogo
    public static int CheckNumParte(String INumeroParte){
        int iReturn = 0;
        try{
            oDB = DBManager.instance();
            sSql = "select IdNumeroParte from catNumerosParte where idNumeroParte = '" + INumeroParte + "'";
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            if(oCursor.getCount() > 0) {
                oCursor.moveToFirst();
                iReturn = oCursor.getInt(0);
            }
        }catch (Exception ex){
            iReturn = -1;
        }
        return iReturn;
    }

    //Devuelve la información del Número de Parte
    public static Cursor NumeroParte(String INumeroParte){
        Cursor iReturn = null;
        try{
            oDB = DBManager.instance();
            sSql = "select IdNumeroParte, iCamas, iCharolasCama, iPiezasCharola, iTotalPiezas, cVersion, cSAP, bActivo ,cSAPEnc " +
                    "from catNumerosparte " +
                    "where idNumeroParte = '" + INumeroParte + "'";
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            if(oCursor.getCount() > 0 && oCursor != null) {
                iReturn = oCursor;
            }
        }catch (Exception ex){
            iReturn = null;
        }
        return iReturn;
    }

    public static String NumeroParteSAPENC(String INumeroParte){
        String sReturn = "";
        try{
            oDB = DBManager.instance();
            sSql = "select cSAPEnc from catNumerosParte where idNumeroParte = '" + INumeroParte + "'";
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

    public static String NumeroParteSAP(String INumeroParte){
        String sReturn = "";
        try{
            oDB = DBManager.instance();
            sSql = "select cSAP from catNumerosParte where idNumeroParte = '" + INumeroParte + "'";
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

    //Devuelve Numeros de parte
    public static Cursor NumerosdeParte(){
        Cursor iReturn = null;
        try{
            oDB = DBManager.instance();
            sSql = "select IdNumeroParte, iCharolasCama, iPiezasCharola, iTotalPiezas, cVersion, cSAP, bActivo, cSAPEnc, iCamas " +
                    "from catNumerosparte order by IdNumeroParte desc";
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            if(oCursor.getCount() > 0) {
                iReturn = oCursor;
            }
        }catch (Exception ex){
            iReturn = null;
        }
        return iReturn;
    }

    //Editar un numero de parte
    public static Cursor Editar(String IdNP, String Version, String SAP, int PiezasCharola, int Piezas, int Activo, String SAPEnc,int iCamas ){
        Cursor iReturn = null;
        try{
            oDB = DBManager.instance();

            sSql = "Update catNumerosparte Set iPiezasCharola=" + PiezasCharola +
                    ", iTotalPiezas =" + Piezas + ", cVersion='" + Version + "', cSAP='" + SAP + "', cSAPEnc='" + SAPEnc +
                    "', bActivo =" + Activo + ", iCamas =" + iCamas +
                    " Where IdNumeroParte ='" + IdNP + "'";
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            if(oCursor.getCount() > 0) {
                iReturn = oCursor;
            }
        }catch (Exception ex){
            iReturn = null;
        }
        return iReturn;
    }

    public static int Nuevo(String IdNP, String Version, String SAP, int PiezasCharola, int Piezas, String SAPEnc, int iCamas){
        int iReturn = 0;
        try{
            oDB = DBManager.instance();
            ContentValues oCampos = new ContentValues();
            oCampos.put("IdNumeroParte",IdNP);
            oCampos.put("iCamas",iCamas);
            oCampos.put("iCharolasCama",1);
            oCampos.put("iPiezasCharola",PiezasCharola);
            oCampos.put("iTotalPiezas",Piezas);
            oCampos.put("bActivo",1);
            oCampos.put("cVersion",Version);
            oCampos.put("cSAP",SAP);
            oCampos.put("cSAPEnc",SAPEnc);
            boolean bResult = false;
            SQLiteDatabase oSQLDB = oDB.getoDataBase();
            bResult = oSQLDB.insert("catNumerosparte",null,oCampos) > 0;

            iReturn = 1;
        }catch (Exception ex){
            iReturn = -1;
        }
        return iReturn;
    }

}
