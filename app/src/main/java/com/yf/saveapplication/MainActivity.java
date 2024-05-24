package com.yf.saveapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button myButton = findViewById(R.id.io_button);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.editTextTextMultiLine);
                String filename = "myfile.txt";
                String fileContents = editText.getText().toString();
                saveText(fileContents, filename);


            }
        });

        Button myButton2 = findViewById(R.id.io_button2);
        myButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                String filename = "myfile.txt";
                EditText editText = findViewById(R.id.editTextTextMultiLine2);
                editText.setText(loadText(filename));

            }
        });

        myButton = findViewById(R.id.io_button3);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.editTextTextPersonName);
                Sample sample = new Sample(3, editText.getText().toString());
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
                try {
                    String json = mapper.writeValueAsString(sample);
                    saveText(json, "sample.txt");
                    EditText editText2 = findViewById(R.id.editTextTextMultiLine2);
                    editText2.setText(json);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        myButton = findViewById(R.id.io_button4);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                String value = loadText("sample.txt");
                ObjectMapper mapper = new ObjectMapper();
                TextView textView = findViewById(R.id.textView);
                try {
                    Sample sample = mapper.readValue(value, Sample.class);
                    textView.setText(sample.toString());
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        myButton = findViewById(R.id.io_button5);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] files = fileList();
                String line=System.getProperty("line.separator");
                StringBuilder sb=new StringBuilder();
                for (String file : files) {
                    sb.append(file).append(line);
                }

                EditText editText = findViewById(R.id.editTextTextMultiLine2);
                editText.setText(sb.toString());
            }
        });

    }

    private String loadText(String filename) {
        Context context = getApplicationContext();
        FileInputStream fis = null;
        try {
            fis = openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            fis.close();
            Toast.makeText(context, "読み込みました", Toast.LENGTH_LONG).show();
            return sb.toString();

        } catch (FileNotFoundException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            return e.getMessage();
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            return e.getMessage();
        }

    }

    private void saveText(String value, String filename) {
        FileOutputStream fos = null;
        Context context = getApplicationContext();

        try {
            fos = openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(value.getBytes());
            fos.close();
            Toast.makeText(context, "保存しました", Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}