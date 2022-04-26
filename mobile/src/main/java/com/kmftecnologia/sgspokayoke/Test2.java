package com.kmftecnologia.sgspokayoke;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import Impresion.cPrinter;
import dbOperacion.cGlobales;
import dbOperacion.cLabel;
import dbOperacion.ctNumerosParte;
import dbOperacion.ctTarimas;

public class Test2 extends AppCompatActivity {
    TextView oError ;
    EditText oCharola;
    TextView oResultado;
    cPrinter oPrinter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        oCharola = findViewById(R.id.txtreCharola);
        oResultado = findViewById(R.id.lblreprinResult);
        oError = findViewById(R.id.lblreeterror);
        oCharola.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(oCharola.getWindowToken(), 0);
                    String sCodigo = oCharola.getText().toString();
                    if(sCodigo.length() > 0){
                        Imprime();
                    }

                    return true;
                }
                return false;
            }
        });


        try{
            //Rutina de Impresión
            boolean bResult = false;
            oPrinter = new cPrinter();
            oPrinter.findBT();
            String sName = "";
            if (oPrinter.mmDevice != null && oPrinter.mmDevice.getName().length() > 0)
                sName = oPrinter.mmDevice.getName();
            if (sName.length() > 0) {
                try {
                    bResult = false;
                    int i = 1;
                    while (bResult == false && i < 6) {
                        bResult = oPrinter.connectToPrinter(sName);
                        i++;
                    }
                    if (bResult == false) {
                        oResultado.setText("Revisar Conexión IMPRESORA");
                        oResultado.setBackgroundResource(R.color.Azul);
                    }
                } catch (IOException ex) {
                    try {
                        oPrinter.closeBT();
                    } catch (IOException ex2) {
                        ex2.printStackTrace();
                        oError.setText(ex2.getMessage());
                        oResultado.setText("Revisar Conexión IMPRESORA");
                        oResultado.setBackgroundResource(R.color.Azul);
                    }
                    try {
                        bResult = oPrinter.connectToPrinter(sName);
                    } catch (IOException ex3) {
                        ex3.printStackTrace();
                        oError.setText(ex3.getMessage());
                        oResultado.setText("Revisar Conexión IMPRESORA!");
                        oResultado.setBackgroundResource(R.color.Azul);
                    }
                    ex.printStackTrace();
                }
            }else{
                oResultado.setText("No se encontró IMPRESORA!");
                oResultado.setBackgroundResource(R.color.Azul);
            }
        }catch(Exception ex5){
            oResultado.setText("Revisar Conexión IMPRESORA!");
            oResultado.setBackgroundResource(R.color.Azul);
        }
    }

    private void Imprime(){
        try{
            String sUsuario = cGlobales.getInstance().getUsuario();
            String sTarima = oCharola.getText().toString();
            String aCharola[] = sTarima.split(":");
            sTarima = aCharola[2];
            String sEmpaque = "";
            Cursor oCur = ctTarimas.TarimasxNombre(sTarima);
            if(oCur != null && oCur.getCount() > 0) {
                Integer iResult = 0;
                Integer IdTarima = oCur.getInt(0);
                //Valido cantidad de charolas e imprimo las faltantes
                Cursor oCursor = ctTarimas.Tarima(IdTarima);
                oCursor.moveToFirst();
                Integer iCharolas = oCursor.getInt(5);
                String sNP = oCursor.getString(3);
                //sEmpaque = oCursor.getString(9);
                //if(iEmbarque == 0){
                Cursor oCursorNP;
                //if(sEmpaque == "1")
                 //   oCursorNP= ctNumerosParte.NumeroParte(sNP,true);
                //else
                 oCursorNP= ctNumerosParte.NumeroParte(sNP);
                //Cursor oCursorNP = ctNumerosParte.NumeroParte(sNP);
                oCursorNP.moveToFirst();
                int iCamas = oCursorNP.getInt(1);
                int iCharxCama = oCursorNP.getInt(2);
                iCharxCama = iCharxCama * iCamas;
                int iDif = iCharxCama - iCharolas;
                if (iDif > 0) {
                    int i = 0;
                    iCharolas++;
                    String sLabel;
                    while (iDif > i) {

                        sLabel = "^XA" +
                                "^FO250,250,^AO,20,20^FD" + sNP + "- " + iCharolas + "- VACIA^FS" + //IdTarima
                                "^FO250,280,^AO,20,20^FD " + sUsuario + "^FS" + //IdTarima
                                "^FO300,10^BY3^BQN,2,10^FDMM, " + sNP + ":" + iCharolas + ":" + sTarima + "^FS^XZ";
                        cGlobales.getInstance().setLastPrint(sLabel);
                        Boolean bResult1 = cLabel.setLastLabel(sLabel);
                        iResult = oPrinter.sendData(sLabel);
                        if (iResult == 1) {
                            oResultado.setText("Impresión culminada...");
                        } else {
                            oResultado.setText("Revisar Conexión IMPRESORA. ");
                            oResultado.setBackgroundResource(R.color.Rojo);
                            return;
                        }
                        iCharolas++;
                        i++;
                    }
                }
            }

        } catch (Exception ex){
            oResultado.setText("Revisa conexión impresora!");
        }
    }

    public void OnClickPrintCharolasRegresar(View v){
        Intent intent = new Intent(this,Principal.class);
        startActivity(intent);
    }
}
