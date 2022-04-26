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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dbOperacion.cGlobales;
import dbOperacion.cLabel;
import dbOperacion.cParametros;
import dbOperacion.ctNumerosParte;
import dbOperacion.ctTarimas;
public class TarimaPerdida extends AppCompatActivity {

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarima_perdida);

        oListView = (ListView)findViewById(R.id.lsPerdidasCerrar);
        oError = (TextView) findViewById(R.id.lblctErrorPer);
        oPregunta= (TextView) findViewById(R.id.lbltParPregunta2Per);
        oTarima= (TextView) findViewById(R.id.lblpaTarima2Per);
        oTexto = findViewById(R.id.lblctError);
        listItem = new ArrayList<String>();

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

    public void OnClickGuardarPerdida(View v){
        cGlobales.getInstance().setActivityOrigen(5);
        String IdNombreTarima = cGlobales.getInstance().getTarima();
        Cursor rsDatos = ctTarimas.TarimasxNombre(IdNombreTarima);
        if(rsDatos != null && rsDatos.getCount() > 0){
            rsDatos.moveToFirst();
            String sIdTarima = rsDatos.getString(0);
            int iTarima = Integer.parseInt(sIdTarima);
            cGlobales.getInstance().setIdTarima(iTarima);
            Intent intent = new Intent(this, Supervisor.class);
            startActivity(intent);
        }

    }
}
