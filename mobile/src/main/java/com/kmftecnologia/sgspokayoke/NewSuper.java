package com.kmftecnologia.sgspokayoke;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import dbOperacion.cGlobales;
import dbOperacion.cSupervisor;

public class NewSuper extends AppCompatActivity {

    EditText oUser;
    EditText oPass;
    TextView lNombre;
    String sNombreSuper;
    boolean bEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_super);
        oUser = findViewById(R.id.txtNameSuper);
        oPass = findViewById(R.id.txtPassSuper2);
        lNombre = findViewById(R.id.lblNombreSuper);
        sNombreSuper = cGlobales.getInstance().getSuper();
        bEdit = cGlobales.getInstance().getEditar();
        if(bEdit){
            oUser.setVisibility(View.INVISIBLE);
            lNombre.setText(sNombreSuper);
        }else{
            oUser.setVisibility(View.VISIBLE);
            lNombre.setText("Nombre");
        }
    }

    public void OnClickRechazar(View v){
        Intent intent = new Intent(this, Principal.class);
        startActivity(intent);
    }

    public void OnClickGuardar(View v){
        try {
            String sNombre = oUser.getText().toString();
            String sPass = oPass.getText().toString();
            String lNom = lNombre.getText().toString();

            if(bEdit){
                cSupervisor.Editar(lNom,sPass);
            }else{
                cSupervisor.Nuevo(sNombre,sPass);
            }
            Intent intent = new Intent(this, Supervisores.class);
            startActivity(intent);
        }catch (Exception ex){
            String sError = ex.getMessage();
        }
    }
}
