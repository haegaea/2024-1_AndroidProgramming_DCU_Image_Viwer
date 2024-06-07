package com.example.dcu_image_viwer;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.Arrays;

public class ViewActivity extends AppCompatActivity {

    private ImageView imageView;
    private RecyclerView recyclerView;
    private RecyclerImageAdapter recyclerImageAdapter;
    private File[] imageFiles;
    private String selectedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        imageView = findViewById(R.id.imageView);
        recyclerView = findViewById(R.id.recyclerView);

        selectedImagePath = getIntent().getStringExtra("imagePath");
        File imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        imageFiles = imageDir.listFiles();
        Arrays.sort(imageFiles, (file1, file2) -> {
            long diff = file1.lastModified() - file2.lastModified();
            if (diff == 0) {
                return file1.getName().compareTo(file2.getName());
            } else {
                return Long.compare(file1.lastModified(), file2.lastModified());
            }
        });

        recyclerImageAdapter = new RecyclerImageAdapter(this, imageFiles, path -> {
            Bitmap selectedBitmap = BitmapFactory.decodeFile(path);
            imageView.setImageBitmap(selectedBitmap);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(recyclerImageAdapter);

        Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
        imageView.setImageBitmap(bitmap);
    }
}
