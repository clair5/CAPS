package com.example.caps;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

import com.bumptech.glide.Glide;
public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private int[] data;

    BluetoothManager btManager;
    BluetoothAdapter btAdapter;
    BluetoothLeScanner btScanner;
    Button startScanningButton;
    Button stopScanningButton;
    TextView textView;
    ImageView img;
    private final static int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private BluetoothSPP bt;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView img2 = (ImageView)findViewById(R.id.food_gif_img);
        img2.setVisibility(View.INVISIBLE);


//        Glide.with(this).load(R.raw.food).into(food_gif_img);


        //peripheralTextView = (TextView) findViewById(R.id.PeripheralTextView);
        textView = (TextView) findViewById(R.id.textView3);

        TextView textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setVisibility(View.INVISIBLE);
        // peripheralTextView.setMovementMethod(new ScrollingMovementMethod());

        startScanningButton = (Button) findViewById(R.id.startScanningButton);
        startScanningButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startScanning();
            }
        });

        stopScanningButton = (Button) findViewById(R.id.stopScanningButton);
        stopScanningButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopScanning();
            }
        });
        stopScanningButton.setVisibility(View.INVISIBLE);

        btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();
        btScanner = btAdapter.getBluetoothLeScanner();


        if (btAdapter != null && !btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
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

        @SuppressLint({"SetTextI18n", "ResourceType"})
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            ImageView img = (ImageView)findViewById(R.id.food_gif_img);
            TextView textView = (TextView) findViewById(R.id.textView3);
            TextView textView2 = (TextView) findViewById(R.id.textView2);


            System.out.println("device " + result.getDevice().getName());
            System.out.println("deviceresult123 " + result.getDevice().toString());
            if (result.getDevice().toString().equals("EB:FD:53:17:5C:D3")) {
                // if문 했을때 메세지 만들면 되니까 ok 만들고 ok-> 다른페이지로 넘어가게 만들면되지
                // ok 생성
                // String name =
                textView.setText("RSSI  " + result.getRssi());
                //textView.setText("rssi 1 (1) " + result.getRssi());
                if (result.getRssi() > -50) {
                    textView.setText("RSSI " + result.getRssi());
                    img.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.VISIBLE);




                } else if (result.getRssi() >-60) {
                    textView.setText("RSSI" + result.getRssi());
                    textView2.setVisibility(View.VISIBLE);
                }


                else {
                    img.setVisibility(View.INVISIBLE);
                    textView2.setVisibility(View.INVISIBLE);

                }

//            if (result.getDevice().getName() != null && result.getDevice().getName().equals("PSM_SHT20")) {
//                textView.setText("rssi 1 " + result.getRssi());
//
//            }
            }
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
        // ok 메세지를 받아서 다음으로 넘어가게 만들기
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
                if (btScanner != null) {
                    btScanner.stopScan(leScanCallback);
                }
            }
        });
    }

};






