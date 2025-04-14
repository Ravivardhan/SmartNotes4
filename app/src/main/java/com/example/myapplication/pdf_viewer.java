package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class pdf_viewer extends AppCompatActivity {
    AppDatabase db;
    RecyclerView pdf_viewer_recycler;
    Button pdf_image_btn;
    PdfImageAdapter pdfImageAdapter;
    List<NoteImage> list;
    int subject_id, note_id;
    private static final int STORAGE_PERMISSION_CODE = 101;

    // Activity result launcher for multiple image selection
    private ActivityResultLauncher<Intent> multipleImagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    // Handle the selected images
                    if (result.getData().getClipData() != null) {
                        // Multiple images selected
                        int count = result.getData().getClipData().getItemCount();
                        for (int i = 0; i < count; i++) {
                            Uri imageUri = result.getData().getClipData().getItemAt(i).getUri();
                            processSelectedImage(imageUri);
                        }
                    } else if (result.getData().getData() != null) {
                        // Single image selected
                        Uri imageUri = result.getData().getData();
                        processSelectedImage(imageUri);
                    }
                }
            });

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pdf_viewer);

        subject_id = getIntent().getIntExtra("subject_id", 0);
        note_id = getIntent().getIntExtra("note_id", 0);

        db = Room.databaseBuilder(this, AppDatabase.class, "latest-db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigrationFrom()
                .build();

        list = db.noteImageDao().getImagesForNote(subject_id, note_id);
        pdf_viewer_recycler = findViewById(R.id.pdf_viewer_recycler);
        pdf_viewer_recycler.setLayoutManager(new LinearLayoutManager(this));
        pdfImageAdapter = new PdfImageAdapter(this, list);
        pdf_viewer_recycler.setAdapter(pdfImageAdapter);

        pdf_image_btn = findViewById(R.id.select_image_btn);

        // Check and request storage permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        }

        pdf_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalleryForMultipleSelection();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void openGalleryForMultipleSelection() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        multipleImagePickerLauncher.launch(intent);
    }

    private void processSelectedImage(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Rotate image if needed
            Bitmap rotatedBitmap = rotateImageIfRequired(bitmap, imageUri);

            // Scale to screen width
            Bitmap scaledBitmap = scaleToScreenWidth(rotatedBitmap);

            // Convert to byte array
            byte[] imageBytes = bitmapToBytes(scaledBitmap);

            // Save to database
            db.noteImageDao().insert(new NoteImage(subject_id, note_id, imageBytes));

            // Update the list
            list = db.noteImageDao().getImagesForNote(subject_id, note_id);
            pdfImageAdapter.updateList(list);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to process image", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap rotateImageIfRequired(Bitmap bitmap, Uri imageUri) throws IOException {
        ExifInterface exifInterface = new ExifInterface(getContentResolver().openInputStream(imageUri));
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                break;
            default:
                return bitmap;
        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private Bitmap scaleToScreenWidth(Bitmap bitmap) {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        float aspectRatio = (float) bitmap.getHeight() / (float) bitmap.getWidth();
        int newHeight = Math.round(screenWidth * aspectRatio);

        return Bitmap.createScaledBitmap(
                bitmap,
                screenWidth,
                newHeight,
                true
        );
    }

    public static byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                //Toast.makeText(this, "Storage permission is required to select images", Toast.LENGTH_SHORT).show();
            }
        }
    }
}