package com.kmftecnologia.sgspokayoke;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

import dbOperacion.cGlobales;
import dbOperacion.cLogs;
import dbOperacion.cParametros;
import dbOperacion.cScrap;

import static android.view.View.FOCUS_UP;

public class Fix extends AppCompatActivity {

    TextView oTarima;
    EditText oCodigo;
    TextView oError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix);

        oTarima = findViewById(R.id.lblfixUltimaTarima);
        oError = findViewById(R.id.lblFixError);
        oCodigo = findViewById(R.id.txtFixNP);
        String sTarima = cGlobales.getInstance().getLastNombreTarima();
        oTarima.setText(sTarima);


        oCodigo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(oCodigo.getWindowToken(), 0);
                    ValidaEscaneo();
                    return true;
                }
                return false;
            }
        });
    }

    private void ValidaEscaneo(){
        try{
            String sEscaneo = oCodigo.getText().toString().trim();
            String sNP = cGlobales.getInstance().getLastNP();
            if(sNP.equals(sEscaneo)){
                cGlobales.getInstance().setActivityOrigen(4);
                Intent intent = new Intent(this,Supervisor.class);
                startActivity(intent);
            }
            else
            {
                oError.setText("Pieza incorrecta!");
                oError.setBackgroundResource(R.color.Rojo);
                oCodigo.setText("");
            }

        }catch(Exception ex){
            oError.setText(ex.getMessage());
        }

    }

    public void onClieckRegresar(View v){
        Intent intent = new Intent(this,Escanear.class);
        startActivity(intent);
    }

    public void onClickExportar(View v){
        try{
            if(checkPermisos( Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                String sPath = "/sdcard/Registros/";
                String kmfArchivo = ArmaNombreArchivo();
                String sLinea = "";
                String Fecha = "";
                String Valor = "";

                File oFile = new File(sPath, kmfArchivo);
                FileOutputStream fOut = new FileOutputStream(oFile);
                String sTexto = "Fecha;Tarima;NumeroParte;Usuario;Hora;\n";
                Cursor oCursor = cLogs.ExportarFix();
                if(oCursor != null && oCursor.getCount() > 0){
                    while(oCursor.moveToNext()){
                        Valor = oCursor.getString(0);
                        Fecha = Valor.substring(0,4) +"/";
                        Fecha = Fecha + Valor.substring(4,6);
                        Fecha = Fecha + "/" + Valor.substring(6,8);
                        sLinea = Fecha; //Fecha
                        sLinea = sLinea + ";" + oCursor.getString(1);//Tarima
                        sLinea = sLinea + ";" + oCursor.getString(2);//NumeroParte
                        sLinea = sLinea + ";" + oCursor.getString(3);//Usuario
                        sLinea = sLinea + ";" + oCursor.getString(4);//Hora
                        sLinea = sLinea + ";\n";
                        sTexto = sTexto + sLinea;
                    }
                }

                //Escribo en el archivo
                fOut.write(sTexto.getBytes());
                //Lo cierro
                fOut.close();

                oError.setText("Archivo guardado /sdcard/Registros/" +kmfArchivo);
            }
        }catch(Exception ex){
            oError.setText(ex.getMessage());
        }
    }

    public boolean checkPermisos (String Permiso){
        int check = ContextCompat.checkSelfPermission(this, Permiso);
        return  (check == PackageManager.PERMISSION_GRANTED);

    }

    private String ArmaNombreArchivo(){
        String sID = "FIX";
        Calendar oCalendario = Calendar.getInstance();
        String sValor = "";
        sValor = Integer.toString(oCalendario.get(Calendar.YEAR));
        sID = sID + sValor.substring(2);
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
}
