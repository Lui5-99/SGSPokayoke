package com.kmftecnologia.sgspokayoke;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

import dbOperacion.cParametros;
import dbOperacion.ctTarimas;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ExportFile extends AppCompatActivity {

    TextView lblArchivo;
    TextView oError;
    Button oBoton;
    String sArchivo;
    String sID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_file);

        lblArchivo = findViewById(R.id.lblexpArchivo);
        oError = findViewById(R.id.lblexpResArchivo);
        oBoton = findViewById(R.id.cmdexpExportar);
        getLastFile();

    }

    public void onClickExport(View v){
        try{
            Exportar();
        }catch(Exception ex)
        {
            oError.setText(ex.getMessage());
        }
    }

    private void getLastFile(){
        try{
            sArchivo = cParametros.UtimoArchivo();
            lblArchivo.setText(sArchivo);

        }catch(Exception ex)
        {
            oError.setText(ex.getMessage());
        }
    }

    private String ArmaNombreArchivo(){
        Calendar oCalendario = Calendar.getInstance();
        String sValor = "";
        sValor = Integer.toString(oCalendario.get(Calendar.YEAR));
        sID = sValor.substring(2);
        sValor = Integer.toString(oCalendario.get(Calendar.MONTH) + 1);
        if(sValor.length() == 1)
            sValor = "0" + sValor;
        sID = sID + sValor;
        sValor = Integer.toString(oCalendario.get(Calendar.DAY_OF_MONTH));
        if(sValor.length() == 1)
            sValor = "0" + sValor;
        sID = sID + sValor;
        sValor = Integer.toString(oCalendario.get(Calendar.HOUR_OF_DAY));
        if(sValor.length() == 1)
            sValor = "0" + sValor;
        sID = sID + sValor;
        sValor = Integer.toString(oCalendario.get(Calendar.MINUTE));
        if(sValor.length() == 1)
            sValor = "0" + sValor;
        sID = sID + sValor;
        sID = sID + ".txt";
        return  sID;
    }
    //https://www.youtube.com/watch?v=7CEcevGbIZU
    protected void Exportar(){
        try{
            if(checkPermisos( Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                String sPath = "/sdcard/Registros/";
                String kmfArchivo = ArmaNombreArchivo();
                String sLinea = "";
                String Fecha = "";
                String Valor = "";

                File oFile = new File(sPath, kmfArchivo);
                FileOutputStream fOut = new FileOutputStream(oFile);
                String sTexto = "Fecha;Tarima;Usuario;NumeroParte;Version;Piezas;Charolas;SAPEnc;Status;Fer1;\n";
                Cursor oCursor = ctTarimas.ExportarTarimas();
                if(oCursor != null && oCursor.getCount() > 0){
                    while(oCursor.moveToNext()){
                        Valor = oCursor.getString(0);
                        Fecha = Valor.substring(0,4) +"/";
                        Fecha = Fecha + Valor.substring(4,6);
                        Fecha = Fecha + "/" + Valor.substring(6,8);
                        sLinea = Fecha; //Fecha
                        sLinea = sLinea + ";" + oCursor.getString(1);//Tarima
                        sLinea = sLinea + ";" + oCursor.getString(2);//Usuario
                        sLinea = sLinea + ";" + oCursor.getString(3);//NP
                        sLinea = sLinea + ";" + oCursor.getString(4);//Version
                        sLinea = sLinea + ";" + oCursor.getString(5);//Piezas
                        sLinea = sLinea + ";" + oCursor.getString(6);//Charolas
                        sLinea = sLinea + ";" + oCursor.getString(7);//SAPEnc
                        sLinea = sLinea + ";" + oCursor.getString(8);//Status
                        sLinea = sLinea + ";" + oCursor.getString(9);//Status
                        sLinea = sLinea + ";\n";
                        sTexto = sTexto + sLinea;
                    }
                }

                //Escribo en el archivo
                fOut.write(sTexto.getBytes());
                //Lo cierro
                fOut.close();


                oError.setText("Archivo guardado /sdcard/Registros/");
                cParametros.ActualizarArchivo(kmfArchivo);
                lblArchivo.setText(kmfArchivo);
            }

        }catch(Exception ex){
            oError.setText(ex.getMessage());
        }
    }
    public boolean checkPermisos (String Permiso){
        int check = ContextCompat.checkSelfPermission(this, Permiso);
        return  (check == PackageManager.PERMISSION_GRANTED);

    }

}
