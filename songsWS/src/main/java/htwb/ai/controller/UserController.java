package htwb.ai.controller;

import htwb.ai.model.SongList;
import htwb.ai.repository.UserRepo;
//import htwb.ai.dao.IUserDAO;
import htwb.ai.model.Users;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "songsWS-sakvis/rest")
public class UserController {
    // just for testing...
    // this controller can be removed to hide user infos
    // for education its cool
    private UserRepo userDAO;

    public UserController(UserRepo dao) {
        this.userDAO = dao;
    }

    // GET http://localhost:8080/authSpring/rest/auth/eschuler
    @GetMapping(value = "/user/{id}")
    public ResponseEntity<Users> getUser(@PathVariable(value = "id") String username) throws IOException {
        Users user = userDAO.findByUsername(username);
        if (user == null) {
            return new ResponseEntity<Users>(user, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Users>(user, HttpStatus.OK);
    }
    @GetMapping(value = "/users")
    public Iterable<Users> getUsers() {
        try{

            return userDAO.findAll();
        } catch (Exception e){
            return null;

        }
//    	return userDAO.getAllUsers();
    }

}
