package com.kmftecnologia.sgspokayoke;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import dbOperacion.cGlobales;

public class Configuracion extends AppCompatActivity {
    Button oNP;
    Button oSuper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        oNP = findViewById(R.id.cmdCatalogo);
        oSuper = findViewById(R.id.cmdSupervisores);
        boolean bIsSuper = cGlobales.getInstance().getIsSuper();
        if(bIsSuper){
            oNP.setVisibility(View.VISIBLE);
            oSuper.setVisibility(View.VISIBLE);
        }else
        {
            oNP.setVisibility(View.INVISIBLE);
            oSuper.setVisibility(View.INVISIBLE);
        }
    }


    public void OnClickCatalogo(View v){
        try{
            Intent intent = new Intent(this,Catalogo.class);
            startActivity(intent);
        }catch(Exception ex){
            TextView oError = (TextView) findViewById(R.id.lblError);
            oError.setVisibility(View.VISIBLE);
            oError.setText(ex.getMessage());
        }
    }

    public void OnClickConfigPrinter(View v){
        try{
            Intent intent = new Intent(this,Bluetooth.class);
            startActivity(intent);
        }catch(Exception ex){
            TextView oError = (TextView) findViewById(R.id.lblError);
            oError.setVisibility(View.VISIBLE);
            oError.setText(ex.getMessage());
        }
    }

    public void OnClickTest(View v){
        try{
            Intent intent = new Intent(this,Test.class);
            startActivity(intent);
        }catch(Exception ex){
            TextView oError = (TextView) findViewById(R.id.lblError);
            oError.setVisibility(View.VISIBLE);
            oError.setText(ex.getMessage());
        }
    }

    public void OnClickSuper(View v){
        try{
            Intent intent = new Intent(this,Supervisores.class);
            startActivity(intent);
        }catch(Exception ex){
            TextView oError = (TextView) findViewById(R.id.lblError);
            oError.setVisibility(View.VISIBLE);
            oError.setText(ex.getMessage());
        }
    }

    public void OnClickReEtiqueta(View v){
        try{
            Intent intent = new Intent(this,GeneraEtiqueta.class);
            startActivity(intent);
        }catch(Exception ex){
            TextView oError = (TextView) findViewById(R.id.lblError);
            oError.setVisibility(View.VISIBLE);
            oError.setText(ex.getMessage());
        }
    }
}
