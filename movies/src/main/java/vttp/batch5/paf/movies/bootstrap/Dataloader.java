package vttp.batch5.paf.movies.bootstrap;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStreamReader;
import vttp.batch5.paf.movies.models.Movie;
import org.springframework.stereotype.Component;

import vttp.batch5.paf.movies.repositories.MySQLMovieRepository;
import vttp.batch5.paf.movies.repositories.MongoMovieRepository;
@Component
public class Dataloader {

  //TODO: Task 2
    private static final String ZIP_FILE_PATH = "../data/movies_post_2010.zip";
    private static final String JSON_FILE_NAME = "movies_post_2010.json";

    @Autowired
    private MySQLMovieRepository sqlrepo;
    @Autowired
    private MongoMovieRepository mongorepo;

    public void loadData() {
        if (sqlrepo.dataLoaded()) {
            System.out.println("Data has already been loaded.");
            return;
        }
        System.out.println("Data was not loaded... loading now");
        loadMoviesFromZip(ZIP_FILE_PATH, JSON_FILE_NAME);
    }

    public void loadMoviesFromZip(String zipFilePath, String jsonFileName) {
        List<Movie> movieBatch = new ArrayList<>();
        //prevent duplicates
        Set<String> processedIds = new HashSet<>();
        int batchSize = 25;

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;
            ObjectMapper objectMapper = new ObjectMapper();
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals(jsonFileName)) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(zis));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        Movie movie = objectMapper.readValue(line, Movie.class);
                        if (!processedIds.contains(movie.getImdb_id()) && isReleasedAfter2018(movie)) {
                            processedIds.add(movie.getImdb_id()); // Add ID to the set
                            movieBatch.add(movie);
                            if (movieBatch.size() == batchSize) {
                                saveBatchToDatabases(movieBatch);
                                movieBatch.clear();
                            }
                        }
                    }
                    //remaining
                    if (!movieBatch.isEmpty()) {
                        saveBatchToDatabases(movieBatch);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveBatchToDatabases(List<Movie> movieBatch) {
        mongorepo.checkConnection();
        sqlrepo.batchInsertMovies(movieBatch);
        mongorepo.batchInsertMovies(movieBatch);

    }

    private boolean isReleasedAfter2018(Movie movie) {
        String releaseDateStr = movie.getRelease_date();
        if (releaseDateStr != null) {
            String yearStr = releaseDateStr.split("-")[0];
            int year = Integer.parseInt(yearStr);
            return year >= 2018;
        }
        return false;
    }


}
