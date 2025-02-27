package com.example.test1;

import java.io.Serializable;

public class Movie implements Serializable
{
private String movieName;
private String movieLong ;
private String ageAllowed;
private String releaseDate;
private String description;
private String category;

    public Movie() {
    }

    @Override
    public String toString() {
        return "Movie{" +
                "movieName='" + movieName + '\'' +
                ", movieLong='" + movieLong + '\'' +
                ", ageAllowed='" + ageAllowed + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

    public Movie (String movieName, String releaseDate , String movieLong, String ageAllowed, String description , String category )
{
    this.movieName=movieName;
    this.movieLong=movieLong;
    this.ageAllowed=ageAllowed;
    this.releaseDate=releaseDate;
    this.description=description;
    this.category=category;
}
public String getCategory() {return category;}

public void setCategory(String category) {this.category = category;}


public String getDescription() {
    return description;
}

public void setDescription(String description) {
    this.description = description;
}

public String getReleaseDate() {
    return releaseDate;
}

public void setReleaseDate(String releaseDate) {
    this.releaseDate = releaseDate;
}

public String getAgeAllowed() {
    return ageAllowed;
}

public void setAgeAllowed(String ageAllowed) {
    this.ageAllowed = ageAllowed;
}

public String getMovieLong() {
    return movieLong;
}

public void setMovieLong(String movieLong) {
    this.movieLong = movieLong;
}

public String getMovieName() {
    return movieName;
}

public void setMovieName(String movieName) {
    this.movieName = movieName;
}
}
