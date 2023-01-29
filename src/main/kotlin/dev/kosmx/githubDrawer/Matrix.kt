package dev.kosmx.githubDrawer

import java.awt.Canvas
import javax.imageio.ImageIO

class Matrix {
    private lateinit var numbers: Array<IntArray>

    fun Matrix(filePath: String){
        val imagestream = Canvas::class.java.getResourceAsStream(filePath)
        val img = ImageIO.read(imagestream)

        numbers = Array(img.height){IntArray(img.width)}
        (0 until img.height).forEach { y ->
            (0 until img.width).forEach { x ->
                numbers[y][x] = totalIntToOutput(img.getRGB(x,y))
            }
        }
    }

    private fun totalIntToOutput(c: Int): Int {
        val red = ((c shr 16 and 0xFF) * 0.299).toInt()
        val green = ((c shr 8 and 0xFF) * 0.587).toInt()
        val blue = ((c and 0xFF) * 0.114).toInt()
        val finalGrayscale = red+green+blue
        val limits = arrayOf(0, 51, 102, 153, 204)
        return limits.indexOfLast { p -> p > finalGrayscale }
    }

    operator fun get(x: Int, y: Int): Int { return numbers[y][x] }
}