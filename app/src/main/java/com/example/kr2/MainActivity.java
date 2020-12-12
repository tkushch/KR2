package com.example.kr2;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


public class MainActivity extends Activity implements View.OnClickListener {
    private Button choose_file, clear;
    private TextView tv;
    private EditText et, etname;
    private Uri uri = null;
    private CheckBox chb;

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        choose_file = findViewById(R.id.choose_file);
        choose_file.setOnClickListener(this);
        tv = findViewById(R.id.tv);
        et = findViewById(R.id.et);
        chb = findViewById(R.id.chb);
        etname = findViewById(R.id.etname);
        clear = findViewById(R.id.clear);
        clear.setOnClickListener(this);
        try {
            String x = "", usname = "";
            File check_box = new File(getDataDir(), "check_box.txt");
            Scanner in = new Scanner(check_box);
            x = in.next();
            if (x == "1"){
                x = "Текст файла";
                usname = "user_name";
            }
            else{
                x = "";
                File file = new File(getDataDir(), "memory.txt");
                in = new Scanner(file);
                boolean first = true;
                while (in.hasNext()) {
                    if (first){
                        first = false;
                        usname = in.nextLine();
                    }
                    else {
                        x += in.nextLine() + "\n";
                    }
                }
                et.setText(x);
                etname.setText(usname);
                in.close();
            }
            etname.setText(usname);
            et.setText(x);
        } catch (FileNotFoundException e) {
            System.out.println("Памяти пока нет");
        }

    }


    @Override
    public void onClick(View v) {
        if (v == choose_file) {
            Intent chooser_intent = new Intent(Intent.ACTION_GET_CONTENT);
            chooser_intent.setType("text/plain");
            chooser_intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(chooser_intent, 1);
        }
        if (v == clear){
            et.setText("");
            etname.setText("");
            chb.setChecked(false);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1: {
                if (resultCode == RESULT_OK) {
                    //1
                    String path = data.getData().getPath();
                    String[] s = path.split("/");
                    tv.setText(s[s.length - 1]);

                    //2
                    setUri(data.getData());
                    try {
                        String x = "";
                        int c = 0;
                        Scanner scanner = new Scanner(getContentResolver().openInputStream(getUri()));
                        while (scanner.hasNext()) {
                            x += scanner.nextLine() + "\n";
                        }
                        scanner.close();
                        et.setText(x);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onPause() {
        String x, usname, isch;

        // 1 (do we need to save all?)
        if (!chb.isChecked()) {
            usname = "user_name";
            x = "Текст файла";
            isch = "0";
        } else {
            isch = "1";
            usname = etname.getText().toString();
            x = et.getText().toString();
        }

        // 2  (saving or clearing all)
        File file = new File(getDataDir(), "memory.txt");
        File check_box = new File(getDataDir(), "check_box.txt");

        try {
            PrintWriter pv = new PrintWriter(new FileWriter(file));
            pv.write(usname + "\n" + x);
            pv.close();

            pv = new PrintWriter(new FileWriter(check_box));
            pv.write(isch);
            pv.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onPause();
    }

}


// вариант 2
                    /*BufferedReader br = null;
                    try {
                        String x = "";
                        br = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(uri)));
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            x += line + "\n";
                        }
                        br.close();
                        et.setText(x);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;*/


/*
try {
                 OutputStream outputStream = getContentResolver().openOutputStream(getUri());
                outputStream.write(Byte.decode(x));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
 */