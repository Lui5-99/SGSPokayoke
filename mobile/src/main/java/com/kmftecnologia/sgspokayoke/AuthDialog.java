package com.kmftecnologia.sgspokayoke;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AuthDialog {

    public interface DatosCuadroDialogo {
        void resultadoCuadroDialogo( String user );
    }

    private DatosCuadroDialogo interfaz;

    public void CuadradoDialogo(Context context, DatosCuadroDialogo actividad) {
        interfaz = actividad;
        final Dialog dialogo = new Dialog(context);
        dialogo.setTitle("Validación de supervisor");
        dialogo.setContentView(R.layout.activity_authdialog);
        final EditText etUser = dialogo.findViewById(R.id.etUser);
        Button btnAuthDialog = dialogo.findViewById(R.id.btnAuthDialog);

        btnAuthDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = etUser.getText().toString();
                interfaz.resultadoCuadroDialogo(user);
                dialogo.dismiss();
            }
        });
        dialogo.show();
    }
}
