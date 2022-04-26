package com.kmftecnologia.sgspokayoke;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import Impresion.cPrinter;
import dbOperacion.cGlobales;
import dbOperacion.cLabel;
import dbOperacion.ctTarimas;

public class GeneraEtiqueta extends AppCompatActivity {

    EditText oCharola;
    EditText oCantidad;
    TextView oError;
    cPrinter oPrinter;
    String sUsuario;
    String sCharola;
    String sCantidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genera_etiqueta);
        oCharola = findViewById(R.id.txtTarimaRePrint);
        oCantidad = findViewById(R.id.txtConsecutivoRePrint);
        oError = findViewById(R.id.lblErrorRePrint);
        oCharola.setText("");
        oCantidad.setText("");
        sUsuario = cGlobales.getInstance().getUsuario();
        //Rutina de Impresión
        try {
            boolean bResult = false;
            //Rutina de Impresión
            oPrinter = new cPrinter();
            oPrinter.findBT();
            String sName = "";
            if (oPrinter.mmDevice != null && oPrinter.mmDevice.getName().length() > 0)
                sName = oPrinter.mmDevice.getName();
            if (sName.length() > 0) {

                try {
                    bResult = false;
                    int i = 1;
                    while(bResult == false && i < 6) {
                        bResult = oPrinter.connectToPrinter(sName);
                        i++;
                    }
                    if(bResult == false) {
                        oError.setText("Revisar Conexión IMPRESORA");
                        oError.setBackgroundResource(R.color.Rojo);
                    }
                }catch (IOException ex){
                    try{
                        oPrinter.closeBT();}
                    catch (IOException ex2){
                        ex2.printStackTrace();
                        oError.setText("Revisar Conexión IMPRESORA");
                        oError.setBackgroundResource(R.color.Rojo);
                    }
                    try {
                        bResult = oPrinter.connectToPrinter(sName);
                    }
                    catch (IOException ex3){
                        ex3.printStackTrace();
                        oError.setText("Revisar Conexión IMPRESORA");
                        oError.setBackgroundResource(R.color.Rojo);
                    }
                    ex.printStackTrace();
                }
            } else {
                oError.setText("No encontró IMPRESORA");
                oError.setBackgroundResource(R.color.Rojo);
            }
        }catch(Exception ex){
            oError.setText("Revisar Conexión IMPRESORA");
            oError.setBackgroundResource(R.color.Rojo);
        }
    }

    public void OnClickReimprimir(View v){
        try{
            String sTarima;
            String sLabel;
            String NumeroParte;
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();

            sCharola = oCharola.getText().toString().trim();
            sCantidad = oCantidad.getText().toString().trim();
            String aCharola[] = sCharola.split(":");
            NumeroParte = aCharola[0].toString();
            sTarima =  aCharola[2].toString();

            //Imprimó Etiqueta Charola antes de incrementar itCharolas
            sLabel = "^XA" +
                    "^FO170,240,^AO,20,20^FD" + NumeroParte + " - " + sCantidad + " - REIMPRESA^FS" + //IdTarima
                    "^FO170,270,^AO,20,20^FD " + sUsuario + "^FS" + //Usuario
                    "^FO200,20^BY3^BQN,2,8^FDMM, " + NumeroParte + ":" + sCantidad + ":" + sTarima + "^FS^XZ";
            cGlobales.getInstance().setLastPrint(sLabel);
            Boolean bGuardar = cLabel.setLastLabel(sLabel);
            int iResult = oPrinter.sendData(sLabel);
            if (iResult == 1) {
                date = new Date();
                oError.setText("Impresión culminada..." + date.toString());
                oCharola.setText("");
                oCantidad.setText("");
            } else {
                oError.setText("Revisar Conexión IMPRESORA");
                oError.setBackgroundResource(R.color.Azul);
                oCharola.setText("");
                oCantidad.setText("");
            }

        }catch(Exception ex){
            oError.setText(ex.getMessage());
            oError.setBackgroundResource(R.color.Rojo);
        }
    }
    public void OnClickMenu(View v){
        Intent intent = new Intent(this,Principal.class);
        startActivity(intent);
    }
}
