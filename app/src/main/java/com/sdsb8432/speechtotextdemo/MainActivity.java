package com.sdsb8432.speechtotextdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;

import com.sdsb8432.speechtotextdemo.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private final int REQ_CODE_SPEECH = 100;

    private ActivityMainBinding mainBinding;

    private CustomSTT customSTT;
    private boolean isOnCustomSTT = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }
        else {
            init();
        }
    }

    private void init() {
        customSTT = new CustomSTT(this, mainBinding, Locale.ENGLISH);
        mainBinding.buttonSpeech.setOnClickListener(this);
        mainBinding.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isOnCustomSTT = isChecked;
                if(!isOnCustomSTT) {
                    customSTT.stopCustomSTT();
                    mainBinding.textViewSpeechStatus.setText("");
                }
            }
        });
    }

    private void startNormalSTT(Locale language) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something");
        startActivityForResult(intent, REQ_CODE_SPEECH);

    }

    @Override
    public void onClick(View v) {
        if(isOnCustomSTT) {
            customSTT.startCustomSTT();
        }
        else {
            startNormalSTT(Locale.ENGLISH);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE_SPEECH) {
            if(resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                mainBinding.textViewSpeechResult.setText(result.get(0));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                }
                else {
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if(customSTT != null) {
            customSTT.stopCustomSTT();
            customSTT = null;
        }
        super.onDestroy();
    }
}
