package prueba.pruebamoviesfirebase.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import prueba.pruebamoviesfirebase.details.presentation.comments.dao.CommentDao
import prueba.pruebamoviesfirebase.details.presentation.comments.dao.CommentDatabase
import prueba.pruebamoviesfirebase.movieList.data.local.movie.MovieDatabase
import prueba.pruebamoviesfirebase.movieList.data.remote.MovieApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @Provides
    @Singleton
    fun providesMovieApi() : MovieApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(MovieApi.BASE_URL)
            .client(client)
            .build()
            .create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun providesMovieDatabase(app: Application): MovieDatabase {
        return Room.databaseBuilder(
            app,
            MovieDatabase::class.java,
            "moviedb.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCommentDao(database: CommentDatabase): CommentDao {
        return database.dao
    }

    @Provides
    @Singleton
    fun providesCommentDatabase(app: Application): CommentDatabase {
        return Room.databaseBuilder(
            app,
            CommentDatabase::class.java,
            "comments.db"
        ).build()
    }

}