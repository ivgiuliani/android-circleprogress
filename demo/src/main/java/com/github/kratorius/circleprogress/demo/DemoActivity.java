package com.github.kratorius.circleprogress.demo;

import android.app.Activity;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.kratorius.circleprogress.CircleProgressView;

public class DemoActivity extends Activity {
    private Button btnFadeInReload;
    private Button btnRollReload;
    private Button btnRollIncremental;

    private CircleProgressView crcBackgroundLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_layout);

        btnFadeInReload = (Button) findViewById(R.id.btn_reload_fade_in);
        btnRollReload = (Button) findViewById(R.id.btn_reload_roll);
        btnRollIncremental = (Button) findViewById(R.id.btn_reload_incremental);
        crcBackgroundLine = (CircleProgressView) findViewById(R.id.crc_bg2);

        Reloader reloader = new Reloader();

        btnFadeInReload.setOnClickListener(reloader);
        btnRollReload.setOnClickListener(reloader);
        btnRollIncremental.setOnClickListener(reloader);

        /*ScaleDrawable scaleDraw = (ScaleDrawable) getResources().getDrawable(R.drawable.circle3_bg);
        scaleDraw
        crcBackgroundLine.setB*/
    }

    private class Reloader implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            finish();
            startActivity(getIntent());
        }
    }
}
