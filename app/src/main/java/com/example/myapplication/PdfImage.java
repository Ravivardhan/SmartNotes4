package com.example.myapplication;

import android.graphics.Bitmap;

public class PdfImage {
     Bitmap bitmap;

    public PdfImage(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
