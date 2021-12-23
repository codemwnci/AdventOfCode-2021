import java.io.File

fun main() {
    Day22().part1()
    Day22().part2()
}

fun String.toRange():IntRange = this.split("..").let { IntRange(it[0].toInt(), it[1].toInt()) }


class Day22 {
    fun String.toRange():IntRange = this.split("..").let { IntRange(it[0].toInt(), it[1].toInt()) }

    data class Point3D(val x:Int, val y:Int, val z:Int)
    data class Cube(val xAxis:IntRange, val yAxis:IntRange, val zAxis:IntRange) {
        fun cubicVolume() = 1L * this.xAxis.count() * this.yAxis.count() * this.zAxis.count()
        fun overlaps(that: Cube) = (this.xAxis.first <= that.xAxis.last && this.xAxis.last >= that.xAxis.first) &&
                (this.yAxis.first <= that.yAxis.last && this.yAxis.last >= that.yAxis.first) &&
                (this.zAxis.first <= that.zAxis.last && this.zAxis.last >= that.zAxis.first)
        fun intersect(that: Cube) = if (!overlaps(that)) null else
            Cube(maxOf(this.xAxis.first, that.xAxis.first)..minOf(this.xAxis.last, that.xAxis.last),
                maxOf(this.yAxis.first, that.yAxis.first)..minOf(this.yAxis.last, that.yAxis.last),
                maxOf(this.zAxis.first, that.zAxis.first)..minOf(this.zAxis.last, that.zAxis.last))
    }
    data class Cuboid(val cube:Cube, val on:Boolean) {
        fun intersect(other: Cuboid) = if (this.cube.overlaps(other.cube)) Cuboid(this.cube.intersect(other.cube)!!, !on) else null
    }

    private val cubes = File("inputs/day22-sample.txt").readLines().map {
        val on = it.split(" ").first() == "on"
        val cube = it.split(" ").last().split(",").let {
            Cube(it[0].drop(2).toRange(), it[1].drop(2).toRange(), it[2].drop(2).toRange())
        }
        Cuboid(cube, on)
    }

    fun part1() {
        val region = Cube(-50..50, -50..50, -50..50)
        val onCubes = mutableSetOf<Point3D>()
        cubes.forEach {
            val (cube, on) = it
            // ignore everything outside the region for part 1
            if (cube.overlaps(region)) {
                cube.xAxis.forEach { x -> cube.yAxis.forEach { y -> cube.zAxis.forEach { z ->
                    if (on) onCubes.add(Point3D(x, y, z))
                    else onCubes.remove(Point3D(x, y, z))
                }}}
            }
        }

        println(onCubes.size)
    }

    fun part2() {
        fun findIntersections(a: Cube, b: Cube): Cube? = if (a.overlaps(b)) a.intersect(b) else null

        // Inspiration from https://todd.ginsberg.com/post/advent-of-code/2021/day22/
        // part 1 stored a physical representation of every cube. That would be too large for the full universe beyond the -50..50 cubed region in part 2
        // so instead, we will need to calculate the answer, rather than model it
        // 1. find all the volumes
        // 2. deduct duplications where volumes overlap (intersections)
        // 3. add them all together
        val volumes = mutableListOf<Cuboid>()

        cubes.forEach { cube ->
            volumes.addAll(volumes.mapNotNull { other -> findIntersections(cube.cube, other.cube)?.let { Cuboid(it, !other.on) } })
            if (cube.on) volumes.add(cube)
        }

        println( volumes.sumOf { it.cube.cubicVolume() * (if (it.on) 1 else -1) } )
    }
}