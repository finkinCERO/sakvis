package htwb.ai.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Table(name = "users")
@Entity
public class Users {

//    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "username", length = 50, nullable = false)
    private String username;
    @Column(length = 50, nullable = true)
    @JsonIgnore
    private String firstname;
    @Column(length = 50, nullable = true)
    @JsonIgnore
    private String lastname;
    @Column(name = "password", length = 100, nullable = false)
    private String password;

    //    @OneToMany(targetEntity = SongList.class, cascade= CascadeType.ALL)
//    @JoinColumn(name="owner", referencedColumnName="username")
    @JsonManagedReference
    @OneToMany(mappedBy="owner", fetch=FetchType.LAZY, cascade=CascadeType.MERGE,//WAS EAGER
            orphanRemoval=true)
    private List<SongList> songLists = new ArrayList<>();


    // Entity-Retaltionship
    // One User can have multiple SongLists
    // referec.. the same name as the pk column of the referenced table

//  @JoinColumn(name="ownerid", referencedColumnName="id")//referencedColumnName is PK
//    @JsonIgnore
//    @OneToMany(mappedBy="owner", cascade  = CascadeType.ALL,
//    orphanRemoval=true)
//    private List<SongList> songLists = new ArrayList<>();
//

    //
//    @ManyToOne()
//    @JoinColumn (name = "owner", referencedColumnName="id")
    public List<SongList> getSongLists() {
        return songLists;
    }

    public void setSongLists(List<SongList> songLists) {
        this.songLists = songLists;
    }
//
//	public List<Song> getSongs() {
//		return songs;
//	}
//
//	public void setSongs(List<Song> songs) {
//		this.songs = songs;
//	}

    private Users(Builder builder) {
        this.username = builder.username;
        this.firstname = builder.firstname;
        this.lastname = builder.lastname;
        this.password = builder.password;
    }

    public Users() {
    }

    public Users(String username, String password, String firstname, String lastname) {
        this.username = username;
        this.password = password;
        this.lastname = lastname;
        this.firstname = firstname;
//        this.songLists = songLists;
//        this.songs = songs;
    }

    public Users(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) { this.password = password; }



    @Override
    public String toString() {
        return "DAOUser [username=" + username + ", firstname=" + firstname + ", lastname=" + lastname + ", password="
                + password + "]";
    }

    /**
     * Creates builder to build {@link Users}.
     *
     * @return created builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder to build {@link Users}.
     */
    public static final class Builder {

        private String username;
        private String firstname;
        private String lastname;
        private String password;

        private Builder() {
        }


        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withFirstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public Builder withLastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Users build() {
            return new Users(this);
        }

    }

}
