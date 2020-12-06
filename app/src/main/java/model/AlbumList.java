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

    public AlbumList(){
        albums = new ArrayList<Album>();
    }

    //add a new album onto our master album list
    public void addAlbum(Album album){albums.add(album);
    }

    public void removeAlbum(){

    }

    //take care of serialization
    public static void serialize(AlbumList userdata) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("/com/example/androidphotos15/data/albums.dat"));
        oos.writeObject(userdata);
        oos.close();
    }
    //take care of deserialization
    public static AlbumList deserialize() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("/com/example/androidphotos15/data/albums.dat"));
        AlbumList userdata = (AlbumList) ois.readObject();
        ois.close();
        return userdata;
    }

    //return the list of albums
    public List<Album> getAlbums(){
        return albums;
    }

}
