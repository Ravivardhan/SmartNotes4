package com.example.myapplication;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PdfImageViewHolder extends RecyclerView.ViewHolder {
    ImageView pdfImageView;

    public PdfImageViewHolder(@NonNull View itemView) {
        super(itemView);
        pdfImageView= itemView.findViewById(R.id.recycler_image_view);
    }
}
