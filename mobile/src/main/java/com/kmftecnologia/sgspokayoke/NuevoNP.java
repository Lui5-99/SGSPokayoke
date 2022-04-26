package com.kmftecnologia.sgspokayoke;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import dbOperacion.ctNumerosParte;
import dbOperacion.cGlobales;

public class NuevoNP extends AppCompatActivity {
    EditText oNP;
    EditText oVersion;
    EditText oSAP;
    EditText oPiezas;
    EditText oPiezasCharola;
    EditText oSAPEnc;
    TextView oError;
    CheckBox oActivo;
     EditText oCamas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_np);

        oNP = findViewById(R.id.txteNP);
        oVersion = findViewById(R.id.txteVersion);
        oSAP =  findViewById(R.id.txteSAP);
        oSAPEnc =  findViewById(R.id.txteSAPEnc);
        oPiezasCharola = findViewById(R.id.txtePCharola);
        oPiezas = findViewById(R.id.txtePiezas);
        oActivo = findViewById(R.id.chkeActivo);
        oCamas = findViewById(R.id.txtCamas);
        oError = findViewById(R.id.lbleNPError);
        
        Boolean bBandera = cGlobales.getInstance().getEditar();
        if(bBandera){
            //Edición
            LlenaCampos();
        }
    }

    private void LlenaCampos() {
        try{
            String sNP = cGlobales.getInstance().getEditIdNumeroParte();
            boolean bEmpaque = false;
            //if(sNP.contains("*")) {
            //    bEmpaque = true;
            //    sNP = sNP.substring(1);
            //}
            Cursor rsNP = ctNumerosParte.NumeroParte(sNP);
            if(rsNP != null && rsNP.getCount()>0){
                rsNP.moveToFirst();
                oNP.setText(rsNP.getString(0));
                oVersion.setText(rsNP.getString(5));
                oSAP.setText(rsNP.getString(6));
                oPiezasCharola.setText(rsNP.getString(3));
                oPiezas.setText(rsNP.getString(4));
                boolean value = rsNP.getInt(7) > 0;
                oSAPEnc.setText(rsNP.getString(8));
                oCamas.setText(rsNP.getString(1));
                oActivo.setChecked(value);
            }
        }catch(Exception ex){
            oError.setText(ex.getMessage());
        }
    }

    public void OnClick(View v){
        try{
            Boolean bBandera = cGlobales.getInstance().getEditar();
            String sNP = oNP.getText().toString();
            String sVersion = oVersion.getText().toString();
            String sSAP = oSAP.getText().toString();
            String sSAPEnc = oSAPEnc.getText().toString();
            int iCamas = Integer.parseInt(oCamas.getText().toString());
            int iPiezasCharolas = Integer.parseInt(oPiezasCharola.getText().toString());
            int iPiezas= Integer.parseInt(oPiezas.getText().toString());
            int iActivo = 1;

            if(oActivo.isChecked())
                iActivo = 1;
            else
                iActivo = 0;

            if(bBandera){
                //Edición
                ctNumerosParte.Editar(sNP,sVersion,sSAP,iPiezasCharolas,iPiezas,iActivo,sSAPEnc,iCamas);
            }else{
                //Nuevo
                ctNumerosParte.Nuevo(sNP,sVersion,sSAP,iPiezasCharolas,iPiezas,sSAPEnc,iCamas);
            }
            Intent intent = new Intent(this,Catalogo.class);
            startActivity(intent);
        }catch(Exception ex){
            oError.setText(ex.getMessage());
        }

    }
}
