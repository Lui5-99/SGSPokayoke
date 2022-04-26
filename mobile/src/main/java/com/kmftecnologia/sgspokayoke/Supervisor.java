package com.kmftecnologia.sgspokayoke;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import Impresion.cPrinter;
import dbOperacion.cGlobales;
import dbOperacion.cLabel;
import dbOperacion.cLogs;
import dbOperacion.cSupervisor;
import dbOperacion.ctNumerosParte;
import dbOperacion.ctTarimas;
import dbConexion.dbDepuracion;

public class Supervisor extends AppCompatActivity {
    TextView oError;
    EditText oPass;
    int iActivity = 0;
    cPrinter oPrinter;
    String sUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor);
        oError = (TextView)findViewById(R.id.lblAuError);
        oPass = (EditText)findViewById(R.id.txtPassSuper);
        iActivity = cGlobales.getInstance().getActivityOrigen();
        Button oBoton = findViewById(R.id.cmdAutorizar);
        sUsuario = cGlobales.getInstance().getUsuario();

        //Rutina de Impresión
        boolean bResult = false;
        try {
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
                        oError.setText("Revisar Conexión IMPRESORA!");
                        //oBoton.setVisibility(View.INVISIBLE);
                    }
                }catch (IOException ex) {
                    try {
                        oPrinter.closeBT();
                    } catch (IOException ex2) {
                        ex2.printStackTrace();
                        oError.setText("Revisar Conexión IMPRESORA");
                        oError.setBackgroundResource(R.color.Rojo);
                    }
                    try {
                        bResult = oPrinter.connectToPrinter(sName);
                    } catch (IOException ex3) {
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

    public void OnAceptar(View v){
        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String date = dateFormat.format(new Date());
            int iCharolas = 0;
            String SAPENC = "";
            String sNP = "";
            String SPass = oPass.getText().toString();
            String sResult = cSupervisor.ValidaPass(SPass);
            String sEmpaque = "";
            int iResult = 0;
            //Regreso a la actividad que invoco
            Intent intent = null;
            if(sResult.length() > 0) {
                if (iActivity == 1) {
                    int iEmbarque = cGlobales.getInstance().getEmbarque();
                    int IdTarima = cGlobales.getInstance().getIdTarima();
                    String vNombreTarima = cGlobales.getInstance().getTarima();
                    if(iEmbarque == 0){
                    //Invoco Tarima Parcial
                        //Valido cantidad de charolas e imprimo las faltantes
                        Cursor oCursor = ctTarimas.Tarima(IdTarima);
                        oCursor.moveToFirst();
                        iCharolas = oCursor.getInt(5);
                        sNP = oCursor.getString(3);
                        //sEmpaque = oCursor.getString(9);
                    //if(iEmbarque == 0){
                        Cursor oCursorNP;
                        //if(sEmpaque.contains("1"))
                        oCursorNP= ctNumerosParte.NumeroParte(sNP);
                        //else
                        //    oCursorNP= ctNumerosParte.NumeroParte(sNP);
                        oCursorNP.moveToFirst();
                        int iCamas = oCursorNP.getInt(1);
                        int iCharxCama = oCursorNP.getInt(2);
                        SAPENC = oCursorNP.getString(8);
                        iCharxCama = iCharxCama * iCamas;
                        int iDif = iCharxCama - iCharolas;
                        if(iDif > 0){
                            int i = 0;
                            iCharolas++;
                            String sLabel;
                            while(iDif > i){

                                sLabel = "^XA" +
                                        "^FO170,240,^AO,20,20^FD" + sNP + "- " + iCharolas + "- VACIA^FS" + //IdTarima
                                        "^FO170,270,^AO,20,20^FD " + sUsuario + "^FS" + //IdTarima
                                        "^FO200,20^BY3^BQN,2,8^FDMM, " + sNP + ":"+ iCharolas + ":" + vNombreTarima + "^FS^XZ";
                                cGlobales.getInstance().setLastPrint(sLabel);
                                Boolean bResult1 = cLabel.setLastLabel(sLabel);
                                iResult =  oPrinter.sendData(sLabel);
                                if(iResult == 1) {
                                    oError.setText("Impresión culminada..." + date.toString());
                                }else
                                {
                                    oError.setText("Revisar Conexión IMPRESORA. ");
                                    oError.setBackgroundResource(R.color.Rojo);
                                }
                                iCharolas++;
                                i++;
                            }
                        }
                    //Imprimo etiqueta
////
                        int iPiezas = 0;
                        String SNumPart = "";
                        Cursor rsDatos = ctTarimas.TarimasxNombre(cGlobales.getInstance().getTarima());
                        if(rsDatos != null && rsDatos.getCount() > 0){
                            rsDatos.moveToFirst();
                            iPiezas = rsDatos.getInt(5);
                            iCharolas = rsDatos.getInt(2);
                            SNumPart = rsDatos.getString(4);
                        }

                        //Print etiqueta final
                        Calendar oCal = Calendar.getInstance();
                        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String sFechaHora = dateformat.format(oCal.getTime());
                        String sLabel = "^XA" +
                                "^FO130,250,^AO,30,20^FD "+ cGlobales.getInstance().getTarima() + "^FS" + //IdTarima
                                "^FO50,280,^AO,20,20^FD Piezas:" + iPiezas + "  Charolas:" + iCharolas + "^FS" + //Texto
                                "^FO60,310,^AO,20,20^FD " + sFechaHora + "^FS" + //Fecha y Hora
                                "^FO180,340,^AO,20,20^FD PARCIAL^FS" + //Leyenda Parcial
                                "^FO150,370,^AO,20,20^FD "+ sUsuario + "^FS" + //usuario
                                "^FO210,20^BY3^BQN,2,8^FDMM,30S"+ SAPENC + "^FS^XZ"; //Código QR
                        Boolean bResult2 = cLabel.setLastLabel(sLabel);
                        iResult =  oPrinter.sendData(sLabel);
                        ctTarimas.EditaStatus(IdTarima,2); //Status Tarima Parcial
                        intent = new Intent(this, Escanear.class);
                        if(iResult == 1) {
                            oError.setText("Impresión culminada..." + date.toString());
                            Toast.makeText(this, "Tarima activada como parcial.", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            oError.setText("Revisar Conexión IMPRESORA. ");
                            oError.setBackgroundResource(R.color.Rojo);
                        }
                        ///
                    }else{

                        intent = new Intent(this, TarimaCharola.class);
/*
                        //Imprimo etiqueta FINAL
                        //Print etiqueta final
                        String SAP = "";
                        int iPiezas = 0;
                        String IdNombreTarima = cGlobales.getInstance().getTarima();
                        Cursor rsDatos = ctTarimas.TarimasxNombre(IdNombreTarima);
                        if(rsDatos != null && rsDatos.getCount() > 0){
                            rsDatos.moveToFirst();
                            iPiezas = rsDatos.getInt(5);
                            iCharolas = rsDatos.getInt(2);
                            sNP = rsDatos.getString(4);
                            SAP = ctNumerosParte.NumeroParteSAPENC(sNP);
                        }


                        Calendar oCal = Calendar.getInstance();
                        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String sFechaHora = dateformat.format(oCal.getTime());
                        String sLabel = "^XA" +
                                "^FO200,150,^AO,30,20^FD "+ cGlobales.getInstance().getTarima() + "^FS" + //IdTarima
                                "^FO130,180,^AO,20,20^FD Piezas:" + iPiezas + "  Charolas:" + iCharolas + "^FS" + //Texto
                                "^FO160,210,^AO,20,20^FD " + sFechaHora + "^FS" + //Fecha y Hora
                                "^FO200,240,^AO,20,20^FD PARCIAL^FS" + //Leyenda Parcial
                                "^FO200,270,^AO,20,20^FD "+ sUsuario + "^FS" + //usuario
                                "^FO200,10^BY3^BCN,100,Y,N,N^FD30S" + SAP + "^FS" + //Codigo de barras SAP ENC
                                "^FO350,450^BY3^BQN,2,4^FDMM,"+ IdNombreTarima + "^FS"+ //QR con nombreTarima
                                "^FO200,300^BY3^BCN,100,Y,N,N^FD" + iPiezas + "^FS^XZ"; //Cantidad

                        Boolean bResult3 = cLabel.setLastLabel(sLabel);
                        iResult =  oPrinter.sendData(sLabel);
                        if(iResult == 1) {
                            oError.setText("Impresión culminada..." + date.toString());
                            ctTarimas.EditaStatus(IdTarima,3); //Status Tarima Parcial
                            Toast.makeText(this, "Tarima parcial embarcada.", Toast.LENGTH_SHORT).show();
                            intent = new Intent(this, Principal.class);
                        }else
                        {
                            oError.setText("Revisar Conexión IMPRESORA. ");
                            oError.setBackgroundResource(R.color.Rojo);
                            return;
                        }

 */
                    }

                } else if(iActivity == 2){
                    //Depuro la tabla de Tarimas
                    Boolean bDep = dbDepuracion.DepuraDB();
                    Toast.makeText(this, "Tarimas completas o embarcada depuradas.", Toast.LENGTH_SHORT).show();
                    intent = new Intent(this, Principal.class);
                } else if(iActivity == 4){
                    //Conteo de pieza vuelta a escanear
                    String sLastNP = cGlobales.getInstance().getLastNP();
                    String sUsuario = cGlobales.getInstance().getUsuario();
                    String sLastTarima = cGlobales.getInstance().getLastNombreTarima();
                    cLogs.InsertaPiezaReEscaneada(sLastTarima,sUsuario,sLastNP);
                    intent = new Intent(this, Escanear.class);
                }else if(iActivity == 5){
                    int iTarima = cGlobales.getInstance().getIdTarima();
                    ctTarimas.EditaStatus(iTarima,0);
                    intent = new Intent(this, Principal.class);
                }

                cGlobales.getInstance().setAutorizacion(true);
                startActivity(intent);
            }else{
                oError.setText("Password incorrecto");
                oPass.setText("");
            }
        }catch (Exception ex){
            oError.setText(ex.getMessage());
        }
    }

    public void OnRegresar(View v)
    {
        Intent intent = new Intent(this,Principal.class);
        startActivity(intent);
    }

    public void OnRechazar(View v){
        try{
            //Regreso a la actividad que invoco
            Intent intent = null;
            if(iActivity == 1){
                int iEmbarque = cGlobales.getInstance().getEmbarque();
                if(iEmbarque == 0)
                    intent = new Intent(this,Escanear.class);
                else
                    intent = new Intent(this, Principal.class);
            }else if(iActivity == 2){
                intent = new Intent(this,Principal.class);
            }else if(iActivity == 5){
                intent = new Intent(this,Principal.class);
            }else{
                intent = new Intent(this,Principal.class);
            }
            cGlobales.getInstance().setAutorizacion(false);
            startActivity(intent);

        }catch (Exception ex){
            oError.setText(ex.getMessage());
        }
    }

    public  void OnBackHome(){
        Intent intent = new Intent(this,Principal.class);
        startActivity(intent);
    }
}
