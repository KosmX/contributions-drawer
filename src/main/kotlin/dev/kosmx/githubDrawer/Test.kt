@file:JvmName("Test")
package dev.kosmx.githubDrawer

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required

fun main(args: Array<String>) {
    val parser = ArgParser("GitHub drawer test mode")
    val fileName by parser.option(ArgType.String, shortName = "f", fullName = "file", description = "input file").required()
    parser.parse(args)

    val chars = arrayOf(' ', '░', '▒', '▓', '█')

    val matrix = Matrix(fileName)
    for (y in 0 until matrix.size.second) {
        for (x in 0 until matrix.size.first) {
            val char = chars[matrix[x, y]]
            print("$char$char")
        }
        println()
    }
}
