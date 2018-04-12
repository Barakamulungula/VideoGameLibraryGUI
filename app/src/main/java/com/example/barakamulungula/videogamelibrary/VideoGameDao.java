package com.example.barakamulungula.videogamelibrary;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface VideoGameDao {

    //allow us to get all video games
    @Query("SELECT * FROM VIDEOGAME")
    List<VideoGame> getVideoGames();

    //allows to add a single game to the list
    @Insert
    void addVideoGame(VideoGame getvideoGame);

    //allows us to update the values of an existing game
    @Update
    void updateVideoGame(VideoGame videoGame);

    //allows us to delete a game from the library
    @Delete
    void deleteVideoGame(VideoGame videoGame);

}
