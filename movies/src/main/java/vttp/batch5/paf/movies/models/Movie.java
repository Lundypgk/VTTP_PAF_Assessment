package vttp.batch5.paf.movies.models;

public class Movie {
    private String title;
    private double vote_average;
    private int vote_count;
    private String status;
    private String release_date;
    private long revenue;
    private int runtime;
    private long budget;
    private String imdb_id;
    private String original_language;
    private String overview;
    private double popularity;
    private String tagline;
    private String genres;
    private String spoken_languages;
    private String casts;
    private String director;
    private double imdb_rating;
    private long imdb_votes;
    private String poster_path;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getCasts() {
        return casts;
    }
    public void setCasts(String casts) {
        this.casts = casts;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public long getBudget() {
        return budget;
    }
    public void setBudget(long budget) {
        this.budget = budget;
    }
    public String getGenres() {
        return genres;
    }
    public void setGenres(String genres) {
        this.genres = genres;
    }
    public long getRevenue() {
        return revenue;
    }
    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }
    public int getRuntime() {
        return runtime;
    }
    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }
    public String getImdb_id() {
        return imdb_id;
    }
    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }
    public String getTagline() {
        return tagline;
    }
    public void setTagline(String tagline) {
        this.tagline = tagline;
    }
    public String getOverview() {
        return overview;
    }
    public void setOverview(String overview) {
        this.overview = overview;
    }
    public String getDirector() {
        return director;
    }
    public void setDirector(String director) {
        this.director = director;
    }
    public int getVote_count() {
        return vote_count;
    }
    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }
    public double getPopularity() {
        return popularity;
    }
    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }
    public long getImdb_votes() {
        return imdb_votes;
    }
    public void setImdb_votes(long imdb_votes) {
        this.imdb_votes = imdb_votes;
    }
    public double getImdb_rating() {
        return imdb_rating;
    }
    public void setImdb_rating(double imdb_rating) {
        this.imdb_rating = imdb_rating;
    }
    public String getPoster_path() {
        return poster_path;
    }
    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }
    public double getVote_average() {
        return vote_average;
    }
    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }
    public String getRelease_date() {
        return release_date;
    }
    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
    public String getSpoken_languages() {
        return spoken_languages;
    }
    public void setSpoken_languages(String spoken_languages) {
        this.spoken_languages = spoken_languages;
    }
    public String getOriginal_language() {
        return original_language;
    }
    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }


}
