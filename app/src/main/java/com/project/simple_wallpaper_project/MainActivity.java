package com.project.simple_wallpaper_project;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private ImageView imageView;
    private final ArrayList<Bitmap> bitmaps = new ArrayList<>();
    private final ArrayList<String> wallpaper_titles = new ArrayList<>();
    private final ArrayList<String> wallpaper_descriptions = new ArrayList<>();
    private Bitmap image;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Adding titles, descriptions from the resources
        Collections.addAll(wallpaper_titles, getResources().getStringArray(R.array.wallpapers));
        Collections.addAll(wallpaper_descriptions, getResources().getStringArray(R.array.description));
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        //Creating bitmaps from resource images
        int[] images = {R.drawable.wallpaper1, R.drawable.wallpaper2};
        for (int value : images)
            bitmaps.add(BitmapFactory.decodeStream(getResources().openRawResource(value)));

        myAdapter = new MyAdapter(this, wallpaper_titles, wallpaper_descriptions, bitmaps);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton addButton = findViewById(R.id.main_add_image);
        addButton.setOnClickListener(v -> createNewDialog());

    }

    public void createNewDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup, null);
        MaterialButton addImageBtn = (MaterialButton) contactPopupView.findViewById(R.id.add_image_btn);
        MaterialButton okBtn = (MaterialButton) contactPopupView.findViewById(R.id.popup_ok_btn);
        MaterialButton cancelBtn = (MaterialButton) contactPopupView.findViewById(R.id.pupup_cancel_btn);

        EditText titleEdit = (EditText) contactPopupView.findViewById(R.id.titlePopup);
        EditText descriptionEdit = (EditText) contactPopupView.findViewById(R.id.descriptionPopup);
        imageView = contactPopupView.findViewById(R.id.imageViewPopup);

        dialogBuilder.setView(contactPopupView);
        Dialog dialog = dialogBuilder.create();
        dialog.show();

        addImageBtn.setOnClickListener(v -> getImage());

        okBtn.setOnClickListener(v -> {
            wallpaper_titles.add(titleEdit.getText().toString());
            wallpaper_descriptions.add(descriptionEdit.getText().toString());
            bitmaps.add(image);
            myAdapter.notifyDataSetChanged();
            dialog.dismiss();
        });

        cancelBtn.setOnClickListener(v -> dialog.dismiss());
    }

    private void getImage() {
        try {
            //Getting image from gallery
            Intent intent_pick = new Intent(Intent.ACTION_GET_CONTENT);
            intent_pick.setType("image/*");
            startActivityForResult(Intent.createChooser(intent_pick, "Select Picture"), PICK_IMAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //After getting the result of getImage() we get the data and turn the image into bitmap
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, "STH went wrong", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                image = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(image); //We set the ImageView on the button to have this image
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}