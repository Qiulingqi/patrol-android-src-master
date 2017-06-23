package com.saic.visit.utils.excelutil;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * Created by 1 on 2017/3/17.
 */

public class ExcelUtil {
    /*//内存地址
    public static String root = Environment.getExternalStorageDirectory()
            .getPath();*/

    public static void writeExcel(Context context, List<Order> exportOrder,
                                  String fileName) throws Exception {
      /*  if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)&&getAvailableStorage()>1000000) {
            Toast.makeText(context, "SD卡不可用", Toast.LENGTH_LONG).show();
            return;
        }*/
        String[] title = {"序号","取证点代码", "取证点信息", "置无效", "证据类型", "文件", "备注"};
        File file;
        //  String theFileName = AddVisitActivity.theFileName;
        File dir = new File("/sdcard/凯迪拉克/凯迪拉克评估/");
        file = new File(dir, fileName + ".xls");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 创建Excel工作表
        WritableWorkbook wwb;
        OutputStream os = new FileOutputStream(file);
        wwb = Workbook.createWorkbook(os);
        // 添加第一个工作表并设置第一个Sheet的名字
        WritableSheet sheet = wwb.createSheet("订单", 0);
        Label label;
        for (int i = 0; i < title.length; i++) {
            // Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z
            // 在Label对象的子对象中指明单元格的位置和内容
            label = new Label(i, 0, title[i], getHeader());
            // 将定义好的单元格添加到工作表中
            sheet.addCell(label);
        }

        for (int i = 0; i < exportOrder.size(); i++) {

            Order order = exportOrder.get(i);
            Label xuhao =   new Label(0, i + 1,order.xuhao);
            Label orderNum = new Label(1, i + 1, order.pointid);
            Label restaurant = new Label(2, i + 1, order.restName);
            Label istrfa = new Label(3, i + 1, order.istrfa);
            Label nameLabel = new Label(4, i + 1, order.restPhone);
            Label address = new Label(5, i + 1, order.receiverAddr);
            Label beizhu = new Label(6, i + 1, order.beizhu);

            sheet.addCell(xuhao);
            sheet.addCell(orderNum);
            sheet.addCell(nameLabel);
            sheet.addCell(istrfa);
            sheet.addCell(restaurant);
            sheet.addCell(address);
            sheet.addCell(beizhu);
//            Toast.makeText(context, "写入成功", Toast.LENGTH_LONG).show();

        }
        // 写入数据
        wwb.write();
        // 关闭文件
        wwb.close();
    }

    public static WritableCellFormat getHeader() {
        WritableFont font = new WritableFont(WritableFont.TIMES, 10,
                WritableFont.BOLD);// 定义字体
        try {
            font.setColour(Colour.BLUE);// 蓝色字体
        } catch (WriteException e1) {
            e1.printStackTrace();
        }
        WritableCellFormat format = new WritableCellFormat(font);
        try {
            format.setAlignment(jxl.format.Alignment.CENTRE);// 左右居中
            format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 上下居中
            // format.setBorder(Border.ALL, BorderLineStyle.THIN,
            // Colour.BLACK);// 黑色边框
            // format.setBackground(Colour.YELLOW);// 黄色背景
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return format;
    }

   /* *//** 获取SD可用容量 *//*
    private static long getAvailableStorage() {

        StatFs statFs = new StatFs(root);
        long blockSize = statFs.getBlockSize();
        long availableBlocks = statFs.getAvailableBlocks();
        long availableSize = blockSize * availableBlocks;
        // Formatter.formatFileSize(context, availableSize);
        return availableSize;
    }*/
}
