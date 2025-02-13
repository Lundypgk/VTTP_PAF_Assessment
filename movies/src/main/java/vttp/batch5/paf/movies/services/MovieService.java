package vttp.batch5.paf.movies.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import vttp.batch5.paf.movies.models.DirectorStatistics;
import vttp.batch5.paf.movies.models.MongoMovie;
import vttp.batch5.paf.movies.models.MySQLMovie;
import vttp.batch5.paf.movies.repositories.MongoMovieRepository;
import vttp.batch5.paf.movies.repositories.MySQLMovieRepository;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperPrint;
import java.io.ByteArrayInputStream; // For ByteArrayInputStream
import java.io.File; // For File

import net.sf.jasperreports.json.data.JsonDataSource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import net.sf.jasperreports.engine.JRException;

@Service
public class MovieService {

  // TODO: Task 2
  
    @Autowired
    private MySQLMovieRepository sqlrepo;

    @Autowired
    private MongoMovieRepository mongorepo;

public List<DirectorStatistics> getProlificDirectors(int count) {
    List<MongoMovie> mongoMovies = mongorepo.findAllMovies(); 
    // System.out.println("mongo movies: " + mongoMovies.size()); 
    List<MySQLMovie> mySqlMovies = sqlrepo.findAllMovies(); 
    // System.out.println("mysql movies: " + mySqlMovies.size()); 
    Map<String, MySQLMovie> mySqlMovieMap = mySqlMovies.stream()
        .collect(Collectors.toMap(MySQLMovie::getImdb_id, mySqlMovie -> mySqlMovie));
    // System.out.println("mysql movie map size: " + mySqlMovieMap.size()); 

    Map<String, List<MongoMovie>> directorsMap = new HashMap<>();
    for (MongoMovie movie : mongoMovies) {
        for (String director : movie.getDirectors()) { //  irectors is List<String>
            // pt in place cause of null values
            if (director != null && !director.trim().isEmpty()) {
                directorsMap.putIfAbsent(director, new ArrayList<>());
                directorsMap.get(director).add(movie);
            }
        }
    }
    List<DirectorStatistics> directorStats = new ArrayList<>();
    for (Map.Entry<String, List<MongoMovie>> entry : directorsMap.entrySet()) {
        String directorName = entry.getKey(); 
        List<MongoMovie> directedMovies = entry.getValue(); // get all movies directed director
        long totalRevenue = 0;
        long totalBudget = 0;

        for (MongoMovie mongoMovie : directedMovies) {
            MySQLMovie mySqlMovie = mySqlMovieMap.get(mongoMovie.getImdb_id()); 
            if (mySqlMovie != null) { //incase of magical errors
                totalRevenue += mySqlMovie.getRevenue(); 
                totalBudget += mySqlMovie.getBudget(); 
            }
        }
        //create DTO
        DirectorStatistics stats = new DirectorStatistics();
        stats.setDirectorName(directorName);
        stats.setMoviesCount(directedMovies.size());
        stats.setTotalRevenue(totalRevenue);
        stats.setTotalBudget(totalBudget);
        directorStats.add(stats);
    }

    return directorStats.stream()
        .sorted(Comparator.comparing(DirectorStatistics::getMoviesCount).reversed())
        .limit(count)
        .collect(Collectors.toList());
}


  // TODO: Task 4
  // You may change the signature of this method by passing any number of parameters
  // and returning any type
    public byte[] generatePDFReport(int count, String name, String batch) throws JRException, JsonProcessingException {
      List<DirectorStatistics> directorStatsList = getProlificDirectors(count);
      List<Map<String, Object>> directorStats = new ArrayList<>();

      for (DirectorStatistics ds : directorStatsList) {
          Map<String, Object> statsMap = new HashMap<>();
          statsMap.put("director", ds.getDirectorName());
          statsMap.put("count", ds.getMoviesCount());
          statsMap.put("revenue", ds.getTotalRevenue());
          statsMap.put("budget", ds.getTotalBudget());
          directorStats.add(statsMap);
      }

        // Overall report data source
        Map<String, Object> ORData = new HashMap<>();
        ORData.put("name", name);
        ORData.put("batch", batch);
        ObjectMapper objectMapper = new ObjectMapper();
        String ORJson = objectMapper.writeValueAsString(ORData);

        ByteArrayInputStream ORInputStream = new ByteArrayInputStream(ORJson.getBytes());
        JsonDataSource reportDS = new JsonDataSource(ORInputStream);

        // Director table dataset
        String directorJson = objectMapper.writeValueAsString(directorStats);
        ByteArrayInputStream directorInputStream = new ByteArrayInputStream(directorJson.getBytes());
        JsonDataSource directorDS = new JsonDataSource(directorInputStream);
        
        Map<String, Object> params = new HashMap<>();
        params.put("DIRECTOR_TABLE_DATASET", directorDS);
        
        File reportFile = new File("../data/director_movies_report.jrxml"); 
        JasperReport report = JasperCompileManager.compileReport(reportFile.getAbsolutePath());
        
        JasperPrint print = JasperFillManager.fillReport(report, params, reportDS);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(print, outputStream);

        return outputStream.toByteArray(); 
    }
}
