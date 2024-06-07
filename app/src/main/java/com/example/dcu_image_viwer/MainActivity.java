package com.example.dcu_image_viwer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private GridView gridView;
    private ImageAdapter imageAdapter;
    private File[] imageFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 권한 체크 및 요청
        if (checkPermission()) {
            loadImages();
        } else {
            requestPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!checkPermission()) {
            requestPermission();
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ?
                        Manifest.permission.READ_MEDIA_IMAGES :
                        Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ?
                        Manifest.permission.READ_MEDIA_IMAGES :
                        Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadImages();
            } else {
                // 권한이 거부되었을 때 처리
                requestPermission();
            }
        }
    }

    private void loadImages() {
        gridView = findViewById(R.id.gridView);
        File imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (imageDir != null) {
            imageFiles = imageDir.listFiles();
            if (imageFiles != null) {
                Log.d("MainActivity", "Image files found: " + imageFiles.length);
                Arrays.sort(imageFiles, (file1, file2) -> {
                    long diff = file1.lastModified() - file2.lastModified();
                    if (diff == 0) {
                        return file1.getName().compareTo(file2.getName());
                    } else {
                        return Long.compare(file1.lastModified(), file2.lastModified());
                    }
                });

                imageAdapter = new ImageAdapter(this, imageFiles, path -> {
                    Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                    intent.putExtra("imagePath", path);
                    startActivity(intent);
                });
                gridView.setAdapter(imageAdapter);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                        intent.putExtra("imagePath", imageFiles[position].getAbsolutePath());
                        startActivity(intent);
                    }
                });
            } else {
                Log.d("MainActivity", "No image files found in the directory.");
            }
        }
    }
}
