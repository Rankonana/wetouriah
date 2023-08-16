package com.example.wetouriah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.kyanogen.signatureview.SignatureView;
import com.mrudultora.colorpicker.ColorPickerPopUp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.UUID;

public class Signature extends AppCompatActivity {

    //ActivitySignatureBinding mainBinding;
    private int mDefaultColor;
    private static final int REQUEST_CODE = 1;

    Button btnSave,btnClear,btnDownload,btnChangePenColor;

    ImageView imgSignature;

    SignatureView signature_view;

    View preview_selected_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signature);
        setTitle("Signature Pad");


        btnSave = findViewById(R.id.btnSave);
        btnClear = findViewById(R.id.btnClear);
        btnDownload = findViewById(R.id.btnDownload);
        btnChangePenColor = findViewById(R.id.btnChangePenColor);

        signature_view = findViewById(R.id.signature_view);
        imgSignature = findViewById(R.id.imgSignature);
        preview_selected_color = findViewById(R.id.preview_selected_color);




        mDefaultColor = Color.BLACK;

        btnSave.setOnClickListener(view -> {
            Bitmap bitmap = signature_view.getSignatureBitmap();
            if(bitmap != null)
            {
                imgSignature.setImageBitmap(bitmap);
            }

            Intent track = new Intent(Signature.this,MainActivity.class);
            startActivity(track);
            finish();


        });

        btnClear.setOnClickListener(view -> {
            signature_view.clearCanvas();
        });

        btnDownload.setOnClickListener(view -> {
            imgSignature.buildDrawingCache();
            if(ContextCompat.checkSelfPermission(Signature.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            {
                saveImage();
            }
            else
            {
                ActivityCompat.requestPermissions(Signature.this,new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
            }
        });

        //Implement the Change Pen Color Code to pen color of signature pad.
        btnChangePenColor.setOnClickListener(view->{
            ColorPickerPopUp colorPickerPopUp = new ColorPickerPopUp(Signature.this);
            colorPickerPopUp.setShowAlpha(true)
                    .setDefaultColor(mDefaultColor)
                    .setDialogTitle("Pick a Color")
                    .setOnPickColorListener(new ColorPickerPopUp.OnPickColorListener() {
                        @Override
                        public void onColorPicked(int color) {
                            preview_selected_color.setBackgroundColor(color); // Set color in view
                            signature_view.setPenColor(color); // Set color in pen
                            mDefaultColor = color;
                        }
                        @Override
                        public void onCancel() {
                            colorPickerPopUp.dismissDialog();
                        }
                    })
                    .show();
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(requestCode == REQUEST_CODE)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                saveImage();
            }
            else
            {
                Toast.makeText(Signature.this,"Please provide required permission",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void saveImage()
    {
        Uri images;
        ContentResolver contentResolver = getContentResolver();
        BitmapDrawable bitmapDrawable = (BitmapDrawable)imgSignature.getDrawable();
        try {
            Bitmap bmp =  bitmapDrawable.getBitmap();
            if(bmp != null)
            {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                {
                    images = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
                }
                else
                {
                    images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }

                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Images.Media.DISPLAY_NAME,"Digital_Sign_" + System.currentTimeMillis() + ".jpg");
                contentValues.put(MediaStore.Images.Media.MIME_TYPE,"images/*");
                Uri uri = contentResolver.insert(images,contentValues);

                try {

                    OutputStream outputStream = contentResolver.openOutputStream(Objects.requireNonNull(uri));
                    bmp.compress(Bitmap.CompressFormat.JPEG,80,outputStream);
                    Objects.requireNonNull(outputStream);

                    Toast.makeText(Signature.this,"Digital Signature Save Successfully",Toast.LENGTH_SHORT).show();
                }catch(Exception e){
                    Toast.makeText(Signature.this,"Error : " + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(Signature.this,"Please Generate Digital Signature To Save",Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e)
        {
            Toast.makeText(Signature.this,"Please Generate Digital Signature To Save",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        // Create an intent for the default activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear the activity stack
        startActivity(intent);
        finish(); // Finish the current activity
    }




}