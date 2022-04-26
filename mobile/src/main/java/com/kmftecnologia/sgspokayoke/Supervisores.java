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

import java.util.ArrayList;

import dbOperacion.cGlobales;
import dbOperacion.cSupervisor;
import dbOperacion.ctTarimas;

public class Supervisores extends AppCompatActivity {
    String sSelected = "";
    ListView oListView;
    ArrayList<String> listItem;
    ArrayAdapter oAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisores);
        oListView = findViewById(R.id.lsSupervisor);
        listItem = new ArrayList<String>();
        oAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listItem);
        oListView.setAdapter(oAdapter);


        LlenaListView();

        oListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sSelected = oListView.getItemAtPosition(position).toString();
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
        try{
            Cursor oSuper = cSupervisor.Supervisores();
            if(oSuper != null && oSuper.getCount() > 0){
                String sSupervisor = "";
                while(oSuper.moveToNext()){
                    String sSupervisorLista = oSuper.getString(1);
                    boolean bIndex = listItem.add(sSupervisorLista);
                    oAdapter.notifyDataSetChanged();
                }
            }

        }catch(Exception ex){
            String sError = ex.getMessage();
        }
    }

    public void onClickNuevo(View v){
        cGlobales.getInstance().setEditar(false);
        Intent intent = new Intent(this, NewSuper.class);
        startActivity(intent);

    }

    public void onClickEditar(View v){
        cGlobales.getInstance().setEditar(true);
        cGlobales.getInstance().setSuper(sSelected);
        Intent intent = new Intent(this, NewSuper.class);
        startActivity(intent);
    }
}
