package htwb.ai;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import htwb.ai.helper.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
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
public class SpringBootHelloWorldApplication implements CommandLineRunner {

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

    @Override
    public void run(String... args) throws Exception {
        try {

            Users user1 = new Users(AppConstants.USER_1, AppConstants.DEFAULT_PASSWORD, "Max", "Muster");
            user1.setPassword(web.passwordEncoder().encode(user1.getPassword()));
            Users user2 = new Users(AppConstants.USER_2, AppConstants.DEFAULT_PASSWORD, "Elena", "Schuler");
            user2.setPassword(web.passwordEncoder().encode(user2.getPassword()));
            File resource = new File("src/main/resources/songs.json");
            String path = resource.getAbsolutePath();
            String[] splitted = path.split("sakvis");
            if (!resource.getAbsolutePath().contains("songsWS")) path = splitted[0] + "sakvis/songsWS" + splitted[1];
            List<Song> songs = readJSONToSongs(path);

            List<SongList> sLists = new ArrayList<>();

            for (int i = 0; i < 35; i++) {
                SongList st = new SongList();
                if (i % 2 == 0) st.setIsPrivate(true);
                else st.setIsPrivate(false);
                st.setName("playlist " + (i + 1));
                songs.get(0).setTitle("title " + i);
                st.setSongList(songs);
                sLists.add(st);

                if (i == 0 || i == 1) {
                    st.setOwner(user1);
                    user1.getSongLists().add(st);
                } else if (i > 5 && i % 2 == 0) {
                    st.setOwner(user1);
                    user2.getSongLists().add(st);
                } else {
                    st.setOwner(user2);
                    user2.getSongLists().add(st);
                }

            }
            // requirements
            sLists.get(0).setIsPrivate(true);
            sLists.get(1).setIsPrivate(false);
            sLists.get(2).setIsPrivate(true);
            sLists.get(3).setIsPrivate(false);
            // update
            this.userRepository.save(user1);
            this.userRepository.save(user2);
        } catch (Exception e) {
            System.out.println("Spring boot run exception -> " + e.getMessage());
            System.out.println("################### stack -> " + e.getStackTrace().toString());
        }

    }

    @SuppressWarnings("unchecked")
    public static List<Song> readJSONToSongs(String filename) throws FileNotFoundException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream is = new BufferedInputStream(new FileInputStream(filename))) {
            return (List<Song>) objectMapper.readValue(is, new TypeReference<List<Song>>() {
            });
        }
    }

    @SuppressWarnings("unchecked")
    public static List<SongList> readJSONToSongList(String filename) throws FileNotFoundException, IOException, Exception {
        ObjectMapper objectMapper = new ObjectMapper();//.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try (InputStream is = new BufferedInputStream(new FileInputStream(filename))) {
            return (List<SongList>) objectMapper.readValue(is, new TypeReference<List<SongList>>() {
            });
        }
    }
}