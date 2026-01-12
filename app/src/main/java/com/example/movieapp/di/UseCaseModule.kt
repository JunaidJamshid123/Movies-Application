package com.example.movieapp.di

import com.example.movieapp.domain.repository.MovieRepository
import com.example.movieapp.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    // Movie UseCases
    @Provides
    @Singleton
    fun provideGetPopularMoviesUseCase(repository: MovieRepository): GetPopularMoviesUseCase {
        return GetPopularMoviesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetTrendingMoviesUseCase(repository: MovieRepository): GetTrendingMoviesUseCase {
        return GetTrendingMoviesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetMovieDetailsUseCase(repository: MovieRepository): GetMovieDetailsUseCase {
        return GetMovieDetailsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetMoviesUseCase(repository: MovieRepository): GetMoviesUseCase {
        return GetMoviesUseCase(repository)
    }

    // Series UseCases
    @Provides
    @Singleton
    fun provideGetPopularSeriesUseCase(repository: MovieRepository): GetPopularSeriesUseCase {
        return GetPopularSeriesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetTrendingSeriesUseCase(repository: MovieRepository): GetTrendingSeriesUseCase {
        return GetTrendingSeriesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetSeriesDetailsUseCase(repository: MovieRepository): GetSeriesDetailsUseCase {
        return GetSeriesDetailsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetSeriesUseCase(repository: MovieRepository): GetSeriesUseCase {
        return GetSeriesUseCase(repository)
    }

    // Favorites UseCases
    @Provides
    @Singleton
    fun provideAddMovieToFavoritesUseCase(repository: MovieRepository): AddMovieToFavoritesUseCase {
        return AddMovieToFavoritesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRemoveMovieFromFavoritesUseCase(repository: MovieRepository): RemoveMovieFromFavoritesUseCase {
        return RemoveMovieFromFavoritesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetFavoriteMoviesUseCase(repository: MovieRepository): GetFavoriteMoviesUseCase {
        return GetFavoriteMoviesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideIsMovieFavoriteUseCase(repository: MovieRepository): IsMovieFavoriteUseCase {
        return IsMovieFavoriteUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideToggleMovieFavoriteUseCase(repository: MovieRepository): ToggleMovieFavoriteUseCase {
        return ToggleMovieFavoriteUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAddSeriesToFavoritesUseCase(repository: MovieRepository): AddSeriesToFavoritesUseCase {
        return AddSeriesToFavoritesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRemoveSeriesFromFavoritesUseCase(repository: MovieRepository): RemoveSeriesFromFavoritesUseCase {
        return RemoveSeriesFromFavoritesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetFavoriteSeriesUseCase(repository: MovieRepository): GetFavoriteSeriesUseCase {
        return GetFavoriteSeriesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideIsSeriesFavoriteUseCase(repository: MovieRepository): IsSeriesFavoriteUseCase {
        return IsSeriesFavoriteUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideToggleSeriesFavoriteUseCase(repository: MovieRepository): ToggleSeriesFavoriteUseCase {
        return ToggleSeriesFavoriteUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetFavoritesUseCase(repository: MovieRepository): GetFavoritesUseCase {
        return GetFavoritesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAddOrRemoveFavoriteUseCase(repository: MovieRepository): AddOrRemoveFavoriteUseCase {
        return AddOrRemoveFavoriteUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetHomeDataUseCase(repository: MovieRepository): GetHomeDataUseCase {
        return GetHomeDataUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetTrendingUseCase(repository: MovieRepository): GetTrendingUseCase {
        return GetTrendingUseCase(repository)
    }
}