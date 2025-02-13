package vttp.batch5.paf.movies.repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.stereotype.Repository;
import vttp.batch5.paf.movies.models.ErrorLog;
import vttp.batch5.paf.movies.models.MongoMovie;
import vttp.batch5.paf.movies.models.Movie;

@Repository
public class MongoMovieRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public boolean checkConnection() {
        try {
            long count = mongoTemplate.getCollection("imdb").countDocuments();
            System.out.println("Connected to MongoDB. Document count: " + count);
            return true; 
        } catch (Exception e) {
            System.err.println("Failed to connect to MongoDB: " + e.getMessage());
            return false; 
        }
    }

 // TODO: Task 2.3
 // You can add any number of parameters and return any type from the method
 // You can throw any checked exceptions from the method
 // Write the native Mongo query you implement in the method in the comments
 //
 //    native MongoDB query here
 //
// db.imdb.insertMany([{ "imdb_id": "example",
//                              "tagline": "example",
//                              "title": "example",
//                              "directors": [example],
//                              "genres": [example],
//                              "imdb_rating": example,
//                              "imdb_votes": example,
//                              "overview": "example" },
//                              example])
 //
    public void batchInsertMovies(List<Movie> movieBatch) {
        checkConnection();
        List<MongoMovie> mongoMovies = new ArrayList<>();
        for (Movie movie : movieBatch) {
            MongoMovie mongoMovie = new MongoMovie();
            mongoMovie.setImdb_id(movie.getImdb_id());
            mongoMovie.setTagline(movie.getTagline());
            mongoMovie.setTitle(movie.getTitle());
            List<String> directors = Arrays.asList(movie.getDirector().split(",\\s*")); 
            mongoMovie.setDirectors(directors);
            List<String> genres = Arrays.asList(movie.getGenres().split(",\\s*")); 
            mongoMovie.setGenres(genres);
            mongoMovie.setImdb_rating(movie.getImdb_rating());
            mongoMovie.setImdb_votes(movie.getImdb_votes());
            mongoMovie.setOverview(movie.getOverview());
            mongoMovies.add(mongoMovie);
        }
        mongoTemplate.insert(mongoMovies, MongoMovie.class); 
    }

 // TODO: Task 2.4
 // You can add any number of parameters and return any type from the method
 // You can throw any checked exceptions from the method
 // Write the native Mongo query you implement in the method in the comments
 //
 //    native MongoDB query here
 //    db.errors.insertOne({ "imdb_ids": [example], 
 //                          "error": "example",
 //                          "timestamp": ISODate("example")
 //

    public void logError(List<String> imdbIds, String errorMessage, Date timestamp) {
        ErrorLog errorLog = new ErrorLog();
        errorLog.setImdb_ids(imdbIds);
        errorLog.setError(errorMessage);
        errorLog.setTimestamp(timestamp);
        mongoTemplate.insert(errorLog, "errors"); // Insert into the 'errors' collection
    }

 // TODO: Task 3
 // Write the native Mongo query you implement in the method in the comments
 //
 //    native MongoDB query here
 // .   db.imdb.findall({})
 //
    public List<MongoMovie> findAllMovies() {
        return mongoTemplate.findAll(MongoMovie.class, "imdb");
    }
}
