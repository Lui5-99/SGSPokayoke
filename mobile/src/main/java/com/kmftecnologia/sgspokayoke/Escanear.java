package com.kmftecnologia.sgspokayoke;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Impresion.cPrinter;
import dbOperacion.cGlobales;
import dbOperacion.cLabel;
import dbOperacion.ctTarimas;
import dbOperacion.ctNumerosParte;

import static android.view.View.FOCUS_DOWN;
import static android.view.View.FOCUS_UP;

public class Escanear extends AppCompatActivity implements AuthDialog.DatosCuadroDialogo {
    TextView oError ;
    EditText oTexto ;
    EditText otxtCharola ;
    TextView oTarima;
    TextView oCharola;
    TextView oPiezas;
    TextView oTotal;
    TextView oResultado;
    TextView oUsuario;
    cPrinter oPrinter;
    String sUsuario;
    String IdNombreTarima;
    int ContadorPiezasTarima;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escanear);
        oError = (TextView)findViewById(R.id.lbleError);
        oTexto = (EditText)findViewById(R.id.txtCodEscan);
        otxtCharola = (EditText)findViewById(R.id.txteCharola);
        oTarima = (TextView)findViewById(R.id.lblTarima);
        oCharola = (TextView)findViewById(R.id.lblTCharolas);
        oPiezas = (TextView)findViewById(R.id.lblTPiezas);
        oTotal = (TextView)findViewById(R.id.lblTTotal);
        oResultado = (TextView)findViewById(R.id.lblcrResultados);
        oUsuario = findViewById(R.id.lbleUsuario);
        oTexto.setText("");
        //oCamas.setText("Camas: ");
        oCharola.setText("Charolas: ");
        oPiezas.setText("Piezas: ");
        oTotal.setText("Total: ");
        oResultado.setText("");
        LlenaListView();
        sUsuario = cGlobales.getInstance().getUsuario();
        oUsuario.setText(sUsuario);
        //oTexto.requestFocus();



        oTexto.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(oTexto.getWindowToken(), 0);
                    String sCodigo = oTexto.getText().toString();
                    if(sCodigo.length() > 0){
                        Boolean bResult =  ValidaNP();
                        if(bResult) {
                            //oTexto.requestFocus(FOCUS_DOWN);
                            oCharola.requestFocus();
                        }
                    }

                    return true;
                }
                return false;
            }
        });
        otxtCharola.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(oTexto.getWindowToken(), 0);
                    String sCodigo = otxtCharola.getText().toString();
                    if(sCodigo.length() > 0)
                        ValidaEscaneo();
                        //oTexto.requestFocus();
                   //boolean brf = otxtCharola.requestFocus(FOCUS_UP);
                    //findViewById(R.id.txtCodEscan).requestFocus();
                    //oTexto.getParent().requestChildFocus(oTexto,oTexto);
                    oCharola.requestFocus(FOCUS_DOWN);
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
    public void OnClickParcial(View v){
        Intent intent = new Intent(this, TarimaParcial.class);
        cGlobales.getInstance().setEmbarque(0);
        startActivity(intent);
    }

    public void OnClickMenu(View v){
        Intent intent = new Intent(this,Principal.class);
        startActivity(intent);
    }

    private void LlenaListView(){
        //Metodo para llenar el Listview
        //https://www.youtube.com/watch?v=VYDLTBjdliY
        try {
            ListView resultsListView = (ListView) findViewById(R.id.lseTarimas);
            HashMap<String, String> datosTarimas = new HashMap<>();
            //Obtengo los datos para llenar la lista
            Cursor oCursorT = ctTarimas.TarimasActivasParciales();
            String sLinea2 = "";
            if (oCursorT != null && oCursorT.getCount() > 0) {
                while (oCursorT.moveToNext()) {
                    sLinea2 = "Charolas: " + oCursorT.getString(2);
                    sLinea2 = sLinea2 + ",   Total Piezas: " + oCursorT.getString(3);
                    int iStatus = oCursorT.getInt(4);
                    if (iStatus == 2)
                        sLinea2 = sLinea2 + " Parcial";
                    datosTarimas.put(oCursorT.getString(1), sLinea2);
                }

                List<HashMap<String, String>> listItems = new ArrayList<>();
                SimpleAdapter oAdapter = new SimpleAdapter(this, listItems, R.layout.list_item,
                        new String[]{"Item", "SubItem"},
                        new int[]{R.id.Encabezado, R.id.SubItems});
                Iterator oIT = datosTarimas.entrySet().iterator();
                while (oIT.hasNext()) {
                    HashMap<String, String> resultMap = new HashMap<>();
                    Map.Entry pair = (Map.Entry) oIT.next();
                    resultMap.put("Item", pair.getKey().toString());
                    resultMap.put("SubItem", pair.getValue().toString());
                    listItems.add(resultMap);
                }
                resultsListView.setAdapter(oAdapter);
            }
        }
        catch(Exception ex)
        {
            oError.setText(ex.getMessage());
        }
    }
    private boolean ValidaNP(){
        try{
            String sCodigo = oTexto.getText().toString().trim();
            if(sCodigo.length() < 3){
                oError.setText("Mal escaneo, intente de nuevo!.");
                oResultado.setText("Error mal escaneo");
                oResultado.setBackgroundResource(R.color.Azul);
                oTexto.setText("");
                otxtCharola.setText("");
                oTarima.setText("");
                return false;
            }else {
                //Primero busco NP Valido
                int iValor = ctNumerosParte.CheckNumParte(sCodigo);
                if (iValor > 0) {
                    //otxtCharola.setFocusable(true);
                    //otxtCharola.setFocusableInTouchMode(true);
                    return true;
                }else{
                    return false;
                }
            }
        }
        catch(Exception ex)
        {
            oError.setText(ex.getMessage());
            return false;
        }
    }

    private void ValidaEscaneo(){
        try {
            String sCodigo = oTexto.getText().toString().trim();
            String sCharola = otxtCharola.getText().toString().trim();
            String aCharola[] = sCharola.split(":");
            sCharola = aCharola[0].toString();
            int IConsecutivo = Integer.parseInt(aCharola[1].toString());
            int iIdTarimaActiva = 0;
            IdNombreTarima = aCharola[2];

            //Valido numero de parte
            if(sCharola.equals(sCodigo))
            {
                //Valido que la charola pertenezca a una tarima activa o parcial
                iIdTarimaActiva = ctTarimas.ValidaTarima(sCodigo);
                if(iIdTarimaActiva > 0) {
                    String vValor = "";
                    Cursor cursor = ctTarimas.Tarima(iIdTarimaActiva);
                    if(cursor != null && cursor.getCount() > 0 ){
                        cursor.moveToFirst();
                        vValor = cursor.getString(1);
                    }
                    //Valido nombre de tarima
                    if(vValor.equals(IdNombreTarima))
                    {
                        cGlobales.getInstance().setLastNP(sCodigo);
                        ActualizaTarima(iIdTarimaActiva, sCodigo, IConsecutivo);
                    }else
                    {
                        oError.setText("La charola no pertenece a la tarima!");
                        oResultado.setText("La charola no pertenece a la tarima!");
                        oResultado.setBackgroundResource(R.color.Rojo);
                        oTexto.setText("");
                        otxtCharola.setText("");
                        oTarima.setText("");
                    }
                }
            }
            else{
                AuthDialog dialog = new AuthDialog();
                dialog.CuadradoDialogo(this, Escanear.this);
            }
        }catch (Exception ex){
            oError.setText(ex.getMessage());
        }
    }

    public void OnClickFix(View v){
        Intent intent = new Intent(this,Fix.class);
        startActivity(intent);
    }


    public void OnClick(View v){
        try{
            ValidaEscaneo();
        }catch (Exception ex){
            oError.setText(ex.getMessage());
        }
    }

    @SuppressLint("ResourceType")
    private void ActualizaTarima(int ITarima, String NumeroParte, int Consecutivo){
        try{
            String sLabel = "";
            int iResultado = 0;
            boolean bEmpaque = false;//cGlobales.getInstance().getIsEmpAlt();
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            //Obtengo el standard pack de la tarima del numero de parte
            Cursor rsCatNumParte = ctNumerosParte.NumeroParte(NumeroParte);
            rsCatNumParte.moveToFirst();
            int icatCamas, icatCharolas, icatPiezas, icatTotalPiezas;
            icatCamas = rsCatNumParte.getInt(1);
            icatCharolas = rsCatNumParte.getInt(2);
            icatPiezas = rsCatNumParte.getInt(3);
            icatTotalPiezas = rsCatNumParte.getInt(4);
            //Obtengo la tarima escaneada
            int itCamas, itCharolas, itPiezas, itTotalPiezas, iStatus;
            Cursor rsTarima  = ctTarimas.Tarima(ITarima);
            rsTarima.moveToFirst();
            itCamas = rsTarima.getInt(4);
            itCharolas = rsTarima.getInt(5);
            itPiezas = rsTarima.getInt(6);
            itTotalPiezas = rsTarima.getInt(7);
            iStatus = rsTarima.getInt(8);
            icatCharolas = icatCharolas * icatCamas;
            if(Consecutivo == itCharolas) {
                //Valido que la tarima esté en status 1 o 2 para poderse escanear
                if (iStatus == 1 || iStatus == 2) {

                    //Pinto el Resultado Verde
                    String sNombreTarima = rsTarima.getString(1);
                    cGlobales.getInstance().setLastNombreTarima(sNombreTarima);

                    oResultado.setText(NumeroParte);
                    oTarima.setText(sNombreTarima);
                    oResultado.setBackgroundResource(R.color.Verde);
                    //Incremento el total de piezas de la tarima y valido que haya terminado
                    itTotalPiezas++;
                    oTotal.setText("Total: " + Integer.toString(icatTotalPiezas));
                    if (icatTotalPiezas == itTotalPiezas) {
                        //Termine
                        cGlobales.getInstance().setLastTarima(ITarima);
                        cGlobales.getInstance().setIdNumeroParte(NumeroParte);
                        iResultado = ctTarimas.EditaConteo(ITarima, icatCamas, itCharolas, itTotalPiezas, itTotalPiezas);
                        iResultado = ctTarimas.EditaStatus(ITarima, 4);
                        oCharola.setText("Charolas: " + Integer.toString(itCharolas));
                        oPiezas.setText("Piezas: " + Integer.toString(itPiezas));
                        oTotal.setText("Total: " + Integer.toString(itTotalPiezas));
                        oResultado.setBackgroundResource(R.color.Azul);
                        oResultado.setText("Tarima completa, cierra tarima!");
                        Toast.makeText(this, "Cierra la tarima!", Toast.LENGTH_SHORT).show();
                        oError.setText("Cierra la tarima!");
                        oTexto.setText("");
                        oCharola.setText("");
                        oTarima.setText("");
                        Intent intent = new Intent(this,CierreTarima.class);
                        startActivity(intent);
                    } else {
                        //Valido cantidad de piezas en la charola
                        cGlobales.getInstance().setLastTarima(ITarima);
                        cGlobales.getInstance().setIdNumeroParte(NumeroParte);
                        itPiezas++;
                        iResultado = ctTarimas.EditaConteo(ITarima, itCamas, itCharolas, itPiezas, itTotalPiezas);
                        oPiezas.setText("Piezas: " + Integer.toString(itPiezas));
                        oTotal.setText("Total: " + Integer.toString(itTotalPiezas));
                        oCharola.setText("Charolas: " + Integer.toString(itCharolas));
                        if (itPiezas == icatPiezas) {
                            itCharolas++;
                            //Llene la charola
                            if(icatCharolas >= itCharolas) {
                                itPiezas = 0;
                                oPiezas.setText("Piezas: " + Integer.toString(itPiezas));
                                oCharola.setText("Charolas: " + Integer.toString(itCharolas));


                                //Imprimó Etiqueta Charola antes de incrementar itCharolas
                                sLabel = "^XA" +
                                        "^FO170,240,^AO,20,20^FD" + NumeroParte + " - " + Integer.toString(itCharolas) + "^FS" + //IdTarima
                                        "^FO170,270,^AO,20,20^FD " + sUsuario + "^FS" + //Usuario
                                        "^FO200,20^BY3^BQN,2,8^FDMM, " + NumeroParte + ":" + Integer.toString(itCharolas) + ":" + IdNombreTarima + "^FS^XZ";
                                cGlobales.getInstance().setLastPrint(sLabel);
                                Boolean bGuardar = cLabel.setLastLabel(sLabel);
                                iResultado = ctTarimas.EditaConteo(ITarima, itCamas, itCharolas, itPiezas, itTotalPiezas);
                                int iResult = oPrinter.sendData(sLabel);
                                if (iResult == 1) {
                                    date = new Date();
                                    oError.setText("Impresión culminada..." + date.toString());
                                    otxtCharola.setText("");
                                    oTexto.setText("");
                                    oTarima.setText("");
                                } else {
                                    oResultado.setText("Revisar Conexión IMPRESORA");
                                    oResultado.setBackgroundResource(R.color.Azul);
                                    oError.setText("Resultado " + iResult);
                                    otxtCharola.setText("");
                                    oTexto.setText("");
                                    oTarima.setText("");
                                }
                            }
                        }
                    }
                } else {
                    oResultado.setBackgroundResource(R.color.Azul);
                    oResultado.setText("Tarima no puede ser escaneada!");
                    otxtCharola.setText("");
                    oTexto.setText("");
                    oTarima.setText("");
                }
            }else
            {
                oResultado.setBackgroundResource(R.color.Azul);
                oResultado.setText("Charola ya escaneada!!");
                otxtCharola.setText("");
                oTexto.setText("");
                oTarima.setText("");
            }
            otxtCharola.setText("");
            oTexto.setText("");
            oTarima.setText("");

            LlenaListView();
        }catch (Exception ex) {
            oError.setText(ex.getMessage());
            otxtCharola.setText("");
            oTexto.setText("");
        }
    }

    @Override
    public void resultadoCuadroDialogo(String user) {
        Toast.makeText(this, user, Toast.LENGTH_LONG).show();
    }
}
