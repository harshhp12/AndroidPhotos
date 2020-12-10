package com.example.androidphotos15;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;



public class movePhoto extends AppCompatActivity {
    //array adapter used to populate our listview
    private ArrayAdapter<String> arrayAdapter;

    //setup viewing of albums
    ListView lv;

    //use a list of albums for this activity purposes
    private static List<String> albums = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.move);

        populateAlbums();
        lv = (ListView) findViewById(R.id.aList);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,  android.R.id.text1, albums);
        lv.setAdapter(arrayAdapter);
        //populate our list for this activity and viewing

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //carry out the move stuff

            }
        });

    }
    private static void populateAlbums(){
        //clear the list of any albums that are lingering in there
        albums.clear();

        //go through our masterlist of albums
        for(int i = 0; i < homepageActivity.masterList.getAlbums().size(); i++){
            //add the album name to our list of local album strings
            albums.add(homepageActivity.masterList.getAlbums().get(i).getAlbumName());
        }
    }
}
