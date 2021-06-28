package htwb.ai.controller;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.google.gson.Gson;

import htwb.ai.model.JwtRequest;
import htwb.ai.model.Song;
import htwb.ai.model.UserDTO;
import htwb.ai.model.Users;
import htwb.ai.repository.SongListRepo;
import htwb.ai.repository.SongRepo;
import htwb.ai.repository.UserRepo;
import htwb.ai.service.JwtUserDetailsService;

@SpringBootTest
@TestPropertySource(locations = "/test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SongListControllerTest {

    private MockMvc mockMvc;
    private MockMvc mockMvc2;
    private MockMvc mockMvc3;
    private MockMvc mockMvc4;

    @Autowired
    private UserRepo uRepo;
    @Autowired
    private SongListRepo slRepo;
    @Autowired
    private SongRepo sRepo;

    @Autowired
    private JwtUserDetailsService jwtUser;
    private Gson gson;
    private UserDTO user1 = new UserDTO();

    @BeforeEach
    public void setupMockMvc() {
        this.gson = new Gson();

        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(uRepo)).build();
        mockMvc2 = MockMvcBuilders.standaloneSetup(new SongListsController(slRepo)).build();
        mockMvc3 = MockMvcBuilders.standaloneSetup(new SongController(sRepo)).build();
        mockMvc4 = MockMvcBuilders.standaloneSetup(new JwtAuthenticationController(jwtUser)).build();

//        user1 = new UserDTO("mmuster","Bobby","Smith","pass1234");
        user1.setUsername("mmuster");
        user1.setFirstname("Bobby");
        user1.setLastname("Smith");
        user1.setPassword("pass1234");

    }
    @Test
    @Order(1)
    public void postUserShouldSaveUserAndReturnNewId2() throws Exception {
    /*
        System.out.println("$$$$$$$$$$$$$ "+ user1);
        String payload = gson.toJson(user1);
        System.out.println(payload);
        MvcResult result = mockMvc4.perform(MockMvcRequestBuilders.post("/songsWS-sakvis/rest/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)).andReturn();

        System.out.println("######################################### "+ result.getResponse().getContentAsString());
        System.out.println("######################################### "+ result.getResponse().getStatus());
        user1.setPassword("pass1234");
        System.out.println("User -> "+user1.toString());
        String payload2 = gson.toJson(user1);
        System.out.println(payload2);
        MvcResult result2 = mockMvc4.perform(MockMvcRequestBuilders.post("/songsWS-sakvis/rest/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload2)).andReturn();

        System.out.println("######################################### "+ result2.getResponse().getContentAsString());
        System.out.println("######################################### "+ result2.getResponse().getStatus());


        MvcResult result3 = mockMvc.perform(MockMvcRequestBuilders.post("/songsWS-sakvis/rest/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)).andReturn();

        System.out.println("######################################### "+ result3.getResponse().getContentAsString());
        System.out.println("######################################### "+ result3.getResponse().getStatus());*/
    }

}
