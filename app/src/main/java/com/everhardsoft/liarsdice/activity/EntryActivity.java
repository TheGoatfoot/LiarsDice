package com.everhardsoft.liarsdice.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.everhardsoft.bullet.DynamicsWorld;
import com.everhardsoft.liarsdice.Application;

public class EntryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application.initialize(this);
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}
