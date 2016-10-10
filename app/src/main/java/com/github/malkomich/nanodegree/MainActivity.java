package com.github.malkomich.nanodegree;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonClick(View view) {
        String appName;

        switch (view.getId()) {
            case R.id.button_app_1:
                appName = getString(R.string.button_app_1).toLowerCase();
                break;
            case R.id.button_app_2:
                appName = getString(R.string.button_app_2).toLowerCase();
                break;
            case R.id.button_app_3:
                appName = getString(R.string.button_app_3).toLowerCase();
                break;
            case R.id.button_app_4:
                appName = getString(R.string.button_app_4).toLowerCase();
                break;
            case R.id.button_app_5:
                appName = getString(R.string.button_app_5).toLowerCase();
                break;
            case R.id.button_app_6:
                appName = getString(R.string.button_app_6).toLowerCase();
                break;
            default:
                appName = null;
                break;
        }

        if(appName != null) {
            Toast.makeText(this, getString(R.string.button_toast, appName), Toast.LENGTH_SHORT)
                .show();
        }
    }
}
