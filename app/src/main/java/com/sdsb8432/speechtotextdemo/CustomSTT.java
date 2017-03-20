package com.sdsb8432.speechtotextdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import com.sdsb8432.speechtotextdemo.databinding.ActivityMainBinding;

import java.util.Locale;

/**
 * Created by sonseongbin on 2017. 3. 19..
 */

public class CustomSTT implements RecognitionListener{



    private final String TAG = CustomSTT.class.getSimpleName();

    private Activity activity;
    private ActivityMainBinding mainBinding;
    private Intent intentSpeech;
    private SpeechRecognizer speechRecognizer;

    public CustomSTT(Activity activity, ActivityMainBinding mainBinding, Locale language) {
        this.activity = activity;
        this.mainBinding = mainBinding;
        init(language);
    }

    public void init(Locale language) {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity);
        speechRecognizer.setRecognitionListener(this);
        intentSpeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intentSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
    }

    public void startCustomSTT() {
        if(speechRecognizer != null && intentSpeech != null) {
            speechRecognizer.startListening(intentSpeech);
        }
    }

    public void stopCustomSTT() {
        if(speechRecognizer != null) {
            speechRecognizer.stopListening();
        }
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.d(TAG, "onReadyForSpeech");
        mainBinding.textViewSpeechStatus.setText("onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d(TAG, "onBeginningOfSpeech");
        mainBinding.textViewSpeechStatus.setText("onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.d(TAG, "onRmsChanged");
        mainBinding.textViewSpeechStatus.setText("onRmsChanged");
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.d(TAG, "onBufferReceived");
        mainBinding.textViewSpeechStatus.setText("onBufferReceived");
    }

    @Override
    public void onEndOfSpeech() {
        Log.d(TAG, "onEndOfSpeech");
        stopCustomSTT();
        mainBinding.textViewSpeechStatus.setText("onEndOfSpeech");
    }

    @Override
    public void onError(int error) {
        Log.d(TAG, "onError");
        mainBinding.textViewSpeechStatus.setText("onError");
    }

    @Override
    public void onResults(Bundle results) {
        Log.d(TAG, "onResults");
        mainBinding.textViewSpeechResult.setText(results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0));
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.d(TAG, "onPartialResults");
        mainBinding.textViewSpeechStatus.setText("onPartialResults");
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.d(TAG, "onEvent");
        mainBinding.textViewSpeechStatus.setText("onEvent");
    }
}
