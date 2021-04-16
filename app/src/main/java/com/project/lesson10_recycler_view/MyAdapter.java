package com.project.lesson10_recycler_view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileOutputStream;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private final List<String> titles;
    private final List<String> descriptions;
    private final List<Bitmap> wallpaper_image;
    private final Context context;
    private String filename;

    public MyAdapter(Context ct, List<String> names, List<String> descriptions, List<Bitmap> wallpaper_image) {
        this.titles = names;
        this.descriptions = descriptions;
        this.wallpaper_image = wallpaper_image;
        this.context = ct;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Bitmap bmp = wallpaper_image.get(position);

        holder.title_textView.setText(titles.get(position));
        holder.description_textView.setText(descriptions.get(position));
        holder.wallpaperView.setImageBitmap(bmp);

        try {
            //Write file
            filename = "bitmap" + position + ".png";
            FileOutputStream stream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            stream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.mainLayout.setOnClickListener(v -> {
            filename = "bitmap" + position + ".png";

            //We create intent and put info to it to be read in the Activity_Second
            Intent intent = new Intent(context, Activity_second.class);
            intent.putExtra("name", titles.get(position));
            intent.putExtra("description", descriptions.get(position));
            intent.putExtra("image", filename);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView title_textView;
        private final TextView description_textView;
        private final ImageView wallpaperView;
        private final ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title_textView = itemView.findViewById(R.id.name);
            description_textView = itemView.findViewById(R.id.description);
            wallpaperView = itemView.findViewById(R.id.wallpaper);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
