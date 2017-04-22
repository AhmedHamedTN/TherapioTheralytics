package com.example.ahmed.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.example.ahmed.therapiodatafordepression.R;

public class SettingsActivity extends AppCompatActivity {

    SharedPreferences settings;
    SharedPreferences.Editor editor;
    //EditText username,passwd,ftp;
    Switch switchState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* username=(EditText)findViewById(R.id.USERNAME);
        passwd=(EditText)findViewById(R.id.PASSWORD);
        ftp=(EditText)findViewById(R.id.FPT_HOST);*/
        Context context = getApplicationContext();
        settings = context.getSharedPreferences("AUDIO_SOURCE", 0);
        editor = settings.edit();
        RadioGroup rg = (RadioGroup) findViewById(R.id.radiogroup);
        switchState = (Switch) findViewById(R.id.SWITCH);
        switchState.setChecked(settings.getBoolean("SWITCH", true));
        //rg.check(R.id.DEFAULT);
        //Set variables
        String choice = settings.getString("AUDIO_SOURCE", "");
        switch (choice) {
            case "DEFAULT":
                rg.check(R.id.DEFAULT);
                break;
            case "VOICE_COMMUNICATION":
                rg.check(R.id.VOICE_COMMUNICATION);
                break;
            case "VOICE_CALL":
                rg.check(R.id.VOICE_CALL);
                break;
            default:
                rg.check(R.id.VOICE_CALL);
        }

/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        editor.remove("AUDIO_SOURCE");
        editor.commit();
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.DEFAULT:
                if (checked) {
                    editor.putString("AUDIO_SOURCE", "DEFAULT");
                    editor.commit();
                    Log.d("AUDIO_SOURCE ", "SET");
                }
                break;
            case R.id.VOICE_COMMUNICATION:
                if (checked) {
                    editor.putString("AUDIO_SOURCE", "VOICE_COMMUNICATION");
                    editor.commit();
                }
                break;
            case R.id.VOICE_CALL:
                if (checked) {
                    editor.putString("AUDIO_SOURCE", "VOICE_CALL");
                    editor.commit();
                }
                break;
        }
    }


    public void SwitchEvent(View view) {
        Log.d("message", "switch event clicked " + switchState.isChecked());
        editor.putBoolean("SWITCH", switchState.isChecked());
        editor.commit();
        /*if (switchState.isChecked())
            Toast.makeText(getApplicationContext(), "Enabled recording !", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "disabled recording !", Toast.LENGTH_SHORT).show();*/
    }


}
