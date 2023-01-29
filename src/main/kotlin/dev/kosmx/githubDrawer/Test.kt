package dev.kosmx.githubDrawer

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

fun main() {
    println(Json.encodeToJsonElement(Clock.System.now().toLocalDateTime(TimeZone.UTC).date to 12))
}