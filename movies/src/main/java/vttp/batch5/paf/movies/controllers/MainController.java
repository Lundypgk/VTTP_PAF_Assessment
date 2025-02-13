package vttp.batch5.paf.movies.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import net.sf.jasperreports.engine.JRException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vttp.batch5.paf.movies.models.DirectorStatistics;
import vttp.batch5.paf.movies.services.MovieService;

import java.io.IOException;                       
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@RestController
public class MainController {

  @Autowired
  private MovieService movieService;  // TODO: Task 3

    @GetMapping("/api/summary")
    public ResponseEntity<List<Map<String, Object>>> getProlificDirectors(int count) {
        List<DirectorStatistics> directorStats = movieService.getProlificDirectors(count);
        List<Map<String, Object>> response = new ArrayList<>();

        for (DirectorStatistics ds : directorStats) {
            Map<String, Object> directorMap = new HashMap<>();
            directorMap.put("director_name", ds.getDirectorName());
            directorMap.put("movies_count", ds.getMoviesCount());
            directorMap.put("total_revenue", ds.getTotalRevenue());
            directorMap.put("total_budget", ds.getTotalBudget());
            response.add(directorMap);
        }

        return ResponseEntity.ok(response); 
    }

  
  // TODO: Task 4
    @GetMapping("/api/summary/pdf")
    public ResponseEntity<byte[]> generatePDFReport(
        int count,
        @RequestParam(defaultValue ="Lundy") String name,
        @RequestParam(defaultValue = "A") String batch
    ) {
        try {
            // Generate the PDF report
            byte[] pdfContent = movieService.generatePDFReport(count, name, batch);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "director_movies_report.pdf");

            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (JRException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); 
        }
    }

}
