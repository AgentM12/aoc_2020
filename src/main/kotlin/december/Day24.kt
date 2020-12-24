package december

import AbstractDay

class Day24 : AbstractDay() {

    private val input = obtainInput()

    private lateinit var part1cache: Set<HexTile>

    // Part 1: 263
    override fun part1(): String {
        val tiles = mutableSetOf<HexTile>()
        for (line in input) {
            var current = HexTile(0, 0)
            var i = 0
            while (i < line.length) {
                current = when (line[i]) {
                    'e', 'w' -> current.neighbor("${line[i++]}")!!
                    'n', 's' -> current.neighbor("${line[i++]}${line[i++]}")!!
                    else -> error("Unresolved direction: ${line[i]}")
                }
            }
            if (current in tiles) tiles.remove(current)
            else tiles.add(current)
        }
        part1cache = tiles
        return tiles.size.toString()
    }

    // Part 2: 3649
    override fun part2(): String {
        var tiles = part1cache.map { it.on() }.toMutableSet()
        val tilesToCheck = mutableSetOf<HexTile>()
        var swap = mutableSetOf<HexTile>()
        for (day in 1..100) {
            tilesToCheck.clear()
            tiles.forEach { t ->
                tilesToCheck.find { t == it }?.set() ?: tilesToCheck.add(t) // Force flips original tiles
                tilesToCheck.addAll(t.neighbors()) // add blank tiles.
            }
            for (t in tilesToCheck) {
                val neighborCount = t.neighbors().count { it in tiles }
                if (t.black && (neighborCount in 1..2) || !t.black && (neighborCount == 2)) swap.add(t.on())
            }
            tiles = swap
            swap = mutableSetOf()
        }
        return tiles.size.toString()
    }

    class HexTile(val q: Int, val r: Int, var black: Boolean = true) {

        fun neighbor(dir: String): HexTile? {
            val hex = HEX_DIRS[dir] ?: return null
            return HexTile(this.q + hex.q, this.r + hex.r, false)
        }

        fun neighbors(): Set<HexTile> = HEX_DIRS.keys.fold(mutableSetOf()) { s, d -> s.also { it.add(neighbor(d)!!) } }

        fun on() = HexTile(this.q, this.r, true) // returns a copy that is flipped on
        fun set() = black.also { black = true } // turns original object on, returns false if it was off before.

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as HexTile
            return q == other.q && r == other.r
        }

        override fun hashCode(): Int = 31 * q + r

        companion object {
            val HEX_DIRS = mapOf(
                "e" to HexTile(+1, 0),
                "ne" to HexTile(+1, -1),
                "nw" to HexTile(0, -1),
                "w" to HexTile(-1, 0),
                "sw" to HexTile(-1, +1),
                "se" to HexTile(0, +1)
            )
        }
    }
}