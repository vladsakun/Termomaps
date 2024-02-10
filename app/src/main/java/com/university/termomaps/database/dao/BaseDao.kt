package com.university.termomaps.database.dao

import androidx.room.Delete
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

abstract class BaseDao<T> {

  abstract fun getAll(): Flow<List<T>>

  @Upsert
  abstract suspend fun upsert(entity: T)

  @Upsert
  abstract suspend fun upsert(entities: List<T>)

  @Delete
  abstract suspend fun delete(entity: T)

  @Delete
  abstract suspend fun deleteAll(entities: List<T>)

  abstract suspend fun deleteAll()
}