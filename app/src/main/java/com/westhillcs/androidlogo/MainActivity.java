package com.westhillcs.androidlogo;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
   private LogoView mLogo;
   private static Button b_fwd, b_right, b_upDown, b_hideShow, b_draw, b_reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // cannot set mLogo here because it has not been built yet
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.init();
    }


    private void init() {
        b_fwd = (Button)findViewById(R.id.fwd_b);
        b_right = (Button)findViewById(R.id.right_b);
        b_upDown = (Button)findViewById(R.id.up_b);
        b_hideShow = (Button)findViewById(R.id.hide_b);
        b_draw = (Button)findViewById(R.id.draw_b);
        b_reset = (Button)findViewById(R.id.reset_b);
        this.setOnClickListeners();
    }

    // initializes the OnClickListeners for all the buttons.
    private void setOnClickListeners() {
        b_fwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLogo = (LogoView)findViewById(R.id.logoView);
                mLogo.fwd();
            }
        });

        b_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLogo = (LogoView)findViewById(R.id.logoView);
                mLogo.right();
            }
        });

        b_upDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLogo = (LogoView)findViewById(R.id.logoView);
                if (b_upDown.getText().equals("UP")) {
                    mLogo.up();
                    b_upDown.setText("DOWN");
                }else {
                    mLogo.down();
                    b_upDown.setText("UP");
                }
            }
        });

        b_hideShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLogo = (LogoView)findViewById(R.id.logoView);
                if (b_hideShow.getText().equals("HIDE")) {
                    mLogo.hide();
                    b_hideShow.setText("SHOW");
                }else {
                    mLogo.show();
                    b_hideShow.setText("HIDE");
                }
            }
        });

        b_draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLogo = (LogoView)findViewById(R.id.logoView);
                mLogo.draw(MainActivity.this);
            }
        });

        b_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLogo = (LogoView)findViewById(R.id.logoView);
                /*
                if (b_reset.getText().equals("START"))
                    b_reset.setText("RESET");
                    */
                b_hideShow.setText("HIDE");
                b_upDown.setText("UP");
                mLogo.initialize();
            }
        });
    }


}
