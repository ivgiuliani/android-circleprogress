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

    private int[] mCirclesId = new int[] {
            R.id.crc_standard,
            R.id.crc_large_thickness,
            R.id.crc_animated_fadeIn,
            R.id.crc_animated_roll,
            R.id.crc_animated_incremental,
            R.id.crc_ovalized,
            R.id.crc_diff_start_angle,
            R.id.crc_bg1,
            R.id.crc_bg2,
            R.id.crc_bg3,
    };

    private int[] mIncButtonsId =  new int[] {
            R.id.btn_inc_standard,
            R.id.btn_inc_large_thickness,
            R.id.btn_inc_animated_fadeIn,
            R.id.btn_inc_animated_roll,
            R.id.btn_inc_animated_incremental,
            R.id.btn_inc_ovalized,
            R.id.btn_inc_diff_start_angle,
            R.id.btn_inc_bg1,
            R.id.btn_inc_bg2,
            R.id.btn_inc_bg3,
    };

    private int[] mDecButtonsId =  new int[] {
            R.id.btn_dec_standard,
            R.id.btn_dec_large_thickness,
            R.id.btn_dec_animated_fadeIn,
            R.id.btn_dec_animated_roll,
            R.id.btn_dec_animated_incremental,
            R.id.btn_dec_ovalized,
            R.id.btn_dec_diff_start_angle,
            R.id.btn_dec_bg1,
            R.id.btn_dec_bg2,
            R.id.btn_dec_bg3,
    };

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

        for (int i = 0; i < mCirclesId.length; i++) {
            CircleProgressView tmp = (CircleProgressView) findViewById(mCirclesId[i]);
            Button inc = (Button) findViewById(mIncButtonsId[i]);
            Button dec = (Button) findViewById(mDecButtonsId[i]);
            inc.setOnClickListener(new Incrementer(tmp));
            dec.setOnClickListener(new Decrementer(tmp));
        }
    }

    private class Reloader implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            finish();
            startActivity(getIntent());
        }
    }

    private static class Incrementer implements View.OnClickListener {
        private CircleProgressView mRef;
        public Incrementer(CircleProgressView ref) {
            mRef = ref;
        }
        @Override
        public void onClick(View view) {
            int val = mRef.getValue();
            val++;
            val = Math.min(Math.max(val, 0), 100);
            mRef.setValue(val);
        }
    }

    private static class Decrementer implements View.OnClickListener {
        private CircleProgressView mRef;
        public Decrementer(CircleProgressView ref) {
            mRef = ref;
        }
        @Override
        public void onClick(View view) {
            int val = mRef.getValue();
            val--;
            val = Math.min(Math.max(val, 0), 100);
            mRef.setValue(val);
        }
    }
}
