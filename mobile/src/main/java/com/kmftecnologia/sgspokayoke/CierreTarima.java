package com.kmftecnologia.sgspokayoke;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Impresion.cPrinter;
import dbOperacion.cGlobales;
import dbOperacion.cLabel;
import dbOperacion.cParametros;
import dbOperacion.ctNumerosParte;
import dbOperacion.ctTarimas;

public class CierreTarima extends AppCompatActivity {

    ArrayList<String> listItem;
    ArrayAdapter oAdapter;
    TextView oError;
    ListView oListView;
    TextView oPregunta;
    TextView oTarima;
    TextView oTitulo;
    TextView oTexto;
    String sSelected;
    int iEmbarque = 0;
    cPrinter oPrinter;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cierre_tarima);
        oListView = (ListView)findViewById(R.id.lsItemsCerrar);
        oError = (TextView) findViewById(R.id.lblparError);
        oPregunta= (TextView) findViewById(R.id.lbltParPregunta2);
        oTarima= (TextView) findViewById(R.id.lblpaTarima2);
        oTexto = findViewById(R.id.lblctError);
        listItem = new ArrayList<String>();

        if(iEmbarque == 1){
            oPregunta.setText("Quieres embarcar la tarima:");
        }

        LlenaListView();
        oListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> mapA = (Map<String, Object>)oListView.getItemAtPosition(position);
                sSelected = (String)mapA.get("Item");
                oTarima.setText(sSelected);
                cGlobales.getInstance().setTarima(sSelected);
                oPregunta.setVisibility(View.VISIBLE);
                oTarima.setVisibility(View.VISIBLE);
                for (int i = 0; i < oListView.getChildCount(); i++) {
                    if(position == i ){
                        oListView.getChildAt(i).setBackgroundResource(R.color.GrisClaro);
                    }else{
                        oListView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }
        });

        try{
            //Rutina de Impresión
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
                        while(bResult == false && i < 6) {
                            bResult = oPrinter.connectToPrinter(sName);
                            i++;
                        }
                        if(bResult == false) {
                            oTarima.setBackgroundResource(R.color.Rojo);
                            oTarima.setText("Revisar Conexión IMPRESORA");
                        }
                    }catch (IOException ex) {
                        try {
                            oPrinter.closeBT();
                        } catch (IOException ex2) {
                            ex2.printStackTrace();
                            oTarima.setBackgroundResource(R.color.Rojo);
                            oTarima.setText("Revisar Conexión IMPRESORA");
                        }
                        try {
                            bResult = oPrinter.connectToPrinter(sName);
                        } catch (IOException ex3) {
                            ex3.printStackTrace();
                            oTarima.setBackgroundResource(R.color.Rojo);
                            oTarima.setText("Revisar Conexión IMPRESORA");
                        }
                        ex.printStackTrace();
                    }
            }else{
                oTarima.setText("No se encontró IMPRESORA!");
                oTarima.setBackgroundResource(R.color.Azul);
            }
        }catch(Exception ex5){
            oTarima.setText("Revisar Conexión IMPRESORA!");
            oTarima.setBackgroundResource(R.color.Azul);
        }
    }

    private void LlenaListView(){
        //Metodo para llenar el Listview
        //https://www.youtube.com/watch?v=VYDLTBjdliY
        try {
            ListView resultsListView = (ListView) findViewById(R.id.lsItemsCerrar);
            HashMap<String, String> datosTarimas = new HashMap<>();
            //Obtengo los datos para llenar la lista
            Cursor oCursorT = ctTarimas.TarimasxCerrar();
            String sLinea2 = "";
            if (oCursorT != null && oCursorT.getCount() > 0) {
                while (oCursorT.moveToNext()) {
                    sLinea2 = "Num Parte: " + oCursorT.getString(2);
                    sLinea2 = sLinea2 + ", Piezas: " + oCursorT.getString(4);
                    sLinea2 = sLinea2 + ", Charolas: " + oCursorT.getString(3);
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
            else{
                oTexto.setText("No hay tarimas por liberar!");
            }
        }
        catch(Exception ex)
        {
            //
        }
    }

    public void OnClickGuardar(View v){
        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            cGlobales.getInstance().setCierreParcial(false);
            if(sSelected.length()> 0)
            {
                boolean bEscanear = cParametros.bEscaneoCharolas();
                Cursor rsDatos = ctTarimas.TarimasxNombre(sSelected);
                if(rsDatos != null && rsDatos.getCount() > 0) {

                    rsDatos.moveToFirst();
                    int iValor = rsDatos.getInt(0);
                    if (bEscanear) {
                        cGlobales.getInstance().setIdTarima(iValor);
                        cGlobales.getInstance().setTarima(sSelected);
                        Intent intent = new Intent(this, TarimaCharola.class);
                        startActivity(intent);
                    } else {
                        //Imprimo etiqueta

                        int iPiezas, iCharolas;
                        iPiezas = rsDatos.getInt(5);
                        iCharolas = rsDatos.getInt(2);
                        String sNP = rsDatos.getString(4);
                        String SAPENC  = ctNumerosParte.NumeroParteSAPENC(sNP);
                        iCharolas = iCharolas -1;

                        //Print etiqueta final
                        Calendar oCal = Calendar.getInstance();
                        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String sFechaHora = dateformat.format(oCal.getTime());
                        String sUsuario = cGlobales.getInstance().getUsuario();

                        String sLabel = "^XA" +
                                "^FO310,150,^AO,20,15^FD " + sSelected + "^FS" + //SAP ENC
                                "^FO260,190,^AO,20,20^FD Piezas:" + iPiezas + "  Charolas:" + iCharolas + "^FS" + //Texto
                                "^FO310,220,^AO,20,20^FD " + sFechaHora + "^FS" + //Fecha y Hora
                                "^FO410,240,^AO,20,20^FD " + sUsuario + "^FS" + //Usuario
                                "^FO350,270^BY3^BCN,100,Y,N,N^FD" + iPiezas + "^FS" +
                                "^FO650,270^BY3^BQN,2,4^FDMM, " + sSelected + "^FS" + //QR con nombreTarima
                                "^FO310,20^BY3^BCN,100,Y,N,N^FD30S" + SAPENC + "^FS^XZ"; //Codigo de barras
                        Boolean bGuardar = cLabel.setLastLabel(sLabel);
                        cGlobales.getInstance().setLastPrint(sLabel);
                        int iResult = oPrinter.sendData(sLabel);

                        if (iResult == 1) {
                            date = new Date();
                            ctTarimas.EditaStatus(iValor, 0);
                            oTexto.setText("Impresión culminada..." + date.toString());
                            Intent intent = new Intent(this, Principal.class);
                            startActivity(intent);
                        } else {
                            oTexto.setText("Revisar Conexión IMPRESORA");
                        }
                    }
                }
            }
            else{
                oTexto.setText("Debes seleccionar una tarima");
            }
        }
        catch(Exception ex)
        {
            oTexto.setText("Selecciona una tarima");
        }
    }

    public void OnClickMenu(View v){
        Intent intent = new Intent(this,Principal.class);
        startActivity(intent);
    }
}
