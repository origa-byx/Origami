package com.ori.origami;

import androidx.annotation.Nullable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.ori.origami.printtest.PdfUtil;
import com.ori.origami.printtest.PrintHelper;
import com.origami.origami.base.AnnotationActivity;
import com.origami.origami.base.annotation.BClick;
import com.origami.origami.base.annotation.BContentView;
import com.origami.origami.base.base_utils.ToastMsg;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

@SuppressLint("NonConstantResourceId")
@BContentView(R.layout.activity_test)
public class TestActivity extends AnnotationActivity {

    String src_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "img_src.pdf";
    String dest_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "img_dest.pdf";
    Context mContext;

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        mContext = this;
    }

    @BClick(R.id.jni_test)
    public void onclickjni(){
        printView(1);
//        boolean[] a = new boolean[30];
//        int i = Test.nativeTest(new byte[]{(byte) 1}, 1, new byte[2], new byte[2], new boolean[1], new boolean[2], a);
//        Log.e("TAG","re->" + i);
//        for (boolean b : a) {
//            Log.e("TAG",b + ", ");
//        }
    }


    public void printView(int page) {
        try{
            ToastMsg.show_msg("开始生成文件");
            Bitmap bitmap = BitmapFactory.decodeStream(getResources().getAssets().open("test.jpg"));
            PrintHelper helper = new PrintHelper(this);
            helper.printBitmap("test",bitmap);
            if(true){return;}
            File src = new File(src_path);
            if (src.exists()) {
                src.delete();
            }
            File dest = new File(dest_path);
            if (dest.exists()) {
                dest.delete();
            }
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter writer = null;
            try {
                writer = PdfWriter.getInstance(document, new FileOutputStream(src));
                TestActivity.MyFooter event = new MyFooter();
                writer.setPageEvent(event);
                document.open();
                for (int i = 1; i <= page; i++) {
                    //设置字体，支持中文
                    Font font = FontFactory.getFont(PdfUtil.FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

                    document.add(new Paragraph("这是第" + i + "页的数据!", font));

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    Image img = Image.getInstance(stream.toByteArray());
                    img.scaleToFit(770, 523);
                    float offsetX = (770 - img.getScaledWidth()) / 2;
                    float offsetY = (523 - img.getScaledHeight()) / 2;
                    img.setAbsolutePosition(36 + offsetX, 36 + offsetY);
                    document.add(img);
                    document.newPage();
                }

                document.close();
                PdfUtil.write(src_path, dest);
                ToastMsg.show_msg("开始尝试打印");
                PdfUtil.print(dest_path);
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }catch (IOException e){ }
    }

    static class MyFooter extends PdfPageEventHelper {
        Font font = FontFactory.getFont(PdfUtil.FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            Phrase header = new Phrase("这是页眉", font);
            Phrase footer = new Phrase("这是页脚", font);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    header,
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.top() + 10, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    footer,
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottom() - 10, 0);
        }
    }

}