package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PdfImageAdapter extends RecyclerView.Adapter<PdfImageViewHolder> {
    Context context;
    List<NoteImage> imageList;

    public PdfImageAdapter(Context context, List<NoteImage> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public PdfImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PdfImageViewHolder(LayoutInflater.from(context).inflate(R.layout.pdf_image_card,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull PdfImageViewHolder holder, int position) {
            holder.pdfImageView.setImageBitmap(bytesToBitmap(imageList.get(position).imageData));

    }
    public static Bitmap bytesToBitmap(byte[] bytes)
    {
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }

    public void updateList(List<NoteImage> newList) {
        // Clear the existing data
        imageList.clear();

        // Add all the new data
        imageList.addAll(newList);

        // Notify the adapter that the dataset has changed
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return imageList.size();
    }
}
