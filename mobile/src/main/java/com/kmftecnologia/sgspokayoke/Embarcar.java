package com.kmftecnologia.sgspokayoke;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import dbOperacion.cGlobales;


public class Embarcar extends AppCompatActivity {

    Button oCancel;
    Button oAcept1;
    Button oAcept2;
    TextView oMensaje;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_embarcar);
        oCancel = findViewById(R.id.cmddbCancel);
        oAcept1 = findViewById(R.id.cmdDepurar);
        oAcept2 = findViewById(R.id.cmdAceptarDep);
        oAcept2.setVisibility(View.INVISIBLE);
        oMensaje = (TextView)findViewById(R.id.lblMensaje);
        oMensaje.setVisibility(View.INVISIBLE);

    }

   public void OnClickCancel(View v)
   {
       Intent intent = new Intent(this,Principal.class);
       startActivity(intent);
   }

   private  void OnClickAceptar(View v){
       oAcept2.setVisibility(View.VISIBLE);
       oMensaje.setVisibility(View.VISIBLE);
   }

    public void OnClickAcept(View v)
    {
        cGlobales.getInstance().setActivityOrigen(2);
        Intent intent = new Intent(this,Principal.class);
        startActivity(intent);
    }
}
