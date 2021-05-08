package edu.nyu.se2440.loadmovies;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.nyu.se2440.loadmovies.models.MovieModel;
import edu.nyu.se2440.loadmovies.models.TMMovieModel;
import edu.nyu.se2440.loadmovies.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.*;
import java.util.Properties;

public class LoadMovies {

    private static final Logger logger = LoggerFactory.getLogger(LoadMovies.class);

    public static void main(String[] args) throws Exception {

        InputStream propStream = new FileInputStream("src/main/resources/application.properties");

        Properties props = new Properties();
        props.load(propStream);

        FileWriter writer = new FileWriter(props.getProperty("movie.data.outfile"));
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        
        Connection connection = connectToDatabase(props);

        File in = new File(props.getProperty("movie.json.infile"));
        BufferedReader input = new BufferedReader(new FileReader(in));

        int i=0;

        ObjectMapper mapper = new ObjectMapper();

        int skippedCount = 0;
        int insertCount = 0;

        String line;
        while ((line = input.readLine()) != null)
        {
            line = line.trim();

            TMMovieModel tmMoviemodel = null;
            try {
                tmMoviemodel = mapper.readValue(line, TMMovieModel.class);

//                String text = mapper.writeValueAsString(tmMoviemodel);
//                System.out.println("Converted: " + text);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                skippedCount++;
                continue;
            }

            if (tmMoviemodel.getImdb_id() == null || tmMoviemodel.getImdb_id().trim().length() == 0)
            {
                i++;
                skippedCount++;
                continue;
            }

            MovieModel movie = null;
            try {
                movie = Utils.mapToMovieModel(tmMoviemodel);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                i++;
                skippedCount++;
                continue;
            }

            Boolean existed = loadMovieFromDB(connection, movie);

            if (existed)
            {
                logger.info("Movie ID: " + movie.getMovieId() + " already exist");
                continue;
            }

            insertMovie(connection, movie, bufferedWriter);
            insertCount++;

            logger.info("Inserted Movie ID: " + movie.getMovieId());

            i++;
        }

        logger.info("Insert " + insertCount + " movies");

        input.close();

        bufferedWriter.close();
    }

    private static Connection connectToDatabase(Properties props) throws ClassNotFoundException, SQLException {

        Class.forName(props.getProperty("db.driver"));

        String server = props.getProperty("db.server");
        String database = props.getProperty("db.database");
        String connectionStr = "jdbc:mysql://" + server + "/" + database + "?serverTimezone=UTC";

//        logger.info("Connection: " + connectionStr);
        Connection connection = DriverManager.getConnection(connectionStr,
                props.getProperty("db.username"), props.getProperty("db.password"));

        return connection;
    }

    public static boolean loadMovieFromDB(Connection connection, MovieModel model) throws Exception
    {
        String query = "SELECT id FROM movies where id=?";

        PreparedStatement ps = connection.prepareStatement(query);

        ps.setString(1, model.getMovieId());

        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    private static void insertMovie(Connection connection, MovieModel movie, BufferedWriter bufferedWriter)
            throws Exception
    {
        String nameStr = "INSERT INTO movies (id";
        String valStr = ") VALUES ('" + movie.getMovieId() + "'";

        if (movie.getTitle() != null) {
            nameStr += ",title";
            valStr += ",?";
        }

        if (movie.getYear() != null)
        {
            nameStr += ",year";
            valStr += ",?";
        }

        if (movie.getDirector() != null)
        {
            nameStr += ",director";
            valStr += ",?";
        }

        if (movie.getRevenue() != null)
        {
            nameStr += ",revenue";
            valStr += ",?";
        }

        if (movie.getRating() != null)
        {
            nameStr += ",rating";
            valStr += ",?";
        }

        if (movie.getNumVotes() != null)
        {
            nameStr += ",numVotes";
            valStr += ",?";
        }

        if (movie.getOverview() != null)
        {
            nameStr += ",overview";
            valStr += ",?";
        }


        valStr += ");";

        String query = nameStr + valStr;

        PreparedStatement ps = connection.prepareStatement(query);

        int col = 1;

        if (movie.getTitle() != null) {
            ps.setString(col++, movie.getTitle());
        }

        if (movie.getYear() != null)
        {
            ps.setInt(col++, movie.getYear());
        }

        if (movie.getDirector() != null)
        {
            ps.setString(col++, movie.getDirector());
        }

        if (movie.getRevenue() != null)
        {
            ps.setInt(col++, movie.getRevenue());
        }

        if (movie.getRating() != null)
        {
            ps.setFloat(col++, movie.getRating());
        }

        if (movie.getNumVotes() != null)
        {
            ps.setInt(col++, movie.getNumVotes());
        }

        if (movie.getOverview() != null)
        {
            ps.setString(col++, movie.getOverview());
        }

        bufferedWriter.write(ps.toString().substring(43) + "\n");

        ps.execute();
    }


}
