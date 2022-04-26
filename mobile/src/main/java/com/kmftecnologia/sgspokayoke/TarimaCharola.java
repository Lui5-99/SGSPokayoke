package com.kmftecnologia.sgspokayoke;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Impresion.cPrinter;
import dbOperacion.cGlobales;
import dbOperacion.cLabel;
import dbOperacion.ctNumerosParte;
import dbOperacion.ctTarimas;

public class TarimaCharola extends AppCompatActivity {

    EditText oCharola;
    TextView oResultado;
    TextView oCharolas;
    TextView oTarima;
    TextView oError;
    TextView oFaltan;
    List<Integer> iCharolas = new ArrayList<Integer>();
    Button oBoton;
    String NumeroParte;
    int iTotalCharolas;
    int IdTarima;
    cPrinter oPrinter;
    String sFaltan;
    String IdNombreTarima;
    String NombreTarima;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarima_charola);
        oCharola = findViewById(R.id.txtcrCharola);
        oResultado = findViewById(R.id.lblcrResultados);
        oCharolas = findViewById(R.id.lblcrCharolas);
        oError = findViewById(R.id.lblcrError);
        oTarima = findViewById(R.id.lblcrTarimaClose);
        oFaltan = findViewById(R.id.lblFaltan);
        String sEmpaque = "";
        //Busco numero de parte
        IdTarima = cGlobales.getInstance().getIdTarima();
        oTarima.setText(cGlobales.getInstance().getTarima());
        Cursor rsDatos = ctTarimas.Tarima(IdTarima);
        rsDatos.moveToFirst();
        if(rsDatos != null && rsDatos.getCount() > 0){
            NumeroParte = rsDatos.getString(3);
            iTotalCharolas =rsDatos.getInt(5);
            NombreTarima = rsDatos.getString(1);
            sEmpaque= rsDatos.getString(9);

        }
        Cursor rsCatNumParte;
        //Obtengo el standard pack de la tarima del numero de parte
        //if(sEmpaque == "1")
        //    rsCatNumParte = ctNumerosParte.NumeroParte(NumeroParte,true);
        //else
        rsCatNumParte = ctNumerosParte.NumeroParte(NumeroParte);
        rsCatNumParte.moveToFirst();
        int icatCamas, icatCharolas;
        icatCamas = rsCatNumParte.getInt(1);
        icatCharolas = rsCatNumParte.getInt(2);
        iTotalCharolas = icatCamas * icatCharolas;
        sFaltan = " " + iTotalCharolas;
        oFaltan.setText(sFaltan);

        oCharola.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    ValidaCharola();
                }
                return false;
            }
        });

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

    public void onClick(View v){
        ValidaCharola();
    }

    private void ValidaCharola(){
        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String sCodigo = oCharola.getText().toString().trim();
            String aCodigo[] = sCodigo.split(":");
            sCodigo = aCodigo[0];
            boolean bCons = false;
            int iConsecutivo = Integer.parseInt(aCodigo[1]);
            IdNombreTarima = aCodigo[2];
            if(NombreTarima.equals(IdNombreTarima)) {
                if (sCodigo.equals(NumeroParte)) {
                    bCons = ValidaConsecutivos(iConsecutivo);
                    if (bCons) {
                        oResultado.setBackgroundResource(R.color.Verde);
                        oResultado.setText("Charola No: " + iConsecutivo);
                        oCharola.setText("");
                        int Diferecia = iTotalCharolas - iCharolas.size();
                        sFaltan = " " + Diferecia;
                        oFaltan.setText(sFaltan);

                        if (iTotalCharolas == iCharolas.size()) {
                            //Taima validada completa
                            ctTarimas.EditaStatus(IdTarima, 0);
                            //Print etiqueta final
                            //Imprimo etiqueta
                            int iPiezas, iCharolas;
                            Cursor rsDatos = ctTarimas.TarimasxNombre(cGlobales.getInstance().getTarima());
                            rsDatos.moveToFirst();
                            iPiezas = rsDatos.getInt(5);
                            iCharolas = rsDatos.getInt(2);
                            Cursor rsNP = ctNumerosParte.NumeroParte(NumeroParte);
                            rsNP.moveToFirst();
                            String SAPENC = rsNP.getString(8);
                            String sUsuario = cGlobales.getInstance().getUsuario();
                            String vParcial = "";
                            //Cierre parcial
                            boolean bParcial = cGlobales.getInstance().getCierreParcial();
                            if(bParcial)
                                vParcial = "PARCIAL";
                            //Print etiqueta final
                            Calendar oCal = Calendar.getInstance();
                            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String sFechaHora = dateformat.format(oCal.getTime());
                            String sLabel = "^XA" +
                                    "^FO160,150,^AO,20,15^FD " + cGlobales.getInstance().getTarima() + " "+ vParcial + "^FS" + //IdTarima
                                    "^FO110,190,^AO,20,20^FD Piezas:" + iPiezas + "  Charolas:" + iCharolas + "^FS" + //Texto
                                    "^FO160,220,^AO,20,20^FD " + sFechaHora + "^FS" + //Fecha y Hora
                                    "^FO260,240,^AO,20,20^FD " + sUsuario + "^FS" + //Usuario
                                    "^FO200,270^BY3^BCN,100,Y,N,N^FD" + iPiezas + "^FS" + //Cantidad
                                    "^FO550,270^BY3^BQN,2,4^FDMM," + IdNombreTarima + "^FS" + //QR con nombreTarima
                                    "^FO160,20^BY3^BCN,100,Y,N,N^FD30S" + SAPENC + "^FS^XZ"; //Codigo de barras SAP ENC


                            cGlobales.getInstance().setLastPrint(sLabel);
                            Boolean bResult = cLabel.setLastLabel(sLabel);

                            int iResult = oPrinter.sendData(sLabel);
                            oCharola.setText("");
                            if (iResult == 1) {
                                date = new Date();
                                oError.setText("Impresión culminada..." + date.toString());
                                oCharola.setText("");
                            } else {
                                oError.setText("Revisar Conexión IMPRESORA");
                                oCharola.setText("");
                            }
                            oResultado.setBackgroundResource(R.color.Azul);
                            oResultado.setText("Tarima cerrada");
                            oCharola.setText("");
                        }
                    } else {
                        oResultado.setBackgroundResource(R.color.Rojo);
                        oResultado.setText("Charola ya escaneada!.");
                        oCharola.setText("");
                    }
                } else {
                    oResultado.setBackgroundResource(R.color.Rojo);
                    oResultado.setText("Charola no corresponde!.");
                    oCharola.setText("");
                }
            }else{
                oResultado.setBackgroundResource(R.color.Rojo);
                oResultado.setText("Charola no corresponde a la tarima!.");
                oCharola.setText("");
            }

        }catch(Exception ex){
            oError.setText(ex.getMessage());
        }
    }

    private boolean ValidaConsecutivos(int ICons){

        try{
            int iCount = iCharolas.size();
            int iItem = 0;
            String sCont = oCharolas.getText().toString();
            if(iCount == 0){
                //Primera charola escaneada
                iCharolas.add(ICons);
                sCont = sCont + "," + ICons;
                oCharolas.setText(sCont);
                return true;
            }
            else {
                int i = 0;
                boolean bFound = false;
                while(iCount > i){
                    iItem = iCharolas.get(i);
                    if(iItem == ICons)
                    {
                        bFound = true;
                        break;
                    }
                    i++;
                }
                if(bFound)
                    return false;
                else{
                    iCharolas.add(ICons);
                    sCont = sCont + "," + ICons;
                    oCharolas.setText(sCont);
                    return true;
                }
            }

        }catch(Exception ex){
            oError.setText(ex.getMessage());
            return  false;
        }

    }
}
