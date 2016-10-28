package com.github.malkomich.nanodegree.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.malkomich.nanodegree.R;

/**
 * Main menu activity.
 */
public class MainActivity extends AppCompatActivity {

    /* (non-Javadoc)
     * @see android.support.v7.app.AppCompatActivity#onCreate()
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Click listener for menu buttons.
     *
     * @param view Button View
     */
    public void onButtonClick(View view) {
        String appName = null;

        switch (view.getId()) {
            case R.id.button_app_1:
                Intent intent = new Intent(this, PopularMoviesActivity.class);
                startActivity(intent);
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
                break;
        }

        if(appName != null) {
            Toast.makeText(this, getString(R.string.button_toast, appName), Toast.LENGTH_SHORT)
                .show();
        }
    }
}
