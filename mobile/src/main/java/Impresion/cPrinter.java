package Impresion;
/*
//////////////////////////////////////////////////////////////////////
KMF Tecnología - Saint Gobain Sekurit
Desarrolló:  Joaquín Andrés García Gutierrez
Fecha:       08/01/2018
Descripción: Clase para Impresión
https://www.androidcode.ninja/android-bluetooth-tutorial/
//////////////////////////////////////////////////////////////////////
*/
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import dbConexion.DBManager;

public class cPrinter extends Activity {
    // android built in classes for bluetooth operations
    public BluetoothAdapter mBluetoothAdapter;
    public BluetoothSocket mmSocket;
    public BluetoothDevice mmDevice;
    private static DBManager oDB;
    private static String sSql  = "";

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    private String vMensaje;
    public void Mensaje (String Cadena){vMensaje = Cadena;}
    public String Mensaje(){return vMensaje;}

    // this will find a bluetooth printer device
    public int findBT() {
        int iReturn = 0;
        String sImpresora = GetPrinter();
        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if(mBluetoothAdapter == null) {
                iReturn = -99;
                //myLabel.setText("No bluetooth adapter available");
            }

            if(!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    if (device.getName().equals(sImpresora)) {
                        mmDevice = device;
                        break;
                    }
                }
            }
            iReturn = 1;

        }catch(Exception e){
            vMensaje = e.getMessage();
            iReturn = -1;
        }
        return  iReturn;
    }

    // tries to open a connection to the bluetooth printer device
    public int openBT() throws IOException {
        int iReturn = 0;
        try {

            // Standard SerialPortService ID
            String sUUID = PuertoBlueTooth();
            //UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            UUID uuid = UUID.fromString(sUUID);

            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();
            iReturn = 1;
            beginListenForData();

        } catch (IOException e) {
            e.printStackTrace();
            iReturn = -1;
        }
        return  iReturn;
    }

    private String PuertoBlueTooth(){
        String sReturn = "";
        try{
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            Method getUuidsMethod = BluetoothAdapter.class.getDeclaredMethod("getUuids", null);
            ParcelUuid[] uuids = (ParcelUuid[]) getUuidsMethod.invoke(adapter, null);

            if(uuids != null) {
                for (ParcelUuid uuid : uuids) {
                    sReturn = uuid.getUuid().toString();
                }
            }else{
                sReturn = "0";
            }
        }catch(Exception ex){
            sReturn = "-1";
        }
        return sReturn;
    }

    /*
     * after opening a connection to bluetooth printer device,
     * we have to listen and check if a data were sent to be printed.
     */
    private void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                        try {
                            int bytesAvailable = mmInputStream.available();
                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);
                                for (int i = 0; i < bytesAvailable; i++) {
                                    byte b = packetBytes[i];
                                    if (b == delimiter) {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );
                                       // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;
                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                                //myLabel.setText(data);
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // this will send text data to be printed by the bluetooth printer
    public int sendData(String sLabel) throws IOException {
        int iReturn = 0;
        try {
            /*
            findBT();
            String sName = mmDevice.getName();
            boolean bResult = connectToPrinter(sName);*/
            if(mmOutputStream != null) {
                mmOutputStream.write(sLabel.getBytes());
                iReturn = 1;
            }else
            {
                iReturn = -2;
                //closeBT();
            }


/*
            findBT();
            String sName = mmDevice.getName();
            boolean bResult = connectToPrinter(sName);
            openBT();
            if(bResult) {

                mmOutputStream.write(sLabel.getBytes());
                iReturn =1;
                closeBT();
            }
            else
            {
                iReturn =10;
                closeBT();
                findBT();
                 sName = mmDevice.getName();
                 bResult = connectToPrinter(sName);
                 mmOutputStream.write(sLabel.getBytes());
                iReturn =11;
            }
            //openBT();
           //Mando a imprimir
            //mmOutputStream.write(sLabel.getBytes());
*/
        } catch (Exception e) {
            e.printStackTrace();
            iReturn = -1;
        }

        return  iReturn;
    }

    public int PrintData(String sLabel) throws IOException {
        int iReturn = 0;
        try {


            //Mando a imprimir
            mmOutputStream.write(sLabel.getBytes());
            iReturn =1;

        } catch (Exception e) {
            e.printStackTrace();
            iReturn = -1;
        }
        return  iReturn;
    }

    // close the connection to bluetooth printer.
    public void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean connectToPrinter(String printerName) throws IOException
    {
        boolean bReturn = false;

        try {

            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
            BluetoothDevice device = mmDevice; // getPrinterByName(printerName);

            if (mmSocket != null) {
                mmSocket.close();
            }
            try {
                Method m = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                mmSocket = (BluetoothSocket) m.invoke(device, 1);
            } catch (Exception e) {
                e.printStackTrace();
                bReturn = false;
            }
            if (mmSocket == null)
                return false;

            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();
            beginListenForData();
            bReturn = true;

        }catch(IOException ex){
           bReturn = false;
        }
        return bReturn;
    }

    public Boolean SavePrnter(String Nombre, String MacAddress){
        boolean bReturn = false;
        try
        {
            oDB = DBManager.instance();
            sSql = "Update catImpresora set bActivo = 0";
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            sSql = "Insert into catImpresora (cImpresora, cMacAddress, bActivo) Values('" +
            Nombre + "','" + MacAddress + "',1)";
            oCursor = oDB.ExecuteSQL(sSql);
            if(oCursor.getCount() > 0 && oCursor != null) {
                bReturn = true;
            }
            bReturn = true;
        }
        catch(Exception ex)
        {
            bReturn = false;
        }
        return bReturn;
    }

    public String GetPrinter()
    {
        String sReturn = "";
        try
        {
            oDB = DBManager.instance();
            sSql = "select cImpresora from catImpresora where bActivo = 1";
            Cursor oCursor = oDB.ExecuteSQL(sSql);
            if(oCursor.getCount() > 0 && oCursor != null) {
                oCursor.moveToFirst();
                sReturn = oCursor.getString(0);
            }
        }
        catch(Exception ex)
        {
            sReturn = "";
        }
        return sReturn;
    }

}
