package vttp.batch5.paf.movies.models;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "errors")
public class ErrorLog {
    @Id
    private String id; 
    private List<String> imdb_ids;
    private String error;
    private Date timestamp;

    // Getters and Setters
    public List<String> getImdb_ids() {
        return imdb_ids;
    }

    public void setImdb_ids(List<String> imdb_ids) {
        this.imdb_ids = imdb_ids;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
