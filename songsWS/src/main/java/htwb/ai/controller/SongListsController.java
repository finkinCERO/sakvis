package htwb.ai.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import htwb.ai.repository.SongListRepo;
import htwb.ai.repository.UserRepo;
import htwb.ai.model.SongList;
import htwb.ai.model.Users;

@RestController
@RequestMapping(value = "songsWS-sakvis/rest/songLists")
public class SongListsController {

    @Autowired
    private SongListRepo songListRepo;
    @Autowired
    private UserRepo userRepo;

    public SongListsController(SongListRepo dao) {
        this.songListRepo = dao;
    }

    @GetMapping
    public ResponseEntity<List<SongList>> getSongListFromUserName(@RequestParam("username") String username, Principal principal) {
        //Optional<SongList> songlist = songListRepo.findByOwner(userId);
        try {
            System.out.println("### username -> " + principal.getName());
            Users u = userRepo.findByUsername(username);

            List<SongList> songs = (List<SongList>) songListRepo.findByOwner(userRepo.findByUsername(username));
            List<SongList> result = new ArrayList<>();
            if (u == null) {
                return new ResponseEntity<List<SongList>>(result,
                        HttpStatus.NOT_FOUND);
            }
            for (SongList s : songs) {
                System.out.println("###### -> " + s.getOwner().getUsername());
                if (s.getOwner().getUsername().equals(principal.getName())) result.add(s);
                else if (!s.getIsPrivate()) result.add(s);
            }

            return new ResponseEntity<List<SongList>>(result,
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<List<SongList>>(new ArrayList<SongList>(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{listId}")
    public ResponseEntity<SongList> getSongList(@PathVariable("listId") String listId, Principal principal) {
        //Optional<SongList> songlist = songListRepo.findByOwner(userId);
        try {
            System.out.println("### username -> " + principal.getName());
            SongList sl = songListRepo.findById(Integer.parseInt(listId)).get();
            if (sl.getOwner().getUsername().equals(principal.getName()))
                return new ResponseEntity<SongList>(sl, HttpStatus.OK);
            else if (!sl.getIsPrivate()) return new ResponseEntity<SongList>(sl, HttpStatus.OK);
            else return new ResponseEntity<SongList>(new SongList(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<SongList>(new SongList(), HttpStatus.NOT_FOUND);
        }
    }


//    @GetMapping(value="/{username}")
//    public ResponseEntity<SongList> getSongList (@PathVariable(value="username") String username){
//
//    	// find by UserId and not by SongId
//    	List<SongList> songList = songListRepo.findByOwner(username);
//
//    	return new ResponseEntity<SongList>((SongList) songList, HttpStatus.ACCEPTED);
//    }

    //    @Transactional/?isername=mmuster
    @PostMapping(consumes = {"application/json"}, produces = "application/json")
    public ResponseEntity<SongList> postSongList(@RequestBody SongList songlist, Principal principal) {
        try {
            Users user = userRepo.findByUsername(principal.getName());

            songlist.setOwner(user);
            SongList list = songListRepo.save(songlist);
            songListRepo.flush();
            String path = "songsWS-sakvis/rest/songLists/" + list.getId();
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("Location",
                    path);
            return new ResponseEntity<SongList>(list, responseHeaders,
                    HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<SongList>(new SongList(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{id}", consumes = {"application/json", "application/xml"}, produces = {"text/plain"})
    public ResponseEntity<String> deleteSongList(@PathVariable(value = "id") Integer id, Principal principal)
            throws IOException {


        System.out.println("###################### id " + id + "@@@@@ count: " + songListRepo.findAll().size());

        try {
            Users user = userRepo.findByUsername(principal.getName());

            SongList sl = songListRepo.findById(id).get();
            if (sl == null) {
                return new ResponseEntity<String>("song list doesn't exist", HttpStatus.NOT_FOUND);
            }
            if (sl.getOwner().getUsername().equals(principal.getName())) {
                songListRepo.deleteById(id);
                return new ResponseEntity<String>("song list deleted", HttpStatus.NO_CONTENT);
            } else
                return new ResponseEntity<String>("", HttpStatus.FORBIDDEN);


        } catch (Exception e) {
            return new ResponseEntity<String>("", HttpStatus.NOT_FOUND);
        }

    }

    //    @Transactional
    @JsonIgnore
    @GetMapping(value = "/all")
    public List<SongList> getAllSongLists() {
        return songListRepo.findAll();
    }

//    @PutMapping("/{username}/")
}
