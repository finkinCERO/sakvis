package htwb.ai.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Type;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import htwb.ai.config.JwtTokenUtil;
import htwb.ai.helper.AppConstants;
import htwb.ai.model.Song;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.google.gson.Gson;

import htwb.ai.model.SongList;
import htwb.ai.model.Users;
import htwb.ai.repository.SongListRepo;
import htwb.ai.repository.SongRepo;
import htwb.ai.repository.UserRepo;
import htwb.ai.service.JwtUserDetailsService;

@SpringBootTest
@TestPropertySource(locations = "/test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SongListControllerTest {

    private MockMvc mockUserController;

    private MockMvc mockSlController;
    private MockMvc mockSongController;
    private MockMvc mockAuthController;


    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepo uRepo;
    @Autowired
    private SongListRepo slRepo;
    @Autowired
    private SongRepo sRepo;

    @Autowired
    private JwtUserDetailsService jwtDetailService;

    SongList songlist1;
    SongList songlist2;
    SongList songlist3;
    SongList songlist4;

    SongList songlist;
    private Gson gson;
    private Users user1 = new Users();
    private Users user2 = new Users();

    Principal securityUser;

    String token = "";
    Boolean check = true;

    @BeforeEach
    public void setupMockMvc() {
        if (check) {
            check=false;
            this.gson = new Gson();
//        securityUser = getPrincipal();

            mockUserController = MockMvcBuilders.standaloneSetup(new UserController(uRepo)).build();
            mockSlController = MockMvcBuilders.standaloneSetup(new SongListsController(slRepo, uRepo)).build();
            mockSongController = MockMvcBuilders.standaloneSetup(new SongController(sRepo)).build();
            mockAuthController = MockMvcBuilders.standaloneSetup(new JwtAuthenticationController(jwtDetailService)).build();


            //SETUP TEST USERS AND RELATE/CREATE SONGLISTS BY UPDATING USER
            // SETUP USER 1:
            user1.setUsername(AppConstants.TEST_USER);
            user1.setFirstname("Bobby");
            user1.setLastname("Smith");
            user1.setPassword(AppConstants.DEFAULT_PASSWORD);
            // SETUP USER 2:
            user2.setUsername(AppConstants.TEST_USER_2);
            user2.setFirstname("Bobby");
            user2.setLastname("Smith");
            user2.setPassword(AppConstants.DEFAULT_PASSWORD);


            songlist = new SongList("songlist default", false, user1);
            Song song = new Song();
            song.setTitle("new song");
            song.setArtist("Davis D. Sky");
            song.setAlbum("Intellij");
            songlist.getSongList().add(song);
            songlist1 = new SongList("songlist no1", false, user1);
            songlist2 = new SongList("songlist no2", true, user1);

            songlist3 = new SongList("songlist no3", false, user2);
            songlist4 = new SongList("songlist no4", true, user2);

            try {
                // OWNER MUST EXISTS FOR RELATION (users -1-n-> songlists -n-1-> song_songlists -1-m-> song)
                // YOU CAN'T CREATE NEW USER THROUGH SONGLIST (User is Owner)
                Users u1 = uRepo.save(user1);
                uRepo.flush();
                // RELATE SONGLISTS TO USER 1:
                u1.getSongLists().add(songlist1);
                u1.getSongLists().add(songlist2);
                // AND UPDATE:
                uRepo.save(u1);
                uRepo.flush();

                Users u2 = uRepo.save(user2);
                uRepo.flush();
                // RELATE SONGLISTS TO USER 2:
                u2.getSongLists().add(songlist3);
                u2.getSongLists().add(songlist4);

                // AND UPDATE:
                uRepo.save(u2);
                uRepo.flush();
                token = jwtTokenUtil.createToken(user1.getUsername());
                System.out.println("### token -->" + token);

            } catch (Exception e) {
                System.out.println("### Save Exception --> " + e.getCause());
            }
            check = false;
        }

    }

    @Test
    @Order(1)
    public void getAPublicForeignSongLists() throws Exception {
        String payload = gson.toJson(user1);
        MvcResult result = mockSlController.perform(get("/songsWS-sakvis/rest/songLists?username=" + AppConstants.TEST_USER_2).header("Content-Type", "application/json").
                header("Authorization", "Bearer " + token).
                content(payload)).andExpect(status().isOk()).andReturn();
        Type listType = new TypeToken<ArrayList<SongList>>() {
        }.getType();
        List<SongList> list = gson.fromJson(result.getResponse().getContentAsString(), listType);
        assertEquals(1, list.size() );

        check=false;
    }
    @Test
    @Order(2)
    public void getAOwnSongLists() throws Exception {
        String payload = gson.toJson(user1);
        MvcResult result = mockSlController.perform(get("/songsWS-sakvis/rest/songLists?username=" + AppConstants.TEST_USER).header("Content-Type", "application/json").
                header("Authorization", "Bearer " + token).
                content(payload)).andExpect(status().isOk()).andReturn();
        Type listType = new TypeToken<ArrayList<SongList>>() {
        }.getType();
        List<SongList> list = gson.fromJson(result.getResponse().getContentAsString(), listType);
        // 4 weil before each 2x ausgeführt wird
        assertEquals(list.size(), 4);

        check=false;
    }


    @Test
    public void getForbiddenSongList() throws Exception {
        String payload = gson.toJson(user1);
        // get song:
        MvcResult result = mockSlController.perform(get("/songsWS-sakvis/rest/songLists/1").header("Content-Type", "application/json").
                header("Authorization", "Bearer " + token).
                content(payload)).andExpect(status().isForbidden()).andReturn();
        System.out.println("### SongLists result --> " + result.getResponse().getContentAsString());

        check=false;
        // ToDo: check status forbidden
    }

    @Test
    public void getOwnSongList() throws Exception {
        String payload = gson.toJson(user1);
        // get songlist:
        MvcResult result = mockSlController.perform(get("/songsWS-sakvis/rest/songLists/37").
                header("Content-Type", "application/json").
                header("Authorization", "Bearer " + token).
                content(payload).
                sessionAttr(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, token)).andExpect(status().isOk()).andReturn();
        System.out.println("### Own SL result --> " + result.getResponse().getContentAsString());
        // ToDo: check status forbidden

        check=false;
    }

    @Test
    public void getForeignPublicSongList() throws Exception {
        String payload = gson.toJson(user1);
        // get song:
        MvcResult result = mockSlController.perform(get("/songsWS-sakvis/rest/songLists/2").header("Content-Type", "application/json").
                header("Authorization", "Bearer " + token).
                content(payload)).andExpect(status().isOk()).andReturn();
        // ToDo: check status forbidden

        check=false;
    }

    @Test
    public void getSongListWithNonNummericId() throws Exception {
        String payload = gson.toJson(user1);
        // get song:
        MvcResult result = mockSlController.perform(get("/songsWS-sakvis/rest/songLists/abc").header("Content-Type", "application/json").
                header("Authorization", "Bearer " + token).
                content(payload)).andExpect(status().isNotFound()).andReturn();

        check=false;
    }
    @Test
    public void getSongListsWrongUsername() throws Exception {
        String payload = gson.toJson(user1);
        MvcResult result = mockSlController.perform(get("/songsWS-sakvis/rest/songLists?username=wrongUsername").header("Content-Type", "application/json").
                header("Authorization", "Bearer " + token).
                content(payload)).andExpect(status().isNotFound()).andReturn();
    }

    @Test
    public void postCorrectSongList() throws Exception {
        String payload = gson.toJson(songlist);
        MvcResult result = mockSlController.perform(post("/songsWS-sakvis/rest/songLists/").header("Content-Type", "application/json").
                header("Authorization", "Bearer " + token).
                content(payload)).andExpect(status().isAccepted()).andReturn();
    }

    @Test
    public void postWithoutNameForSongList() throws Exception {
        songlist.setName(null);
        String payload = gson.toJson(songlist);
        MvcResult result = mockSlController.perform(post("/songsWS-sakvis/rest/songLists/").header("Content-Type", "application/json").
                header("Authorization", "Bearer " + token).
                content(payload)).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void postEmptyNameForSongList() throws Exception {
        songlist.setName("");
        String payload = gson.toJson(songlist);
        MvcResult result = mockSlController.perform(post("/songsWS-sakvis/rest/songLists/").header("Content-Type", "application/json").
                header("Authorization", "Bearer " + token).
                content(payload)).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void postWithoutTitleInSongOfSongList() throws Exception {
        Song s = new Song();
        s.setTitle(null);
        songlist.getSongList().add(s);
        String payload = gson.toJson(songlist);
        MvcResult result = mockSlController.perform(post("/songsWS-sakvis/rest/songLists/").header("Content-Type", "application/json").
                header("Authorization", "Bearer " + token).
                content(payload)).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void postEmptyTitleInSongOfSongList() throws Exception {
        Song s = new Song();
        s.setTitle("");
        songlist.getSongList().add(s);
        String payload = gson.toJson(songlist);
        MvcResult result = mockSlController.perform(post("/songsWS-sakvis/rest/songLists/").header("Content-Type", "application/json").
                header("Authorization", "Bearer " + token).
                content(payload)).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void postEmptyWhiteSpaceTitleInSongOfSongList() throws Exception {
        Song s = new Song();
        s.setTitle("  ");
        songlist.getSongList().add(s);
        String payload = gson.toJson(songlist);
        MvcResult result = mockSlController.perform(post("/songsWS-sakvis/rest/songLists/").header("Content-Type", "application/json").
                header("Authorization", "Bearer " + token).
                content(payload)).andExpect(status().isBadRequest()).andReturn();
    }


}
