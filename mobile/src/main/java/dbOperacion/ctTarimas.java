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

public class ctTarimas {
    private static DBManager oDB;
    private static String sSql  = "";



    //Valida un numero parte en una tarima activa
    public static int ValidaTarima(String INumeroParte){
        int iReturn = 0;
        try{
            oDB = DBManager.instance();
            sSql = "select IdTarima from Tarimas where idNumeroParte = '" + INumeroParte + "'  and IdStatus in (1,2)";
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            if(oCursor.getCount() > 0 && oCursor != null) {
                oCursor.moveToFirst();
                iReturn = oCursor.getInt(0);
            }
        }catch (Exception ex){
            iReturn = -1;
        }
        return iReturn;
    }


    //Inserta una nueva Tarima
    public static int InsertaNuevaTarima(String Nombre, String Usuario, String INumeroParte,boolean bEmpAlt){
        int iReturn = 0;
        try{
            oDB = DBManager.instance();
            sSql = "Select Max(IdTarima) from Tarimas ";
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            int iID = 0;
            if(oCursor.getCount() > 0 && oCursor != null) {
                oCursor.moveToFirst();
                iID = oCursor.getInt(0);
            }
            iID++;
            /*a Ir@puat0
            sSql = "Insert into Tarimas(IdTarima, cNombre, cUsuario, IdNumeroParte, iCamas, iCharolas, iPiezas, iTotalPiezas,IdStatus)" +
                    " Values(" + iID +",'" + Nombre +  "','" + Usuario + "','" + INumeroParte + "',1,1,1,1,1)";
            Cursor oInsert = oDB.ExecuteSQL(sSql);*/
            String sFecha = DevuelveFecha(0);
            ContentValues oCampos = new ContentValues();
            oCampos.put("IdTarima",iID);
            oCampos.put("cNombre",Nombre);
            oCampos.put("cUsuario",Usuario);
            oCampos.put("IdNumeroParte",INumeroParte);
            oCampos.put("iCamas",1);
            oCampos.put("iCharolas",1);
            oCampos.put("iPiezas",1);
            oCampos.put("iTotalPiezas",1);
            oCampos.put("IdStatus",1);
            oCampos.put("bExportado",0);
            if(bEmpAlt)
                oCampos.put("bEmpAlt",1);
            else
                oCampos.put("bEmpAlt",0);
            oCampos.put("iFecha",Integer.parseInt(sFecha));
            boolean bResult = false;
            SQLiteDatabase oSQLDB = oDB.getoDataBase();
            bResult = oSQLDB.insert("Tarimas",null,oCampos) > 0;
            //oSQLDB.close();
            iReturn = iID;
        }catch (Exception ex){
            iReturn = -1;
        }
        return iReturn;
    }

    //Devuelve los datos de una tarima
    public static Cursor Tarima(int IdTarima){
        Cursor iReturn = null;
        try{
            oDB = DBManager.instance();
            sSql = "Select IdTarima, cNombre, cUsuario, IdNumeroParte, iCamas, iCharolas, iPiezas, iTotalPiezas, IdStatus, bEmpAlt " +
                    "From Tarimas Where IdTarima = " + IdTarima;
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
    public static Cursor ExportarTarimas(){
        Cursor iReturn = null;
        String sFechaIni = DevuelveFecha(-2);
        String sFecha = DevuelveFecha(0);
         try{
            oDB = DBManager.instance();
            sSql = "select T.iFecha, T.cNombre, T.cUsuario, NP.IdNumeroParte, NP.cVersion , T.iTotalPiezas, T.iCharolas, NP.cSAPEnc, S.cStatus, cFer1 "
                    + " from Tarimas T inner join catNumerosParte NP on T.IdNumeroParte = NP.IdNumeroParte "
                    + " inner join catStatus S on T.IdStatus = S.IdStatus "
                    + " where T.iFecha between " + sFechaIni + " and " + sFecha ;
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            if(oCursor.getCount() > 0 && oCursor != null) {
                iReturn = oCursor;
            }
        }catch (Exception ex){
            iReturn = null;
        }
        return iReturn;
    }

    //Devuelve los datos de las tarimas activas
    public static Cursor TarimasActivas(){
        Cursor iReturn = null;
        try{
            oDB = DBManager.instance();
            sSql = "Select IdTarima, cNombre From Tarimas Where IdStatus = 1";
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            if(oCursor.getCount() > 0 && oCursor != null) {
                iReturn = oCursor;
            }
        }catch (Exception ex){
            iReturn = null;
        }
        return iReturn;
    }

    //Devuelve los datos de las tarimas parciales
    public static Cursor TarimasParciales(){
        Cursor iReturn = null;
        try{
            oDB = DBManager.instance();
            sSql = "Select IdTarima, cNombre From Tarimas Where IdStatus = 2";
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            if(oCursor.getCount() > 0 && oCursor != null) {
                iReturn = oCursor;
            }
        }catch (Exception ex){
            iReturn = null;
        }
        return iReturn;
    }

    //Devuelve los datos de tarimas activas y parciales
    public static Cursor TarimasActivasParciales(){
        Cursor iReturn = null;
        try{
            oDB = DBManager.instance();
            sSql = "Select IdTarima, cNombre, iCharolas, iTotalPiezas, IdStatus From Tarimas Where IdStatus in (1,2)";
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            if(oCursor.getCount() > 0 && oCursor != null) {
                iReturn = oCursor;
            }
        }catch (Exception ex){
            iReturn = null;
        }
        return iReturn;
    }

    //Tarimas pendientes de cerrar
    public static Cursor TarimasxCerrar(){
        Cursor iReturn = null;
        try{
            oDB = DBManager.instance();
            sSql = "Select IdTarima, cNombre, IdNumeroParte, iCharolas, iTotalPiezas, IdStatus From Tarimas Where IdStatus =4";
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            if(oCursor.getCount() > 0 && oCursor != null) {
                iReturn = oCursor;
            }
        }catch (Exception ex){
            iReturn = null;
        }
        return iReturn;
    }



    //Devuelve los datos de una tarima buscando por nombre
    public static Cursor TarimasxNombre(String Nombre){
        Cursor iReturn = null;
        try{
            oDB = DBManager.instance();
            sSql = "Select IdTarima, cNombre, iCharolas, iPiezas, IdNumeroParte,ITotalPiezas From Tarimas Where cNombre = '" + Nombre + "'";
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            if(oCursor.getCount() > 0 && oCursor != null) {
                iReturn = oCursor;
            }
        }catch (Exception ex){
            iReturn = null;
        }
        return iReturn;
    }
    //Edita conteo de piezas de una tarima
    public static int EditaConteo(int IdTarima, int Camas, int Charolas, int Piezas, int TotalPiezas){
        int iReturn = 0;
        try{
            oDB = DBManager.instance();
            sSql = "Update Tarimas set iCamas= " + Camas + ", iCharolas= " + Charolas + ", iPiezas= " + Piezas + ", iTotalPiezas = " + TotalPiezas + " Where IdTarima = " + IdTarima;
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            int iID = 0;
            if(oCursor.getCount() > 0 && oCursor != null) {
                iReturn = 1;
            }

        }catch (Exception ex){
            iReturn = -1;
        }
        return iReturn;
    }

    public static int EditaFer(int IdTarima, String Fer){
        int iReturn = 0;
        try{
            oDB = DBManager.instance();
            sSql = "Update Tarimas set cFer1= '" + Fer + "' Where IdTarima = " + IdTarima;
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            int iID = 0;
            if(oCursor.getCount() > 0 && oCursor != null) {
                iReturn = 1;
            }

        }catch (Exception ex){
            iReturn = -1;
        }
        return iReturn;
    }

    //Edita status de una tarima
    public static int EditaStatus(int IdTarima, int IStatus){
        int iReturn = 0;
        try{
            oDB = DBManager.instance();
            sSql = "Update Tarimas set IdStatus = " + IStatus + " Where IdTarima = " + IdTarima;
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            if(oCursor.getCount() > 0 && oCursor != null) {
                iReturn = 1;
            }
        }catch (Exception ex){
            iReturn = -1;
        }
        return iReturn;
    }
}
