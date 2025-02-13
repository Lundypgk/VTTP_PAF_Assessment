
package vttp.batch5.paf.movies.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "imdb")
public class MongoMovie {
    @Id
    private String imdb_id; 
    private String tagline;
    private String title;
    private List<String> directors; 
    private List<String> genres; 
    private double imdb_rating;
    private long imdb_votes;
    private String overview;
    public double getImdb_rating() {
        return imdb_rating;
    }
    public void setImdb_rating(double imdb_rating) {
        this.imdb_rating = imdb_rating;
    }
    public long getImdb_votes() {
        return imdb_votes;
    }
    public void setImdb_votes(long imdb_votes) {
        this.imdb_votes = imdb_votes;
    }
    public String getOverview() {
        return overview;
    }
    public void setOverview(String overview) {
        this.overview = overview;
    }
    public String getTagline() {
        return tagline;
    }
    public void setTagline(String tagline) {
        this.tagline = tagline;
    }
    public String getImdb_id() {
        return imdb_id;
    }
    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }
    public List<String> getGenres() {
        return genres;
    }
    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<String> getDirectors() {
        return directors;
    }
    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }

}
