package dbConexion;
/*
//////////////////////////////////////////////////////////////////////
KMF Tecnología - Saint Gobain Sekurit
Desarrolló:  Joaquín Andrés García Gutierrez
Fecha:       26/12/2018
Descripción: Clase para administrar la base de datos

//////////////////////////////////////////////////////////////////////
*/

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kmftecnologia.sgspokayoke.Principal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBManager extends SQLiteOpenHelper  {

    //public static  String DB_PATH = "/data/data/com.kmftecnologia.sgspokayoke/databases/";
    //public static  String DB_PATH = "/storage/emulated/0/kmf/";
    public static  String DB_PATH = "/sdcard/kmf/";
    //public static  String DB_PATH = "/storage/kmf/";
    public static final int VERSION = 2;
    public static final String DB_NAME = "kmfPokayoke.db";
    private String sSQL = "";

    private static SQLiteDatabase oDataBase;
    private static DBManager oInstance = null;

    public DBManager() {
       super(Principal.activity, DB_NAME, null, VERSION);
       try{
           createDataBase();
           openDatabase();
       }catch(Exception ex){
           ex.printStackTrace();
        }
    }

    public SQLiteDatabase getoDataBase(){return  oDataBase;}
    //Valida que exista la base de datos en el dispositivo, sino la copia.
    //https://www.youtube.com/watch?v=RrxveIpRro8
    private void createDataBase() throws IOException{
        boolean bExist = checkDatabase();
        SQLiteDatabase db_Read = null;

        if(bExist){
            //La base de datos ya existe
        } else {
            db_Read = this.getReadableDatabase();
            db_Read.close();
            try{
                copyDataBase();
            }
            catch (Exception ex){
                throw new Error("Error copiado base de datos");
            }
        }
    }


    //Instancia de la calse DBManager
    public  static  DBManager instance(){
        if(oInstance == null){
            oInstance = new DBManager();
        }
        return  oInstance;
    }


    //Valida la existencia de la base de datos
    public boolean checkDatabase(){
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    //Abre la base de datos
    private void openDatabase() throws SQLException {

        String sPath = DB_PATH + DB_NAME;
        oDataBase = SQLiteDatabase.openDatabase(sPath,null,SQLiteDatabase.OPEN_READWRITE);
    }


    //Copia la base de datos  al directorio en el dispositivo
    public void copyDataBase() throws IOException{

        InputStream oInput = Principal.activity.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream oOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int iLength;
        while ((iLength = oInput.read(buffer)) > 0){
            oOutput.write(buffer,0,iLength);
        }
        oOutput.flush();
        oOutput.close();
        oInput.close();
    }

    //Cursor con el que se ejecuta sentencias SQL
    public Cursor ExecuteSQL(String Query) throws  SQLException{
        return oDataBase.rawQuery(Query,null);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
