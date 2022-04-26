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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dbConexion.DBManager;
public class cLogs {

    private static DBManager oDB;
    private static String sSql  = "";

    public static int InsertaPiezaReEscaneada(String NombreTarima, String Usuario, String INumeroParte){
        int iReturn = 0;
        try{
            String sFecha = ctTarimas.DevuelveFecha(0);
            String sHora = DevuelveHora();
            oDB = DBManager.instance();
            sSql = "Select Max(IdTarima) from Tarimas ";
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            int iID = 0;
            if(oCursor.getCount() > 0 && oCursor != null) {
                oCursor.moveToFirst();
                iID = oCursor.getInt(0);
            }
            iID++;


            ContentValues oCampos = new ContentValues();
            oCampos.put("Fecha",Integer.parseInt(sFecha));
            oCampos.put("Tarima",NombreTarima);
            oCampos.put("NumeroParte",INumeroParte);
            oCampos.put("Usuario",Usuario);
            oCampos.put("bExportado",0);
            oCampos.put("Hora",sHora);

            boolean bResult = false;
            SQLiteDatabase oSQLDB = oDB.getoDataBase();
            bResult = oSQLDB.insert("logPiezasReEscaneadas",null,oCampos) > 0;
            //oSQLDB.close();
            iReturn = iID;
        }catch (Exception ex){
            iReturn = -1;
        }
        return iReturn;
    }

    public static String DevuelveHora(){
        Calendar oCalendario = Calendar.getInstance();
        String sValor, sID;
        sValor = "";
        sID = "";
        sValor = Integer.toString(oCalendario.get(Calendar.HOUR_OF_DAY));
        if(sValor.length() == 1)
            sValor = "0" + sValor;
        sID = sID + sValor;
        sValor = Integer.toString(oCalendario.get(Calendar.MINUTE));
        if(sValor.length() == 1)
            sValor = "0" + sValor;
        sID = sID + ":" + sValor;
        return  sID;
    }

    public static Cursor ExportarFix(){
        Cursor iReturn = null;
        String sFechaIni = DevuelveFecha(-2);
        String sFecha = DevuelveFecha(0);
        try{
            oDB = DBManager.instance();
            sSql = "Select Fecha, Tarima, NumeroParte, Usuario, Hora " +
                    "from logPiezasReEscaneadas where Fecha between " + sFechaIni + " and " + sFecha ;
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            if(oCursor.getCount() > 0 && oCursor != null) {
                iReturn = oCursor;
            }
        }catch (Exception ex){
            iReturn = null;
        }
        return iReturn;
    }

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
