package com.kmftecnologia.sgspokayoke;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.ActivityCompat;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class Retraso extends AppCompatActivity {
    EditText etNumRetraso;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retraso);
        etNumRetraso = findViewById(R.id.etNumRetraso);
        int PERMISSION = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(PERMISSION != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }
    public void onClickAceptar(View v){
        //File file = new File(Environment.getExternalStorageDirectory() + "/KMF/" , "delay.txt");
        crearTxt();
    }
    private void crearTxt(){
        try{
            OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput("delay.txt", Activity.MODE_PRIVATE));
            archivo.write(etNumRetraso.getText().toString());
            archivo.flush();
            archivo.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            Toast.makeText(this, "Se agregaron " + etNumRetraso.getText().toString() + " Seg de retraso", Toast.LENGTH_SHORT).show();
        }
    }
}