package com.example.androidphotos15;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import model.AlbumList;
import model.Photo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class albumView extends AppCompatActivity {
    //declare variables here:

    //this is the place where we will display our images
    private ImageView imageView;

    //this will be our button to add a photo into the album
    private Button addButton;

    //this will be our button to show tags
    private Button showButton;

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

    //tag text
    private String m_Text;


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
            Uri imgUri = Uri.parse(homepageActivity.masterList.getCurr().getPhotos().get(i).getphotoPath());
            imageView.setImageURI(imgUri);

        }

        //connect the xml to our local button
        addButton = (Button) findViewById(R.id.addBtn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //what happens upon clicking the add button

                //want file chooser capability
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
        //connect the xml to our local button
        showButton = (Button) findViewById(R.id.showTagsBtn);
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //what happens upon clicking the show tags button


                List personTags = homepageActivity.masterList.getCurr().getPhotos().get(i).getpersonTags();
                List locationTags = homepageActivity.masterList.getCurr().getPhotos().get(i).getlocationTags();


                for(int i =0; i<personTags.size(); i++)
                {
                    AlertDialog.Builder n = new AlertDialog.Builder(albumView.this);
                    n.setTitle("Person:" + personTags.get(i));
                    n.show();
                }
                for(int i =0; i<locationTags.size(); i++)
                {
                    AlertDialog.Builder n = new AlertDialog.Builder(albumView.this);
                    n.setTitle("Location:" + locationTags.get(i));
                    n.show();
                }
            }
        });
        //connect the xml to our local buttoni
        removeButton = (Button) findViewById(R.id.removeBtn);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //what happens upon clicking the remove button
                if(homepageActivity.masterList.getCurr().getPhotos().size() == 0){
                    Toast.makeText(albumView.this, "No more photos to delete", Toast.LENGTH_SHORT).show();
                    imageView.setImageResource(0);
                }

                else{
                    //remove the photo
                    homepageActivity.masterList.getCurr().getPhotos().remove(i);
                    Toast.makeText(albumView.this, "Photo Removed", Toast.LENGTH_SHORT).show();


                    imageView.setImageResource(0);


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

               Intent a = new Intent(albumView.this, movePhoto.class);
                startActivity(a);


                //PopupMenu popup = new PopupMenu(albumView.this);
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

                    //i//update the viewing
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
                //upon click, we want to bring up menu options
                registerForContextMenu(imageView);
                openContextMenu(imageView);

            }
        });

    }

    //This is after selecting a photo from the file chooser thing
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


                Uri uri = null;
                uri = data.getData();
                String image_uristr = uri.toString();

                //check duplicates

              //add the photo onto our main list
              homepageActivity.masterList.getCurr().addPhoto(image_uristr);
              imageView.setImageURI(uri);
               System.out.println(data.getData().getPath());

                //now serialize the list
                try{
                    AlbumList.serialize(homepageActivity.masterList);
                }
                catch (IOException e){
                    e.printStackTrace();
                }


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        menu.setHeaderTitle("Select Tag Type");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

         if (item.getItemId() == R.id.personAction){
            //actions to rename
            AlertDialog.Builder n = new AlertDialog.Builder(this);
            n.setTitle("Person Tag");

            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
            n.setView(input);

            // Set up the buttons
            n.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    m_Text = input.getText().toString();
                    homepageActivity.masterList.getCurr().getPhotos().get(i).addPersonTag(m_Text);

                    //serialize again
                    try {
                        AlbumList.serialize(homepageActivity.masterList);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
            n.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            n.show();
        }
        else if (item.getItemId() == R.id.locationAction){
            //actions to rename
            AlertDialog.Builder n = new AlertDialog.Builder(this);
            n.setTitle("Location Tag");

            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
            n.setView(input);

            // Set up the buttons
            n.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    m_Text = input.getText().toString();
                    homepageActivity.masterList.getCurr().getPhotos().get(i).addLocationTag(m_Text);
                    //serialize again
                    try {
                        AlbumList.serialize(homepageActivity.masterList);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            n.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            n.show();
        }



        return true;
    }

}