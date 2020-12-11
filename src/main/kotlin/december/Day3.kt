package december

import AbstractDay

class Day3 : AbstractDay() {

    private val input = obtainInput()

    // Part 1: 209
    override fun part1(): String = traverse(input, 3, 1).toString()

    // Part 2: 1574890240
    override fun part2(): String {
        val a = traverse(input, 1, 1)
        val b = traverse(input, 3, 1)
        val c = traverse(input, 5, 1)
        val d = traverse(input, 7, 1)
        val e = traverse(input, 1, 2)
        return (a * b * c * d * e).toString()
    }

    /**
     * Traverses `input` starting at the top-left counting trees (`#`) til the bottom row while repeating `input` right
     *
     * @param input Grid of trees (`#`) and blank spaces (`.`).
     * @param right Value to traverse right per step.
     * @param down Value to traverse down per step.
     * @return The sum of trees (`#`) at each step position in the grid.
     */
    private fun traverse(input: List<String>, right: Int, down: Int): Int {
        var count = isTree(input[0][0])
        var x = 0
        for (y in down until input.size step down) {
            val line = input[y]
            x = (x + right) % line.length
            count += isTree(line[x])
        }
        return count
    }

    private fun isTree(c: Char) = if (c == '#') 1 else 0
}