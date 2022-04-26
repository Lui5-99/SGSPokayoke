package com.kmftecnologia.sgspokayoke;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dbOperacion.cGlobales;
import dbOperacion.ctTarimas;
import dbOperacion.ctNumerosParte;

public class Catalogo extends AppCompatActivity {
    String sSelected = "";
    ListView oListView;
    Button oBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);
        LlenaListView();
        oListView  = (ListView) findViewById(R.id.lsNPTarimas);
        oBtn = findViewById(R.id.cmdNuevoNP);
        oListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> mapA = (Map<String, Object>)oListView.getItemAtPosition(position);
                sSelected = (String)mapA.get("Item");
                cGlobales.getInstance().setEditIdNumeroParte(sSelected);
                for (int i = 0; i < oListView.getChildCount(); i++) {
                    if(position == i ){
                        oListView.getChildAt(i).setBackgroundResource(R.color.GrisClaro);
                    }else{
                        oListView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }
        });
    }

    public void OnClickNuevo(View v){
        cGlobales.getInstance().setEditar(false);
        Intent intent = new Intent(this,NuevoNP.class);
        startActivity(intent);
    }

    public void OnClickEditar(View v){
        if(sSelected.length() > 0) {
            cGlobales.getInstance().setEditar(true);
            Intent intent = new Intent(this, NuevoNP.class);
            startActivity(intent);
        }
    }


    private void LlenaListView(){
        //Metodo para llenar el Listview
        //https://www.youtube.com/watch?v=VYDLTBjdliY
        try {
            ListView resultsListView = (ListView) findViewById(R.id.lsNPTarimas);
            HashMap<String, String> datosTarimas = new HashMap<>();
            //Obtengo los datos para llenar la lista
            Cursor oCursorT = ctNumerosParte.NumerosdeParte();
            String sLinea1 = "";
            String sLinea2 = "";
            String sEmpaque = "";
            if (oCursorT != null && oCursorT.getCount() > 0) {
                while (oCursorT.moveToNext()) {
                    sLinea2 = "Versión: " + oCursorT.getString(4);
                    sLinea2 = sLinea2 + ", Camas: " + oCursorT.getString(8);
                    sLinea2 = sLinea2 + ", Piezas: " + oCursorT.getString(3);
                    sLinea2 = sLinea2 + ", SAP: " + oCursorT.getString(5);
                    sLinea2 = sLinea2 + ", Activo: " + oCursorT.getString(6);
                    //sLinea2 = sLinea2 + ", Empaque Alt: " + oCursorT.getString(8);
                    //sEmpaque = oCursorT.getString(8);
                    //if(sEmpaque.contains("1"))
                    //    sLinea1 = "*" + oCursorT.getString(0);
                    //else
                    sLinea1 = oCursorT.getString(0);
                    datosTarimas.put(sLinea1, sLinea2);
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
            //
        }
    }

}
