package dbConexion;/*
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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import dbConexion.NumerosParteContract;
import dbConexion.SupervisorContract;
import dbConexion.cTarimas;

public class MyDBHelped extends SQLiteOpenHelper {
    public static final int version = 1;
    public static final String name = "kmfpokayoke.db";
    private String sSQL = "";

    public MyDBHelped(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
/*
        //Creamos la tabla de catálogo de Numeros de Parte
        sSQL = "CREATE TABLE " + NumerosParteContract.NumeroParteEntry.TABLE_NAME + " (" +
                NumerosParteContract.NumeroParteEntry.COLUMN_NumeroParte + " TEXT PRIMARY KEY, " +
                NumerosParteContract.NumeroParteEntry.COLUMN_Nombre + " TEXT, " +
                NumerosParteContract.NumeroParteEntry.COLUMN_Camas + " INTEGER, " +
                NumerosParteContract.NumeroParteEntry.COLUMN_CharolasCama + " INTEGER, " +
                NumerosParteContract.NumeroParteEntry.COLUMN_PiezasCharolas + " INTEGER)";
        db.execSQL(sSQL);

        //Creamos el catálogo Supervisores
        sSQL = "CREATE TABLE " + SupervisorContract.SupervisorEntry.TABLE_NAME + " (" +
                SupervisorContract.SupervisorEntry.COLUMN_ID + " TEXT PRIMARY KEY, " +
                SupervisorContract.SupervisorEntry.COLUMN_Password + " TEXT)";
        db.execSQL(sSQL);

        //Creamos la tabla Tarimas
        sSQL = "CREATE TABLE " + TarimaContract.TarimaEntry.TABLE_NAME + " (" +
                TarimaContract.TarimaEntry.COLUMN_ID + " TEXT PRIMARY KEY, " +
                TarimaContract.TarimaEntry.COLUMN_NumeroParte + " TEXT, " +
                TarimaContract.TarimaEntry.COLUMN_Usuario + " TEXT, " +
                TarimaContract.TarimaEntry.COLUMN_Camas + " INTEGER, " +
                TarimaContract.TarimaEntry.COLUMN_CharolasCama + " INTEGER, " +
                TarimaContract.TarimaEntry.COLUMN_PiezasCharolas + " INTEGER, " +
                TarimaContract.TarimaEntry.COLUMN_Status + " INTEGER)";

        db.execSQL(sSQL);
        //Cerramos conexión
        db.close();
        */
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       /* db.execSQL("DROP TABLE IF EXISTS " + NumerosParteContract.NumeroParteEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SupervisorContract.SupervisorEntry.TABLE_NAME);
        //db.execSQL("DROP TABLE IF EXISTS " + TarimaContract.TarimaEntry.TABLE_NAME);
        onCreate(db);*/
    }
/*
    public long NuevaTarima(cTarimas oTarima){
        long lReturn = 0;
        try{
            //sSQL = "Insert into " + TarimaContract.TarimaEntry.TABLE_NAME + " (" +
            //        TarimaContract.TarimaEntry.COLUMN_ID + ", " +
            //        TarimaContract.TarimaEntry.COLUMN_Usuario + ", " +
            //        TarimaContract.TarimaEntry.COLUMN_NumeroParte + ", " +
            //        TarimaContract.TarimaEntry.COLUMN_Camas + ", " +
            //        TarimaContract.TarimaEntry.COLUMN_CharolasCama + ", " +
            //        TarimaContract.TarimaEntry.COLUMN_PiezasCharolas + ", " +
            //        TarimaContract.TarimaEntry.COLUMN_Status + ") Values('" +
            //        oTarima.getIdTarima() + "','" + oTarima.getUsuario() + "','" +
            //        oTarima.getNumeroParte() + "',0,0,1,0)";

            ContentValues valores = new ContentValues();
            SQLiteDatabase db = getWritableDatabase();
            valores.put(TarimaContract.TarimaEntry.COLUMN_ID,oTarima.getIdTarima());
            valores.put(TarimaContract.TarimaEntry.COLUMN_Usuario, oTarima.getUsuario());
            valores.put(TarimaContract.TarimaEntry.COLUMN_NumeroParte, oTarima.getNumeroParte());
            valores.put(TarimaContract.TarimaEntry.COLUMN_Camas,0);
            valores.put(TarimaContract.TarimaEntry.COLUMN_CharolasCama,0);
            valores.put(TarimaContract.TarimaEntry.COLUMN_PiezasCharolas,1);
            valores.put(TarimaContract.TarimaEntry.COLUMN_Status,0);

            lReturn = db.insert(TarimaContract.TarimaEntry.TABLE_NAME,null,valores);

        }catch (Exception ex){
            lReturn = -1;
        }
        return  lReturn;
    }

    public String ExisteTarima(String sNumeroParte){
        String sResult = "";
        try {
            sResult = "";
            SQLiteDatabase db = getReadableDatabase();
            //Resultset, solo ocupo el Id de la tarima
            String[] rsResult = {
                    TarimaContract.TarimaEntry.COLUMN_ID
            };
            String sWhere = TarimaContract.TarimaEntry.COLUMN_NumeroParte + " = ?";
            String[] sWhereArgumento = {sNumeroParte};
            Cursor cursor = db.query(TarimaContract.TarimaEntry.TABLE_NAME,
                    rsResult, sWhere,sWhereArgumento,null,null,null);
            List ItemsResult = new ArrayList<>();
            int iRows = cursor.getCount();
            if(iRows > 0){
                cursor.moveToFirst();
                sResult = cursor.getString(0);
            }
        }catch (Exception ex){
            sResult = "0";
        }
        return sResult;
    }
    */
}
