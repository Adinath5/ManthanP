package com.atharvainfo.manthan.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.atharvainfo.manthan.Class.Common;
import com.atharvainfo.manthan.Class.PdfDucumentAdapter;
import com.atharvainfo.manthan.R;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class ExamHallTicket extends AppCompatActivity {

    Button btnhallticket;
    Image image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_hall_ticket);

        btnhallticket = (Button) findViewById(R.id.btncreatehallt);

        btnhallticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPDFFile(Common.getAppPath(ExamHallTicket.this)+"hallticket.pdf");
            }
        });

        /*Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        btnhallticket.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                                createPDFFile(Common.getAppPath(ExamHallTicket.this)+"hallticket.pdf");

                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                });*/

    }

    public void createPDFFile(String path){
            if(new File(path).exists())
                new File(path).delete();

            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(path));

                document.open();;

                document.setPageSize(PageSize.A4);
                document.addCreationDate();
                document.addAuthor("Manthan Publication");
                document.addCreator("Zorwas.in");

                BaseColor colorAccent = new BaseColor(0,153,204,255);

                float fontSize = 20.0f;
                float valueFontSize =26.0f;
                BaseFont fontName = BaseFont.createFont("assets/fonts/brandon_regular.otf", "UTF-8", BaseFont.EMBEDDED);

                try {
                    InputStream ims = getAssets().open("logo.png");;
                    Bitmap bmp = BitmapFactory.decodeStream(ims);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    image = Image.getInstance(stream.toByteArray());
                    //image.scalePercent(30);
                    image.scaleAbsolute(15, 10);
                    image.setAlignment(Element.ALIGN_LEFT);
                   // document.add(image);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Font titlefont = new Font(fontName, 16.0f, Font.BOLD, BaseColor.BLACK);
                Font subtitlefont = new Font(fontName, 12.0f, Font.BOLD, BaseColor.BLACK);
                Font subtitlefont1 = new Font(fontName, 11.0f, Font.NORMAL, BaseColor.BLACK);
                Font subtitlefont2 = new Font(fontName, 9.0f, Font.NORMAL, BaseColor.BLACK);

                Font blueFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, new CMYKColor(255, 0, 0, 0));
                Font redFont = FontFactory.getFont(FontFactory.COURIER, 12, Font.BOLD, new CMYKColor(0, 255, 0, 0));
                Font yellowFont = FontFactory.getFont(FontFactory.COURIER, 14, Font.BOLD, new CMYKColor(0, 0, 255, 0));



                PdfPTable table = new PdfPTable(2); // 3 columns.
                table.setWidthPercentage(100); //Width 100%
                table.setSpacingBefore(10f); //Space before table
                table.setSpacingAfter(10f); //Space after table
                float[] columnWidths = {1f, 1f};
                table.setWidths(new float[]{30,70});

                PdfPCell cell1 = new PdfPCell(image, true);
                cell1.setBorderColor(BaseColor.WHITE);
                cell1.setPaddingLeft(10);
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell cell2 = new PdfPCell();
                cell2.setBorderColor(BaseColor.WHITE);
                cell2.setPaddingLeft(10);
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

                Paragraph paragraphhead = new Paragraph();
                paragraphhead.add("Manthan Welfare Foundation Organized");
                paragraphhead.setAlignment(Element.ALIGN_CENTER);
                paragraphhead.setFont(subtitlefont);
                cell2.addElement(paragraphhead);

                Paragraph paragraphhead1 = new Paragraph();
                paragraphhead1.add("State Level");
                paragraphhead1.setAlignment(Element.ALIGN_CENTER);
                paragraphhead1.setFont(subtitlefont2);
                cell2.addElement(paragraphhead1);

                Paragraph paragraphhead2 = new Paragraph();
                paragraphhead2.add("Manthan General Knowledge Examination - 2020");
                paragraphhead2.setAlignment(Element.ALIGN_CENTER);
                paragraphhead2.setFont(titlefont);
                cell2.addElement(paragraphhead2);

                Paragraph paragraphhead3 = new Paragraph();
                paragraphhead2.add("Std. 1st- Std. 8th (Marathi, English, Semi-Eng.)");
                paragraphhead3.setAlignment(Element.ALIGN_CENTER);
                paragraphhead3.setFont(subtitlefont);
                cell2.addElement(paragraphhead3);

                table.addCell(cell1);
                table.addCell(cell2);
                document.add(table);
                //addNewItem(document, "Manthan Welfare Foundation Organized", Element.ALIGN_CENTER, subtitlefont);
                //addNewItem(document, "State Level", Element.ALIGN_CENTER, subtitlefont2);
                //addNewItem(document, "Manthan General Knowledge Examination - 2020", Element.ALIGN_CENTER, titlefont);

                PdfPTable table1 = new PdfPTable(4); // 3 columns.
                table1.setWidthPercentage(100); //Width 100%
                table1.setSpacingBefore(10f); //Space before table
                table1.setSpacingAfter(10f); //Space after table
                //float[] columnWidths = {1f, 1f};
                table1.setWidths(new float[]{10,60, 10, 20});

                PdfPCell stnamecell = new PdfPCell();
                stnamecell.setBorderColor(BaseColor.WHITE);
                stnamecell.setPaddingLeft(10);
                stnamecell.setHorizontalAlignment(Element.ALIGN_LEFT);
                stnamecell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                Paragraph paragraphstname = new Paragraph();
                paragraphstname.add("Student Name");
                paragraphstname.setAlignment(Element.ALIGN_LEFT);
                paragraphstname.setFont(redFont);
                stnamecell.addElement(paragraphstname);
                table1.addCell(stnamecell);




                /*Font orderNumberFont = new Font(fontName, fontSize, Font.NORMAL,colorAccent);
                addNewItem(document, "ID No. :", Element.ALIGN_LEFT, orderNumberFont);

                Font orderNumberValueFont = new Font(fontName, valueFontSize, Font.NORMAL,colorAccent);
                addNewItem(document, "Student ID :", Element.ALIGN_LEFT, orderNumberValueFont);

                addLineSeperator(document);

                addNewItem(document,"Exam Date", Element.ALIGN_LEFT, orderNumberFont);
                addNewItem(document, "2019-01-19", Element.ALIGN_LEFT, orderNumberValueFont);

                addLineSeperator(document);
                addNewItem(document,"Student Name", Element.ALIGN_LEFT, orderNumberFont);
                addNewItem(document, "Adinath Mungase", Element.ALIGN_LEFT, orderNumberValueFont);

                addLineSeperator(document);

                addLineSpace(document);
                addNewItem(document, "Exam Detail", Element.ALIGN_CENTER, titlefont);*/

                addLineSeperator(document);

                //addNewItemWithLeftAndRight(document, "STD", "3rd", titlefont, orderNumberValueFont);
                //addNewItemWithLeftAndRight(document, "School Name", "Amrutvalini", titlefont, orderNumberValueFont);


                addLineSpace(document);
                addLineSpace(document);

                document.close();

                Toast.makeText(this, "Success",Toast.LENGTH_SHORT);

                printPDF();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }



    private void printPDF() {

        PrintManager printManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            printManager = (PrintManager)getSystemService(Context.PRINT_SERVICE);
        }


        try {
            PrintDocumentAdapter printDocumentAdapter = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                printDocumentAdapter = new PdfDucumentAdapter(ExamHallTicket.this, Common.getAppPath(ExamHallTicket.this) +"hallticket.pdf");
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());
            }

        } catch (Exception ex){
            Log.e("ExamlHallTiket", ""+ ex.getMessage());

        }
    }

    private void addNewItemWithLeftAndRight(Document document, String textLeft, String textRight, Font textleftfont, Font textRightFont) throws DocumentException {

        Chunk chunkTextLeft = new Chunk(textLeft, textleftfont);
        Chunk chunkTextRight = new Chunk(textRight, textRightFont);
        Paragraph p = new Paragraph(chunkTextLeft);
        p.add(new Chunk(new VerticalPositionMark()));
        p.add(chunkTextRight);
        document.add(p);


    }

    private void addLineSeperator(Document document) throws DocumentException {
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0,0,0,68));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
        addLineSpace(document);


    }

    private void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));

    }

    private void addNewItem(Document document, String text, int alignCenter, Font font) throws DocumentException {
        Chunk chunk = new Chunk(text, font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(alignCenter);

        document.add(paragraph);


    }
}
