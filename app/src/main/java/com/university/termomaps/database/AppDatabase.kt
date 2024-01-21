package com.university.termomaps.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
  entities = [TermoMarker::class],
  version = 1,
)
abstract class AppDatabase: RoomDatabase() {
  abstract fun termoMarkerDao(): TermoMarkerDao
}