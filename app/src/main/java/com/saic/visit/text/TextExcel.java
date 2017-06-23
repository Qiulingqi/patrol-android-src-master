package com.saic.visit.text;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.saic.visit.R;
import com.saic.visit.utils.excelutil.Const;
import com.saic.visit.utils.excelutil.ExcelUtil;
import com.saic.visit.utils.excelutil.Order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TextExcel extends AppCompatActivity implements View.OnClickListener {

    Button btn;
    List<Order> orders = new ArrayList<Order>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_excel);
        int length = Const.OrderInfo.orderOne.length;
        for (int i = 0; i < length; i++) {
            Order order = new Order(Const.OrderInfo.orderOne[i][0], Const.OrderInfo.orderOne[i][1], Const.OrderInfo.orderOne[i][2], Const.OrderInfo.orderOne[i][3], Const.OrderInfo.orderOne[i][4], Const.OrderInfo.orderOne[i][5], Const.OrderInfo.orderOne[i][6]);
            orders.add(order);
            initView();
        }
    }

    private void initView() {
        Button output = (Button) findViewById(R.id.output);
        output.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.output:
                try {
                    ExcelUtil.writeExcel(TextExcel.this, orders, "excel_" + new Date().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
