package com.everhardsoft.liarsdice.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.everhardsoft.liarsdice.R;

import butterknife.ButterKnife;

public class MultiplayerPlayActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayerplay);
        ButterKnife.bind(this);
    }
}
