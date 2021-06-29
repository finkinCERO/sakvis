package htwb.ai;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.fasterxml.jackson.databind.ObjectMapper;

import htwb.ai.config.WebSecurityConfig;
import htwb.ai.controller.SongListsController;
import htwb.ai.repository.SongListRepo;
import htwb.ai.repository.SongRepo;
import htwb.ai.repository.UserRepo;
import htwb.ai.model.Users;
import htwb.ai.model.Song;
import htwb.ai.model.SongList;

@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = {"htwb.ai"}, exclude = JpaRepositoriesAutoConfiguration.class)
@EnableJpaRepositories({"htwb.ai.repository"})
//@ComponentScan(basePackages= {"htwb.ai.*"})
public class SpringBootHelloWorldApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootHelloWorldApplication.class, args);
    }

    @Autowired(required = true)
    private UserRepo userRepository;
    @Autowired
    private SongRepo songRepository;
    @Autowired
    private SongListsController sc;
    @Autowired
    private SongListRepo songlistRepository;
    //	@Autowired(required = true)
//	private PasswordEncoder bcryptEncoder;
    @Autowired
    WebSecurityConfig web;

    

    @SuppressWarnings("unchecked")
    public static List<Song> readJSONToSongs(String filename) throws FileNotFoundException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream is = new BufferedInputStream(new FileInputStream(filename))) {
            return (List<Song>) objectMapper.readValue(is, new TypeReference<List<Song>>() {
            });
        }
    }

    @SuppressWarnings("unchecked")
    public static List<SongList> readJSONToSongList(String filename) throws FileNotFoundException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream is = new BufferedInputStream(new FileInputStream(filename))) {
            return (List<SongList>) objectMapper.readValue(is, new TypeReference<List<SongList>>() {
            });
        }
    }
}