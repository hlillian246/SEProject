package edu.nyu.se2440.movie;

import edu.nyu.se2440.movie.core.MovieShareService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MovieShareApplication {

    private static final Logger logger = LoggerFactory.getLogger(MovieShareApplication.class);

    public static void main(String[] args) throws IOException {

        logger.info("Movie Share Application");

        InputStream propStream = new FileInputStream("src/main/resources/application.properties");

        Properties props = new Properties();
        props.load(propStream);

        MovieShareService peerService = new MovieShareService(props);
    }
}
