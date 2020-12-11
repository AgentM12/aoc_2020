package december

import AbstractDay

class Day1 : AbstractDay() {

    private val input = obtainInput { s -> s.toInt() }.sorted()

    // Part 1: 692916
    override fun part1(): String = findProductOfMatchingSum(2, 2020, input).toString()

    // Part 2: 289270976
    override fun part2(): String = findProductOfMatchingSum(3, 2020, input).toString()

    /**
     * Finds the product of the first combination of `terms` in `list` that sum to `target`.
     *
     * @param terms Amount of terms to combine.
     * @param target The target sum.
     * @param list The list to search in (preferably sorted).
     * @return The product of the first `terms` in the `list` that sum to `target` or null if it cannot be found.
     */
    private fun findProductOfMatchingSum(terms: Int, target: Int, list: List<Int>, prevSum: Int = 0): Int? {
        if (terms < 1) return 0
        if (terms == 1) return list.find { i -> prevSum + i == target }
        for (i in 0..list.size - terms) {
            val v = list[i]
            val n = findProductOfMatchingSum(terms - 1, target, list.subList(i, list.size), prevSum + v)
            if (n != null) return v * n
        }
        return null
    }
}