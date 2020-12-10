package com.example.androidphotos15;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import model.Album;
import model.AlbumList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class homepageActivity extends AppCompatActivity {

    //declare variables here:

    //our button to open up the seach activity
    private Button searchButton;

    //this is our button on the home_page_layout xml to add a new album
    private FloatingActionButton addAlbumBtn;

    //array adapter used to populate our listview
    private ArrayAdapter<String> arrayAdapter;

    //finding the albums data file if it exists
    File albumsData = new File("data/data/com.example.androidphotos15/albums.dat");

    //create new instance of the AlbumList class
    //create new instance of the AlbumList class
    public static AlbumList masterList = new AlbumList();

    //use a list of albums for this activity purposes
    private static List<String> albums = new ArrayList<String>();

    //this is to get the input of the album name entered to create
    private String m_Text;

    //index of selected album
    private int currentIndex;

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
            //System.out.println("here");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("File not created");
            }
        }

        //once the serialization and everything is done, we can start to setup the viewing
        populateAlbums();
        //this is what sets up the listview:
        //https://stackoverflow.com/questions/5070830/populating-a-listview-using-an-arraylist
        lv = (ListView) findViewById(R.id.albumsList);
        //first parameter is context, then the type of list view as the second parameter, and our array as the last
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,  android.R.id.text1, albums);
        lv.setAdapter(arrayAdapter);

        //now to add some user functionality
        //setup the onclick listener for the button
        addAlbumBtn = (FloatingActionButton) findViewById(R.id.addAlbumBtn);

        addAlbumBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){

                    //implement what happens upon clicking the add album button
                    AlertDialog.Builder builder = new AlertDialog.Builder(homepageActivity.this);
                    builder.setTitle("New Album Name");

                    // Set up the input
                    final EditText input = new EditText(homepageActivity.this);
                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                    builder.setView(input);

                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            m_Text = input.getText().toString();

                            //now we create a new album
                            Album newAlbum = new Album(m_Text);

                            //now add the album onto our master list
                            masterList.addAlbum(newAlbum);

                            //want to reset the listview
                            try{
                                AlbumList.serialize(masterList);
                            }
                            catch (IOException e){
                                e.printStackTrace();
                            }

                            //process to refresh the list
                            lv = (ListView) findViewById(R.id.albumsList);
                            populateAlbums();
                            arrayAdapter.notifyDataSetChanged();
                            lv.setAdapter(arrayAdapter);

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
        });

        //lets setup what happens when you click the search button
        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //basically just open the search page
                Intent searchA = new Intent(homepageActivity.this, searchActivity.class);
                startActivity(searchA);
            }
        });


       //listener for clicking item in the listview
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //set onclicked album as current album
                Album album = masterList.getAlbums().get(i);
                masterList.setCurr(album);
                currentIndex = i;

                //upon click, we want to bring up menu options
                registerForContextMenu(lv);
                openContextMenu(lv);
            }
        });

        //if we want to rename, delete or open anything on our list


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.setHeaderTitle("Select Action");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.deleteAction){
            //delete the selected item
            masterList.removeAlbum(currentIndex);

            //serialize again
            try {
                AlbumList.serialize(masterList);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //refresh our list
            lv = (ListView)findViewById(R.id.albumsList);
            populateAlbums();
            arrayAdapter.notifyDataSetChanged();
            lv.setAdapter(arrayAdapter);

            //notify the user
            Toast.makeText(homepageActivity.this, "Album deleted", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId() == R.id.openAction){
            //open up the album viewer
            Intent a = new Intent(homepageActivity.this, albumView.class);
            startActivity(a);
        }
        else if (item.getItemId() == R.id.renameAction){
            //actions to rename
            AlertDialog.Builder n = new AlertDialog.Builder(homepageActivity.this);
            n.setTitle("New Album Name");

            // Set up the input
            final EditText input = new EditText(homepageActivity.this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
            n.setView(input);

            // Set up the buttons
            n.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    m_Text = input.getText().toString();

                    //now we rename our album
                    masterList.getAlbums().get(currentIndex).setAlbumName(m_Text);

                    //want to reset the listview
                    try{
                        AlbumList.serialize(masterList);
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }

                    //process to refresh the list
                    lv = (ListView) findViewById(R.id.albumsList);
                    populateAlbums();
                    arrayAdapter.notifyDataSetChanged();
                    lv.setAdapter(arrayAdapter);

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