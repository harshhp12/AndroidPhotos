package com.example.androidphotos15;

import androidx.appcompat.app.AppCompatActivity;
import model.Photo;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class searchActivity extends AppCompatActivity {
;

    int currIndex;
    private Button searchButton;
    private List<Photo> searchResults = new ArrayList<Photo>();
    private EditText locationEntered;
    private EditText personEntered;
    private List<String> locationTags;
    private List<String> personTags;
    private Button addTagButton;
    private ImageView imageView;
    private Button next;
    private Button prev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        addTagButton = (Button) findViewById(R.id.addTagButton);
        locationTags = new ArrayList<String>();
        personTags = new ArrayList<String>();
        imageView = (ImageView) findViewById(R.id.imageView);

        next = (Button) findViewById(R.id.Next);
        prev = (Button) findViewById(R.id.Prev);

        next.setVisibility(View.INVISIBLE);
        prev.setVisibility(View.INVISIBLE);

        locationEntered = (EditText) findViewById(R.id.locationEntered);
        personEntered = (EditText) findViewById(R.id.editTextTextPersonName2);

        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String personVal = personEntered.getText().toString();
                String locationVal = locationEntered.getText().toString();

                if(personVal.isEmpty() && locationVal.isEmpty()){
                    Toast.makeText(searchActivity.this, "Both fields cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!personVal.isEmpty()){personTags.add(personVal);}
                if(!locationVal.isEmpty()){locationTags.add(locationVal);}


                personEntered.setText("");
                locationEntered.setText("");

            }
        });

        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currIndex =0;
                searchResults.clear();
                searchResults.addAll(homepageActivity.masterList.getPhotosWithTags(locationTags, personTags));

                if (!searchResults.isEmpty()){
                    next.setVisibility(View.VISIBLE);
                    prev.setVisibility(View.VISIBLE);

                    //show the first photo if it is not empty
                    Uri imgUri = Uri.parse(searchResults.get(currIndex).getphotoPath());
                    imageView.setImageURI(imgUri);
                }

                else{
                    Toast.makeText(searchActivity.this, "No Results Found for Search", Toast.LENGTH_SHORT).show();

                }


            }
        });

       next.setOnClickListener(new View.OnClickListener(){

           @Override
           public void onClick(View view) {
               //what happens upon clicking the next button
               //increment the counter
               currIndex++;

               //reached the photo, cannot go next
               if(currIndex > searchResults.size()-1){
                   currIndex--;
                   Toast.makeText(searchActivity.this, "Reached last Photo, cannot go next", Toast.LENGTH_SHORT).show();
               }

               else{

                   //i//update the viewing
                   Uri imgUri = Uri.parse(searchResults.get(currIndex).getphotoPath());
                   imageView.setImageURI(imgUri);
               }
           }
       });

       prev.setOnClickListener(new View.OnClickListener(){

           @Override
           public void onClick(View view) {
               //what happens upon clicking the prev button
               currIndex--;
               if(currIndex < 0){
                   currIndex++;
                   Toast.makeText(searchActivity.this, "Reached first Photo, cannot go prev", Toast.LENGTH_SHORT).show();

               }
               else{
                   //update the viewing
                   Uri imgUri = Uri.parse(searchResults.get(currIndex).getphotoPath());
                   imageView.setImageURI(imgUri);
               }
           }
       });
    }
}