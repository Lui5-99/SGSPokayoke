package com.kmftecnologia.sgspokayoke;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import dbConexion.DBManager;
import dbOperacion.cGlobales;
import dbOperacion.cSupervisor;

import static android.view.View.FOCUS_UP;

import dbConexion.DBManager;
import dbOperacion.cGlobales;

public class Users extends AppCompatActivity {
    public static Activity activity;
    DBManager oDB;
    private String sSql  = "";
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    EditText oEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        oEdit = (EditText) findViewById(R.id.txtEmpleado);

        oEdit.setText("");
        oEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(oEdit.getWindowToken(), 0);
                    entrar();
                    return true;
                }
                return false;
            }
        });
    }

    public void OnClick(View v){
        entrar();

    }

    private void entrar(){
        if(oEdit.getText().length() > 0){
            cGlobales.getInstance().setUsuario(oEdit.getText().toString());
            String sResult = cSupervisor.ValidaPass(oEdit.getText().toString());
            if(sResult.length()> 0){
                cGlobales.getInstance().setIsSuper(true);
            }else
                cGlobales.getInstance().setIsSuper(false);
            Intent intent = new Intent(this,Principal.class);
            startActivity(intent);
        }
    }

   }
