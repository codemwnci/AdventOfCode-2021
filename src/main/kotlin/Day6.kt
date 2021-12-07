import java.io.File


fun main() {
    Day6().puzzle1()
    Day6().puzzle2()
}

class Day6 {

    val fish = File("day6.txt").readLines().first().split(",").map{it.toInt()}.toMutableList()

    fun puzzle1() {

        // tick 80 times
        for(i in 1..80) {
            val newFish = mutableListOf<Int>()
            for (idx in fish.indices) {
                if (fish[idx] == 0) {
                    fish[idx] = 6
                    newFish.add(8)
                }
                else {
                    fish[idx]--
                }
            }
            fish.addAll(newFish)

            //println("After day $i: $fish")
        }
        println("Total Fish: ${fish.size}")
    }

    fun puzzle2() {
        data class NewFish(var age: Int, val numNewFish:Long)

        val spawnedFish = mutableListOf<NewFish>()

        // tick 256 times
        for(i in 1..256) {
            var newFish = 0L
            for (idx in fish.indices) {
                if (fish[idx] == 0) {
                    fish[idx] = 6
                    newFish++
                }
                else {
                    fish[idx]--
                }
            }
            // now loop the new spawned fish
            for(spawned in spawnedFish) {
                if (spawned.age == 0) {
                    newFish += spawned.numNewFish
                    spawned.age = 6
                }
                else {
                    spawned.age--
                }
            }

            spawnedFish.add(NewFish(8, newFish))
        }

        val total = spawnedFish.sumOf { it.numNewFish.toLong() } + fish.size
        println("Total Fish: $total")
    }

}