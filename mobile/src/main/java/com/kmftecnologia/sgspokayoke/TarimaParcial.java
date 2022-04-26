package com.kmftecnologia.sgspokayoke;

import android.app.admin.SystemUpdateInfo;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import dbOperacion.cGlobales;
import dbOperacion.cSupervisor;
import dbOperacion.ctTarimas;

public class TarimaParcial extends AppCompatActivity {

    ArrayList<String> listItem;
    ArrayAdapter oAdapter;
    TextView oError;
    ListView oListView;
    TextView oPregunta;
    TextView oTarima;
    TextView oTitulo;
    String sSelected;
    int iEmbarque = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarima_parcial);
        oListView = (ListView)findViewById(R.id.lsTarimas);
        oError = (TextView) findViewById(R.id.lblparError);
        oPregunta= (TextView) findViewById(R.id.txtParPregunta);
        oTarima= (TextView) findViewById(R.id.lblpaTarima);
        oTitulo = (TextView)findViewById(R.id.lblTamParcial);

        listItem = new ArrayList<String>();
        iEmbarque = cGlobales.getInstance().getEmbarque();
        if(iEmbarque == 1){
            oPregunta.setText("Quieres embarcar la tarima:");
        }

        LlenaLista();
        oListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sSelected = oListView.getItemAtPosition(position).toString();
                oTarima.setText(sSelected);
                oPregunta.setVisibility(View.VISIBLE);
                oTarima.setVisibility(View.VISIBLE);
            }
        });
    }

    private void LlenaLista() {
        try{
            Cursor oDatos;
            if(iEmbarque == 0)
                oDatos = ctTarimas.TarimasActivas();
            else {
                oTitulo.setText("Embarque Parcial");
                oDatos = ctTarimas.TarimasParciales();
            }
              if(oDatos != null && oDatos.getCount() > 0){
                while (oDatos.moveToNext()){
                    listItem.add(oDatos.getString(1));
                }
                oAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,listItem);
                oListView.setAdapter(oAdapter);
            }
            else{
                oError.setText("No hay tarimas!");
            }
        }
        catch(Exception ex){
            oError.setText(ex.getMessage());
        }
    }

    public void OnAceptar(View v){
        try{
            Cursor oCursor = ctTarimas.TarimasxNombre(sSelected);
            if(oCursor != null && oCursor.getCount() > 0) {
                oCursor.moveToFirst();
                int ITarima = oCursor.getInt(0);
                int ICharolas = oCursor.getInt(2);
                cGlobales.getInstance().setIdTarima(ITarima);
                cGlobales.getInstance().setTarima(sSelected);
                cGlobales.getInstance().setActivityOrigen(1);
                cGlobales.getInstance().setCharolas(ICharolas);
                cGlobales.getInstance().setCierreParcial(true);
                Intent intent = new Intent(this, Supervisor.class);
                startActivity(intent);
            }

        }catch (Exception ex){
            oError.setText(ex.getMessage());
        }
    }

    public void OnRechazar(View v){
        try{
            //Regreso a la actividad que invoco
            Intent intent = new Intent(this,Escanear.class);
            startActivity(intent);

        }catch (Exception ex){
            oError.setText(ex.getMessage());
        }
    }
}
