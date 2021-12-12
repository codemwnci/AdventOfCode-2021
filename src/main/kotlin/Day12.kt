import java.io.File

fun main() {
    Day12().puzzle1()
    Day12().puzzle2()
}

class Day12 {

    data class Node(val name: String, val isLarge: Boolean = false, var connections: List<Node> = mutableListOf())

    fun puzzle1() {
        val nodes = mutableMapOf<String, Node>()

        fun followPaths(node: Node, path: List<Node>): List<List<Node>> {
            val newPath = path+node
            return if (node.name == "end" || (path.contains(node) && !node.isLarge)) {
                listOf(newPath)
            } else {
                node.connections.flatMap {
                    followPaths(it, newPath)
                }
            }
        }

        File("inputs/day12.txt").readLines().forEach {
            val pathway = it.split("-").map { node ->
                if (!nodes.containsKey(node)) {
                    nodes[node] = Node(node, node.isUpperCase())
                }
                nodes[node]!!
            }

            pathway[0].connections += pathway[1]
            pathway[1].connections += pathway[0]
        }

        val start = nodes["start"]!!
        val paths = followPaths(start, listOf()).filter { it.last().name == "end" } // only keep nodes that actually end
        println("Puzzle 1 - Number of paths: ${paths.size}")

    }

    // my first attempt was causing StackOverflow errors -
    // resolved with inspiration from https://github.com/tginsberg/advent-2021-kotlin/blob/master/src/main/kotlin/com/ginsberg/advent2021/Day12.kt
    // my first attempt is commented out below
    fun puzzle2() {
        val nodes = HashMap<String, HashSet<String>>()

        File("inputs/day12.txt").readLines().forEach {
            val (a,b) = it.split("-")
            nodes.getOrPut(a) { HashSet() }.add(b)
            nodes.getOrPut(b) { HashSet() }.add(a)
        }

        fun canVisit(name: String, path: List<String>): Boolean =
            when {
                name.isUpperCase() -> true
                name == "start" -> false
                name !in path -> true
                else -> path.filterNot { it.isUpperCase() }.groupBy { it }.none { it.value.size == 2 }
            }

        fun followPaths(path: List<String> = listOf("start")): List<List<String>> =
            if (path.last() == "end") listOf(path)
            else nodes.getValue(path.last())
                .filter  { canVisit(it, path) }
                .flatMap { followPaths( path + it) }

        println("Puzzle 2 - Number of paths: ${followPaths().size}")
    }


//    fun puzzle2() {
//
//        fun followPaths(node: Node, path: String = "", smallCaveLoopbacks: Int = 0): List<String> {
//
//            val smallPathLoopback = (path.split(",").contains(node.name) && !node.isLarge)
//
//            val newPath = if (path.isEmpty()) path + "," + node.name else node.name
//
//            return if (node.name == "end" || (smallPathLoopback && smallCaveLoopbacks == 1)) {
//                listOf(newPath)
//            } else {
//                node.connections.flatMap {
//                    followPaths(it, newPath, if (smallPathLoopback) 1 else 0)
//                }
//            }
//        }
//
//        File("inputs/day12-sample.txt").readLines().forEach {
//            val pathway = it.split("-").map { node ->
//                if (!nodes.containsKey(node)) {
//                    nodes[node] = Node(node, node.isUpperCase())
//                }
//                nodes[node]!!
//            }
//
//            pathway[0].connections += pathway[1]
//            pathway[1].connections += pathway[0]
//        }
//
//        val start = nodes["start"]!!
//        val paths = followPaths(start).filter { it.split(",").last() == "end" } // only keep nodes that actually end
//        println("Number of paths: ${paths.size}")
//    }

}

private fun String.isUpperCase(): Boolean = this == this.uppercase()
