package dev.kosmx.githubDrawer

import kotlinx.serialization.Serializable

@Serializable
data class Config(val token: String, val mapPath: String = "timeMap.json")
