package com.kmftecnologia.sgspokayoke;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Impresion.cPrinter;
import dbOperacion.cGlobales;
import dbOperacion.ctNumerosParte;

public class Bluetooth extends AppCompatActivity {
    TextView olblError;
    ListView oListDevices;
    String sSelected = "";
    String sMacSelected = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        olblError = findViewById(R.id.lblbtpError);
        oListDevices = findViewById(R.id.lsDevices);
        LlenaLista();
        oListDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> mapA = (Map<String, Object>)oListDevices.getItemAtPosition(position);
                sSelected = (String)mapA.get("Item");
                sMacSelected = (String)mapA.get("SubItem");
                cGlobales.getInstance().setIdNumeroParte(sSelected);
                for (int i = 0; i < oListDevices.getChildCount(); i++) {
                    if(position == i ){
                        oListDevices.getChildAt(i).setBackgroundResource(R.color.GrisClaro);
                    }else{
                        oListDevices.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }
        });
    }

    private void LlenaLista(){
        //Metodo para llenar el Listview
        //https://www.youtube.com/watch?v=VYDLTBjdliY
        BluetoothAdapter mBluetoothAdapter;
        BluetoothSocket mmSocket;
        BluetoothDevice mmDevice;
        try {
            ListView resultsListView = (ListView) findViewById(R.id.lsDevices);
            HashMap<String, String> dispositivos = new HashMap<>();
            //Obtengo los datos para llenar la lista
            String sLinea1 = "";
            String sLinea2 = "";
//Aqui busca los dispositivos Bluetooth
            try {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if(mBluetoothAdapter == null) {
                    return;
                }

                if(!mBluetoothAdapter.isEnabled()) {
                    Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBluetooth, 0);
                }
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                if(pairedDevices.size() > 0) {
                    for (BluetoothDevice device : pairedDevices) {
                        sLinea1 = device.getName();
                        sLinea2 = device.getAddress();
                        dispositivos.put(sLinea1, sLinea2);
                        }
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }

//Aqui Termina

            List<HashMap<String, String>> listItems = new ArrayList<>();
            SimpleAdapter oAdapter = new SimpleAdapter(this, listItems, R.layout.list_item,
                    new String[]{"Item", "SubItem"},
                    new int[]{R.id.Encabezado, R.id.SubItems});
            Iterator oIT = dispositivos.entrySet().iterator();
            while (oIT.hasNext()) {
                HashMap<String, String> resultMap = new HashMap<>();
                Map.Entry pair = (Map.Entry) oIT.next();
                resultMap.put("Item", pair.getKey().toString());
                resultMap.put("SubItem", pair.getValue().toString());
                listItems.add(resultMap);
            }
            resultsListView.setAdapter(oAdapter);
        }
        catch(Exception ex)
        {
            olblError.setText(ex.getMessage());
        }
    }

    public void onClickBTPrinter(View v){
        try{
            cPrinter oPrinter = new cPrinter();
            boolean bResultado = oPrinter.SavePrnter(sSelected,sMacSelected);
            if(bResultado){
                olblError.setText("Impresora guardada!");
            }else
            {
                olblError.setText("Error!");
            }

        }
        catch(Exception ex) {
            olblError.setText(ex.getMessage());
        }
    }
}
