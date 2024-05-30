#!/usr/bin/env kotlin

@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("com.squareup.okhttp3:okhttp:4.10.0")
@file:DependsOn("com.squareup.okio:okio:3.9.0")
@file:DependsOn("com.squareup.moshi:moshi:1.15.1")
@file:DependsOn("com.squareup.moshi:moshi-adapters:1.15.1")
@file:DependsOn("com.squareup.moshi:moshi-kotlin:1.15.1")

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer
import java.util.*
import okhttp3.Request

/*
 * This script shows you how you can read a json file
 *  then parse it into a Kotlin data classes.
 *  It also shows you how to download a json file from the web
 *
 * run this script like so:

    $>  brew install kotlin
    $>  chmod +x 3.json.main.kts
    $>  ./3.json.main.kts
 */


/*
 * ****************************
 * Read from local json file
 * ****************************
 */
val jsonFeedFilePath = "./feed.json".toPath()

println("\uD83D\uDEE0️ ******  Load json file from \uD83D\uDCBE ****** \uD83D\uDEE0")
val jsonFile: String = FileSystem.SYSTEM
    .source(jsonFeedFilePath)
    .buffer()
    .readUtf8()

val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .add(Date::class.java, Rfc3339DateJsonAdapter())
    .build()

var feed: Feed = moshi
    .adapter(Feed::class.java)
    .fromJson(jsonFile) as Feed

println("🤖 local feed has ${feed.pages.count()} pages")

/*
 * ****************************
 * Download RSS json file
 * ****************************
 */
val jsonFeedUrl = "https://kau.sh/blog/feed.json"

println("\uD83D\uDEE0️ ******  Download blog feed json file from \uD83C\uDF0D ****** \uD83D\uDEE0")

OkHttpClient()
    .newCall(
            Request.Builder()
                    .url(jsonFeedUrl)
                    .build(),
    )
    .execute()
    .use { response ->
      feed = moshi
          .adapter(Feed::class.java)
          .fromJson(response.body!!.source()) as Feed
    }

println("🤖 remote found ${feed.pages.count()} pages")

data class Page(
  val title: String,
  @Json(name = "date_published")
  val publishedDate: Date,
  @Json(name = "file_path")
  val filePath: String?,
  val description: String?,
)

data class Feed(
  val title: String,
  val description: String,
  @Json(name = "items")
  val pages: List<Page>
)

