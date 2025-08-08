package com.evan.floatscreenrecorder;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.evan.floatscreenrecorder.fab.model.FloatingConfigurationModel;
import com.evan.floatscreenrecorder.record.callback.NativeFloatingButtonCallback;
import com.evan.floatscreenrecorder.record.callback.OutputVideoCallback;
import com.evan.floatscreenrecorder.record.callback.RecordingStatusCallback;

public class MainActivity extends AppCompatActivity {

    private Button btn_showFAB, btn_displayFAB, btn_hideFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setOnClick();
    }


    public void init(){
        btn_showFAB = findViewById(R.id.showFAB);
        btn_displayFAB = findViewById(R.id.displayFab);
        btn_hideFAB = findViewById(R.id.hideFAB);

        EvanSDK.init(this);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
    }

    public void setOnClick(){
        btn_showFAB.setOnClickListener(v -> EvanSDK.showFloatingButtonWithConfiguration(MainActivity.this, new FloatingConfigurationModel(), new NativeFloatingButtonCallback() {
            @Override
            public void onClick(int customizedChildButtonIndex) {

            }

            @Override
            public void onError(String msg) {

            }

            @Override
            public void onClose(String msg) {

            }
        }));

        btn_displayFAB.setOnClickListener(v -> {
            EvanSDK.setFloatingButtonDisplay(MainActivity.this, true);
        });

        btn_hideFAB.setOnClickListener(v -> {
            EvanSDK.setFloatingButtonDisplay(MainActivity.this, false);
        });
    }
}
