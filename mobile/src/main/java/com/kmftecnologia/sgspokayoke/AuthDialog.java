package com.kmftecnologia.sgspokayoke;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dbOperacion.cSupervisor;

public class AuthDialog {

    public interface DatosCuadroDialogo {
        void resultadoCuadroDialogo( String user );
    }

    private DatosCuadroDialogo interfaz;
    private EditText etUser;

    Dialog dialogo;

    public void CuadradoDialogo(Context context, DatosCuadroDialogo actividad) {
        interfaz = actividad;
        dialogo = new Dialog(context);
        dialogo.setTitle("Validación de supervisor");
        dialogo.setContentView(R.layout.activity_authdialog);
        etUser = dialogo.findViewById(R.id.etUser);
        dialogo.setCancelable(false);
        Button btnAuthDialog = dialogo.findViewById(R.id.btnAuthDialog);

        btnAuthDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateID(context);
            }
        });
        dialogo.show();
    }

    private void validateID(Context context) {
        String user = etUser.getText().toString();
        if (user.length() > 0) {
            String sResult = cSupervisor.ValidaPass(user);
            if(sResult.length()> 0){
                interfaz.resultadoCuadroDialogo(user);
                dialogo.dismiss();
            } else {
                Toast.makeText(context, "Supervisor no válido, intente de nuevo", Toast.LENGTH_SHORT).show();
            }
            etUser.setText("");
        }
        else
            Toast.makeText(context, "Contacte a su supervisor", Toast.LENGTH_SHORT).show();
    }
}
