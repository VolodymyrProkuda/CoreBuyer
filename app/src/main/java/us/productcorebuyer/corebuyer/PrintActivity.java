package us.productcorebuyer.corebuyer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;

import com.zebra.sdk.printer.PrinterLanguage;
import com.zebra.sdk.printer.PrinterStatus;
import com.zebra.sdk.printer.SGD;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;
import com.zebra.sdk.printer.ZebraPrinterLinkOs;

import java.util.ArrayList;
import java.util.List;

public class PrintActivity extends AppCompatActivity {

    private Connection connection;

    private Button testButton;
    private ZebraPrinter printer;
    private TextView statusField;
    private TextView textViewFields;

    private String paymentMethodGet = "";

    private String data2_ein_number = "";
    private String data2_seller_namer = "";
    private String data2_email = "";
    private String data2_tell = "";
    private String data2_adress = "";
    private String data2_company_name = "";
    private String data2_buyer_name = "";
    private ArrayList<String> itemOrderPrices;
    private ArrayList<Integer> itemOrderQuantities;
    private String totalQuantity = "";
    private String totalPrice = "";
    private String timeAndDateNow = "";
    private String locationString = "";
    private ArrayList<String>  notesString;

    private static final String bluetoothAddressKey = "ZEBRA_DEMO_BLUETOOTH_ADDRESS";
    private static final String tcpAddressKey = "ZEBRA_DEMO_TCP_ADDRESS";
    private static final String tcpPortKey = "ZEBRA_DEMO_TCP_PORT";
    private static final String PREFS_NAME = "OurSavedAddress";



    private static final String DISCLAIMER1 = "ARC Recycling, LLC";
    private static final String DISCLAIMER2 = "1130-1132 N Godfrey Street";
    private static final String DISCLAIMER3 = "ALLENTOWN, PA, 18109";
    private static final String DISCLAIMER4 = "(201)-647-6376";
    private static final String DISCLAIMER5 = "Buyer: Roman";
    private static final String DISCLAIMER6 = "Arcrecycling2014@gmail.com";

/*

    private static final String DISCLAIMER1 = "Terrikon inc";
    private static final String DISCLAIMER2 = "Buyer: Anton";
    private static final String DISCLAIMER3 = "150 Langham st,";
    private static final String DISCLAIMER4 = "Brooklyn, NY, 11235";
    private static final String DISCLAIMER5 = "201 561 - 1410";
    private static final String DISCLAIMER6 = "Terrikon.inc@gmail.com";
*/
    /*
    private static final String DISCLAIMER1 = "Ev Logistics Inc";
    private static final String DISCLAIMER2 = "1230 Ave X, Apt 2B";
    private static final String DISCLAIMER3 = "Brooklyn, NY, 11235";
    private static final String DISCLAIMER4 = "Buyer: Evgeniy";
    private static final String DISCLAIMER5 = "6466335420";
    private static final String DISCLAIMER6 = "evgeniy39mm@gmail.com";
*/

    private static final String DISCLOSURE1 = "This is a Bill of Sale scrap metal. I state that I am the legal";
    private static final String DISCLOSURE2 = "owner of described scrap metal and that I have the authority to";
    private static final String DISCLOSURE3 = "sell it to the buyer I hereby acknowledge that I have received";
    private static final String DISCLOSURE4 = "the sum stated above as payment is full.";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        statusField = (TextView) this.findViewById(R.id.statusText);
        testButton = (Button) this.findViewById(R.id.testButton);


        Intent intentFromProdAct = getIntent();
        data2_ein_number = intentFromProdAct.getStringExtra("data_ein_number").toString();
        data2_seller_namer = intentFromProdAct.getStringExtra("data_seller_namer").toString();
        data2_email = intentFromProdAct.getStringExtra("data_email").toString();
        data2_tell = intentFromProdAct.getStringExtra("data_tell").toString();
        data2_adress = intentFromProdAct.getStringExtra("data_adress").toString();
        data2_company_name = intentFromProdAct.getStringExtra("data_company_name").toString();
        data2_buyer_name = intentFromProdAct.getStringExtra("data_buyer_name").toString();

        itemOrderPrices = intentFromProdAct.getStringArrayListExtra("itemOrderPrice");
        itemOrderQuantities = intentFromProdAct.getIntegerArrayListExtra("itemOrderQuantity");

        totalQuantity = intentFromProdAct.getStringExtra("Total Quantity").toString();
        totalPrice = intentFromProdAct.getStringExtra("Total Price").toString();
        timeAndDateNow = intentFromProdAct.getStringExtra("Time And Date").toString();
        locationString = intentFromProdAct.getStringExtra("GeoLocation").toString();
        notesString = intentFromProdAct.getStringArrayListExtra("notesString");

        paymentMethodGet = intentFromProdAct.getStringExtra("paymentMethodGet").toString();

        new Thread(new Runnable() {
            public void run() {
                enableTestButton(false);
                doConnectionTest();
            }
        }).start();


        testButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

/*
        testButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
            }
        });

*/




    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/


    public ZebraPrinter connect() {
        setStatus("Connecting...", Color.YELLOW);
        connection = null;
        connection = new BluetoothConnection("80:6F:B0:CE:48:97");

        try {
            connection.open();
            setStatus("Connected", Color.GREEN);
        } catch (ConnectionException e) {
            setStatus("Comm Error! Disconnecting", Color.RED);
            DemoSleeper.sleep(1000);
            disconnect();
        }

        ZebraPrinter printer = null;

        if (connection.isConnected()) {
            try {

                printer = ZebraPrinterFactory.getInstance(connection);
                setStatus("Determining Printer Language", Color.YELLOW);
                String pl = SGD.GET("device.languages", connection);
                setStatus("Printer Language " + pl, Color.BLUE);
            } catch (ConnectionException e) {
                setStatus("Unknown Printer Language", Color.RED);
                printer = null;
                DemoSleeper.sleep(1000);
                disconnect();
            } catch (ZebraPrinterLanguageUnknownException e) {
                setStatus("Unknown Printer Language", Color.RED);
                printer = null;
                DemoSleeper.sleep(1000);
                disconnect();
            }
        }

        return printer;
    }

    public void disconnect() {
        try {
            setStatus("Disconnecting", Color.RED);
            if (connection != null) {
                connection.close();
            }
            setStatus("Not Connected", Color.RED);
        } catch (ConnectionException e) {
            setStatus("COMM Error! Disconnected", Color.RED);
        } finally {
            enableTestButton(true);
        }
    }

    private void setStatus(final String statusMessage, final int color) {
        runOnUiThread(new Runnable() {
            public void run() {
                statusField.setBackgroundColor(color);
                statusField.setText(statusMessage);
            }
        });
        DemoSleeper.sleep(1000);
    }


    private void sendTestLabel() {
        try {
            ZebraPrinterLinkOs linkOsPrinter = ZebraPrinterFactory.createLinkOsPrinter(printer);

            PrinterStatus printerStatus = (linkOsPrinter != null) ? linkOsPrinter.getCurrentStatus() : printer.getCurrentStatus();

            if (printerStatus.isReadyToPrint) {
                byte[] configLabel = getConfigLabel();
                connection.write(configLabel);
                setStatus("Sending Data", Color.BLUE);
            } else if (printerStatus.isHeadOpen) {
                setStatus("Printer Head Open", Color.RED);
            } else if (printerStatus.isPaused) {
                setStatus("Printer is Paused", Color.RED);
            } else if (printerStatus.isPaperOut) {
                setStatus("Printer Media Out", Color.RED);
            }
            DemoSleeper.sleep(1500);
            if (connection instanceof BluetoothConnection) {
                String friendlyName = ((BluetoothConnection) connection).getFriendlyName();
                setStatus(friendlyName, Color.MAGENTA);
                DemoSleeper.sleep(500);
            }
        } catch (ConnectionException e) {
            setStatus(e.getMessage(), Color.RED);
        } finally {
            disconnect();
        }
    }

    private void enableTestButton(final boolean enabled) {
        runOnUiThread(new Runnable() {
            public void run() {
                testButton.setEnabled(enabled);
            }
        });
    }


    private byte[] getConfigLabel() {
        byte[] configLabel = null;
        try {
            PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();
            SGD.SET("device.languages", "zpl", connection);

            if (printerLanguage == PrinterLanguage.ZPL) {
                String edt = "";
                configLabel = formString().getBytes();
            } else if (printerLanguage == PrinterLanguage.CPCL) {
                String cpclConfigLabel = "difenitley\r\n" + "WORKS\r\n" + "!!!!!\r\n";
                configLabel = cpclConfigLabel.getBytes();
            }
        } catch (ConnectionException e) {

        }
        return configLabel;
    }



    private void doConnectionTest() {
        printer = connect();

        if (printer != null) {
            sendTestLabel();
        } else {
            disconnect();
        }
    }

    private String formString() {
        String fS = "";
        int printPosition = 140;
        fS +=    "^XA^FO500,"+printPosition+"^A0,32,25^FD"+DISCLAIMER1;
        printPosition+=40;
        fS +=       "^FS^FO500,"+printPosition+"^A0,32,25^FD"+DISCLAIMER2;
        printPosition+=40;
        fS +=       "^FS^FO500,"+printPosition+"^A0,32,25^FD"+DISCLAIMER3;
        printPosition+=40;
        fS +=       "^FS^FO500,"+printPosition+"^A0,32,25^FD"+DISCLAIMER4;
        printPosition+=40;
        fS +=       "^FS^FO500,"+printPosition+"^A0,32,25^FD"+DISCLAIMER5;
        printPosition+=40;
        fS +=       "^FS^FO500,"+printPosition+"^A0,32,25^FD"+DISCLAIMER6;
        printPosition+=40;
        printPosition+=40;
        fS +=       "^FS^FO50,"+printPosition+"^A0,32,25^FDSeller information:";
        printPosition+=40;
        fS +=       "^FS^FO50,"+printPosition+"^A0,32,25^FDCustomer Name: "+data2_buyer_name;
        printPosition+=40;
        fS +=       "^FS^FO50,"+printPosition+"^A0,32,25^FDCompany Name: "+data2_company_name;
        printPosition+=40;
        fS +=       "^FS^FO50,"+printPosition+"^A0,32,25^FDAddress: "+data2_adress;
        printPosition+=40;
        fS +=       "^FS^FO50,"+printPosition+"^A0,32,25^FDPhone: "+data2_tell;
        printPosition+=40;
        fS +=       "^FS^FO50,"+printPosition+"^A0,32,25^FDE-mail: "+data2_email;
        printPosition+=40;
        fS +=       "^FS^FO50,"+printPosition+"^A0,32,25^FDSeller Name: "+data2_seller_namer;
        printPosition+=40;
        fS +=       "^FS^FO50,"+printPosition+"^A0,32,25^FDEIN NUMBER: "+data2_ein_number;
        printPosition+=40;
        printPosition+=40;


        for (int i = 1; i < itemOrderQuantities.size(); i++) {
            Integer ii = i;
            Integer q = itemOrderQuantities.get(i);
            String p = itemOrderPrices.get(i);
            fS +="^FS^FO10,"+printPosition+"^ADN,36,20^FD"+ii.toString()+") "+q.toString() + " units " + p.toString();
            printPosition+=50;
        }
        fS += "^FS^FO50,"+printPosition+"^ADN,36,20^FD___________________";
        printPosition+=50;
        fS += "^FS^FO50,"+printPosition+"^ADN,36,20^FDTotal Quantity: "+totalQuantity;
        printPosition+=50;
        fS += "^FS^FO50,"+printPosition+"^ADN,36,20^FDTotal Price: $"+totalPrice;
        printPosition+=50;
        fS += "^FS^FO50,"+printPosition+"^ADN,36,20^FDPayment method: "+paymentMethodGet;
        printPosition+=50;
        fS += "^FS^FO50,"+printPosition+"^ADN,18,10^FD"+"----------------------------";
        printPosition+=30;
        fS += "^FS^FO50,"+printPosition+"^ADN,18,10^FD"+"Notes :";
        printPosition+=30;
      
        for (int i = 0; i < notesString.size(); i++) {
            //Integer ii = i;
            String p = notesString.get(i);
            fS +="^FS^FO50,"+printPosition+"^ADN,18,10^FD"+p.toString();
            printPosition+=30;
        }
        fS += "^FS^FO50,"+printPosition+"^ADN,18,10^FD"+"----------------------------";
        printPosition+=30;

        fS += "^FS^FO50,"+printPosition+"^ADN,18,10^FD"+DISCLOSURE1;
        printPosition+=30;
        fS += "^FS^FO50,"+printPosition+"^ADN,18,10^FD"+DISCLOSURE2;
        printPosition+=30;
        fS += "^FS^FO50,"+printPosition+"^ADN,18,10^FD"+DISCLOSURE3;
        printPosition+=30;
        fS += "^FS^FO50,"+printPosition+"^ADN,18,10^FD"+DISCLOSURE4;
        printPosition+=80;
        fS += "^FS^FO50,"+printPosition+"^A0,32,25^FDSignature:                   Date/Time: "+timeAndDateNow;
        printPosition+=50;
        fS += "^FS^FO320,"+printPosition+"^A0,32,25^FDGeolocation: "+locationString;
        printPosition+=50;

        fS +=  "^FS^XZ";

        return fS;

    }




}