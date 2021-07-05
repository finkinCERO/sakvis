package htwb.ai.servlet;


import java.io.*;
import java.util.Enumeration;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import com.sun.istack.NotNull;
import htwb.ai.repository.SongsDao;
import htwb.ai.model.Song;

public class SongsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;


    int id = -1;

    private EntityManagerFactory emf;
    private static final String PERSISTENCE_UNIT_NAME = "songsDB-PU";

    public SongsServlet() {
        super();
        //readJSONToSongs("songs.json");
    }

    @Override
    public void init() throws ServletException {
        super.init();
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        List<Song> list = new SongsDao(emf).getSongs();
        System.out.println("########################################");
        System.out.println("how many songs in db? => " + list.size());
        System.out.println("########################################");
        if (list.size() == 0)
            this.initSongs();

        emf.close();
    }

    // GET SECTION
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("################### new get ######################");
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        SongsDao dao = new SongsDao(emf);

        response.setContentType("application/json");
        Boolean acceptJSON = this.acceptOnlyJson(request.getHeader("Accept"));

        if (acceptJSON) {
            if (request.getParameterMap().containsKey("all")) {
                this.getAll(dao, response);
            } else if (request.getParameterMap().containsKey("songId")) {
                try {

                    int id = Integer.parseInt(request.getParameter("songId"));
                    this.respondSong(id, dao, response);
                } catch (NumberFormatException ex) {

                    this.sendResponse("wrong format of id", response, HttpServletResponse.SC_BAD_REQUEST);
                }
            } else {
                this.sendResponse("not known parameter in request", response, HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {

            this.sendResponse("not acceptable header information in request", response, HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private boolean acceptOnlyJson(String header) {
        if (header.equals("application/json")) return true;
        else return false;
    }

    private void respondSong(int id, SongsDao dao, HttpServletResponse response) {
        Song song = dao.getSong(id);
        String jsonPayload = new Gson().toJson(song);
        PrintWriter out = null;

        try {
            out = response.getWriter();
            if (jsonPayload != null && !jsonPayload.equals("null")) {
                out.print(jsonPayload);
            } else {
                sendResponse("no song found with this id: " + id, response, HttpServletResponse.SC_NO_CONTENT);
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getAll(SongsDao dao, HttpServletResponse response) {
        List<Song> list = dao.getSongs();
        // 2.) pack it to json - imported Gson library to convert List to JSON
        //String payload = SongsToJSON();
        String jsonPayload = new Gson().toJson(list);
        // 3.) response it
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.print(jsonPayload);
        out.flush();
        out.close();
    }
    //
    //
    // POST SECTION

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("################### new post ######################");
        String artist = request.getParameter("artist");
        String label = request.getParameter("label");
        response.setContentType("application/json");
        if (checkParamsPost(request)) {
            try {
                String title = request.getParameter("title");
                String released = request.getParameter("released");
                System.out.println("released -> " + released);
                if (title != null && !title.equals("")) {

                    create(response, title, artist, label, released);
                } else {
                    sendResponse("set title to create new song", response, HttpServletResponse.SC_NOT_ACCEPTABLE);
                }

            } catch (NumberFormatException | NullPointerException e) {
                // return bad request 400 or something with wrong format
                sendResponse("", response, HttpServletResponse.SC_BAD_REQUEST);
                e.printStackTrace();
            }
        } else {
            sendResponse("", response, HttpServletResponse.SC_BAD_REQUEST);
        }


    }


    private boolean checkParamsPost(HttpServletRequest request) {
        boolean b = true;
        Enumeration<String> paramNames = request.getParameterNames();
        String i;
        while (paramNames.hasMoreElements()) {
            i = paramNames.nextElement();
            if (i.equals("title")) {

            } else if (i.equals("artist")) {

            } else if (i.equals("label")) {

            } else if (i.equals("released")) {

            } else {
                return false;
            }
        }
        return true;
    }

    // HELPER
    private void create(HttpServletResponse response, @NotNull String title, String artist, String label, String released) {
        try {
            Song song = new Song();
            song.setTitle(title);
            song.setArtist(artist);
            song.setLabel(label);
            Integer _released = null;
            if (released != null) {
                _released = Integer.parseInt(released);
                song.setReleased(_released);
            } else song.setReleased(null);

            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            SongsDao dao = new SongsDao(emf);

            Integer newId = dao.generateId().intValue();
            song.setId(newId);
            Integer id = dao.save(song);
            Song existing = dao.getSong(id);

            try (PrintWriter out = response.getWriter()) {
                if (existing == null) {
                    sendResponse("parameter to large", response, HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
                } else if (id != null) {
                    //fetcht alles was hinter dem Fragezeichen (die Parameter) im URL kommt
                    response.setHeader("Location", "/songsservlet/songs?id= " + id);
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    out.flush();
                } else {
                    sendResponse("", response, HttpServletResponse.SC_CONFLICT);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (PersistenceException pex) {
            // TODO Auto-generated catch block
            System.out.println("###ERROR### PersistenceException: " + pex.getMessage());
        } finally {
            if (emf != null) {
                emf.close();
            }
        }

    }

    public void sendResponse(String payload, HttpServletResponse response, int status) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            response.setContentType("application/json");
            response.setStatus(status);
            out.write(payload);
            out.flush();
        } finally {
            response.getWriter().close();
        }
    }

    private boolean initSongs() {
        try {
            this.emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            SongsDao dao = new SongsDao(this.emf);
            List<Song> songs = readJSONToSongs("src/main/resources/songs.json");

            for (Song song : songs) {
                System.out.println();
                System.out.println("song -> " + song.toSting());
                Integer jsonId = song.getId();
                //song.setId(null);

                Integer oldId = dao.save(song);
                // here set id
                System.out.println("###################");
                System.out.println("id -> " + oldId + ",json id -> " + jsonId);
                //dao.replaceId(oldId, jsonId);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private static List<Song> readJSONToSongs(String filename) throws FileNotFoundException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream is = new BufferedInputStream(new FileInputStream(filename))) {
            return (List<Song>) objectMapper.readValue(is, new TypeReference<List<Song>>() {
            });
        }
    }
}