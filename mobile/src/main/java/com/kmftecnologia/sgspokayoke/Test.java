package com.kmftecnologia.sgspokayoke;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import Impresion.cPrinter;
import dbOperacion.cGlobales;
import dbOperacion.cLabel;

public class Test extends AppCompatActivity {

    cPrinter oPrinter;
    TextView oText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean bResult = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        oText = (TextView) findViewById(R.id.txtTestPrintMessage);
        oPrinter = new cPrinter();
        oPrinter.findBT();
        String sName = oPrinter.mmDevice.getName();
        try {
            bResult = false;
            int i = 1;
            while(bResult == false) {
                bResult = oPrinter.connectToPrinter(sName);
                i++;
            }
        }catch (IOException ex){
            try{
            oPrinter.closeBT();}
            catch (IOException ex2){
                ex2.printStackTrace();
                oText.setText(ex2.getMessage());
            }
            try {
                bResult = oPrinter.connectToPrinter(sName);
            }
            catch (IOException ex3){
                ex3.printStackTrace();
                oText.setText(ex3.getMessage());
            }
            ex.printStackTrace();
            oText.setText(ex.getMessage());
        }

    }

    public void prinEtiquetas(View v) {
        oText = (TextView) findViewById(R.id.txtTestPrintMessage);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        oText.setText("Iniciando Impresión..." + date.toString());
        try {
            //Busco la impresora conectada Bluetooth.

            /*String sLabel = "^XA" + //Inicio
                    "^FO210,250,^AO,20,20^FD74441181901151133^FS" + //IdTarima
                    "^FO300,10^BY3^BQN,2,10^FDMM, 7444118:1^FS^XZ"; //Código QR
            //String sLabel = "^XA^FO380,10^BY3^BQN,2,5^FDMM, " + "1234567890" + "^FS^XZ";
            //oPrinter.sendData("^XA^FO230,180,^AO,30,20^FDFDTesting^FS^FO200,30^BY3^BCN,50,Y,N,N^FD1100000003^FS^XZ");
            oPrinter.sendData(sLabel);
            TimeUnit.SECONDS.sleep(10);
            sLabel = "^XA" +
                    "^FO250,250,^AO,20,20^FD7444118 - 1^FS" + //IdTarima
                    "^FO300,10^BY3^BQN,2,10^FDMM, 7444118:1^FS^XZ";*/
            Calendar oCal = Calendar.getInstance();
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String sFechaHora = dateformat.format(oCal.getTime());
            String sLabel = "^XA" +
                    "^FO200,150,^AO,30,20^FD TEST1234^FS" + //SAP ENC
                    "^FO130,180,^AO,20,20^FD Piezas:56  Charolas:28^FS" + //Texto
                    "^FO160,210,^AO,20,20^FD " + sFechaHora + "^FS" + //Fecha y Hora
                    "^FO200,10^BY3^BCN,100,Y,N,N^FD1234^FS^XZ"; //Codigo de barras

           int iResult =  oPrinter.sendData(sLabel);

           if(iResult == 1) {
               date = new Date();
               oText.setText("Impresión culminada..." + date.toString());
           }else
           {
               oText.setText("Resultado " +iResult );
           }

        } catch (Exception ex) {
            oText.setText(ex.getMessage());
        }
    }

    public void printLastLabel(View v) {
        TextView oText = (TextView) findViewById(R.id.txtTestPrintMessage);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        oText.setText("Iniciando Impresión..." + date.toString());
        try {
            //Busco la impresora conectada Bluetooth.
            String sLabel = cLabel.getLastLabel();
            int iResult =  oPrinter.sendData(sLabel);

            if(iResult == 1) {
                date = new Date();
                oText.setText("Impresión culminada..." + date.toString());
            }else
            {
                oText.setText("Resultado " +iResult );
            }
            date = new Date();
        } catch (Exception ex) {
            oText.setText(ex.getMessage());
        }
    }

    public void OnClickPrintCharolasVacias(View v){
        Intent intent = new Intent(this,Test2.class);
        startActivity(intent);
    }
}
