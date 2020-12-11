package december

import AbstractDay

class Day5 : AbstractDay() {

    private val input = obtainInput { it.replace('R', 'H') }.sorted()

    private val input2 = input.map { bins(it) }

    // Part 1: 866
    override fun part1(): String = bins(input[0]).toString()

    // Part 2: 583
    override fun part2(): String = findMissing(input).toString()

    /**
     * Instead of a bin search I could have just parsed the whole list to integers.
     */
    private fun bins(s: String): Int {
        if (s.length != 10) return -1
        var rMin = 0
        var rMax = 127
        var cMin = 0
        var cMax = 7
        for (i in 0..6) {
            val rMid = rMin + ((rMax - rMin) / 2)
            if (s[i] == 'F') rMax = rMid
            else rMin = rMid + 1
        }
        for (i in 7..9) {
            val cMid = cMin + ((cMax - cMin) / 2)
            if (s[i] == 'L') cMax = cMid
            else cMin = cMid + 1
        }
        return rMin * 8 + cMin
    }

    private fun hPos(s: String) =
        (if (s[7] == 'L') 4 else 0) + (if (s[8] == 'L') 2 else 0) + (if (s[9] == 'L') 1 else 0)

    private fun findMissing(l: List<String>): Int {
        var i = hPos(l[0])
        for (s in l) {
            val j = hPos(s)
            if (i == j) i = (i + 1) % 8
            else return bins(s) + 1
        }
        return -1
    }
}