package dev.kosmx.githubDrawer

import kotlinx.datetime.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File
import java.io.FileOutputStream


typealias TimeMap = List<Pair<LocalDate, String>>

@OptIn(ExperimentalSerializationApi::class)
fun main() {
    val config = File("config.json").inputStream().use { Json.decodeFromStream<Config>(it) }

    val timeMap = File(config.mapPath).inputStream().use { Json.decodeFromStream<TimeMap>(it) }

    val now = Clock.System.now().toLocalDateTime(TimeZone.UTC).date

    val file = timeMap.find { it.first <= now } ?: error("No image for today")
    val row: Int = now.dayOfWeek.value % 7 // dayOfWeek -> 1(monday) - 7(saturday)
    val column: Int = ((now - DatePeriod(days = row)) - file.first).days / 7
    println("row=$row, column=$column")

    for (i in 0 .. 4) {
        commit(i)
    }
    push()
}

fun commit(idx: Int) {
    val targetFile = File("repo/.target").bufferedReader().use { it.readLine() }
    val source = File("repo/.source").inputStream().use { it.readBytes() }
    val file = File("repo/$targetFile")
    val size = file.length()
    FileOutputStream(file, true).use { it.write(if (source.size > size) source[size.toInt()].toInt() else '\n'.code) }

    val now = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
    ProcessBuilder("git commit -am".split(" ") + "${now}:$idx").directory(File("repo")).start().waitFor()
}

fun push() {

}
