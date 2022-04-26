package com.kmftecnologia.sgspokayoke;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;



import org.w3c.dom.Text;

import dbConexion.DBManager;
import dbOperacion.cGlobales;

public class Principal extends AppCompatActivity {
    public static Activity activity;
    DBManager oDB;
    private String sSql  = "";
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        TextView oVersion = (TextView) findViewById(R.id.lblVersion);
        activity = this;
        verifyStoragePermissions(activity);
        try {
            oDB = DBManager.instance();
            sSql = "Select Idversion, cNombre from catSistema";
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            if (oCursor.getCount() > 0) {
                oCursor.moveToFirst();
                oVersion.setText("Versión: 08.20"); //" + oCursor.getString(0));
            }
        }catch(Exception ex) {
            TextView oError = (TextView) findViewById(R.id.lblError);
            oError.setVisibility(View.VISIBLE);
            oError.setText(ex.getMessage());
        }
    }

    public void OnClick(View v){
        try{
            Intent intent = new Intent(this,NewPalletActivity.class);
            startActivity(intent);
        }catch(Exception ex){
            TextView oError = (TextView) findViewById(R.id.lblError);
            oError.setVisibility(View.VISIBLE);
            oError.setText(ex.getMessage());
        }
    }

    public void OnClickEscan(View v){
        try{
            Intent intent = new Intent(this,Escanear.class);
            startActivity(intent);
        }catch(Exception ex){
            TextView oError = (TextView) findViewById(R.id.lblError);
            oError.setVisibility(View.VISIBLE);
            oError.setText(ex.getMessage());
        }
    }

    public void OnClickEmbarcar(View v){
        try{
            cGlobales.getInstance().setEmbarque(1);
            Intent intent = new Intent(this,TarimaParcial.class);
            startActivity(intent);
        }catch(Exception ex){
            TextView oError = (TextView) findViewById(R.id.lblError);
            oError.setVisibility(View.VISIBLE);
            oError.setText(ex.getMessage());
        }
    }

    public void OnClickConfig(View v){
        try{
            cGlobales.getInstance().setEmbarque(1);
            Intent intent = new Intent(this,Configuracion.class);
            startActivity(intent);
        }catch(Exception ex){
            TextView oError = (TextView) findViewById(R.id.lblError);
            oError.setVisibility(View.VISIBLE);
            oError.setText(ex.getMessage());
        }
    }


    public void OnClickScrap(View v){
        try{
            cGlobales.getInstance().setEmbarque(1);
            Intent intent = new Intent(this, Scrap.class);
            startActivity(intent);
        }catch(Exception ex){
            TextView oError = (TextView) findViewById(R.id.lblError);
            oError.setVisibility(View.VISIBLE);
            oError.setText(ex.getMessage());
        }
    }

    public void OnClickFlexnet(View v){
        try{
            cGlobales.getInstance().setEmbarque(1);
            Intent intent = new Intent(this, FlexNet.class);
            startActivity(intent);
        }catch(Exception ex){
            TextView oError = (TextView) findViewById(R.id.lblError);
            oError.setVisibility(View.VISIBLE);
            oError.setText(ex.getMessage());
        }
    }

    public void OnClickExportar(View v){
        try{
            cGlobales.getInstance().setEmbarque(1);
            Intent intent = new Intent(this,ExportFile.class);
            startActivity(intent);
        }catch(Exception ex){
            TextView oError = (TextView) findViewById(R.id.lblError);
            oError.setVisibility(View.VISIBLE);
            oError.setText(ex.getMessage());
        }
    }

    public void OnClickCloseTarima(View v){
        try{
            cGlobales.getInstance().setEmbarque(1);
            Intent intent = new Intent(this,CierreTarima.class);
            startActivity(intent);
        }catch(Exception ex){
            TextView oError = (TextView) findViewById(R.id.lblError);
            oError.setVisibility(View.VISIBLE);
            oError.setText(ex.getMessage());
        }
    }

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
