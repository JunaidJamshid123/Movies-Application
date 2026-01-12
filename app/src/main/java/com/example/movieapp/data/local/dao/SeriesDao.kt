package com.example.movieapp.data.local.dao
import androidx.room.*
import com.example.movieapp.data.local.entities.SeriesEntity
import com.example.movieapp.data.local.entities.SeriesDetailsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SeriesDao {

    // ========== SERIES LIST OPERATIONS ==========

    @Query("SELECT * FROM series ORDER BY popularity DESC")
    fun getAllSeries(): Flow<List<SeriesEntity>>

    @Query("SELECT * FROM series WHERE isFavorite = 1 ORDER BY addedAt DESC")
    fun getFavoriteSeries(): Flow<List<SeriesEntity>>

    @Query("SELECT * FROM series WHERE id = :seriesId")
    suspend fun getSeriesById(seriesId: Int): SeriesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeries(series: SeriesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultipleSeries(series: List<SeriesEntity>)

    @Update
    suspend fun updateSeries(series: SeriesEntity)

    @Delete
    suspend fun deleteSeries(series: SeriesEntity)

    @Query("DELETE FROM series")
    suspend fun clearAllSeries()

    // Toggle Favorite
    @Query("UPDATE series SET isFavorite = :isFavorite WHERE id = :seriesId")
    suspend fun updateFavoriteStatus(seriesId: Int, isFavorite: Boolean)

    @Query("SELECT EXISTS(SELECT 1 FROM series WHERE id = :seriesId AND isFavorite = 1)")
    fun isSeriesFavorite(seriesId: Int): Flow<Boolean>


    // ========== SERIES DETAILS OPERATIONS ==========

    @Query("SELECT * FROM series_details WHERE id = :seriesId")
    suspend fun getSeriesDetails(seriesId: Int): SeriesDetailsEntity?

    @Query("SELECT * FROM series_details WHERE id = :seriesId")
    fun getSeriesDetailsFlow(seriesId: Int): Flow<SeriesDetailsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeriesDetails(seriesDetails: SeriesDetailsEntity)

    @Update
    suspend fun updateSeriesDetails(seriesDetails: SeriesDetailsEntity)

    @Delete
    suspend fun deleteSeriesDetails(seriesDetails: SeriesDetailsEntity)

    @Query("DELETE FROM series_details WHERE id = :seriesId")
    suspend fun deleteSeriesDetailsById(seriesId: Int)

    @Query("DELETE FROM series_details")
    suspend fun clearAllSeriesDetails()

    // Toggle Favorite in Details
    @Query("UPDATE series_details SET isFavorite = :isFavorite WHERE id = :seriesId")
    suspend fun updateSeriesDetailsFavoriteStatus(seriesId: Int, isFavorite: Boolean)

    @Query("SELECT EXISTS(SELECT 1 FROM series_details WHERE id = :seriesId AND isFavorite = 1)")
    fun isSeriesDetailsFavorite(seriesId: Int): Flow<Boolean>

    // Clear old cache (older than 24 hours)
    @Query("DELETE FROM series_details WHERE cachedAt < :timestamp")
    suspend fun clearOldSeriesDetailsCache(timestamp: Long)
}