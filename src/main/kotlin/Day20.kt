import java.io.File

// Solve this for the sample image pretty quickly - got stuck on the actual input image though
// kept getting a number too high
// With a little help from https://todd.ginsberg.com/post/advent-of-code/2021/day20/, when expanding into the infinite space
// dark pixels are always switched on, on the even iterations (because the zero-th algorithm index is "On"). I therefore
// needed to flip the default value if one was not found from false to true. The rest of the code is pretty concise and
// pretty easy to understand
fun main() {
    Day20().solve(2)
    Day20().solve(50)
}

@OptIn(ExperimentalStdlibApi::class)
class Day20 {
    data class Point(val x:Int, val y:Int)

    private val rawData = File("inputs/day20.txt").readLines()

    private val algorithm = rawData.first()
    var pixels = buildMap<Point, Boolean> {
        rawData.drop(2).forEachIndexed { y, str ->  str.forEachIndexed { x, c -> put(Point(x, y), c == '#') } }
    }.toMutableMap()


    fun getPixelAt(x:Int, y:Int, flip:Boolean = false) = pixels[Point(x, y)] ?: flip // if it is not in the map, regardless of being true/false, it is false (in the infinite space)
    fun getEnhancePixel(centreX:Int, centreY:Int, flip:Boolean): Boolean {
        val binary = StringBuilder()
        for (y in centreY-1 .. centreY+1) {
            for (x in centreX-1 .. centreX+1) {
                binary.append( if (getPixelAt(x, y, flip)) '1' else '0' )
            }
        }
        return algorithm[binary.toString().toInt(2)] == '#'
    }

    fun printImage() {
        val xAxis = pixels.keys.minOf { it.x } .. pixels.keys.maxOf { it.x }
        val yAxis = pixels.keys.minOf { it.y } .. pixels.keys.maxOf { it.y }

        for (y in yAxis) {
            println()
            for (x in xAxis) print(if (getPixelAt(x, y)) "#" else ".")
        }
    }

    fun solve(steps: Int) {
        repeat(steps) {
            // loop around 1 pixel wider in all directions to pick up pixels affected by the 3x3 pixels
            val minX = pixels.keys.minOf { it.x } - 1
            val maxX = pixels.keys.maxOf { it.x } + 1
            val minY = pixels.keys.minOf { it.y } - 1
            val maxY = pixels.keys.maxOf { it.y } + 1

            val newImage = mutableMapOf<Point, Boolean>()
            for (y in minY..maxY) {
                for (x in minX..maxX) {
                    newImage[Point(x, y)] = getEnhancePixel(x, y, it % 2 == 1)
                }
            }
            pixels = newImage
        }

        println(pixels.count { it.value })
    }
}