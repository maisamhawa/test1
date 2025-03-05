package com.example.test1;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Movie implements Parcelable
{
private String movieName;
private String movieLong ;
private String ageAllowed;
private String releaseDate;
private String description;
private String category;
private String photo;


    public Movie() {
    }

    protected Movie(Parcel in) {
        movieName = in.readString();
        movieLong = in.readString();
        ageAllowed = in.readString();
        releaseDate = in.readString();
        description = in.readString();
        category = in.readString();
        photo = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public String toString() {
        return "Movie{" +
                "movieName='" + movieName + '\'' +
                ", movieLong='" + movieLong + '\'' +
                ", ageAllowed='" + ageAllowed + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }


    public Movie (String movieName, String releaseDate , String movieLong, String ageAllowed, String description , String category ,String photo)
{
    this.movieName=movieName;
    this.movieLong=movieLong;
    this.ageAllowed=ageAllowed;
    this.releaseDate=releaseDate;
    this.description=description;
    this.category=category;
    this.photo=photo;
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
public String getphoto() {
        return photo;
    }
 public void setphoto(String photo) {
        photo = photo;
    }



    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(movieName);
        dest.writeString(movieLong);
        dest.writeString(ageAllowed);
        dest.writeString(releaseDate);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeString(photo);
    }
}
