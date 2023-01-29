package dev.kosmx.githubDrawer

import java.io.File
import javax.imageio.ImageIO

class Matrix(filePath: String) {
    private val numbers: Array<IntArray>

    init {
        val imageStream = File(filePath).inputStream()
        val img = ImageIO.read(imageStream)

        numbers = Array(img.height) { IntArray(img.width) }
        (0 until img.height).forEach { y ->
            (0 until img.width).forEach { x ->
                numbers[y][x] = totalIntToOutput(img.getRGB(x, y))
            }
        }
        imageStream.close()
    }

    private fun totalIntToOutput(c: Int): Int {
        val red = ((c shr 16 and 0xFF) * 0.299).toInt()
        val green = ((c shr 8 and 0xFF) * 0.587).toInt()
        val blue = ((c and 0xFF) * 0.114).toInt()
        val finalGrayscale = red + green + blue
        val limits = arrayOf(0, 51, 102, 153, 204)
        return limits.indexOfLast { p -> p <= finalGrayscale }
    }

    val size: Pair<Int, Int>
        get() = numbers[0].size to numbers.size

    operator fun get(x: Int, y: Int): Int = numbers[y][x]
}