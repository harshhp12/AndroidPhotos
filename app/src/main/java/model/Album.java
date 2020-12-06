package model;

import java.util.ArrayList;
import java.util.List;

public class Album {
    private String albumName;
    private List<Photo> photos;
    private Photo currentPhoto = null;

    public Album(String albumName){
        this.albumName = albumName;
        photos = new ArrayList<Photo>();
    }

    public void addPhoto(String photoPath) {
        Photo newPhoto = new Photo(photoPath);
        photos.add(newPhoto);
    }

    /**
     * to remove a photo from a list of photos at a given index
     * @param photoIndex        index of the photo to remove
     */
    public void removePhoto(int photoIndex) {
        photos.remove(photoIndex);
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public Photo getcurrentPhoto() {
        return currentPhoto;
    }

    public void setCurrentPhoto(Photo currentPhoto) {
        this.currentPhoto = currentPhoto;
    }
}
