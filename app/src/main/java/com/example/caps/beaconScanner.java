package com.example.caps;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.WriteResult;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

public class beaconScanner extends AppCompatActivity {
    BluetoothManager btManager;
    BluetoothAdapter btAdapter;
    BluetoothLeScanner btScanner;
    Button startScanningButton;
    Button stopScanningButton;
    TextView textView;
    private final static int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private BluetoothSPP bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_2);
        Button bibi  = findViewById(R.id.bibi);
        bibi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(beaconScanner.this,MainActivity.class);
                startActivity(intent);

                finish();
            }
        });

        //peripheralTextView = (TextView) findViewById(R.id.PeripheralTextView);
        textView = (TextView)findViewById(R.id.textView1);
        // peripheralTextView.setMovementMethod(new ScrollingMovementMethod());

        startScanningButton = (Button) findViewById(R.id.StartScanButton);
        startScanningButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startScanning();
            }
        });

        stopScanningButton = (Button) findViewById(R.id.StopScanButton);
        stopScanningButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopScanning();
            }
        });
        stopScanningButton.setVisibility(View.INVISIBLE);

        btManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();
        btScanner = btAdapter.getBluetoothLeScanner();


        if (btAdapter != null && !btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
        }

        // Make sure we have access coarse location enabled, if not, prompt the user to enable it
        if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("This app needs location access");
            builder.setMessage("Please grant location access so this app can detect peripherals.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                }
            });
            builder.show();
        }
    }

    // Device scan callback.
    private final ScanCallback leScanCallback = new ScanCallback() {

        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            TextView textView = (TextView) findViewById(R.id.textView1);
            //FirebaseFirestore database = FirebaseFirestore.getInstance();
            //System.out.println("devicename "+result.getDevice().getName()+result);
            System.out.println("device " + result.getDevice().getName());
            System.out.println("deviceresult123 " + result.getDevice().toString());
            if (result.getDevice().toString().equals("EB:FD:53:17:5C:D3")) {
                //textView.setText("rssi 1 (1) " + result.getRssi());
                if(result.getRssi() > -60)
                {
                    textView.setText("rssi 1 (1) " + result.getRssi());
                    System.out.println("check");
                    AlertDialog.Builder alert = new AlertDialog.Builder(beaconScanner.this);

                    alert.setTitle("앱을 종료하시겠습니까?");
                    alert.setIcon(R.drawable.apple);
                    alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 액티비티 종료
                            finish();
                        }
                    });

                    alert.setNegativeButton("no", null);

                    // 이 alert dialog 의 버튼을
                    // 안누르면 화면이 넘어가지 않도록 하는 방법
                    alert.setCancelable(false);
                    // alert dialog 를 화면에 표시
                    alert.show();

                }
            }

//            if (result.getDevice().getName() != null && result.getDevice().getName().equals("PSM_SHT20")) {
//                textView.setText("rssi 1 " + result.getRssi());
//
//            }
        }

        ;
        //textView.setText("rssi 1 "+result.getRssi());
            /*if(result.getDevice().getName()!=null && result.getDevice().getName().equals("Plutocon111"))
            {
                textView.setText("rssi 1 "+result.getRssi());
                HashMap<String,Object> student = new HashMap<>();
                student.put("1p",result.getRssi());
                textView.setText("rssi 1 "+result.getRssi());
                database.collection("beacon").document("beacon").update(student)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                //peripheralTextView.append("Device: " + result.getDevice().getName() + " rssi: " + result.getRssi() + "\n" );
                textView.setText("rssi 1 " + result.getRssi());

            }
            if(result.getDevice().getName()!=null && result.getDevice().getName().equals("Plutocon222"))
            {

                HashMap<String,Object> stu = new HashMap<>();
                stu.put("2p",result.getRssi());
                database.collection("beacon").document("beacon").update(stu)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TextView textView = (TextView)findViewById(R.id.textView2);
                        textView.setText("rssi 2 " + result.getRssi());
                        //peripheralTextView.append("Device: " + result.getDevice().getName() + " rssi: " + result.getRssi() + "\n" );
                    }
                },5000);

            }
            if(result.getDevice().getName()!=null && result.getDevice().getName().equals("Plutocon333"))
            {


                HashMap<String,Object> stu = new HashMap<>();
                stu.put("3p",result.getRssi());
                database.collection("beacon").document("beacon").update(stu)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TextView textView = (TextView)findViewById(R.id.textView4);
                        textView.setText("rssi 3 " + result.getRssi());
                        //peripheralTextView.append("Device: " + result.getDevice().getName() + " rssi: " + result.getRssi() + "\n" );
                    }
                },5000);

            }
            if(result.getDevice().getName()!=null && result.getDevice().getName().equals("Plutocon444"))
            {

                HashMap<String,Object> stu = new HashMap<>();
                stu.put("4p",result.getRssi());
                database.collection("beacon").document("beacon").update(stu)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TextView textView = (TextView)findViewById(R.id.textView5);
                        textView.setText("rssi 4 " + result.getRssi());
                        // peripheralTextView.append("Device: " + result.getDevice().getName() + " rssi: " + result.getRssi() + "\n" );
                    }
                },5000);
            }
            // auto scroll for text view
            //  final int scrollAmount = peripheralTextView.getLayout().getLineTop(peripheralTextView.getLineCount()) - peripheralTextView.getHeight();
            // if there is no need to scroll, scrollAmount will be <=0
            //  if (scrollAmount > 0)
            //   peripheralTextView.scrollTo(0, scrollAmount);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_COARSE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("coarse location permission granted");
            } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Functionality limited");
                builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface dialog) {
                    }

                });
                builder.show();
            }
        }
    }*/
    };

    public void startScanning() {
        System.out.println("start scanning");
//        peripheralTextView.setText("");
        startScanningButton.setVisibility(View.INVISIBLE);
        stopScanningButton.setVisibility(View.VISIBLE);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                btScanner.startScan(leScanCallback);
            }
        });

    }


    public void stopScanning() {
        //System.out.println("stopping scanning");
        // peripheralTextView.append("Stopped Scanning");
        startScanningButton.setVisibility(View.VISIBLE);
        stopScanningButton.setVisibility(View.INVISIBLE);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                if(btScanner !=null) {
                    btScanner.stopScan(leScanCallback);
                }
            }
        });
    }
};
