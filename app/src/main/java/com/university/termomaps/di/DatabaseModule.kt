package com.university.termomaps.di

import android.content.Context
import androidx.room.Room
import com.university.termomaps.database.AppDatabase
import com.university.termomaps.database.dao.TermoMapDao
import com.university.termomaps.database.dao.TermoMarkerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

  @Provides
  @Singleton
  fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
    return Room.databaseBuilder(
      context,
      AppDatabase::class.java,
      "termo-map-db"
    ).build()
  }

  @Provides
  fun provideTermoMarketDao(database: AppDatabase): TermoMarkerDao = database.termoMarkerDao()

  @Provides
  fun provideTermoMapDao(database: AppDatabase): TermoMapDao = database.termoMapDao()
}