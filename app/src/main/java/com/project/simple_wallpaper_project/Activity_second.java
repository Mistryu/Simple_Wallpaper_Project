package com.project.simple_wallpaper_project;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileInputStream;
import java.io.IOException;

public class Activity_second extends AppCompatActivity {

    private String name, description_txt;
    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        ImageView imageView = findViewById(R.id.imageView);
        TextView title = findViewById(R.id.title);
        TextView description = findViewById(R.id.text_description);

        FloatingActionButton backBtn = (FloatingActionButton) findViewById(R.id.second_back_btn);
        backBtn.setOnClickListener(v -> finish());

        getData();
        setData(title, description, imageView);

        MaterialButton btn_wallpaper_set = findViewById(R.id.set_wallpaper_btn);

        btn_wallpaper_set.setOnClickListener(v -> {

            //We get the size of the screen
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            int height = metrics.heightPixels;
            int width = metrics.widthPixels;

            int imageHeight = image.getHeight();  //We calculate the middle
            int y = (imageHeight - height) / 2;

            if (height > imageHeight) {
                height = imageHeight;
                y = 0;
            }

            int imageWidth = image.getWidth();
            int x = (imageWidth - width) / 2;

            if (width > imageWidth) {
                width = imageWidth;
                x = 0;
            }
            Bitmap bitmap = Bitmap.createBitmap(image, x, y, width, height);

            WallpaperManager wallpaperManager = WallpaperManager.getInstance(Activity_second.this);
            try {
                wallpaperManager.setBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void getData() {
        //Getting data from the Intent
        if (getIntent().hasExtra("name") && getIntent().hasExtra("description")
                && getIntent().hasExtra("image")) {

            name = getIntent().getStringExtra("name");
            description_txt = getIntent().getStringExtra("description");

            String filename = getIntent().getStringExtra("image");   //Saving the image into file
            try {
                FileInputStream is = this.openFileInput(filename);
                image = BitmapFactory.decodeStream(is);
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(TextView title, TextView description, ImageView imageView) {
        title.setText(name);
        description.setText(description_txt);
        imageView.setImageBitmap(image);
    }
}