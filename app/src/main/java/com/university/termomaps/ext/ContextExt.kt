package com.university.termomaps.ext

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.university.termomaps.database.entity.TermoMapWithMarkers
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

fun Context.shareMap(json: String, mapName: String = "termo_markers") {
  shareJsonFile(json, "$mapName.json")
}

fun Context.shareMap(termoMapWithMarkers: TermoMapWithMarkers) {
  val json = Json.encodeToString(termoMapWithMarkers)
  shareJsonFile(json, "${termoMapWithMarkers.termoMap.name}.json")
}

fun Context.shareJsonFile(json: String, fileName: String) {
  val file = File(cacheDir, fileName)
  file.writeText(json)

  val contentUri = FileProvider.getUriForFile(this, "$packageName.fileprovider", file)

  val shareIntent = Intent().apply {
    action = Intent.ACTION_SEND
    putExtra(Intent.EXTRA_STREAM, contentUri)
    type = "application/json"
    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
  }

  startActivity(Intent.createChooser(shareIntent, "Share JSON file"))
}