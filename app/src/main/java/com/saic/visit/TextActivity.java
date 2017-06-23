package com.saic.visit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by 1 on 2017/4/1.
 */

public class TextActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 6.0权限设置
         */
       /* requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                0);
*/
        setContentView(R.layout.activity_text);
        initView();

    }

    private void initView() {
        TextView text = (TextView) findViewById(R.id.text);
        text.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {

            }
        });


        TextView text2 = (TextView) findViewById(R.id.text2);

        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }


}
