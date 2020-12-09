package com.example.androidphotos15;

import androidx.appcompat.app.AppCompatActivity;
import model.AlbumList;
import model.Photo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class albumView extends AppCompatActivity {
    //declare variables here:

    //this is the place where we will display our images
    private ImageView imageView;

    //this will be our button to add a photo into the album
    private Button addButton;

    //to remove a photo from the album
    private Button removeButton;

    //button clicked when we want to move a photo to another album
    private Button moveButton;

    //move to next photo in our photos list
    private Button nextButton;

    //move to previous photo in our photos list
    private Button prevButton;

    //add tags to our photof
    private Button addTags;

    //start off at the front of our photos list
    int i = 0;

    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_view);

        //initialize our imageview from our xml
        imageView = (ImageView) findViewById(R.id.imageView3);



        //if the list of photos in our current album is empty
        if (homepageActivity.masterList.getCurr().getPhotos().isEmpty()){
            //notify the user that there are no photos
            Toast.makeText(albumView.this, "Empty Album, Please add photos", Toast.LENGTH_SHORT).show();
        }
        else {
            //show the first photo if it is not empty
            //File imgFile = new File(homepageActivity.masterList.getCurr().getPhotos().get(i).getphotoPath());
            Uri imgUri = Uri.parse(homepageActivity.masterList.getCurr().getPhotos().get(i).getphotoPath());

           // if(imgFile.exists())
            //{
                //imageView.setImageURI(Uri.fromFile(imgFile));
                imageView.setImageURI(imgUri);
                //}

            //else{
                System.out.println(homepageActivity.masterList.getCurr().getPhotos().get(i).getphotoPath());
            //}

            //imageView.setImageBitmap(BitmapFactory.decodeFile(homepageActivity.masterList.getCurr().getPhotos().get(i).getphotoPath()));

        }

        //connect the xml to our local button
        addButton = (Button) findViewById(R.id.addBtn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //what happens upon clicking the add button

                //want file chooser capability
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        //connect the xml to our local button
        removeButton = (Button) findViewById(R.id.removeBtn);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //what happens upon clicking the remove button
                if(homepageActivity.masterList.getCurr().getPhotos().size() == 0){
                    Toast.makeText(albumView.this, "No more photos to delete", Toast.LENGTH_SHORT).show();
                }

                else{
                    //remove the photo
                    homepageActivity.masterList.getCurr().getPhotos().remove(i);
                    Toast.makeText(albumView.this, "Photo Removed", Toast.LENGTH_SHORT).show();


                    //ADD STUFF HERE




                    //serialize the data again
                    //now serialize the list
                    try{
                        AlbumList.serialize(homepageActivity.masterList);
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        moveButton = (Button) findViewById(R.id.moveBtn);
        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //what happens upon clicking the move button
            }
        });

        nextButton = (Button) findViewById((R.id.nextBtn));
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //what happens upon clicking the next button
                //increment the counter
                i++;

                //reached the photo, cannot go next
                if(i > homepageActivity.masterList.getCurr().getPhotos().size()-1){
                    i--;
                    Toast.makeText(albumView.this, "Reached last Photo, cannot go next", Toast.LENGTH_SHORT).show();
                }

                else{
                    //update the viewing
                    Uri imgUri = Uri.parse(homepageActivity.masterList.getCurr().getPhotos().get(i).getphotoPath());
                    imageView.setImageURI(imgUri);
              }


            }
        });

        prevButton = (Button) findViewById(R.id.prevBtn);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //what happens upon clicking the prev button
                i--;
                if(i < 0){
                    i++;
                    Toast.makeText(albumView.this, "Reached first Photo, cannot go prev", Toast.LENGTH_SHORT).show();

                }
                else{
                    //update the viewing
                    Uri imgUri = Uri.parse(homepageActivity.masterList.getCurr().getPhotos().get(i).getphotoPath());
                    imageView.setImageURI(imgUri);
                }
            }
        });

        addTags = (Button) findViewById(R.id.addTagsBtn);
        addTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //what happens upon clicking add tags button
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

                //add the photo onto our main list
                homepageActivity.masterList.getCurr().addPhoto(data.getData().getPath());
                System.out.println(data.getData().getPath());

                //set up the selected image
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    imageView.setImageURI(selectedImage);
                }

                //now serialize the list
                try{
                    AlbumList.serialize(homepageActivity.masterList);
                }
                catch (IOException e){
                    e.printStackTrace();
                }

       // }
    }
}