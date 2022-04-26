package dbOperacion;

/*
//////////////////////////////////////////////////////////////////////
KMF Tecnología - Saint Gobain Sekurit
        Desarrolló:  Joaquín Andrés García Gutierrez
        Fecha:       08/01/2018
        Descripción: Administra el scarp
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

public class cScrap {

    private static DBManager oDB;
    private static String sSql  = "";

    //Inserta un nuevo campo
    public static boolean Registrar(String INumeroParte, String Usuario, String IDefecto){
        boolean bReturn = false;
        try{

            String sFecha = DevuelveFecha(0);
            ContentValues oCampos = new ContentValues();
            oCampos.put("iFecha",Integer.parseInt(sFecha));
            oCampos.put("IdNumeroParte",INumeroParte);
            oCampos.put("Usuario",Usuario);
            oCampos.put("Defectos",IDefecto);

            boolean bResult = false;
            SQLiteDatabase oSQLDB = oDB.getoDataBase();
            //Long iResut = oSQLDB.insert("Scrap",null,oCampos);
            bReturn = oSQLDB.insert("Scrap",null,oCampos) > 0;

            //bReturn = true;

        }catch (Exception ex){
            bReturn = false;
        }
        return  bReturn;
    }

    //Exporta Scrap
    public static Cursor ExportarScrap(){
        Cursor iReturn = null;
        String sFechaIni = DevuelveFecha(-2);
        String sFecha = DevuelveFecha(0);
        try{
            oDB = DBManager.instance();
            sSql = "Select iFecha, IdNumeroParte, Usuario, Defectos " +
                    "from Scrap where iFecha between " + sFechaIni + " and " + sFecha ;
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            if(oCursor.getCount() > 0 && oCursor != null) {
                iReturn = oCursor;
            }
        }catch (Exception ex){
            iReturn = null;
        }
        return iReturn;
    }

    public static Cursor Defectos(){
        Cursor iReturn = null;
        String sFechaIni = DevuelveFecha(-2);
        String sFecha = DevuelveFecha(0);
        try{
            oDB = DBManager.instance();
            sSql = "Select IdDefecto, cDefecto from catDefectos Where bActivo = 1 ";
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            if(oCursor.getCount() > 0 && oCursor != null) {
                iReturn = oCursor;
            }
        }catch (Exception ex){
            iReturn = null;
        }
        return iReturn;
    }

    //Devuelve la fecha como
    public static String DevuelveFecha(int iMenosDias){
        String sID;
        Calendar oCalendario = Calendar.getInstance();
        String sValor = "";
        sID = Integer.toString(oCalendario.get(Calendar.YEAR));

        sValor = Integer.toString(oCalendario.get(Calendar.MONTH) + 1);
        if(sValor.length() == 1)
            sValor = "0" + sValor;
        sID = sID + sValor;
        if(iMenosDias < 0)
        {
            oCalendario.add(Calendar.DAY_OF_MONTH, iMenosDias);
        }
        sValor = Integer.toString(oCalendario.get(Calendar.DAY_OF_MONTH));
        if(sValor.length() == 1)
            sValor = "0" + sValor;
        sID = sID + sValor;
        return  sID;
    }
}
