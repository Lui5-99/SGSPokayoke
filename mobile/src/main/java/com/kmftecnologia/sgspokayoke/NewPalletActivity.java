package com.kmftecnologia.sgspokayoke;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import dbOperacion.cLabel;
import dbOperacion.ctNumerosParte;
import dbOperacion.ctTarimas;
import dbOperacion.cGlobales;
import Impresion.cPrinter;


public class NewPalletActivity extends AppCompatActivity {
    EditText oTexto;
    TextView oMensaje;
    Button oBoton;
    cPrinter oPrinter;
    String sUsuario;
    CheckBox ochkEmpaque;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pallet);
        boolean bResult = false;
        oTexto = (EditText) findViewById(R.id.txtCodigo);
        oMensaje = (TextView) findViewById(R.id.lblMensaje);
        ochkEmpaque = (CheckBox) findViewById(R.id.chkEmpAlt);

        oBoton = findViewById(R.id.btValidar);
        sUsuario = cGlobales.getInstance().getUsuario();
        oTexto.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String sTexto = oTexto.getText().toString().trim();
                    if (sTexto.length() > 0) {
                        Click();
                    }
                    // Perform action on key press

                    return true;
                }
                return false;
            }
        });

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
                    while (bResult == false && i < 6) {
                        bResult = oPrinter.connectToPrinter(sName);
                        i++;
                    }
                    if (bResult == false) {
                        oMensaje.setText("Revisar Conexión IMPRESORA");
                        oMensaje.setBackgroundResource(R.color.Rojo);
                    }
                } catch (IOException ex) {
                    try {
                        oPrinter.closeBT();
                    } catch (IOException ex2) {
                        ex2.printStackTrace();
                        oMensaje.setText("Revisar Conexión IMPRESORA");
                        oMensaje.setBackgroundResource(R.color.Rojo);
                        Toast.makeText(this, "Error con IMPRESORA", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        bResult = oPrinter.connectToPrinter(sName);
                    } catch (IOException ex3) {
                        ex3.printStackTrace();
                        oMensaje.setText("Revisar Conexión IMPRESORA");
                        oMensaje.setBackgroundResource(R.color.Rojo);
                        Toast.makeText(this, "Revisar Conexión IMPRESORA", Toast.LENGTH_SHORT).show();
                    }
                    ex.printStackTrace();
                }
            } else {
                oMensaje.setText("No encontró IMPRESORA");
                oMensaje.setBackgroundResource(R.color.Rojo);
            }
        }catch(Exception ex){
            oMensaje.setText("Revisar Conexión IMPRESORA");
            oMensaje.setBackgroundResource(R.color.Rojo);
        }
    }

    private void Click(){
        try{
            oMensaje.setText("");
            String sTexto = oTexto.getText().toString().trim();
            int iValor = 0;
            boolean bEmpaque = false; //ochkEmpaque.isChecked();
            cGlobales.getInstance().setIsEmpAlt(bEmpaque);
            iValor = ctNumerosParte.CheckNumParte(sTexto);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            if(iValor > 0) {
                //Existe el numero de parte
                iValor = ctTarimas.ValidaTarima(sTexto);
                if (iValor > 0) {
                    //Ya existe la Tarima
                    oMensaje.setText("Ya existe la tarima");
                    oTexto.setText("");
                } else if (iValor == -1) {
                    oMensaje.setText("Hubo un error al consultar en la base de datos.");
                    oTexto.setText("");
                } else if (iValor == 0) {
                    //Armar ID
                    String sIdTarima = ArmaId(sTexto);
                    String sUsuario = cGlobales.getInstance().getUsuario();
                    int iid = ctTarimas.InsertaNuevaTarima(sIdTarima,sUsuario,sTexto,bEmpaque);
                    oMensaje.setText("Se guardó " + sIdTarima + " con éxito");
                    //Faltan funciones de impresión
                    String sLabel = "^XA" + //Inicio
                            "^LL450" + // Largo de etiqueta
                            "^FO100,240,^AO,20,20^FD"+ sIdTarima + "^FS" + //IdTarima
                            "^FO210,260,^AO,20,20^FD TARIMA^FS" + //IdTarima
                            "^FO150,280,^AO,20,20^FD " + sUsuario + "^FS" + //Usuario
                            "^FO230,20^BY3^BQN,2,8^FDMM, "+ sTexto + ":" + sIdTarima + "^FS^XZ"; //Código QR
                    /* String sLabel = "^XA" + //Inicio
                            "^FO230,160,^AO,20,20^FD" + sIdTarima + "^FS" + //IdTarima
                            "FO380,10^BY3^BQN,2,5^FDMM, " + sTexto + "//" + sIdTarima + "^FS^XZ"; //Código QR*/
                    //cPrinter oPrinter = new cPrinter();
                    Boolean bGuardar2 = cLabel.setLastLabel(sLabel);
                    cGlobales.getInstance().setLastPrint(sLabel);
                    int iResult =oPrinter.sendData(sLabel);
                    if(iResult == 1) {
                        date = new Date();
                        oMensaje.setText("Impresión culminada TARIMA..." + date.toString());
                        oTexto.setText("");
                    }else
                    {
                        oMensaje.setText("Revisar Conexión IMPRESORA" );
                        oTexto.setText("");
                    }
                    sLabel = "^XA" +
                            "^LL450" +
                            "^FO170,240,^AO,20,20^FD" + sTexto + " - 1^FS" + //IdTarima
                            "^FO170,270,^AO,20,20^FD" + sUsuario + "^FS" + //Usuario
                            "^FO200,20^BY3^BQN,2,8^FDMM, " + sTexto + ":1:" + sIdTarima + "^FS^XZ";
                    Boolean bGuardar = cLabel.setLastLabel(sLabel);
                    cGlobales.getInstance().setLastPrint(sLabel);
                    //oBoton.setVisibility(View.VISIBLE);
                    //cGlobales.getInstance().setLastPrint(sLabel);
                    iResult = oPrinter.sendData(sLabel);
                    if(iResult == 1) {
                        date = new Date();
                        oMensaje.setText("Impresión culminada CHAROLA..." + date.toString());
                        oTexto.setText("");
                        CambiaPantalla();
                    }else
                    {
                        oMensaje.setText("Revisar Conexión IMPRESORA" );
                        oTexto.setText("");
                    }
                }
            } else if (iValor == -1) {
                oMensaje.setText("Hubo un error al consultar en la base de datos.");
                oTexto.setText("");
            } else if (iValor == 0) {
                oMensaje.setText("Numero de parte no existe en la base de datos.");
                oTexto.setText("");
            }
            oTexto.setText("");
        }catch(Exception ex){
            oMensaje.setText(ex.getMessage());
        }
    }

    private void ImprimirEtiqueta(String Etiqueta){
        try{
            cPrinter oPrinter = new cPrinter();
            cGlobales.getInstance().setLastPrint(Etiqueta);
            oPrinter.sendData(Etiqueta);

        }catch(Exception ex){
            oMensaje.setText(ex.getMessage());
        }
    }

    public void OnClick(View v){
        try{
            String sLabel = cGlobales.getInstance().getLastPrint();
            ImprimirEtiqueta(sLabel);

        }catch(Exception ex){
            oMensaje.setText(ex.getMessage());
        }
    }

    //Arma el Nombre de la nueva tarima NPYYMMDDHHMM
    private String ArmaId(String sNumPart){
        Calendar oCalendario = Calendar.getInstance();
        String sValor = "";
        String sID = sNumPart;
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
        return  sID;
    }

    private void CambiaPantalla(){
        Intent intent = new Intent(this,Escanear.class);
        startActivity(intent);
    }

    private void prinEtiquetas(){
        TextView oText = (TextView)findViewById(R.id.txtPrintMessage);
        oText.setText("Iniciando Impresión...");
        try{
            //Busco la impresora conectada Bluetooth.
            cPrinter oPrinter = new cPrinter();
            oText.append(System.getProperty("line.separator"));
            oText.append("Buscando Impresora...");
            int iResult = oPrinter.findBT();
            if(iResult == -1){
                oText.append(System.getProperty("line.separator"));
                oText.append("Error buscando Impresora...");
            }else if(iResult == 0){
                oText.append(System.getProperty("line.separator"));
                oText.append("No encontró impresora ...");
            }else if(iResult == 1){
                oText.append(System.getProperty("line.separator"));
                oText.append("Encontró la impresora: ") ;//+ oPrinter.mmDevice.getName());
                //Abrimos conexión con impresora
                iResult = oPrinter.openBT();
                if(iResult == -1){
                    oText.append(System.getProperty("line.separator"));
                    oText.append("Error abriendo conexión Impresora...");
                }else if(iResult == 0){
                    oText.append(System.getProperty("line.separator"));
                    oText.append("No se conectó a impresora ...");
                }else if(iResult == 1){
                    oText.append(System.getProperty("line.separator"));
                    oText.append("Conexión con: " );//+ oPrinter.mmDevice.getName());

                    //Armo cadenas a imprimir
                    String sEtiquetaTarima = "";
                    sEtiquetaTarima = "^XA^FO230,180,^AO,30,20^FDFDTesting^FS^FO200,30^BY3^BCN,50,Y,N,N^FD1100000003^FS^XZ";
                    iResult = oPrinter.sendData(sEtiquetaTarima);
                    if(iResult > 0){
                        oText.append(System.getProperty("line.separator"));
                        oText.append("Se imprimió la etiqueta de tarima");
                    }else{
                        oText.append(System.getProperty("line.separator"));
                        oText.append("Error en la etiqueta de tarima");
                    }

                    String sEtiquetaCharola = "";
                    iResult = oPrinter.sendData((sEtiquetaCharola));
                    if(iResult > 0){
                        oText.append(System.getProperty("line.separator"));
                        oText.append("Se imprimió la etiqueta de charola");
                    }else{
                        oText.append(System.getProperty("line.separator"));
                        oText.append("Error en la etiqueta de charola");
                    }

                    //Cerramos conexión
                    oPrinter.closeBT();
                    oText.append(System.getProperty("line.separator"));
                    oText.append("Cerramos conexión.");

                }
            }
        }catch (Exception ex){
            oText.setText(ex.getMessage());
        }
    }
}
