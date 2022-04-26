package dbConexion;
/*
//////////////////////////////////////////////////////////////////////
KMF Tecnología - Saint Gobain Sekurit
Desarrolló:  Joaquín Andrés García Gutierrez
Fecha:       26/12/2018
Descripción: Clase para depurar la base de datos

//////////////////////////////////////////////////////////////////////
*/
import android.database.Cursor;
import dbConexion.DBManager;

public class dbDepuracion {

    private static DBManager oDB;
    private static String sSql  = "";
    //Depura la base de datos

    public static boolean DepuraDB(){
        boolean bReturn = false;
        try{
            oDB = DBManager.instance();
            sSql = "Delete from Tarimas where IdStatus in(0,3)";
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            bReturn = true;
        }catch (Exception ex){
            bReturn = false;
        }
        return bReturn;
    }
}
