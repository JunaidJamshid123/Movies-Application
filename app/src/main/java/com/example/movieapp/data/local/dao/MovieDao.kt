package com.example.movieapp.data.local.dao
import androidx.room.*
import com.example.movieapp.data.local.entities.MovieEntity
import com.example.movieapp.data.local.entities.MovieDetailsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    // ========== MOVIE LIST OPERATIONS ==========

    @Query("SELECT * FROM movies ORDER BY popularity DESC")
    fun getAllMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE isFavorite = 1 ORDER BY addedAt DESC")
    fun getFavoriteMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): MovieEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Update
    suspend fun updateMovie(movie: MovieEntity)

    @Delete
    suspend fun deleteMovie(movie: MovieEntity)

    @Query("DELETE FROM movies")
    suspend fun clearAllMovies()

    // Toggle Favorite
    @Query("UPDATE movies SET isFavorite = :isFavorite WHERE id = :movieId")
    suspend fun updateFavoriteStatus(movieId: Int, isFavorite: Boolean)

    @Query("SELECT EXISTS(SELECT 1 FROM movies WHERE id = :movieId AND isFavorite = 1)")
    fun isMovieFavorite(movieId: Int): Flow<Boolean>


    // ========== MOVIE DETAILS OPERATIONS ==========

    @Query("SELECT * FROM movie_details WHERE id = :movieId")
    suspend fun getMovieDetails(movieId: Int): MovieDetailsEntity?

    @Query("SELECT * FROM movie_details WHERE id = :movieId")
    fun getMovieDetailsFlow(movieId: Int): Flow<MovieDetailsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetails(movieDetails: MovieDetailsEntity)

    @Update
    suspend fun updateMovieDetails(movieDetails: MovieDetailsEntity)

    @Delete
    suspend fun deleteMovieDetails(movieDetails: MovieDetailsEntity)

    @Query("DELETE FROM movie_details WHERE id = :movieId")
    suspend fun deleteMovieDetailsById(movieId: Int)

    @Query("DELETE FROM movie_details")
    suspend fun clearAllMovieDetails()

    // Toggle Favorite in Details
    @Query("UPDATE movie_details SET isFavorite = :isFavorite WHERE id = :movieId")
    suspend fun updateMovieDetailsFavoriteStatus(movieId: Int, isFavorite: Boolean)

    @Query("SELECT EXISTS(SELECT 1 FROM movie_details WHERE id = :movieId AND isFavorite = 1)")
    fun isMovieDetailsFavorite(movieId: Int): Flow<Boolean>

    // Clear old cache (older than 24 hours)
    @Query("DELETE FROM movie_details WHERE cachedAt < :timestamp")
    suspend fun clearOldMovieDetailsCache(timestamp: Long)
}