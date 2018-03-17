package com.example.lenovo.hackathontourism;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class QRCodeGeneration extends AppCompatActivity {

    ImageView imageView;
    Button button;
    EditText editText;
    String EditTextValue ;
    Thread thread ;
    public final static int QRcodeWidth = 500 ;
    Bitmap bitmap ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_generation);

        imageView = (ImageView)findViewById(R.id.imageView);
        button = (Button)findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                qr_code();

            }
        });
    }

    public static final String QR_URL="https://scienceakk.000webhostapp.com/php/booking.php";

    //data = {'userid' : '1', 'monumentid' : '1', 'quantity' : 5, 'date' : '2018-03-23'}

    public static final String KEY_USERID="userid";
    public static final String KEY_MONUMENT_ID="monumentid";
    public static final String KEY_QUANTITY="quantity";
    public static final String KEY_DATE="date";



    public void qr_code() {

        EditText muserid=(EditText)findViewById(R.id.userid);
        EditText mmonumentid=(EditText)findViewById(R.id.monumentid);
        EditText mquantity=(EditText)findViewById(R.id.quantityid);
        EditText mdate=(EditText)findViewById(R.id.dateid);
       final String muserid1=muserid.getText().toString();
        final String mmonumentid1=mmonumentid.getText().toString();
       final String mquantity1=mquantity.getText().toString();
       final String mdate1=mdate.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, QR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jObj = null;
                        try {
                            jObj = new JSONObject(response);


                            try {
                                bitmap = TextToImageEncode(response);

                                imageView.setImageBitmap(bitmap);

                            } catch (WriterException e) {
                                Toast.makeText(QRCodeGeneration.this,"fdsg",Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }


                        } catch (JSONException e) {
                            Toast.makeText(QRCodeGeneration.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> prams = new HashMap<>();

                prams.put(KEY_USERID, muserid1);
                prams.put(KEY_MONUMENT_ID, mmonumentid1);
                prams.put(KEY_QUANTITY, mquantity1);
                prams.put(KEY_DATE, mdate1);

                return prams;
            }
        };

    RequestQueue requestQueue = Volley.newRequestQueue(QRCodeGeneration.this);
            requestQueue.add(stringRequest);
}



    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.colorPrimaryDark):getResources().getColor(R.color.colorWhite);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}
