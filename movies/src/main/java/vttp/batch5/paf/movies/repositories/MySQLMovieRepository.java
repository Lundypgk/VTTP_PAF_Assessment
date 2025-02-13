package vttp.batch5.paf.movies.repositories;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import vttp.batch5.paf.movies.models.Movie;
import vttp.batch5.paf.movies.models.MySQLMovie;
@Repository
public class MySQLMovieRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MongoMovieRepository mongoRepo;

  public boolean dataLoaded() {
        String sql = "SELECT COUNT(*) FROM imdb";
        Integer rowCount = jdbcTemplate.queryForObject(sql, Integer.class);
        return rowCount != null && rowCount > 0; 
  }


  // TODO: Task 2.3
  // You can add any number of parameters and return any type from the method
    @Transactional
    public void batchInsertMovies(List<Movie> movieBatch) {
        String insertSQL = "INSERT INTO imdb (imdb_id, vote_average, vote_count, release_date, revenue, budget, runtime) VALUES (?, ?, ?, ?, ?, ?, ?) " ;
        try {
          jdbcTemplate.batchUpdate(insertSQL, new BatchPreparedStatementSetter() {
              public void setValues(PreparedStatement pst, int i) throws SQLException {
                    Movie movie = movieBatch.get(i);
                    pst.setString(1, movie.getImdb_id());
                    pst.setDouble(2, movie.getVote_average());
                    pst.setInt(3, movie.getVote_count());
                    pst.setString(4, movie.getRelease_date());
                    pst.setLong(5, movie.getRevenue());
                    pst.setLong(6, movie.getBudget());
                    pst.setInt(7, movie.getRuntime());
                }
                public int getBatchSize() {
                    return movieBatch.size(); 
                }
            });
        } catch (Exception e) {
            List<String> imdbIds = movieBatch.stream()
                                              .map(Movie::getImdb_id)
                                              .collect(Collectors.toList());
            String errorMessage = e.getMessage();
            Date timestamp = new Date();
            mongoRepo.logError(imdbIds, errorMessage, timestamp); 
            throw e; 
        }
    }

  // TODO: Task 3
  public List<MySQLMovie> findAllMovies() {
      String sql = "SELECT * FROM imdb"; 
      return jdbcTemplate.query(sql, (rs, rowNum) -> {
          MySQLMovie movie = new MySQLMovie();
          movie.setImdb_id(rs.getString("imdb_id"));
          movie.setVote_average(rs.getDouble("vote_average"));
          movie.setVote_count(rs.getInt("vote_count"));
          movie.setRelease_date(rs.getString("release_date"));
          movie.setRevenue(rs.getLong("revenue"));
          movie.setBudget(rs.getLong("budget"));
          movie.setRuntime(rs.getInt("runtime"));
          return movie;
      });
  }

}
