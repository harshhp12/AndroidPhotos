package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;
/*
creating a class to manage the list of albums we would have
kind of like the user class in the fx project
 */
public class AlbumList implements Serializable {

    //the complete list of albums
    private List<Album> albums;

    //in order to select a current item for view
    private Album curr;

    //this is our constructor
    public AlbumList(){
        albums = new ArrayList<Album>();
    }

    //add a new album onto our master album list
    public void addAlbum(Album album){albums.add(album);}

    //remove an album from the lsit
    public void removeAlbum(int index){
        albums.remove(index);
    }

    //take care of serialization
    public static void serialize(AlbumList userdata) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/data/com.example.androidphotos15/albums.dat"));
        oos.writeObject(userdata);
        oos.close();
    }
    //take care of deserialization
    public static AlbumList deserialize() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data/data/com.example.androidphotos15/albums.dat"));
        AlbumList userdata = (AlbumList) ois.readObject();
        ois.close();
        return userdata;
    }

    //return the list of albums
    public List<Album> getAlbums(){
        return albums;
    }

    //set the current album
    public void setCurr(Album a){
        this.curr = a;
    }

    //return the current album
    public Album getCurr(){return this.curr;}

}
