package december

import AbstractDay

class Day15 : AbstractDay() {

    private val input = obtainInput()[0].split(",").map { it.trim().toInt() }

    // Part 1: 387
    override fun part1(): String = run(2020).toString()

    // Part 2: 6428
    override fun part2(): String = run(30000000).toString()

    private fun run(turns: Int): Int {
        var turn = 1
        val historyMap = mutableMapOf<Int, TurnHistory>()
        for (i in input) historyMap[i] = TurnHistory(turn++)
        var last = input.last()
        while (turn <= turns) {
            last = historyMap[last]!!.diff()
            if (last in historyMap) historyMap[last]!!.push(turn++) else historyMap[last] = TurnHistory(turn++)
        }
        return last
    }

    class TurnHistory(private var cur: Int = -1, private var prev: Int = -1) {

        fun push(v: Int) {
            prev = cur
            cur = v
        }

        fun diff() = if (prev > -1) cur - prev else 0
    }
}