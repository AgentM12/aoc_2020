package december

import AbstractDay

class Day6 : AbstractDay() {

    private val input = obtainInput()

    // Part 1: 6310
    override fun part1(): String {
        var cSum = 0
        val seen = HashSet<Char>()
        for (line in input) {
            if (line.isBlank()) {
                cSum += seen.size
                seen.clear()
            }
            line.forEach { seen.add(it) } // Union: Any
        }
        cSum += seen.size
        return cSum.toString()
    }

    // Part 2: 3193
    override fun part2(): String {
        var cSum = 0
        var seen = HashSet<Char>()
        var firstLine = true
        for (line in input) {
            when {
                line.isBlank() -> {
                    cSum += seen.size
                    seen.clear()
                    firstLine = true
                }
                firstLine -> {
                    line.forEach { seen.add(it) }
                    firstLine = false
                }
                else -> {
                    val seen2 = HashSet<Char>()
                    line.forEach { seen2.add(it) }
                    seen = seen.intersect(seen2) as HashSet<Char> // Intersect: All
                }
            }
        }
        cSum += seen.size
        return cSum.toString()
    }
}