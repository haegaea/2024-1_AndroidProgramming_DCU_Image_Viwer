package com.example.dcu_image_viwer;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;

public class RecyclerImageAdapter extends RecyclerView.Adapter<RecyclerImageAdapter.ViewHolder> {

    private Context context;
    private File[] imageFiles;
    private ImageAdapter.OnItemClickListener onItemClickListener;

    public RecyclerImageAdapter(Context context, File[] imageFiles, ImageAdapter.OnItemClickListener onItemClickListener) {
        this.context = context;
        this.imageFiles = imageFiles;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return imageFiles.length;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bitmap bitmap = BitmapFactory.decodeFile(imageFiles[position].getAbsolutePath());
        holder.imageView.setImageBitmap(bitmap);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(imageFiles[position].getAbsolutePath()));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
