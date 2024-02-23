package com.university.termomaps.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.university.termomaps.database.dao.TermoMapDao
import com.university.termomaps.database.dao.TermoMarkerDao
import com.university.termomaps.database.entity.TermoMap
import com.university.termomaps.database.entity.marker.TermoMarker

@Database(
  entities = [TermoMarker::class, TermoMap::class],
  version = 1,
)
abstract class AppDatabase: RoomDatabase() {
  abstract fun termoMarkerDao(): TermoMarkerDao
  abstract fun termoMapDao(): TermoMapDao
}