package dev.kosmx.githubDrawer

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.datetime.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File
import java.io.FileOutputStream
import java.util.Scanner


typealias TimeMap = List<Pair<LocalDate, String>>

@OptIn(ExperimentalSerializationApi::class)
fun main(args: Array<String>) {
    val parser = ArgParser("drawer")
    val auto by parser.option(ArgType.Boolean, shortName = "c", fullName = "cron", description = "auto mode, running from cron").default(false)
    parser.parse(args)

    val config = File("config.json").inputStream().use { Json.decodeFromStream<Config>(it) }

    val timeMap = File(config.mapPath).inputStream().use { Json.decodeFromStream<TimeMap>(it) }

    val now = Clock.System.now().toLocalDateTime(TimeZone.UTC).date

    val file = timeMap.find { it.first <= now } ?: error("No image for today")
    val row: Int = now.dayOfWeek.value % 7 // dayOfWeek -> 1(monday) - 7(saturday)
    val col: Int = ((now - DatePeriod(days = row)) - file.first).days / 7

    val px = Matrix(file.second)[col, row]

    println("row=$row, column=$col, committing $px times")

    if (!auto) {
        val scanner = Scanner(System.`in`)
        println("manual mode, please confirm! (true/false)")
        if (!scanner.nextBoolean()) {
            println("exiting")
            return@main
        }
    }

    val source by lazy {File("repo/.source").inputStream().use { it.readBytes() }}

    for (i in 0 until px) {
        commit(i, config, source)
    }
    push()
}

fun commit(idx: Int, config: Config, source: ByteArray) {
    val targetFile = File("repo/.target").bufferedReader().use { it.readLine() }
    val file = File("repo/$targetFile")
    val size = file.length()
    FileOutputStream(file, true).use { it.write(if (source.size > size) source[size.toInt()].toInt() else '\n'.code) }

    val now = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
    ProcessBuilder("git commit -am".split(" ") + "${now}:$idx" + "--author" + config.author).directory(File("repo")).start().waitFor()
}

fun push() {
    ProcessBuilder("git push".split(" ")).directory(File("repo")).start().waitFor()
}
