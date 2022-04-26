package com.kmftecnologia.sgspokayoke;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import dbOperacion.cGlobales;
import dbOperacion.cParametros;
import dbOperacion.cScrap;
import dbOperacion.ctTarimas;

public class Scrap extends AppCompatActivity {

    ArrayList<String> listItem;
    Button oRegresar;
    Button oExportar;
    Button oGuardar;
    EditText oCodigo;
    ListView lsDefectos;
    TextView oError;
    ArrayAdapter oAdapter;
    String sSelected;
    String sDefectos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrap);

        oRegresar = findViewById(R.id.btscrRergresar);
        oExportar = findViewById(R.id.btScrapExport);
        oGuardar = findViewById(R.id.btscGuardar);
        lsDefectos = findViewById(R.id.lsDefectos);
        oCodigo = findViewById(R.id.txtScrap);
        oError = findViewById(R.id.lblscrError);
        listItem = new ArrayList<String>();
        sDefectos = "";
        LlenaLista();

        lsDefectos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sSelected = lsDefectos.getItemAtPosition(position).toString();
                String aDefecto[] = sSelected.split(":");
                String iDefecto = aDefecto[1];
                sDefectos = sDefectos + " | " + iDefecto;
            }
        });
    }

    private void LlenaLista() {
        try{
            Cursor oDatos = cScrap.Defectos();
            String sDefecto = "";
            if(oDatos != null && oDatos.getCount() > 0){
                while (oDatos.moveToNext()){
                    sDefecto = oDatos.getString(0);
                    sDefecto = sDefecto + ": " + oDatos.getString(1);
                    listItem.add(sDefecto);
                }
                oAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,listItem);
                lsDefectos.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                lsDefectos.setAdapter(oAdapter);
            }
            else{
                oError.setText("No hay defectos!");
            }
        }
        catch(Exception ex){
            oError.setText(ex.getMessage());
        }
    }

    public void OnClickGuardarDefecto(View v){
        try{
            String sUsuario = cGlobales.getInstance().getUsuario();
            String sNP = oCodigo.getText().toString();
            if(sNP.length() > 0) {


                boolean bResultado = cScrap.Registrar(sNP, sUsuario,sDefectos);
                if(bResultado){
                    oError.setText("Cristal Registrado!");
                    oCodigo.setText("");
                }else
                {
                    oError.setText("No se registro el cristal!");
                    oCodigo.setText("");
                }

            }else{
                oError.setText("Debe escanear cristal!");
                oCodigo.setText("");

            }

        }catch(Exception ex){
            oError.setText(ex.getMessage());
        }
    }

    public void OnClickRegresar(View v){
        Intent intent = new Intent(this,Principal.class);
        startActivity(intent);
    }

    public void OnExportar(View v){
        try{
            if(checkPermisos( Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                String sPath = "/sdcard/Registros/";
                String kmfArchivo = ArmaNombreArchivo();
                String sLinea = "";
                String Fecha = "";
                String Valor = "";

                File oFile = new File(sPath, kmfArchivo);
                FileOutputStream fOut = new FileOutputStream(oFile);
                String sTexto = "Fecha;NumeroParte;Usuario;Defecto;\n";
                Cursor oCursor = cScrap.ExportarScrap();
                if(oCursor != null && oCursor.getCount() > 0){
                    while(oCursor.moveToNext()){
                        Valor = oCursor.getString(0);
                        Fecha = Valor.substring(0,4) +"/";
                        Fecha = Fecha + Valor.substring(4,6);
                        Fecha = Fecha + "/" + Valor.substring(6,8);
                        sLinea = Fecha; //Fecha
                        sLinea = sLinea + ";" + oCursor.getString(1);//NumeroParte
                        sLinea = sLinea + ";" + oCursor.getString(2);//Usuario
                        sLinea = sLinea + ";" + oCursor.getString(3);//Defecto
                        sLinea = sLinea + ";\n";
                        sTexto = sTexto + sLinea;
                    }
                }

                //Escribo en el archivo
                fOut.write(sTexto.getBytes());
                //Lo cierro
                fOut.close();
                oError.setText("Archivo guardado /sdcard/Registros/ + kmfArchivo");
            }

        }catch(Exception ex){
            oError.setText(ex.getMessage());
        }
    }

    public boolean checkPermisos (String Permiso){
        int check = ContextCompat.checkSelfPermission(this, Permiso);
        return  (check == PackageManager.PERMISSION_GRANTED);

    }


    private String ArmaNombreArchivo(){
        String sID = "SCRAP";
        Calendar oCalendario = Calendar.getInstance();
        String sValor = "";
        sValor = Integer.toString(oCalendario.get(Calendar.YEAR));
        sID = sID + sValor.substring(2);
        sValor = Integer.toString(oCalendario.get(Calendar.MONTH) + 1);
        if(sValor.length() == 1)
            sValor = "0" + sValor;
        sID = sID + sValor;
        sValor = Integer.toString(oCalendario.get(Calendar.DAY_OF_MONTH));
        if(sValor.length() == 1)
            sValor = "0" + sValor;
        sID = sID + sValor;
        sValor = Integer.toString(oCalendario.get(Calendar.HOUR_OF_DAY));
        if(sValor.length() == 1)
            sValor = "0" + sValor;
        sID = sID + sValor;
        sValor = Integer.toString(oCalendario.get(Calendar.MINUTE));
        if(sValor.length() == 1)
            sValor = "0" + sValor;
        sID = sID + sValor;
        sID = sID + ".txt";
        return  sID;
    }
}
