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
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import Impresion.cPrinter;
import dbOperacion.cGlobales;
import dbOperacion.cLabel;
import dbOperacion.ctTarimas;
import dbOperacion.ctNumerosParte;

import static android.view.View.FOCUS_DOWN;
import static android.view.View.FOCUS_UP;
import static android.view.View.generateViewId;

public class FlexNet extends AppCompatActivity {

    EditText oTarima;
    EditText oFlexnet;
    EditText oQTarima;
    EditText oQFlexnet;
    TextView olblFer1;
    EditText oFer1;
    TextView oMensaje;
    TextView ot22;
    EditText oQR;
    Integer IdTarima;
    cPrinter oPrinter;
    boolean bComp1 = false;
    boolean bComp2 = false;
    boolean bBack = true;
    String sNParteLabel = "";
    String sCantidadLabel = "";
    String sQRLabel = "";
    String sSAP = "";
    String IdNP = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flex_net);

        oTarima = findViewById(R.id.txtFlexTarima);
        oFlexnet = findViewById(R.id.txtfleFlexnet);
        oQTarima = findViewById(R.id.txtCantTarima);
        oQFlexnet = findViewById(R.id.txtCantFlex);
        olblFer1 = findViewById(R.id.lblFer1);
        oFer1 = findViewById(R.id.txtFer1);
        oMensaje = findViewById(R.id.lblflexResultado);
        ot22 = findViewById(R.id.textView22);
        oQR = findViewById(R.id.oflexQR);
        boolean bResult = false;

        oQR.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(oTarima.getWindowToken(), 0);
                    //otxtCharola.requestFocus(FOCUS_UP);
                    //oResultado.requestFocus(FOCUS_UP);
                    sQRLabel = oQR.getText().toString();
                    LlamaTarima();
                    oQR.requestFocus(FOCUS_DOWN);
                    return true;
                }
                return false;
            }
        });

        oTarima.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(oTarima.getWindowToken(), 0);
                    //otxtCharola.requestFocus(FOCUS_UP);
                    //oResultado.requestFocus(FOCUS_UP);
                    oTarima.requestFocus(FOCUS_DOWN);
                    return true;
                }
                return false;
            }
        });

        oFlexnet.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(oFlexnet.getWindowToken(), 0);
                    //otxtCharola.requestFocus(FOCUS_UP);
                    //oResultado.requestFocus(FOCUS_UP);
                    ValidarCodigo();

                    return true;
                }
                return false;
            }
        });

        oQTarima.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(oTarima.getWindowToken(), 0);
                    //otxtCharola.requestFocus(FOCUS_UP);
                    //oResultado.requestFocus(FOCUS_UP);
                    oQTarima.requestFocus(FOCUS_DOWN);
                    return true;
                }
                return false;
            }
        });
        oQFlexnet.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                boolean bReturn = false;
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(oTarima.getWindowToken(), 0);
                    //otxtCharola.requestFocus(FOCUS_UP);
                    //oResultado.requestFocus(FOCUS_UP);
                    ValidarCantidad();
                    if(bBack)
                    {
                        if (bComp1 == true && bComp2 == true) {
                            ImprimeEtiqueta();
                            bBack = false;
                        } else {
                            oMensaje.setBackgroundResource(R.color.Rojo);
                            oMensaje.setText("Comparaciones Incorrectas");
                        }
                    }

                    return true;
                }
                return false    ;
            }
        });

        oFer1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(oFlexnet.getWindowToken(), 0);
                    SaveFER1();
                    //otxtCharola.requestFocus(FOCUS_UP);
                    //oResultado.requestFocus(FOCUS_UP);
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

    @Override
    public void onBackPressed() {

    }

    private void ValidarCodigo(){
        try{
            String sTarima = oTarima.getText().toString();
            String sFlexnet = oFlexnet.getText().toString();
            sTarima = sTarima.trim();
            sFlexnet = sFlexnet.trim();
            if(sTarima.length() > 0 && sFlexnet.length() > 0) {
                if (sTarima.equals(sFlexnet)) {
                    sNParteLabel = sTarima;
                    oMensaje.setBackgroundResource(R.color.Verde);
                    oMensaje.setText("Tarimas Correctas");
                    oTarima.setText("");
                    oFlexnet.setText("");
                    bComp1 = true;
                    oFlexnet.requestFocus(FOCUS_DOWN);

                } else {
                    oMensaje.setBackgroundResource(R.color.Rojo);
                    oMensaje.setText("Tarimas Incorrectas");
                    oTarima.setText("");
                    oFlexnet.setText("");
                    bComp1 = false;
                    oFlexnet.requestFocus(FOCUS_UP);

                }
            }
            else{
                oMensaje.setBackgroundResource(R.color.Rojo);
                oMensaje.setText("Debe escanear etiquetas.");
                oTarima.setText("");
                oFlexnet.setText("");
                bComp1 = false;
            }
        }catch (Exception ex)
        {
            oMensaje.setText(ex.getMessage());
        }
    }

    public void OnClickregresar(View v){
        if(bComp1== true && bComp2 == true) {
            Intent intent = new Intent(this, Principal.class);
            startActivity(intent);
        }else{
            oMensaje.setBackgroundResource(R.color.Rojo);
            oMensaje.setText("Comparaciones Incorrectas");
        }
    }

    private void ValidaEtiqueta(){
        if(bComp1 == true && bComp2 == true) {
            ImprimeEtiqueta();
            //oQFlexnet.requestFocus(FOCUS_DOWN);

        }else{
            oMensaje.setBackgroundResource(R.color.Rojo);
            oMensaje.setText("Comparaciones Incorrectas");
        }
    }

    private void ValidarCantidad(){
        String sFlexnet = "";
        try{
            String sTarima = oQTarima.getText().toString();
            sFlexnet = oQFlexnet.getText().toString();
            sTarima = sTarima.trim();
            sFlexnet = sFlexnet.trim();
            if(sTarima.length() > 0 && sFlexnet.length() > 0) {

                sFlexnet = sFlexnet.substring(1);
                Integer iTarCant = Integer.parseInt(sTarima);
                Integer iTarFlex = Integer.parseInt(sFlexnet);
                if (iTarCant == iTarFlex) {
                    bComp2 = true;
                    sCantidadLabel = sTarima;
                    oMensaje.setBackgroundResource(R.color.Verde);
                    oMensaje.setText("Cantidades Correctas");
                    oQTarima.setText("");
                    oQFlexnet.setText("");

                    oQFlexnet.requestFocus(FOCUS_DOWN);
                } else {
                    oMensaje.setBackgroundResource(R.color.Rojo);
                    oMensaje.setText("Cantidades Incorrectas");
                    oTarima.setText("");
                    oQFlexnet.setText("");
                    bComp2 = false;
                    oQFlexnet.requestFocus(FOCUS_UP);
                }
            }else
            {
                oMensaje.setBackgroundResource(R.color.Rojo);
                oMensaje.setText("Debe introducir las cantidades");
                oTarima.setText("");
                oQFlexnet.setText("");
                bComp2 = false;
                oQFlexnet.requestFocus(FOCUS_UP);
            }

        }catch (Exception ex)
        {
            oMensaje.setText(ex.getMessage());
            oMensaje.setText(sFlexnet);
        }
    }

    private void SaveFER1(){
        try{

            String sFer = oFer1.getText().toString();
            ctTarimas.EditaFer(IdTarima,sFer);
            oMensaje.setText("Fer1 Guardado!");
        }catch (Exception ex)
        {
            oMensaje.setText("Error guardar FER");
        }
    }

    private void LlamaTarima(){
        try{
            String sTarima = oQR.getText().toString();
            Cursor oCursor =  ctTarimas.TarimasxNombre(sTarima);
            if(oCursor.getCount() > 0 && oCursor != null) {
                oCursor.moveToNext();
                IdTarima = oCursor.getInt(0);
                IdNP = oCursor.getString(4);
                sSAP = ctNumerosParte.NumeroParteSAP(IdNP);
            }
            else{
                oMensaje.setText("No Tarima");
            }
        }
        catch (Exception ex){
            oMensaje.setText("Error Tarima");
        }
    }

    private void ImprimeEtiqueta(){
        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String sFecha = dateFormat.format((date)).toString();
            String sUsuario = cGlobales.getInstance().getUsuario();
            String sLabel = "^XA" +
                    "^FO130,20,^AO,25,25^FDMATERIAL LIBERADO^FS" +
                    "^FO130,90,^AO,20,20^FDCANTIDADES:OK^FS" +
                    "^FO130,110,^AO,20,20^FDNO. PARTE:OK^FS" +
                    "^FO130,150,^AO,20,20^FDSAP:" + sNParteLabel + "^FS" +
                    "^FO130,170,^AO,20,20^FDCANTIDAD:" +sCantidadLabel +"^FS" +
                    "^FO130,190,^AO,20,20^FDSGI:" + sUsuario + "^FS" +
                    "^FO130,240,^AO,15,15^FD" + sFecha + "^FS" +
                    "^FO530,70^BY3^BQN,2,6^FDMM,A" + sQRLabel + "^FS" +
                    "^FO130,270^BY3^BCN,100,Y,N,N^FD30S" + sSAP + "^FS" +
                    "^XZ";
            //cPrinter oPrinter = new cPrinter();
            Boolean bGuardar2 = cLabel.setLastLabel(sLabel);
            cGlobales.getInstance().setLastPrint(sLabel);
            int iResult =oPrinter.sendData(sLabel);
            if(iResult == 1) {
                oMensaje.setText("Impresión culminada TARIMA..." + date.toString());
            }else
            {
                oMensaje.setText("Revisar Conexión IMPRESORA" );
            }
        }
        catch (Exception ex){
            oMensaje.setText("Error imprimir etiqueta");
        }
    }
}
