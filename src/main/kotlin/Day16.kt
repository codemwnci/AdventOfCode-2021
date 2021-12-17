import java.io.File

fun main() {
    Day16().puzzle1()
    Day16().puzzle2()
}

// This one is a bit messier than I would like
// I changed my approach a few times, and a bit of refactoring would tidy this up a lot
// Kotlin was a MASSIVE help on this one

class Day16 {
    private val filedata = File("inputs/day16.txt").readLines().first()
    private val binary = filedata.fold("") {acc, c -> acc + c.toHexVal() }
    private val bits = BitsReader(binary)

    class BitsReader(val bits: String) {
        private var cursor: Int = 0
        fun takeNext(num: Int): String {
            val res = bits.substring(cursor, cursor+num)
            cursor += num
            return res
        }

        fun calcTarget(len: Int): Int = cursor + len
        fun hasReachedCursor(cursorTarget: Int): Boolean = cursor >= cursorTarget
    }

    sealed class Packet(val version: Int) {
        abstract fun calcVal() : Long

        class Literal(version: Int, val value: Long) : Packet(version) {
            override fun calcVal() = value
        }

        class Operator(version: Int, val opType: Int, val subpackets: List<Packet>) : Packet(version) {
            override fun calcVal(): Long {
                return when (opType) {
                    0 -> subpackets.sumOf { it.calcVal() }                               // 0 = sum operation
                    1 -> subpackets.fold(1L) { sum, p -> sum * p.calcVal() }       // 1 = multiply (the product of all sub packets, so use fold)
                    2 -> subpackets.minOf { it.calcVal() }                               // 2 = min of
                    3 -> subpackets.maxOf { it.calcVal() }                               // 3 = max of
                    5 -> if(subpackets[0].calcVal() > subpackets[1].calcVal()) 1 else 0  // greater than
                    6 -> if(subpackets[0].calcVal() < subpackets[1].calcVal()) 1 else 0  // less than
                    7 -> if(subpackets[0].calcVal() == subpackets[1].calcVal()) 1 else 0 // equal to than
                    else -> 0
                }
            }
        }
    }

    fun readPacket(): Packet {

        val v = bits.takeNext(3).toInt(2)
        val t = bits.takeNext(3).toInt(2)

        if (t == 4) {
            var continueBit: Boolean = true
            var res = ""
            while(continueBit) {
                continueBit = bits.takeNext(1) == "1"
                res += bits.takeNext(4)
            }

            return Packet.Literal(v, res.toLong(2))
        }
        else {
            val lType = bits.takeNext(1).toInt(2)
            val subpackets = if (lType == 1) {
                val numSubPackets = bits.takeNext(11).toInt(2)
                (1..numSubPackets).map { readPacket() }
            }
            else {
                val len = bits.takeNext(15).toInt(2)
                val cursorTarget = bits.calcTarget(len)
                val subpackets = mutableListOf<Packet>()
                while (!bits.hasReachedCursor(cursorTarget)) {
                    subpackets.add(readPacket())
                }
                subpackets
            }
            return Packet.Operator(v, t, subpackets)
        }
    }

    private fun sumAllPackets(packet: Packet): Long {
        return when(packet) {
            is Packet.Literal -> packet.version.toLong()
            is Packet.Operator -> packet.version + packet.subpackets.sumOf { sumAllPackets(it) }
        }
    }

    fun puzzle1() = println(sumAllPackets(readPacket()))
    fun puzzle2() = println(readPacket().calcVal())

    fun Char.toHexVal() = this.digitToInt(16).toString(2).padStart(4, '0')
}