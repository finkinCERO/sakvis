package htwb.ai.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Table(name = "songs")
@Entity
public class Song implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", unique = true)
    private Integer id;
    @Column(name = "title", length = 100, nullable = false)
    private String title;
    @Column(name = "artist", length = 100)
    private String artist;
    @Column(name = "label", length = 100)
    private String label;
    @Column(name = "released")
    private Integer released;

    public Song(Integer id, String title, String artist, String label, Integer released) {

    }

    public Song() {

    }

    public String values() {
        return "'value':'test'";
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getReleased() {
        return released;
    }

    public void setReleased(Integer released) {
        this.released = released;
    }

    public String toSting() {
        return "id: " + this.id + ", title: " + this.title + ", artist: " + this.artist + ", label:" + this.label + ", released:"
                + this.released;
    }
    public boolean validate(Song s, String title, String artist, String label, Integer released){


        return false;
    }
}
