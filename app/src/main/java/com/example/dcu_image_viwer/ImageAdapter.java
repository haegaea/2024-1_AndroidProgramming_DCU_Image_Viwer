package com.example.dcu_image_viwer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;

public class ImageAdapter extends BaseAdapter {

    private Context context;
    private File[] imageFiles;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(String path);
    }

    public ImageAdapter(Context context, File[] imageFiles, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.imageFiles = imageFiles;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        return imageFiles.length;
    }

    @Override
    public Object getItem(int position) {
        return imageFiles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(300, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        Bitmap bitmap = BitmapFactory.decodeFile(imageFiles[position].getAbsolutePath());
        imageView.setImageBitmap(bitmap);
        imageView.setOnClickListener(v -> onItemClickListener.onItemClick(imageFiles[position].getAbsolutePath()));

        return imageView;
    }
}
