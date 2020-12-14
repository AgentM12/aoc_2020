package december

import AbstractDay

class Day14 : AbstractDay() {

    private val input = obtainInput {
        val line = it.split('=')
        val type = line[0].trim()
        val data = line[1].trim()
        if (type == "mask") {
            var andMask = 0L
            var orMask = 0L
            for (c in data) {
                andMask = andMask shl 1
                orMask = orMask shl 1
                when (c) {
                    '1' -> {
                        andMask = andMask or 1
                        orMask = orMask or 1
                    }
                    'X' -> andMask = andMask or 1
                }
            }
            Mask(andMask, orMask, data)
        } else {
            val memes = type.split(Regex("[\\[\\]]"))
            Mem(memes[1].trim().toLong(), data.toLong())
        }
    }

    // Part 1: 17481577045893
    override fun part1(): String {
        var andBitmask = 0L // 1 = unchanged, 0 becomes 0
        var orBitmask = 0L // 1 becomes 1, 0 = unchanged
        val mem = mutableMapOf<Long, Long>()
        for (op in input) {
            if (op is Mask) {
                andBitmask = op.andMask
                orBitmask = op.orMask
            } else if (op is Mem) {
                val orData = op.data or orBitmask
                mem[op.address] = orData and andBitmask
            }
        }
        return mem.map { it.value }.sum().toString()
    }

    // Part 2: 4160009892257
    override fun part2(): String {
        var xBitmask = ""
        val mem = mutableMapOf<Long, Long>()
        for (op in input) {
            if (op is Mask) xBitmask = op.xMask
            else if (op is Mem) memoryVariations(op.address, xBitmask).forEach { mem[it] = op.data }
        }
        return mem.map { it.value }.sum().toString()
    }

    private fun memoryVariations(x: Long, mask: String, index: Int = mask.length - 1, cur: Long = 0): List<Long> {
        if (index < 0) return listOf(cur)
        val next = cur shl 1
        return when (mask[mask.length - 1 - index]) {
            '0' -> memoryVariations(x, mask, index - 1, next or (x shr index and 1L))
            '1' -> memoryVariations(x, mask, index - 1, next or 1L)
            'X' -> listOf(0L, 1L).flatMap { memoryVariations(x, mask, index - 1, next or it) }
            else -> error("Invalid character")
        }
    }

    interface Op
    class Mem(val address: Long, val data: Long) : Op
    class Mask(val andMask: Long, val orMask: Long, val xMask: String) : Op
}