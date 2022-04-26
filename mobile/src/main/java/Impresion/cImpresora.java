package Impresion;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.kmftecnologia.sgspokayoke.Bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


public class cImpresora extends AppCompatActivity {

    BluetoothAdapter obtAdapter;
    BluetoothSocket obtSocket;
    BluetoothDevice obtDevice;

    OutputStream oOutputStream;
    InputStream InputStream;
    Thread thread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    private String vMensaje;
    public void Mensaje (String Cadena){vMensaje = Cadena;}
    public String Mensaje(){return vMensaje;}


    int FindBluetooth() {
        int iReturn = 0;
        try {
            obtAdapter = BluetoothAdapter.getDefaultAdapter();
            if (obtAdapter == null) {
                vMensaje = "No encontró Dispositivo bluetooth";
                iReturn = -1;
            }
            if (obtAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }
            Set<BluetoothDevice> pairDevice = obtAdapter.getBondedDevices();

        } catch (Exception ex) {

        }
        return  iReturn;
    }
}
