package com.university.termomaps.ext

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

fun Context.shareJsonFile(json: String) {
  val file = File(cacheDir, "termo_markers.json")
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