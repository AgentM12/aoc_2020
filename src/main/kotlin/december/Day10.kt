package december

import AbstractDay

class Day10 : AbstractDay() {

    private val input = obtainInput { it.toInt() }.sorted()

    // Part 1: 1755
    override fun part1(): String {
        val x = input.fold(Triple(0, 0, 1), { a, i ->
            Triple(i, a.second + if (i - a.first == 1) 1 else 0, a.third + if (i - a.first == 3) 1 else 0)
        })
        return (x.second * x.third).toString()
    }

    // Part 2: 4049565169664
    override fun part2(): String = paths(-1).toString()

    /**
     * Memoization is key!
     */
    private fun paths(i: Int, mem: Array<Long> = Array(input.size) { -1L }): Long {
        if (i >= input.size - 1) return 1L
        if (mem[i + 1] != -1L) return mem[i + 1] // Immediately return if computed before.
        val x = if (i == -1) 0 else input[i]

        mem[i + 1] = 0
        for (j in i + 1 until input.size) {
            if (input[j] - x <= 3) {
                mem[i + 1] += paths(j, mem)
            } else break
        }
        return mem[i + 1]
    }
}