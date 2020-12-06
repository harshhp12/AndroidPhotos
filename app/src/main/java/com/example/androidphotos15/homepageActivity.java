package com.example.androidphotos15;

import androidx.appcompat.app.AppCompatActivity;
import model.AlbumList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class homepageActivity extends AppCompatActivity {

    //declare variables here:

    //this is our button on the home_page_layout xml to add a new album
    private FloatingActionButton addAlbumBtn;

    //array adapter used to populate our listview
    private ArrayAdapter<String> arrayAdapter;

    //finding the albums data file if it exists
    File albumsData = new File("/data/albums.dat");

    //create new instance of the AlbumList class
    public static AlbumList masterList = new AlbumList();

    //use a list of albums for this activity purposes
    private static List<String> albums = new ArrayList<String>();

    //setup the viewing of the albums
    ListView lv;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //try to deserialize the albumlist with the dat file that may be there
        try {
            masterList = AlbumList.deserialize();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_layout);

        //albums data file won't exist the first time the app is started
        //need to make it
        if (!albumsData.exists()) {
            //https://stackoverflow.com/questions/3572463/what-is-context-on-android
            Context context = this;
            File file = new File(context.getFilesDir(), "albums.dat");

            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("File not created");
            }
        }

        //once the serialization and everything is done, we can start to setup the viewing
        populateAlbums();
        //this is what sets up the listview:
        //https://stackoverflow.com/questions/5070830/populating-a-listview-using-an-arraylist
        lv = (ListView) findViewById(R.id.albumsList);
        //first parameter is context, then the type of list view as the second parameter, and our array as the last
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.home_page_layout, android.R.layout.simple_list_item_1,albums);
        lv.setAdapter(arrayAdapter);

        //now to add some user functionality
        //setup the onclick listener for the button
        addAlbumBtn = (FloatingActionButton) findViewById(R.id.addAlbumBtn);

        addAlbumBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    //implment what happens upon clicking the add album button
                }
        });
    }

    //populate our list for this activity and viewing
    private static void populateAlbums(){
        //clear the list of any albums that are lingering in there
        albums.clear();

        //go through our masterlist of albums
        for(int i = 0; i < masterList.getAlbums().size(); i++){
            //add the album name to our list of local album strings
            albums.add(masterList.getAlbums().get(i).getAlbumName());
        }
    }

}