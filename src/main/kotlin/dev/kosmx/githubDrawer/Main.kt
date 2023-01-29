package dev.kosmx.githubDrawer

import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File


typealias TimeMap = List<Pair<Instant, String>>

@OptIn(ExperimentalSerializationApi::class)
fun main(args: Array<String>) {
    val config = File("config.json").inputStream().use { Json.decodeFromStream<Config>(it) }

    val timeMap = File(config.mapPath).inputStream().use { Json.decodeFromStream<TimeMap>(it) }

}
